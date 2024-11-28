package net.technearts.planner;

import org.gradle.internal.impldep.com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.StringWriter;
import java.math.BigDecimal;

class PlannerFileTest {

    //@Test
    void testReadingPeople() {
        PlannerFile plannerFile = new PlannerFile("C:\\Users\\paulo\\planner", "file-", "scheduled", 1, 2024);
        plannerFile.readPeople();
    }

    @Test
    void testDumpingPeople() {

        Person p = new Person("Teste", new BigDecimal(33), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(p, writer);
        System.out.println(writer.toString());

    }
}