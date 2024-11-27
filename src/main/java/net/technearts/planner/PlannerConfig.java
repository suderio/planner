package net.technearts.planner;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "planner.files")
public interface PlannerConfig {

    String prefix();

    String suffix();

    String directory();
}
