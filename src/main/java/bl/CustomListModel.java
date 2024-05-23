package bl;

import data.Meal;

import javax.swing.*;
import java.util.LinkedList;

public class CustomListModel extends AbstractListModel<Meal> {

    private LinkedList<Meal> mealList = new LinkedList<Meal>();
    private final LinkedList<Meal> filteredMealList= new LinkedList<Meal>();

    @Override
    public int getSize() {
        return filteredMealList.size();
    }

    @Override
    public Meal getElementAt(int index) {
        return filteredMealList.get(index);
    }

    public LinkedList<Meal> getMealList() {
        return mealList;
    }

    public void addMeal(Meal meal) {
        mealList.add(meal);
        fireContentsChanged(this,0,mealList.size());
    }

    public void filter(String filter) {
        filteredMealList.clear();
        for(Meal meal:mealList){
            if (meal.getName().contains(filter))
            {
                filteredMealList.add(meal);
            }
        }
        fireContentsChanged(this,0,filteredMealList.size());
    }

    public void setMealList(LinkedList<Meal> mealList) {
        this.mealList = mealList;
        filteredMealList.addAll(mealList);
        fireContentsChanged(this,0,mealList.size());
    }
}
