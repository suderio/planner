package net.technearts.planner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Holidays {
    private static final Logger log = LoggerFactory.getLogger(Holidays.class);
    private final List<LocalDate> holidays = new ArrayList<>();
    private final LocalDate easter;

    public Holidays(int year) {
        holidays.add(LocalDate.of(year, 1, 1));     // Confraternização Universal
        holidays.add(LocalDate.of(year, 4, 21));    // Tiradentes
        holidays.add(LocalDate.of(year, 5, 1));     // Dia do Trabalho
        holidays.add(LocalDate.of(year, 9, 7));     // Independência do Brasil
        holidays.add(LocalDate.of(year, 10, 12));   // Nossa Sr.a Aparecida
        holidays.add(LocalDate.of(year, 11, 2));    // Finados
        holidays.add(LocalDate.of(year, 11, 15));   // Proclamação da República
        holidays.add(LocalDate.of(year, 11, 20));   // Consciência Negra
        holidays.add(LocalDate.of(year, 12, 25));   // Natal
        holidays.add(LocalDate.of(year, 12, 31));   // Reveillon

        easter = easter(year);
    }

    private static LocalDate easter(int year) {
        // Gauss Easter Algorithm
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(year, month, day);
    }

    public final List<LocalDate> holidays() {
        holidays.add(easter.minusDays(46)); // Segunda de Carnaval
        holidays.add(easter.minusDays(47)); // Terça de Carnaval
        holidays.add(easter.minusDays(2));  // Sexta da Paixão
        holidays.add(easter.plusDays(60));     // Corpus Christi
        return holidays;
    }
}

