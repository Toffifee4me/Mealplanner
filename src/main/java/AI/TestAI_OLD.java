package AI;

import BL.DayPanel;
import BL.IOHandler;
import Data.Meal;
import Data.Profile;

import java.util.*;

import static java.lang.Math.abs;

public class TestAI_OLD {

    private DayPanel[] dayPanels;
    private Profile profile;


    public TestAI_OLD(DayPanel[] dayPanels, Profile profile) {

        LinkedList<LinkedList<Meal>> dayList = new LinkedList<LinkedList<Meal>>();
//        for (int i = 0; i < dayPanels.length; i++) {
//            System.out.println(dayPanels[i].getDay().getName());
//            LinkedList<Meal> helplist = computeDay(dayList, profile.getMaxMacros());
//            dayList.add(helplist);
//        }
        dayList=computeWeek(profile.getMaxMacros(),profile);

        for (int i = 0; i < dayPanels.length; i++) {
            dayPanels[i].getDay().setMeals(dayList.get(i));
            dayPanels[i].updateData();
        }

    }

    public LinkedList<Meal> computeDay(LinkedList<LinkedList<Meal>> dayList, int[] goals) {
        IOHandler io = new IOHandler();
        int tries = 100000;
        double score = 0;
        LinkedList<Meal> allMeals = io.loadMealscsv();
        LinkedList<Meal> chosenMeals;
        TreeMap<Double, LinkedList<Meal>> map = new TreeMap<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < tries; i++) {
            //chosenMeals = tAI.chooseMeals(allMeals);
            //chosenMeals  = new LinkedList<>(tAI.chooseMealsNoMultiples(allMeals));
            //chosenMeals = chooseMealsWithTags(allMeals);
            chosenMeals = chooseMealsWithTagsAndDayList(allMeals, dayList);
            score = getScore(chosenMeals, goals);
            map.put(score, chosenMeals);
//            double percent = ((double) i / tries) * 100;
//            if (percent % 1 == 0) {
//                 System.out.println(percent + "%");
//                if (percent % 10 == 0) {
//                     System.out.println(percent+"%");
//                     long currTime = System.currentTimeMillis();
//                     long currTotalTime = currTime - startTime;
//                     System.out.println("Total time taken: " + currTotalTime + " milliseconds");
//                }
//            }

        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time taken: " + totalTime + " milliseconds");
        System.out.println("Best Score " + map.lastEntry().getKey());
        System.out.println(outPut(map.lastEntry().getValue()));
        System.out.println("Goals: " + goals[0] + ";" + goals[1] + ";" + goals[2] + ";" + goals[3] + ";");
        return map.lastEntry().getValue();
    }

    public LinkedList<LinkedList<Meal>> computeWeek(int[] goals,Profile profile) {
        IOHandler io = new IOHandler();
        int tries = 100000;
        double score = 0;
        LinkedList<Meal> allMeals = io.loadMealscsv();
        LinkedList<Meal> chosenMeals;
        LinkedList<LinkedList<Meal>> chosenWeek = new LinkedList<>();
        TreeMap<Double,  LinkedList<LinkedList<Meal>>> map = new TreeMap<>();
        long startTime = System.currentTimeMillis();
        System.out.println(profile.getDays().size());
        for (int i = 0; i < tries; i++) {
            chosenWeek.clear();
            for (int j = 0; j <profile.getDays().size() ; j++) {
                chosenMeals = chooseMealsWithTagsAndDayList(allMeals, chosenWeek);
                chosenWeek.add(chosenMeals);
            }
            score = getWeeklyScore(chosenWeek, goals);
            map.put(score, chosenWeek);
            double percent = ((double) i / tries) * 100;
            if (percent % 1 == 0) {
                 //System.out.println(percent + "%");
                if (percent % 10 == 0) {
                     System.out.println(percent+"%");
                     long currTime = System.currentTimeMillis();
                     long currTotalTime = currTime - startTime;
                     System.out.println("Total time taken: " + currTotalTime + " milliseconds");
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time taken: " + totalTime + " milliseconds");
        System.out.println("Best Score " + map.lastEntry().getKey());
        //System.out.println(outPut(map.lastEntry().getValue()));
        System.out.println("Goals: " + goals[0] + ";" + goals[1] + ";" + goals[2] + ";" + goals[3] + ";");
        return map.lastEntry().getValue();
    }


    public String getMealsString(LinkedList<Meal> meals) {
        final String[] text = {""};
        meals.forEach(meal -> text[0] += meal.getName() + ";");
        return text[0];
    }

    public String outPut(LinkedList<Meal> meals) {
        String output = "";
        int[] macros = {0, 0, 0, 0};
        for (Meal meal : meals) {
            macros[0] += meal.getCalories();
            macros[1] += meal.getProtein();
            macros[2] += meal.getCarbs();
            macros[3] += meal.getFiber();
            output += meal.getCalories() + ";" + meal.getProtein() + ";" + meal.getCarbs() + ";" + meal.getFiber() + ";" + meal.getName() + "\n";
        }
        output += "Macro: " + macros[0] + ";" + macros[1] + ";" + macros[2] + ";" + macros[3] + ";";
        return output;
    }

    public LinkedList<Meal> chooseMeals(LinkedList<Meal> allMeals) {
        LinkedList<Meal> chosenMeals = new LinkedList<>();
        Random r1 = new Random();

        //Count of Meals
        for (int i = 0; i < r1.nextInt(allMeals.size() - 1); i++) {
            //Choose Random Meal
            chosenMeals.add(allMeals.get(r1.nextInt(allMeals.size() - 1)));
        }
        return chosenMeals;
    }

    public HashSet<Meal> chooseMealsNoMultiples(LinkedList<Meal> allMeals) {
        HashSet<Meal> chosenMeals = new HashSet<>();
        Random r1 = new Random();
        Meal meal;
        //Count of Meals
        for (int i = 0; i < r1.nextInt(allMeals.size() - 1); i++) {
            //Choose Random Meal

            chosenMeals.add(allMeals.get(r1.nextInt(allMeals.size() - 1)));
        }
        //System.out.println(chosenMeals.size());
        return chosenMeals;
    }

    public LinkedList<Meal> chooseMealsWithTags(LinkedList<Meal> allMeals) {
        LinkedList<Meal> chosenMeals = new LinkedList<>();
        Random r1 = new Random();
        String tags = "";
        //Count of Meals
        for (int i = 0; i < r1.nextInt(allMeals.size() - 1); i++) {
            //Choose Random Meal
            Meal meal = allMeals.get(r1.nextInt(allMeals.size() - 1));
            if (!(meal.getTags().contains("c") && tags.contains("c")) && !(meal.getTags().contains("b") && tags.contains("b"))) {
                if (!chosenMeals.contains(meal) || meal.getTags().contains("m")) {
                    chosenMeals.add(meal);
                    tags += meal.getTags();
                }
            }
        }
        // System.out.println(tags);
        return chosenMeals;
    }

    public LinkedList<Meal> chooseMealsWithTagsAndDayList(LinkedList<Meal> allMeals, LinkedList<LinkedList<Meal>> dayList) {
        LinkedList<Meal> chosenMeals = new LinkedList<>();
        Random r1 = new Random();
        String tags = "";
        //Count of Meals
        // r1.nextInt(allMeals.size() - 1)
        int i = 0;
        while (i < r1.nextInt(4, 9)) {
            //Choose Random Meal
            Meal meal = allMeals.get(r1.nextInt(allMeals.size() - 1));
            if (!(meal.getTags().contains("c") && tags.contains("c")) && !(meal.getTags().contains("b") && tags.contains("b"))) {
                if (!chosenMeals.contains(meal) || meal.getTags().contains("m")) {
                    if (!dayList.isEmpty()) {
                        //System.out.println(getMealsString(dayList.getLast()));
                        //System.out.println(meal.getName()+"CHECK"+dayList.getLast().contains(meal));
                        if (!dayList.getLast().contains(meal) || meal.getTags().contains("d")) {
                            chosenMeals.add(meal);
                            tags += meal.getTags();
                            i++;
                        }
                    } else {
                        chosenMeals.add(meal);
                        tags += meal.getTags();
                        i++;
                    }
                }
            }
        }
        // System.out.println(tags);
        return chosenMeals;
    }


    public double getScore(LinkedList<Meal> list, int[] goals) {
        double score = 0;
        int[] macros = {0, 0, 0, 0};
        double[] deviations = {0, 0, 0, 0};

        for (Meal meal : list) {
            macros[0] += meal.getCalories();
            macros[1] += meal.getProtein();
            macros[2] += meal.getCarbs();
            macros[3] += meal.getFiber();
        }
        for (int i = 0; i < 4; i++) {
            deviations[i] = (double) abs(macros[i] - goals[i]) / goals[i];
            if (i == 1 && macros[0] > goals[0] * 1.025) {
                deviations[i] = 1;
            }
            if (i == 2 && macros[1] < goals[1] * 0.9) {
                deviations[i] = 1;
            }
        }
        //System.out.println(deviations[0]+":"+deviations[1]+":"+deviations[2]+":"+deviations[3]);
        score = 1 - ((deviations[0] + deviations[1] + deviations[2] + deviations[3]) / 4);
        return score;
    }

    public double getWeeklyScore(LinkedList<LinkedList<Meal>> list, int[] goals) {
        double[] score = new double[list.size()];
        double weeklyScore = 0;
        int[][] macros = new int[4][list.size()];
        double[][] deviations = new double[5][list.size()];
        double hlp = 0;
        HashMap<Meal, Integer> countMap = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            LinkedList<Meal> meals = list.get(i);
            for (Meal meal : meals) {
                macros[0][i] += meal.getCalories();
                macros[1][i] += meal.getProtein();
                macros[2][i] += meal.getCarbs();
                macros[3][i] += meal.getFiber();
                if (countMap.containsKey(meal) && !meal.getTags().contains("d")) {
                    countMap.put(meal, countMap.get(meal) + 1);
                    deviations[4][i] = Math.max(deviations[4][i],0.15) * countMap.get(meal);

                } else if (!meal.getTags().contains("d")){
                    countMap.put(meal, 1);
                }
            }
        }
        List<Map.Entry<Meal, Integer>> entries = new ArrayList<>(countMap.entrySet());

// Sort entries by value using custom comparator
        Collections.sort(entries, new Comparator<Map.Entry<Meal, Integer>>() {
            public int compare(Map.Entry<Meal, Integer> a, Map.Entry<Meal, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        // Print sorted entries
//        for (Map.Entry<Meal, Integer> entry : entries) {
//            System.out.println(entry.getKey() + " repeated " + entry.getValue() + " times");
//        }

        for (int j = 0; j < list.size(); j++) {
            for (int i = 0; i < 4; i++) {
                deviations[i][j] = (double) abs(macros[i][j] - goals[i]) / goals[i];
                if (i == 1 && macros[0][j] > goals[0] * 1.025) {
                    deviations[i][j] = deviations[i][j]+1;
                }
                if (i == 2 && macros[1][j] < goals[1] * 0.9) {
                    deviations[i][j] = deviations[i][j]+1;
                }
            }
            score[j] = 1 - ((deviations[0][j] + deviations[1][j] + deviations[2][j] + deviations[3][j]+ deviations[4][j]) / 5);
            hlp = hlp + score[j];
        }
        //System.out.println(deviations[0]+":"+deviations[1]+":"+deviations[2]+":"+deviations[3]);
        weeklyScore = hlp / list.size();
        return weeklyScore;
    }

}
