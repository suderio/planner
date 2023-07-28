package net.technearts;

import java.util.List;

public class Person {

    private String name;
    private Integer hours;

    private List<Integer> unavailable;

    public Person() {

    }

    public Person(String name, Integer hours, List<Integer> unavailable) {
        this.name = name;
        this.hours = hours;
        this.unavailable = unavailable;
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
