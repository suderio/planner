package net.technearts.planner;

import java.time.DayOfWeek;
import java.time.MonthDay;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

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
    
    public Boolean getHoliday() {
        return holiday;
    }
    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public Integer getWorkingHours() {
        return this.person.getHours() + this.getHours();
    }

    public Integer getHours() {
        return this.getHoliday() || this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY ? 24 : 12;
    }

    @Override
    public String toString() {
        return "%s - %s (%s)".formatted(this.monthDay, this.person.getName(), this.getHours());
    }
}
