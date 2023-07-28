package net.technearts;

import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "planner")
public interface PlannerConfig {
    List<People> people();

    interface People {

        String name();

        Integer hours();

        Optional<List<Integer>> unavailable();
    }
}
