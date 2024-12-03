package net.technearts.planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class SolverTest {

    private final List<Person> people = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var p1 = new Person("P1", new BigDecimal(15), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var p2 = new Person("P2", new BigDecimal(30), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var p3 = new Person("P3", new BigDecimal(20), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var p4 = new Person("P4", new BigDecimal(5), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var p5 = new Person("P5", new BigDecimal(0), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        people.add(p1);
        people.add(p2);
        people.add(p3);
        people.add(p4);
        people.add(p5);
    }

    @Test
    void findSolution() {
        Solver solver = new Solver(people, 1, 2024);
        TimeTable solution = solver.solve(5).getSolution();
        solution.getTimeslotList().forEach(System.out::println);
        solver.getTotals().forEach((person, sum) -> System.out.printf("%s: %s%n%n", person.getName(), sum));
    }
}