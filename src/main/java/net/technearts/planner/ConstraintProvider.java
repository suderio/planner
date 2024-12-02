package net.technearts.planner;


import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.Joiners;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.equal;
import static org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore.ONE_HARD;
import static org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore.ONE_SOFT;

public class ConstraintProvider implements org.optaplanner.core.api.score.stream.ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{dayConflict(constraintFactory),
                previousDay(constraintFactory),
                unavailableDay(constraintFactory),
                workingHours(constraintFactory),
                tooManyWorkingHours(constraintFactory),
                tooManyWorkingDays(constraintFactory),
                preferredDay(constraintFactory)
        };
    }

    Constraint dayConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class,
                        equal(Timeslot::getMonthDay),
                        equal(Timeslot::getPerson),
                        Joiners.lessThan(Timeslot::getId))
                .penalize(ONE_HARD)
                .asConstraint("Day Conflict");
    }

    Constraint previousDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class, equal(Timeslot::getPerson),
                        Joiners.filtering((x, y) -> x.getMonthDay().getDayOfMonth() - y.getMonthDay().getDayOfMonth() == 1))
                .penalize(ONE_HARD)
                .asConstraint("Previous Day Conflict");
    }

    Constraint unavailableDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .filter(t -> t.getPerson().getUnavailable().contains(t.getMonthDay().getDayOfMonth()))
                .penalize(ONE_HARD)
                .asConstraint("Unavailable Day Conflict");
    }

    Constraint workingHours(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class, equal(Timeslot::getPerson), Joiners.lessThan(Timeslot::getId))
                .penalizeBigDecimal(ONE_SOFT, (t1, t2) -> t1.getWorkingHours().add(t2.getWorkingHours()))
                .asConstraint("workingHours");
    }

    Constraint tooManyWorkingHours(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .groupBy(Timeslot::getPerson, sumBigDecimal(Timeslot::getWorkingHours), averageBigDecimal(Timeslot::getWorkingHours))
                .penalizeBigDecimal(ONE_SOFT, (person, sum, avg) -> sum.subtract(avg).abs())
                .asConstraint("tooManyWorkingHours");
    }

    Constraint tooManyWorkingDays(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .groupBy(Timeslot::getPerson, count())
                .penalize(ONE_SOFT, (person, days) -> days)
                .asConstraint("tooManyWorkingDays");
    }

    Constraint preferredDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .filter(t -> t.getPerson().getPreferred().contains(t.getMonthDay().getDayOfMonth()))
                .reward(ONE_SOFT)
                .asConstraint("Preferred Day Reward");
    }


}
