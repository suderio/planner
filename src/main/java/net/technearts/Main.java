package net.technearts;

import io.quarkus.arc.log.LoggerName;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.time.DateTimeException;
import java.time.Duration;

import static java.lang.System.exit;
import static java.util.regex.Pattern.matches;
import static net.technearts.Util.generateDemoData;

@Command(name = "planner", mixinStandardHelpOptions = true, abbreviateSynopsis = true, description = "A planner for monthly allocation.")
public class Main implements Runnable {

    @Option(names = {"-l", "--limit"}, defaultValue = "5", description = "Time limit in seconds")
    Integer limit;

    @Parameters(paramLabel = "month/year", arity = "1", description = "Month/year to generate")
    String monthYear;

    @Inject
    PlannerConfig config;

    @LoggerName("planner")
    Logger Log;

    @Override
    public void run() {
        Log.info("Time limit: %s".formatted(limit));
        Log.info("Number of people: %s".formatted(config.people().size()));
        SolverFactory<TimeTable> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(TimeTable.class)
                .withEntityClasses(Timeslot.class)
                .withConstraintProviderClass(TimeTableConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(limit)));


        try {
            if (!matches("\\d\\d?/\\d\\d\\d\\d", monthYear)) {
                throw new IllegalArgumentException();
            }

            // Load the problem
            TimeTable problem = generateDemoData(config.people(), monthYear);
            // Solve the problem
            Solver<TimeTable> solver = solverFactory.buildSolver();
            TimeTable solution = solver.solve(problem);

            // Visualize the solution
            solution.getTimeslotList().forEach(Log::info);
            solution.getPersonList().forEach(p -> Log.info("%s: %s".formatted(p.getName(), solution.getTimeslotList().stream().filter(t -> t.getPerson().equals(p)).mapToInt(Timeslot::getWorkingHours).sum())));

        } catch (IllegalArgumentException | DateTimeException e) {
            Log.error("Month/Year Parameter must be MM/YYYY, not %s".formatted(monthYear));
            exit(1);
        }



    }

}
