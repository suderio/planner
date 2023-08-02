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

import static java.lang.System.exit;
import static java.util.regex.Pattern.matches;

@TopCommand
@Command(name = "planner",
        mixinStandardHelpOptions = true,
        abbreviateSynopsis = true,
        description = "A planner for monthly allocation.",
        subcommands = {ConfigCommand.class})
public class MainCommand implements Runnable {

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
                throw new IllegalArgumentException();
            }
            if (config.people() == null || config.people().isEmpty()) {
                throw new ConfigurationException("Must have at least one person");
            }
            Log.info("Time limit: %s".formatted(limit));
            Log.info("Number of people: %s".formatted(config.people().size()));

            // Get the solution
            Solver solver = new Solver(config.people(), config.holidays(), monthYear);
            TimeTable solution = solver.solve(limit).getSolution();

            // Visualize the solution
            solution.getTimeslotList().forEach(System.out::println);

            // Visualize totals per person
            if (totals) {
                solver.getTotals().forEach((person, sum) -> System.out.printf("%s: %s%n", person.getName(), sum));
/*                solution.getPersonList().forEach(p ->
                        System.out.printf("%s: %s%n", p.getName(),
                                solution.getTimeslotList().stream().filter(t ->
                                        t.getPerson().equals(p)).mapToInt(Timeslot::getHours).sum() + p.getHours()));*/
            }
        } catch (IllegalArgumentException | DateTimeException e) {
            Log.error("Month/Year Parameter must be MM/YYYY, not %s".formatted(monthYear));
            exit(1);
        } catch (ConfigurationException e) {
            Log.error("Config file must have at least one person, probably more.");
        }
    }
}

