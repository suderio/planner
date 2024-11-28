package net.technearts.planner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.MonthDay;

@PlanningEntity
public class Timeslot {

    @PlanningId
    private Long id;
    private DayOfWeek dayOfWeek;
    private MonthDay monthDay;
    private Boolean holiday;

    @PlanningVariable
    private Person person;

    public Timeslot() {

    }

    public Timeslot(Long id, DayOfWeek dayOfWeek, MonthDay monthDay, Boolean holiday) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.monthDay = monthDay;
        this.holiday = holiday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    public BigDecimal getWorkingHours() {
        return this.person.getHours().add(this.getHours());
    }

    public BigDecimal getHours() {
        return this.getHoliday() || this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY ? new BigDecimal(24) : new BigDecimal(12);
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    @Override
    public String toString() {
        return "%s - %s (%s)".formatted(this.monthDay, this.person.getName(), this.getHours());
    }
}
