package net.technearts.planner;

import io.smallrye.config.ConfigMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "planner")
public interface PlannerConfig {
    List<People> people();
    List<LocalDate> holidays();

    interface People {

        String name();

        Integer hours();

        Optional<List<Integer>> unavailable();

        Optional<List<Integer>> preferred();
    }
}
