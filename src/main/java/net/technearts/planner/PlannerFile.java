package net.technearts.planner;

import io.quarkus.arc.log.LoggerName;
import org.jboss.logging.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

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
        LoaderOptions options = new LoaderOptions();
        this.yaml = new Yaml(new Constructor(Person.class, options));
    }

    public String yamlFileName() {
        //return prefix + String.format("%04d", year) + String.format("%02d", month) + ".yaml";
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
        try (var writer = new PrintWriter(newBufferedWriter(path, StandardOpenOption.WRITE))) {
            yaml.dumpAll(timeslots.stream().map(Timeslot::getPerson).collect(Collectors.toSet()).iterator(), writer);
        } catch (IOException e) {
            Log.error("Cannot find file %s".formatted(yamlFileName()));
            System.exit(1);
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
