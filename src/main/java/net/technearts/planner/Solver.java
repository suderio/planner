package net.technearts.planner;

import net.technearts.planner.PlannerConfig.People;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import java.time.Duration;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public final class Solver {

    private final List<People> people;
    private final List<LocalDate> holidays;
    private final Integer month;
    private final Integer year;
    private TimeTable solution;

    public Solver(List<People> people, List<LocalDate> holidays, String monthYear) {
        this.people = people;
        this.holidays = holidays;
        Integer[] monthYearInt = Arrays.stream(monthYear.split("/")).map(Integer::parseInt).toArray(Integer[]::new);
        month = monthYearInt[0];
        year = monthYearInt[1];
    }

    public TimeTable generateProblemData() {
        List<Timeslot> timeslotList = new ArrayList<>(31);
        LocalDate date = LocalDate.of(year, month, 1);
        long id = 0;
        while (month.equals(date.getMonthValue())) {
            timeslotList.add(new Timeslot(
                    id++,
                    date.getDayOfWeek(),
                    MonthDay.of(date.getMonthValue(), date.getDayOfMonth()),
                    holidays.contains(date)));
            date = date.plusDays(1);
        }
        List<Person> peopleList = people.stream().map(p -> new Person(
                        p.name(),
                        p.hours(),
                        p.unavailable().isEmpty() ? Collections.emptyList() : p.unavailable().get(),
                        p.preferred().isEmpty() ? Collections.emptyList() : p.preferred().get()))
                .toList();

        return new TimeTable(timeslotList, peopleList);
    }

    public Solver solve(Integer limit) {
        // Create the solver
        SolverFactory<TimeTable> solverFactory = SolverFactory
                .create(new SolverConfig().withSolutionClass(TimeTable.class)
                        .withEntityClasses(Timeslot.class)
                        .withConstraintProviderClass(TimeTableConstraintProvider.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(limit)));
        // Load the problem
        TimeTable problem = generateProblemData();
        // Solve the problem
        org.optaplanner.core.api.solver.Solver<TimeTable> solver = solverFactory.buildSolver();
        solution = solver.solve(problem);
        return this;
    }

    public TimeTable getSolution() {
        return solution;
    }

    public Map<Person, Integer> getTotals() {
        return solution.getTimeslotList().stream()
                .collect(groupingBy(Timeslot::getPerson, summingInt(Timeslot::getHours)));
    }
}
