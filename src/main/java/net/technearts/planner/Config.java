package net.technearts.planner;

import picocli.CommandLine;

@CommandLine.Command(name = "config",
        mixinStandardHelpOptions = true,
        abbreviateSynopsis = true,
        description = "Config manager.")
public class Config implements Runnable {
    @CommandLine.Option(names = {"--generate"}, description = "Generate sample config file.")
    Boolean generate;
    @Override
    public void run() {

    }
}
