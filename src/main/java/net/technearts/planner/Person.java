package net.technearts.planner;

import java.math.BigDecimal;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Person {

    private String alias;
    private BigDecimal hours;
    private BigDecimal workDayHours;
    private ArrayList<Integer> unavailable;
    private ArrayList<Integer> preferred;
    private ArrayList<Integer> scheduled;

    public Person() {
    }

    public Person(String alias, BigDecimal hours, BigDecimal workDayHours, ArrayList<Integer> unavailable, ArrayList<Integer> preferred, ArrayList<Integer> scheduled) {
        this.alias = alias;
        this.hours = hours;
        this.workDayHours = workDayHours;
        this.unavailable = unavailable;
        this.preferred = preferred;
        this.scheduled = scheduled;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public BigDecimal getWorkDayHours() {
        return workDayHours;
    }

    public void setWorkDayHours(BigDecimal workDayHours) {
        this.workDayHours = workDayHours;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<Integer> getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(ArrayList<Integer> unavailable) {
        this.unavailable = unavailable;
    }

    public ArrayList<Integer> getPreferred() {
        return preferred;
    }

    public void setPreferred(ArrayList<Integer> preferred) {
        this.preferred = preferred;
    }

    public ArrayList<Integer> getScheduled() {
        return scheduled;
    }

    public void setScheduled(ArrayList<Integer> scheduled) {
        this.scheduled = scheduled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        if (alias == null) {
            if (other.alias != null) return false;
        } else if (!alias.equals(other.alias)) return false;
        return true;
    }
}
