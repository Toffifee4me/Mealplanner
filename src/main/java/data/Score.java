package data;

import java.util.LinkedList;

public class Score implements Comparable<Score>{
    private double weeklyscore;
    private LinkedList<Double> dailyscores = new LinkedList<>();


    public double getWeeklyScore() {
        calcWeeklyScore();
        return weeklyscore;
    }

    public void setWeeklyScore(double weeklyscore) {
        this.weeklyscore = weeklyscore;
    }

    public LinkedList<Double> getDailyScores() {
        return dailyscores;
    }

    public void setDailyScores(LinkedList<Double> dailyscores) {
        this.dailyscores = dailyscores;
    }

    public void addDailyScore(Double score) {
        dailyscores.add(score);
        calcWeeklyScore();
    }

    public void calcWeeklyScore() {
        Double hlp = 0.0;
        for (Double aDouble : dailyscores) {
            hlp += aDouble;
        }
        weeklyscore = hlp / dailyscores.size();

    }

    @Override
    public String toString() {
        String string = weeklyscore+"| ";
        for (Double d : dailyscores)
        {
            string+=d+";";
        }
        return string;
    }

    @Override
    public int compareTo(Score o) {
        return Double.compare(this.weeklyscore,o.weeklyscore);
    }
}
