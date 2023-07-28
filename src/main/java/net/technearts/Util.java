package net.technearts;

import net.technearts.PlannerConfig.People;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Util {

    public static TimeTable generateDemoData(List<People> pessoas, String monthYear) {
        Integer[] monthYearInt = Arrays.stream(monthYear.split("/")).map(Integer::parseInt).toArray(Integer[]::new);
        Integer month = monthYearInt[0];
        Integer year = monthYearInt[1];
        List<Timeslot> timeslotList = new ArrayList<>(31);
        LocalDate date = LocalDate.of(year, month, 1);
        long id = 0;
        while (month.equals(date.getMonthValue())) {
            timeslotList.add(new Timeslot(id++, date.getDayOfWeek(), MonthDay.of(date.getMonthValue(), date.getDayOfMonth()), false));
            date = date.plusDays(1);
        }
        List<Person> peopleList = pessoas.stream().map(p -> new Person(p.name(), p.hours(), p.unavailable().isEmpty() ? Collections.emptyList() : p.unavailable().get())).toList();

        return new TimeTable(timeslotList, peopleList);
    }
}
