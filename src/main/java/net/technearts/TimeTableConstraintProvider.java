package net.technearts;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.Joiners.equal;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{dayConflict(constraintFactory),
                previousDay(constraintFactory),
                unavailableDay(constraintFactory),
                workingHours(constraintFactory),
                tooManyWorkingHours(constraintFactory),
                tooManyWorkingDays(constraintFactory)};
    }

    Constraint dayConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class,
                        equal(Timeslot::getMonthDay),
                        equal(Timeslot::getPerson),
                        Joiners.lessThan(Timeslot::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Day Conflict");
    }

    Constraint previousDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class, equal(Timeslot::getPerson),
                        Joiners.filtering((x, y) -> x.getMonthDay().getDayOfMonth() - y.getMonthDay().getDayOfMonth() == 1))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Previous Day Conflict");
    }

    Constraint unavailableDay(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .filter(t -> t.getPerson().getUnavailable().contains(t.getMonthDay().getDayOfMonth()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Unavailable Day Conflict");
    }

    Constraint workingHours(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .join(Timeslot.class, equal(Timeslot::getPerson), Joiners.lessThan(Timeslot::getId))
                .penalize(HardSoftScore.ONE_SOFT, (t1, t2) -> t1.getWorkingHours() + t2.getWorkingHours())
                .asConstraint("workingHours");
    }

    Constraint tooManyWorkingHours(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .groupBy(Timeslot::getPerson, sum(Timeslot::getWorkingHours))
                .penalize(HardSoftScore.ONE_SOFT, (person, hours) -> hours)
                .asConstraint("tooManyWorkingHours");
    }

    Constraint tooManyWorkingDays(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Timeslot.class)
                .groupBy(Timeslot::getPerson, count())
                .penalize(HardSoftScore.ONE_SOFT, (person, days) -> days)
                .asConstraint("tooManyWorkingDays");
    }


}
