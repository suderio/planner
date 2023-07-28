package net.technearts.planner;

import io.quarkus.arc.log.LoggerName;
import org.eclipse.microprofile.config.spi.Converter;
import org.jboss.logging.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;

public class LocalDateConverter implements Converter<LocalDate> {
    @LoggerName("planner")
    Logger log;
    @Override
    public LocalDate convert(String s) throws IllegalArgumentException, NullPointerException {
        String[] yearMonthDay = s.split("-");
        try {
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            log.error("Holiday's configuration must be yyyy-mm-dd", e);
            return null;
        }
    }
}
