package net.technearts;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class TimeTable {
    
    @PlanningEntityCollectionProperty
    private List<Timeslot> timeslotList;
    
    
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Person> personList;

    @PlanningScore
    private HardSoftScore score;

    public TimeTable() {
    }

    public TimeTable(List<Timeslot> timeslotList, List<Person> rosterList) {
        this.timeslotList = timeslotList;
        this.personList = rosterList;
    }

    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public HardSoftScore getScore() {
        return score;
    }
}
