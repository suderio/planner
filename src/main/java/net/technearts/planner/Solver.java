package net.technearts.planner;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;


public final class Solver {

    private static final Logger log = LoggerFactory.getLogger(Solver.class);
    private final List<Person> people;
    private final List<LocalDate> holidays;
    private final Integer month;
    private final Integer year;
    private TimeTable solution;

    public Solver(List<Person> people, Integer month, Integer year) {
        this.people = people;
        this.month = month;
        this.year = year;
        this.holidays = (new Holidays(year)).holidays();
    }

    public Solver solve(Integer limit) {
        // Create the solver
        SolverFactory<TimeTable> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(TimeTable.class)
                .withEntityClasses(Timeslot.class)
                .withConstraintProviderClass(ConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(limit)));
        // Load the problem
        TimeTable problem = generateProblemData();
        // Solve the problem
        org.optaplanner.core.api.solver.Solver<TimeTable> solver = solverFactory.buildSolver();
        solution = solver.solve(problem);
        return this;
    }

    private TimeTable generateProblemData() {
        List<Timeslot> timeslotList = new ArrayList<>(31);
        LocalDate date = LocalDate.of(year, month, 1);
        long id = 0;
        while (month.equals(date.getMonthValue())) {
            timeslotList.add(new Timeslot(id++, date.getDayOfWeek(), MonthDay.of(date.getMonthValue(), date.getDayOfMonth()), holidays.contains(date)));
            date = date.plusDays(1);
        }
        return new TimeTable(timeslotList, people);
    }

    public TimeTable getSolution() {
        return solution;
    }

    public void updatePeople() {
        var totals = getTotals();
        var oldTotals = people.stream().collect(Collectors.toMap(identity(), Person::getHours));
        var newTotals = people.stream().collect(Collectors.toMap(identity(), p -> oldTotals.get(p).add(totals.get(p))));
        var minTotal = newTotals.values().stream().min(BigDecimal::compareTo).orElse(ZERO);
        people.forEach(p -> newTotals.replace(p, newTotals.get(p).subtract(minTotal)));
        solution.getTimeslotList().forEach(t -> t.getPerson().setHours(newTotals.get(t.getPerson())));
    }

    public Map<Person, BigDecimal> getTotals() {
        return solution.getTimeslotList().stream()
                .collect(groupingBy(Timeslot::getPerson, Collectors.reducing(ZERO, Timeslot::getHours, BigDecimal::add)));
    }

}
