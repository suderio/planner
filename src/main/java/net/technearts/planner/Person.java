package net.technearts.planner;

import java.math.BigDecimal;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Person {

    private String name;
    private BigDecimal hours;
    private ArrayList<Integer> unavailable;
    private ArrayList<Integer> preferred;
    private ArrayList<Integer> scheduled;

    public Person() {
    }

    public Person(String name, BigDecimal hours, ArrayList<Integer> unavailable, ArrayList<Integer> preferred, ArrayList<Integer> scheduled) {
        this.name = name;
        this.hours = hours;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
