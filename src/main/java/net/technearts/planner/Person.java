package net.technearts.planner;

import java.util.List;

public class Person {

    private String name;
    private Integer hours;
    private List<Integer> unavailable;

    private List<Integer> preferred;
    public Person() {

    }

    public Person(String name, Integer hours, List<Integer> unavailable, List<Integer> preferred) {
        this.name = name;
        this.hours = hours;
        this.unavailable = unavailable;
        this.preferred = preferred;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(List<Integer> unavailable) {
        this.unavailable = unavailable;
    }

    public List<Integer> getPreferred() {
        return preferred;
    }

    public void setPreferred(List<Integer> preferred) {
        this.preferred = preferred;
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
