package net.technearts.planner;

import net.technearts.planner.PlannerConfig.People;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import java.time.Duration;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;

public final class Util {

    public static TimeTable generateProblemData(List<People> people, List<LocalDate> holidays, String monthYear) {
        Integer[] monthYearInt = Arrays.stream(monthYear.split("/")).map(Integer::parseInt).toArray(Integer[]::new);
        Integer month = monthYearInt[0];
        Integer year = monthYearInt[1];
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

    static TimeTable getSolution(List<People> people, String monthYear, List<LocalDate> holidays, Integer limit) {
        // Create the solver
        SolverFactory<TimeTable> solverFactory = SolverFactory
                .create(new SolverConfig().withSolutionClass(TimeTable.class)
                        .withEntityClasses(Timeslot.class)
                        .withConstraintProviderClass(TimeTableConstraintProvider.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(limit)));
        // Load the problem
        TimeTable problem = generateProblemData(people, holidays, monthYear);
        // Solve the problem
        Solver<TimeTable> solver = solverFactory.buildSolver();
        return solver.solve(problem);
    }
}
