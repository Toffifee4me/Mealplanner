package AI;

import BL.IOHandler;
import Data.Meal;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.LinkedList;

/** MIP example with a variable array. */
public class MipVarArray {
    static class DataModel {

        public final double[] upperBounds = {2100, 150, 235, 35};
        public final double[] lowerBounds = {1900, 130, 215, 25};
        public final int numConstraints = 4;
    }

    public static void main(String[] args) throws Exception {
        IOHandler io = new IOHandler();
        LinkedList<Meal> allMeals = io.loadMealscsv();
        LinkedList<Meal> meals = new LinkedList<>();
        double[][] constraintCoeffs = new double[4][allMeals.size()];
        int numVars = 0;
        if  (allMeals.isEmpty())
        {
             numVars = 1;
        }
        else {
             numVars = allMeals.size();
        }

        for (int i = 0; i < allMeals.size();i++)
        {
            Meal meal = allMeals.get(i);
            constraintCoeffs[0][i]=meal.getCalories();
            constraintCoeffs[1][i]=meal.getProtein();
            constraintCoeffs[2][i]=meal.getCarbs();
            constraintCoeffs[3][i]=meal.getFiber();
        }


        Loader.loadNativeLibraries();
        final DataModel data = new DataModel();

        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return;
        }
        MPVariable[] x = new MPVariable[numVars];
        for (int j = 0; j < numVars; ++j) {
            x[j] = solver.makeIntVar(0.0, 1, "");
        }
        System.out.println("Number of variables = " + solver.numVariables());

        // Create the constraints.
        for (int i = 0; i < data.numConstraints; ++i) {
            MPConstraint constraint = solver.makeConstraint(data.lowerBounds[i], data.upperBounds[i], "");
            for (int j = 0; j < numVars; ++j) {
                constraint.setCoefficient(x[j], constraintCoeffs[i][j]);
            }
        }
        System.out.println("Number of constraints = " + solver.numConstraints());

        MPObjective objective = solver.objective();
        for (int j = 0; j < numVars; ++j) {
            objective.setCoefficient(x[j],1);
        }
        objective.setMaximization();

        final MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Objective value = " + objective.value());
            for (int j = 0; j < numVars; ++j) {
                System.out.println("x[" + j + "] = " + x[j].solutionValue());
                if(x[j].solutionValue()==1)
                {
                    meals.add(allMeals.get(j));
                }
            }
            System.out.println();
            System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
            System.out.println("Problem solved in " + solver.iterations() + " iterations");
            System.out.println("Problem solved in " + solver.nodes() + " branch-and-bound nodes");
        } else {
            System.err.println("The problem does not have an optimal solution.");
        }
        LinkedList<LinkedList<Meal>> meals2 = new LinkedList<>();
        meals2.add(meals);
        System.out.println(outPut(meals2));

    }

    public static String outPut(LinkedList<LinkedList<Meal>> meals) {
        StringBuilder output = new StringBuilder();
        int[] goals = {2000,140,225,30};
        for (LinkedList<Meal> list : meals) {
            int[] macros = {0, 0, 0, 0};
            for (Meal meal : list) {
                macros[0] += meal.getCalories();
                macros[1] += meal.getProtein();
                macros[2] += meal.getCarbs();
                macros[3] += meal.getFiber();
                output.append(meal.getCalories()).append(";").append(meal.getProtein()).append(";").append(meal.getCarbs()).append(";").append(meal.getFiber()).append(";").append(meal.getName()).append("\n");
            }
            output.append("Macro: ").append(macros[0]).append(";").append(macros[1]).append(";").append(macros[2]).append(";").append(macros[3]).append("\n");
            output.append("Goals: ").append(goals[0]).append(";").append(goals[1]).append(";").append(goals[2]).append(";").append(goals[3]);
        }
        return output.toString();
    }

}
