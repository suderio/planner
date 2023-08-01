package net.technearts.planner;

import io.smallrye.config.ConfigMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ConfigMapping(prefix = "planner")
public interface PlannerConfig {

    Weight weights();
    List<People> people();
    List<LocalDate> holidays();

    interface Weight {
        Map<DayOfWeek, Integer> week();
        Optional<Integer> holiday();
    }
    interface People {

        String name();

        Integer hours();

        Optional<List<Integer>> unavailable();

        Optional<List<Integer>> preferred();
    }
}
