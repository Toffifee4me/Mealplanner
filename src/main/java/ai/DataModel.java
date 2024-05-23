package ai;

import bl.IOHandler;
import data.Meal;
import data.Profile;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.LinkedList;

public class DataModel {


    private final double[] upperBounds;
    private final double[] lowerBounds;
    private LinkedList<Meal> filteredMeals;

    public DataModel(Profile profile) {
        int[] goals = profile.getMaxMacros();
        upperBounds = new double[]{goals[0]*1.05, goals[1]*1.1, goals[2]*1.1, goals[3]*1.1};
        lowerBounds = new double[]{goals[0]*0.95, goals[1]*0.9, goals[2]*0.9, goals[3]*0.9};
        IOHandler io = new IOHandler();
        LinkedList<Meal> allMeals = io.loadMealscsv();
        filteredMeals=allMeals;
    }

    public LinkedList<Meal> getFilteredMeals() {
        return filteredMeals;
    }

    public void setFilteredMeals(LinkedList<Meal> filteredMeals) {
        this.filteredMeals = filteredMeals;
    }

    public LinkedList<Meal> getMealsforDay()
    {
        LinkedList<Meal> meals = new LinkedList<>();
        double[][] constraintCoeffs = new double[4][filteredMeals.size()];
        int numVars = 0;
        if  (filteredMeals.isEmpty())
        {
            numVars = 1;
        }
        else {
            numVars = filteredMeals.size();
        }

        for (int i = 0; i < filteredMeals.size();i++)
        {
            Meal meal = filteredMeals.get(i);
            constraintCoeffs[0][i]=meal.getCalories();
            constraintCoeffs[1][i]=meal.getProtein();
            constraintCoeffs[2][i]=meal.getCarbs();
            constraintCoeffs[3][i]=meal.getFiber();
        }


        Loader.loadNativeLibraries();


        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        MPVariable[] x = new MPVariable[numVars];
        for (int j = 0; j < numVars; ++j) {
            x[j] = solver.makeIntVar(0.0, 1, "");
        }
        System.out.println("Number of variables = " + solver.numVariables());

        // Create the constraints.
        int numConstraints = 4;
        for (int i = 0; i < numConstraints; ++i) {
            MPConstraint constraint = solver.makeConstraint(lowerBounds[i], upperBounds[i], "");
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
                //System.out.println("x[" + j + "] = " + x[j].solutionValue());
                if(x[j].solutionValue()==1)
                {
                    Meal meal = filteredMeals.get(j);
                    meals.add(meal);
                    if (!meal.getTags().contains("d")) {
                        filteredMeals.remove(meal);
                        numVars--;
                    }
                }
            }
            System.out.println();
            System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
            System.out.println("Problem solved in " + solver.iterations() + " iterations");
            System.out.println("Problem solved in " + solver.nodes() + " branch-and-bound nodes");
        } else {
            System.err.println("The problem does not have an optimal solution.");
        }
       return meals;

    }
}
