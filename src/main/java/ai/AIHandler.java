package ai;

import bl.DayPanel;
import bl.IOHandler;
import data.Meal;
import data.Profile;
import data.Score;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static java.lang.Math.abs;

public class AIHandler {

    private DayPanel[] dayPanels;
    private Profile profile;
    private LinkedList<Meal> weeklyFilter;


    public AIHandler(DayPanel[] dayPanels, Profile profile) {

        LinkedList<LinkedList<Meal>> dayList;
        dayList = computeWeek(profile.getMaxMacros(), profile);
        for (int i = 0; i < dayPanels.length; i++) {
            dayPanels[i].getDay().setMeals(dayList.get(i));
            dayPanels[i].updateData();
        }
    }

    /*
    Tags:
    c = chicken     - only one chicken per day
    b = beef        - only one beef per day
    m = multiple    - multiple per day allowed
    d = daily       - daily allowed

    Ideas: Filter before picking meal
    */


    public LinkedList<LinkedList<Meal>> computeWeek(int[] goals, Profile profile) {
        IOHandler io = new IOHandler();
        weeklyFilter = new LinkedList<>();
        int tries = 10000000;
        Score score;
        LinkedList<Meal> allMeals = io.loadMealscsv();
        System.out.println("all" + allMeals.size());
        LinkedList<Meal> chosenMeals;
        LinkedList<LinkedList<Meal>> chosenWeek = new LinkedList<>();
        TreeMap<Score, LinkedList<LinkedList<Meal>>> map = new TreeMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < tries; i++) {
            chosenWeek.clear();
            weeklyFilter.clear();
            weeklyFilter.addAll(allMeals);

            //System.out.println("all"+allMeals.size());
            for (int j = 0; j < profile.getDays().size(); j++) {
                chosenMeals = chooseMealsWithFilter();
                chosenWeek.add(chosenMeals);
            }
            //score = getWeeklyScore(chosenWeek, goals);
            score = getScore(chosenWeek, goals);
            map.put(score, chosenWeek);
            double percent = ((double) i / tries) * 100;
            if (percent % 1 == 0) {
                System.out.println(percent + "%");
                if (percent % 10 == 0) {
                    //System.out.println(percent + "%");
                    long currTime = System.currentTimeMillis();
                    long currTotalTime = currTime - startTime;
                    System.out.println("Time taken so far: " + currTotalTime / 1000 + "seconds");
                    Map.Entry<Score, LinkedList<LinkedList<Meal>>> highscore = map.lastEntry();
                    System.out.println("Current HS: " + highscore.getKey());
                    map.clear();
                    map.put(highscore.getKey(), highscore.getValue());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time taken: " + totalTime + " milliseconds");
        System.out.println("Total time taken: " + totalTime / 1000 + " seconds");
        System.out.println("Best Score " + map.lastEntry().getKey());
        //System.out.println(outPut(map.lastEntry().getValue()));
        //System.out.println("Goals: " + goals[0] + ";" + goals[1] + ";" + goals[2] + ";" + goals[3] + ";");
        return map.lastEntry().getValue();
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

    public LinkedList<Meal> chooseMealsWithFilter() {
        LinkedList<Meal> chosenMeals = new LinkedList<>();
        Random r1 = new Random();
        LinkedList<Meal> dailyFilter = new LinkedList<>();
        dailyFilter.addAll(weeklyFilter);
        StringBuilder tags = new StringBuilder();

        Meal meal;
        int helpCnt = 0;
        int i = 0;

        while (i < r1.nextInt(3, 9)) {
            if (tags.toString().contains("c")) {
                dailyFilter.removeIf(mealtmp -> mealtmp.getTags().contains("c"));
            }
            if (tags.toString().contains("b")) {
                dailyFilter.removeIf(mealtmp -> mealtmp.getTags().contains("b"));
            }
            if (dailyFilter.size() > 1) {
                meal = dailyFilter.get(r1.nextInt(dailyFilter.size() - 1));
            } else if (dailyFilter.size() == 1) {
                meal = dailyFilter.getFirst();
            } else {
                return chosenMeals;
            }

            if (!meal.getTags().contains("d") && !meal.getTags().contains("m")) {
                chosenMeals.add(meal);
                tags.append(meal.getTags());
                dailyFilter.remove(meal);
                weeklyFilter.remove(meal);
            }
            if (meal.getTags().contains("d") && !meal.getTags().contains("m")) {

                chosenMeals.add(meal);
                dailyFilter.remove(meal);

            }
            if (!meal.getTags().contains("d") && meal.getTags().contains("m")) {

                if (helpCnt < 2) {
                    chosenMeals.add(meal);
                    helpCnt++;
                } else {
                    dailyFilter.remove(meal);
                    weeklyFilter.remove(meal);
                }
            }
            if (meal.getTags().contains("d") && meal.getTags().contains("m")) {
                if (helpCnt < 2) {
                    chosenMeals.add(meal);
                    helpCnt++;
                } else {
                    dailyFilter.remove(meal);
                }
            }
            i++;
        }
        // System.out.println(tags);
        return chosenMeals;
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
//                if (i == 1 && macros[0][j] > goals[0] * 1.025) {
//                    deviations[i][j] = deviations[i][j] + 1;
//                }
//                if (i == 1 && macros[0][j] > goals[0] * 1.2) {
//                    deviations[i][j] = deviations[i][j] + 20;
//                }
//                if (i == 2 && macros[1][j] < goals[1] * 0.9) {
//                    deviations[i][j] = deviations[i][j] + 1;
//                }
            }
            if (deviations[0][j] > 0.025) {
                score.addDailyScore(0.0);
            } else if (deviations[1][j] > 0.1) {
                score.addDailyScore(0.0);
            } else {
                score.addDailyScore(1 - ((deviations[0][j] + deviations[1][j] + deviations[2][j] + deviations[3][j]) / 4));
            }

        }

        return score;
    }


}
