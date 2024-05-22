package BL;

import Data.Day;
import Data.Meal;
import Events.MyEvent;
import Events.MyEventListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class DayPanel extends JPanel {

    private final Day day;
    private final JLabel labelProt;
    private final JLabel labelFib;
    private final JLabel labelCarb;
    private final JLabel labelCal;
    private final JList<Meal> labelMenu;
    private final JLabel labelName;
    private DefaultListModel<Meal> dlm;

    private ArrayList<MyEventListener> listeners = new ArrayList<MyEventListener>();

    public DayPanel(Day day) {
        this.day = day;
        //setLayout(new GridLayout(6, 1));


        setBorder(new BevelBorder(BevelBorder.RAISED));

        labelName = new JLabel(day.getName());
        labelName.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        labelName.setHorizontalAlignment(SwingConstants.CENTER);

        dlm = new DefaultListModel<>();

        labelMenu = new JList<>(dlm);
        dlm.addAll(day.getMeals());
        labelMenu.setBorder(new MatteBorder(0, 0, 2, 0, Color.lightGray));

        labelMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Do something when label is clicked
                JDialog dialog = createDialog();
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });

        labelProt = new JLabel(day.getProtein() + "g Protein");
        labelProt.setHorizontalAlignment(SwingConstants.CENTER);
        labelProt.setBorder(new MatteBorder(0, 0, 2, 0, Color.lightGray));

        labelCarb = new JLabel(day.getCarbs() + "g Carbs");
        labelCarb.setHorizontalAlignment(SwingConstants.CENTER);
        labelCarb.setBorder(new MatteBorder(0, 0, 2, 0, Color.lightGray));

        labelFib = new JLabel(day.getFiber() + "g Fibers");
        labelFib.setHorizontalAlignment(SwingConstants.CENTER);
        labelFib.setBorder(new MatteBorder(0, 0, 2, 0, Color.lightGray));

        labelCal = new JLabel(day.getCalories() + "cal");
        labelCal.setHorizontalAlignment(SwingConstants.CENTER);

        labelMenu.setPreferredSize(new Dimension(0, 20 * labelMenu.getModel().getSize()));
        CustomGridLAyout cgl = new CustomGridLAyout(6, 1);
        setLayout(cgl);


        add(labelName);
        add(labelMenu);
        add(labelProt);
        add(labelCarb);
        add(labelFib);
        add(labelCal);
    }


    public Day getDay() {
        return day;
    }

    public void updateData() {
        labelName.setText(day.getName());
        dlm.clear();
        dlm.addAll(day.getMeals());
        labelCal.setText(day.getCalories() + "cal /" + labelCal.getText().split("/")[1]);
        labelProt.setText(day.getProtein() + "g /" + labelProt.getText().split("/")[1]);
        labelCarb.setText(day.getCarbs() + "g /" + labelCarb.getText().split("/")[1]);
        labelFib.setText(day.getFiber() + "g /" + labelFib.getText().split("/")[1]);
        MyEvent event = new MyEvent(DayPanel.this, "Hello World");
        DayPanel.this.fireEvent(event);
    }

    public void updateData(int[] maxMacros) {
        labelName.setText(day.getName());
        dlm.clear();
        dlm.addAll(day.getMeals());
        // labelMenu.setText(day.getMealsString());
        labelCal.setText(day.getCalories() + "cal / " + maxMacros[0] + "cal");
        labelProt.setText(day.getProtein() + "g / " + maxMacros[1] + "g Protein");
        labelCarb.setText(day.getCarbs() + "g / " + maxMacros[2] + "g Carbs");
        labelFib.setText(day.getFiber() + "g / " + maxMacros[3] + "g Fiber");
        MyEvent event = new MyEvent(DayPanel.this, "Hello World");
        DayPanel.this.fireEvent(event);
    }

    private JDialog createDialog() {
        IOHandler io = new IOHandler();
        JDialog dialog = new JDialog();
        JPanel buttonPane = new JPanel();

        JList<Meal> list = new JList<Meal>();
        CustomListModel clm = new CustomListModel();

        JTextField searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }

            private void applyFilter() {
                clm.filter(searchField.getText());
            }
        });


        LinkedList<Meal> mealsGlobal = new LinkedList<>();

        dialog.setTitle("Meals");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.add(searchField, BorderLayout.NORTH);
        try {
            //mealsGlobal = io.loadMeals();
            mealsGlobal.addAll(io.loadMealscsv());
        } catch (Exception ex) {
            System.out.println("Could not load meals");
            ex.printStackTrace();
        }
        clm.setMealList(mealsGlobal);

        list.setModel(clm);
        list.setCellRenderer(new CustomCellRenderer());
        list.setSelectionModel(new CustomListSelectionModel());


        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        dialog.add(scrollPane, BorderLayout.CENTER);
        buttonPane.setLayout(new GridLayout(3, 1));

        JButton buttonChooseMeal = new JButton("Choose Meals");
        buttonChooseMeal.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkedList<Meal> mealsTemp = new LinkedList<>(list.getSelectedValuesList());
                day.setMeals(mealsTemp);
                dialog.dispose();
                updateData();

            }
        });
        buttonPane.add(buttonChooseMeal);

        JButton buttonAddMeal = new JButton("Add Meals");
        buttonAddMeal.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog createMealDialog = createMealDialog(clm);
                createMealDialog.setModal(true);
                createMealDialog.setVisible(true);
            }
        });
        buttonPane.add(buttonAddMeal);

        JButton buttonSafeMeal = new JButton("Safe Meals");
        buttonSafeMeal.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                io.saveMeals(clm.getMealList());
            }
        });
        buttonPane.add(buttonSafeMeal);
        dialog.add(buttonPane, BorderLayout.SOUTH);
        return dialog;
    }

    private JDialog createMealDialog(CustomListModel clm) {
        JDialog newProfileDialog = new JDialog();
        newProfileDialog.setTitle("new Meal");
        newProfileDialog.setSize(400, 300);
        newProfileDialog.setLocationRelativeTo(null);
        newProfileDialog.setLayout(new GridLayout(7, 2));
        Component[][] compArrDia = new Component[7][2];
        String[] titles = {"Name: ", "Calories: ", "Protein: ", "Carbs: ", "Fiber: ", "Ingredients: (split with';')"};

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    JLabel lb = new JLabel();
                    lb.setText(titles[i]);
                    compArrDia[i][j] = lb;
                    newProfileDialog.add(compArrDia[i][j]);
                } else {
                    JTextField tf = new JTextField();
                    compArrDia[i][j] = tf;
                    newProfileDialog.add(compArrDia[i][j]);
                }
            }
        }
        JButton confirmButt = new JButton("confirm");
        confirmButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ((JTextField) compArrDia[0][1]).getText();
                int calories = Integer.parseInt(((JTextField) compArrDia[1][1]).getText());
                int protein = Integer.parseInt(((JTextField) compArrDia[2][1]).getText());
                int carbs = Integer.parseInt(((JTextField) compArrDia[3][1]).getText());
                int fiber = Integer.parseInt(((JTextField) compArrDia[4][1]).getText());
                String[] ing = ((JTextField) compArrDia[5][1]).getText().split(";");
                Meal newMeal = new Meal(name, calories, protein, carbs, fiber, ing, null, null);
                clm.addMeal(newMeal);
                newProfileDialog.dispose();
            }
        });
        JButton cancelButt = new JButton("cancel");
        cancelButt.addActionListener(e -> newProfileDialog.dispose());

        newProfileDialog.add(confirmButt);
        newProfileDialog.add(cancelButt);
        return newProfileDialog;
    }

    public void addMyEventListener(MyEventListener listener) {
        listeners.add(listener);
    }

    public void removeMyEventListener(MyEventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(MyEvent event) {
        for (MyEventListener listener : listeners) {
            listener.handleMyEvent(event);
        }
    }
}
