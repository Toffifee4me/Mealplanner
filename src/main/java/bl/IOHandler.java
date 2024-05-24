package bl;

import data.Meal;
import data.Profile;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.LinkedList;

public class IOHandler {


    private final File mealPath;
    private final File profilePath;
    private final File mealPathcsv;

    public IOHandler() {
        this.mealPath = Paths.get(System.getProperty("user.dir") + "/Meals.ser").toFile();
        this.profilePath = Paths.get(System.getProperty("user.dir") + "/Profile.ser").toFile();
        this.mealPathcsv = Paths.get(System.getProperty("user.dir") + "/Meals.csv").toFile();
        System.out.println(profilePath);
    }


    public void saveMeals(LinkedList<Meal> meals) {
        try {
            FileOutputStream fileOut = new FileOutputStream(mealPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (Meal meal : meals) {
                out.writeObject(meal);
            }
            out.close();
            fileOut.close();
            System.out.println("Object saved to file.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public LinkedList<Meal> loadMealscsv() {
        LinkedList<Meal> meals = new LinkedList<Meal>();

        try (BufferedReader br = new BufferedReader(new FileReader(mealPathcsv))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(";");
                URL resource = Main.class.getResource("/" + arr[6]);
                assert resource != null;
                meals.add(new Meal(arr[0], Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[5].split(":"), new ImageIcon(resource), arr[7]));
            }
            br.close();
            System.out.println("Meals.csv loaded");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }


        return meals;
    }

    public LinkedList<Meal> loadMeals() {
        LinkedList<Meal> meals = new LinkedList<Meal>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mealPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    Object obj = ois.readObject();

                    Meal meal = (Meal) obj;
                    meals.add(meal);

                } catch (Exception ex) {
                    //Catch the end of the file exception
                    break;
                }
            }
            System.out.println("Meals.ser loaded");
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            System.out.println("Meal file not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return meals;
    }

    public void saveProfile(LinkedList<Profile> profiles) {

        try {
            FileOutputStream fileOut = new FileOutputStream(profilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (Profile profile : profiles) {
                out.writeObject(profile);
            }
            out.close();
            fileOut.close();
            System.out.println("Profiles saved to file.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public LinkedList<Profile> loadProfiles() {
        LinkedList<Profile> profiles = new LinkedList<Profile>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(profilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    Object obj = ois.readObject();

                    Profile profile = (Profile) obj;
                    profiles.add(profile);

                } catch (Exception ex) {
                    //Catch the end of the file exception
                    break;
                }
            }
            ois.close();
            fis.close();
            System.out.println(profiles.size());
            System.out.println("Profiles.ser loaded");
        } catch (FileNotFoundException e) {
            System.out.println("Profile file not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return profiles;
    }
}
