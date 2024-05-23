package data;

import javax.swing.*;
import java.io.Serializable;
import java.util.Objects;

public class Meal implements Serializable {
    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private int fiber;
    private String[] ingredients;
    private ImageIcon icon;
    private String tags;

    public Meal(String name, int calories, int protein, int carbs, int fiber, String[] ingredients,ImageIcon icon,String tags) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fiber = fiber;
        this.ingredients = ingredients;
        this.icon = icon;
        this.tags = tags;
        if(this.tags.contains("a"))
        {
            this.tags =this.tags.replace("a","");
        }
    }

    public String getTags() {
        return tags;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Meal m)) {
            return false;
        }
        return Objects.equals(name,m.name);
    }


}

