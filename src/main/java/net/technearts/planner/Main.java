package net.technearts.planner;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.time.DateTimeException;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;
import static java.util.regex.Pattern.matches;

@TopCommand
@Command(name = "planner",
        mixinStandardHelpOptions = true,
        abbreviateSynopsis = true,
        description = "A planner for monthly allocation.")
public class Main implements Runnable {

    @Option(names = {"-l", "--limit"}, defaultValue = "5", description = "Time limit in seconds")
    Integer limit;

    @Option(names = {"-t", "--totals"}, defaultValue = "false", description = "Show totals by person")
    Boolean totals;

    @Parameters(paramLabel = "month/year", arity = "1", description = "Month/year to generate")
    String monthYear;

    @Inject
    PlannerConfig config;

    @LoggerName("planner")
    Logger Log;

    @Override
    public void run() {
        try {
            if (!matches("\\d\\d?/\\d\\d\\d\\d", monthYear)) {
                throw new IllegalArgumentException("MonthYear must be in the format MM/YYYY");
            }
            if (config.directory() == null || config.directory().isEmpty()) {
                Log.info("Working directory must be set in the config (.env) file.");
            }

            Integer[] monthYearInt = Arrays.stream(monthYear.split("/")).map(Integer::parseInt).toArray(Integer[]::new);
            Integer month = monthYearInt[0];
            Integer year = monthYearInt[1];

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12.");
            }

            if (year < 0) {
                throw new IllegalArgumentException("Negative years are not supported.");
            }
            Log.info("Working directory: %s".formatted(config.directory()));
            PlannerFile plannerFile = new PlannerFile(config.directory(), config.prefix(), config.suffix(), month, year);
            Log.info("Using file: %s".formatted(plannerFile.yamlFileName()));
            Log.info("Generating file %s".formatted(plannerFile.txtFileName()));
            List<Person> people = plannerFile.readPeople();
            Log.info("Number of people: %s".formatted(people.size()));
            Log.info("Time limit: %s".formatted(limit));

            // Get the solution
            Solver solver = new Solver(people, month, year);
            TimeTable solution = solver.solve(limit).getSolution();

            // Visualize the solution
            solution.getTimeslotList().forEach(Log::info);

            // Visualize totals per person
            if (totals) {
                solver.getTotals().forEach((person, sum) -> System.out.printf("%s: %s%n", person.getName(), sum));
            }

            // Write solutions file
            plannerFile.dumpTimeslots(solution.getTimeslotList());
            plannerFile.dumpPeople(solution.getTimeslotList());

        } catch (IllegalArgumentException | DateTimeException e) {
            Log.error("Some unknown problem with one of the arguments.", e);
            exit(1);
        } catch (ConfigurationException e) {
            Log.error("Some unknown problem with the Config file.", e);
            exit(1);
        }
    }
}

