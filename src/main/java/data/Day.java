package data;

import java.io.Serializable;
import java.util.LinkedList;

public class Day implements Serializable {
    private final String name;
    private int calories;
    private int protein;
    private int carbs;
    private int fiber;
    private LinkedList<Meal> meals;

    public Day(String name, LinkedList<Meal> meals) {
        this.name = name;
        this.meals = meals;
        calcMacros();
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFiber() {
        return fiber;
    }

    public LinkedList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(LinkedList<Meal> meals) {
        this.meals = meals;
        calcMacros();
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        calcMacros();
    }

    public String getMealsString() {
        StringBuilder text = new StringBuilder();
        for (Meal meal:meals)
        {
            text.append("<html>").append(meal.getName()).append("<br>").append("</html>");
        }
        return text.toString();
    }

    public void calcMacros() {
        this.calories = 0;
        this.protein = 0;
        this.carbs = 0;
        this.fiber = 0;
        for (Meal meal : meals) {
            this.calories += meal.getCalories();
            this.protein += meal.getProtein();
            this.carbs += meal.getCarbs();
            this.fiber += meal.getFiber();
        }
    }
}

