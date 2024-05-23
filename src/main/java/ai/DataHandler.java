package ai;

import bl.DayPanel;
import data.Meal;
import data.Profile;
import data.Score;

import java.util.LinkedList;

import static java.lang.Math.abs;

public class DataHandler {


    private final DayPanel[] dayPanels;
    private final Profile profile;

    public DataHandler(DayPanel[] dayPanels, Profile profile) {
        this.dayPanels = dayPanels;
        this.profile = profile;
    }

    public LinkedList<LinkedList<Meal>> getDayList()
    {
        LinkedList<LinkedList<Meal>> dayList = new LinkedList<>();
        DataModel dm = new DataModel(profile);
        for (int i =0;i<profile.getDays().size();i++)
        {
            dayList.add(dm.getMealsforDay());
        }
        System.out.println(getScore(dayList,profile.getMaxMacros()).toString());
        return dayList;
    }

    public void updatePanels(LinkedList<LinkedList<Meal>> dayList)
    {
        for (int i = 0; i < dayPanels.length; i++) {
            dayPanels[i].getDay().setMeals(dayList.get(i));
            dayPanels[i].updateData();
        }
    }

    public String outPut(LinkedList<LinkedList<Meal>> meals) {
        StringBuilder output = new StringBuilder();
        for (LinkedList<Meal> list : meals) {
            int[] macros = {0, 0, 0, 0};
            for (Meal meal : list) {
                macros[0] += meal.getCalories();
                macros[1] += meal.getProtein();
                macros[2] += meal.getCarbs();
                macros[3] += meal.getFiber();
                output.append(meal.getCalories()).append(";").append(meal.getProtein()).append(";").append(meal.getCarbs()).append(";").append(meal.getFiber()).append(";").append(meal.getName()).append("\n");
            }
            output.append("Macro: ").append(macros[0]).append(";").append(macros[1]).append(";").append(macros[2]).append(";").append(macros[3]).append(";");
        }
        return output.toString();
    }

    public Score getScore(LinkedList<LinkedList<Meal>> list, int[] goals) {
        Score score = new Score();
        int[][] macros = new int[4][list.size()];
        double[][] deviations = new double[4][list.size()];
        double hlp = 0;
        for (int i = 0; i < list.size(); i++) {
            LinkedList<Meal> meals = list.get(i);
            for (Meal meal : meals) {
                macros[0][i] += meal.getCalories();
                macros[1][i] += meal.getProtein();
                macros[2][i] += meal.getCarbs();
                macros[3][i] += meal.getFiber();
            }
        }
        for (int j = 0; j < list.size(); j++) {
            for (int i = 0; i < 4; i++) {
                deviations[i][j] = (double) abs(macros[i][j] - goals[i]) / goals[i];
            }
            score.addDailyScore(1 - ((deviations[0][j] + deviations[1][j] + deviations[2][j] + deviations[3][j]) / 4));
        }
        return score;
    }
}
