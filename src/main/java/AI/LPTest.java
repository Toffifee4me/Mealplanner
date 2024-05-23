package AI;

import BL.DayPanel;
import BL.IOHandler;
import Data.Meal;
import Data.Profile;
import Data.Score;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static java.lang.Math.abs;

public class LPTest {
    private DayPanel[] dayPanels;
    private Profile profile;
    private LinkedList<Meal> weeklyFilter;


    public static void main(String[] args) {
        LinkedList<Meal> dayList;
        LinkedList<LinkedList<Meal>> weeklyMeals = new LinkedList<>();
        IOHandler io = new IOHandler();
        int[] goals = {2000,140,225,30};
        LinkedList<Meal> allMeals = io.loadMealscsv();
        for (int i = 0; i < 1; i++) {
            dayList = chooseMealsWithLP(allMeals,goals);
            weeklyMeals.add(dayList);
        }
        System.out.println(outPut(weeklyMeals));
    }


    public static String outPut(LinkedList<LinkedList<Meal>> meals) {
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


    public static LinkedList<Meal> chooseMealsWithLP(LinkedList<Meal> filteredMeals, int[] goals)
    {
        LinkedList<Meal> chosenMeals = new LinkedList<>();
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return chosenMeals;
        }

        MPVariable x = solver.makeIntVar(0.0, 1, "x");

        System.out.println("Number of variables = " + solver.numVariables());

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
