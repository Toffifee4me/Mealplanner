package Data;

import java.io.Serializable;
import java.util.LinkedList;

public class Profile implements Serializable {
    private String name;
    private LinkedList<Day> days;
    private int[] maxMacros;

    public Profile(String name, LinkedList<Day> days, int[] maxMacros) {
        this.name = name;
        this.days = days;
        this.maxMacros = maxMacros;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Day> getDays() {
        return days;
    }

    public void setDays(LinkedList<Day> days) {
        this.days = days;
    }

    public int[] getMaxMacros() {
        return maxMacros;
    }

    public void setMaxMacros(int[] maxMacros) {
        this.maxMacros = maxMacros;
    }

    @Override
    public String toString() {
        return name;
    }
}
