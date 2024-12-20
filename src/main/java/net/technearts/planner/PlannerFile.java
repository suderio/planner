package net.technearts.planner;

import io.quarkus.arc.log.LoggerName;
import org.jboss.logging.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.writeString;
import static java.nio.file.StandardOpenOption.*;
import static org.yaml.snakeyaml.DumperOptions.FlowStyle.FLOW;

/**
 * Manages all the reading and writing of files, be it yaml or text, but not application.yaml config file.
 */
public class PlannerFile {
    private final String directory;
    private final String prefix;
    private final String suffix;
    private final Integer month;
    private final Integer year;
    private final Yaml yaml;
    @LoggerName("planner")
    Logger Log;

    public PlannerFile(String directory, String prefix, String suffix, Integer month, Integer year) {
        this.directory = directory;
        this.prefix = prefix;
        this.suffix = suffix;
        this.month = month;
        this.year = year;
        LoaderOptions loaderOptions = new LoaderOptions();
        DumperOptions dumperOptions = new DumperOptions();
        //dumperOptions.setDefaultFlowStyle(FLOW);
        //dumperOptions.setPrettyFlow(true);
        //dumperOptions.setDereferenceAliases(true);
        //dumperOptions.setAllowUnicode(true);
        dumperOptions.setExplicitStart(true);
        dumperOptions.setExplicitEnd(true);
        Representer representer = new Representer(dumperOptions);
        //representer.addClassTag(Person.class, Tag.MAP);
        //representer.addClassTag(Float.class, Tag.FLOAT);
        BaseConstructor constructor = new Constructor(Person.class, loaderOptions);
        this.yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);

    }

    public String yamlFileName() {
        return "%s%04d%02d.yaml".formatted(prefix, year, month);
    }

    public String txtFileName() {
        return "%s%04d%02d%s.txt".formatted(prefix, year, month, suffix);
    }

    public List<Person> readPeople() {
        List<Person> result = new ArrayList<>();
        Path path = Path.of(directory, yamlFileName());
        try (InputStream inputStream =  Files.newInputStream(path)) {
            yaml.loadAll(inputStream).forEach(p -> result.add((Person) p));
        } catch (IOException e) {
            Log.error("Cannot find file %s".formatted(yamlFileName()));
            System.exit(1);
        }
        return result;
    }

    public void dumpPeople(List<Timeslot> timeslots) {
        Path path = Path.of(directory, yamlFileName());
        timeslots.forEach(t -> t.getPerson().getScheduled().add(t.getMonthDay().getDayOfMonth()));
        try(var writer = new StringWriter()) {
            yaml.dumpAll(timeslots.stream().map(Timeslot::getPerson).collect(Collectors.toSet()).iterator(), writer);
            var result = writer.toString().replace("!!net.technearts.planner.Person", "");
            result = result.replace("!!float ", "");
            writeString(path, result, WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dumpTimeslots(List<Timeslot> timeslots) {
        Path path = Path.of(directory, txtFileName());
        try (var writer = new PrintWriter(newBufferedWriter(path, TRUNCATE_EXISTING, CREATE))){
            timeslots.stream().map(ts -> ts.toString()
                    .replaceFirst("^\\-", this.year.toString()))
                    .forEach(writer::println);
        } catch (IOException e) {
            Log.error("Unable to write in file %s".formatted(txtFileName()));
            System.exit(1);
        }
    }
}
