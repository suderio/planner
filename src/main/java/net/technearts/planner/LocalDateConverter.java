package net.technearts.planner;

import io.quarkus.arc.log.LoggerName;
import org.eclipse.microprofile.config.spi.Converter;
import org.jboss.logging.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;

import static java.time.LocalDate.now;

public class LocalDateConverter implements Converter<LocalDate> {
    @LoggerName("planner")
    Logger log;

    /**
     * This method is a converter for LocalDate Configurations.
     * Dates can be yyyy-mm-dd or --mm-dd. The last format will always create dates
     * in the current year.
     * This is a bug!
     * TODO Find a way of retrieving the Month/Year parameter.
     *
     * @param s a String in the format yyyy-mm-dd or --mm-dd.
     * @return a LocalDate
     * @throws IllegalArgumentException If dates are not in the correct format.
     * @throws NullPointerException If date is null
     */
    @Override
    public LocalDate convert(String s) throws IllegalArgumentException, NullPointerException {
        String[] yearMonthDay = (s.startsWith("--") ?
                s.replaceFirst("-", "" + now().getYear()) :
                s)
                .split("-");
        try {
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            log.error("Holiday's configuration must be yyyy-mm-dd or --mm-dd", e);
            throw new IllegalArgumentException("Wrong Holiday configuration.");
        }
    }
}
