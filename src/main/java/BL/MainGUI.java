package BL;

import AI.AIHandler;
import Data.Day;
import Data.Meal;
import Data.Profile;
import Events.MyEvent;
import Events.MyEventListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.LinkedList;

public class MainGUI {
    CustomComboBoxModel ccbm;
    private JPanel panelCenter;
    private Component[][] compArrTL;
    private DayPanel[] dayPanels;
    private JFrame frame;
    private JLabel labelMaxCalories;
    private JLabel labelMaxProtein;
    private JLabel labelMaxCarbs;
    private JLabel labelMaxFiber;
    private Profile profile;


    private static LinkedList<Day> createDayList(int count) {
        LinkedList<Day> days = new LinkedList<>();
        String name = "";
        for (int i = 1; i <= count; i++) {
            if (i % 7 == 1) name = "Monday";
            if (i % 7 == 2) name = "Tuesday";
            if (i % 7 == 3) name = "Wednesday";
            if (i % 7 == 4) name = "Thursday";
            if (i % 7 == 5) name = "Friday";
            if (i % 7 == 6) name = "Saturday";
            if (i % 7 == 0) name = "Sunday";
            Day day = new Day(name, new LinkedList<Meal>());
            days.add(day);
        }

        return days;
    }

    public void setupGUI() {
        JPanel panelTop = new JPanel();
        IOHandler io = new IOHandler();
        JPanel panelTL = new JPanel();
        String[] arr = {"Calories", "Protein", "Carbs", "Fiber"};
        JPanel panelTR = new JPanel();
        JComboBox<Profile> box;
        JDialog newProfileDialog;
        JButton loadButton;
        JButton saveButton;
        JButton addButton;
        JPanel panelBot = new JPanel();
        JPanel panelTC = new JPanel();

        panelTL.setBorder(new TitledBorder("Macro Goals"));
        panelTR.setBorder(new TitledBorder("Profiles"));
        panelBot.setBorder(new TitledBorder("Weekly progress"));

        //Default Data
        ccbm = new CustomComboBoxModel(io.loadProfiles());
        if (ccbm.getProfiles().isEmpty()) {
            LinkedList<Day> dl = new LinkedList<>();
            dl.add(new Day("Default", new LinkedList<Meal>()));
            Profile profile = new Profile("Default", dl, new int[]{0, 0, 0, 0});
            ccbm.addProfiles(profile);
        }
        profile = ccbm.getProfiles().getFirst();

        //Setup GUI
        frame = new JFrame("Meal planner");
        frame.setSize(1400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        labelMaxCalories = new JLabel();
        labelMaxCalories.setHorizontalAlignment(SwingConstants.CENTER);
        labelMaxProtein = new JLabel();
        labelMaxProtein.setHorizontalAlignment(SwingConstants.CENTER);
        labelMaxCarbs = new JLabel();
        labelMaxCarbs.setHorizontalAlignment(SwingConstants.CENTER);
        labelMaxFiber = new JLabel();
        labelMaxFiber.setHorizontalAlignment(SwingConstants.CENTER);

        //Center panel
        panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(1, profile.getDays().size()));

        dayPanels = new DayPanel[profile.getDays().size()];
        for (int i = 0; i < profile.getDays().size(); i++) {
            dayPanels[i] = new DayPanel(profile.getDays().get(i));
            dayPanels[i].updateData(profile.getMaxMacros());
            panelCenter.add(dayPanels[i]);
        }
        for (DayPanel dayPanel : dayPanels) {
            dayPanel.addMyEventListener(new MyEventListener() {
                @Override
                public void handleMyEvent(MyEvent event) {
                    maxmaxMacros(labelMaxCalories, labelMaxProtein, labelMaxCarbs, labelMaxFiber);
                    frame.validate();
                    frame.repaint();
                }
            });
        }

        //Top panel
        panelTop.setLayout(new BorderLayout());
        panelTL.setLayout(new GridLayout(4, 2));
        compArrTL = new Component[4][2];
        for (int i = 0; i < 4; i++) {
            JLabel lb = new JLabel(arr[i]);
            lb.setHorizontalAlignment(SwingConstants.CENTER);
            compArrTL[i][0] = lb;
        }
        for (int i = 0; i < 4; i++) {
            JTextField tf = new JTextField();
            tf.setText(profile.getMaxMacros()[i] + "");
            compArrTL[i][1] = tf;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 1) {
                    JTextField tf = (JTextField) compArrTL[i][j];
                    tf.addActionListener(new CustomActionListener(compArrTL, dayPanels, profile));
                }
                panelTL.add(compArrTL[i][j]);
            }
        }
        panelTop.add(panelTL, BorderLayout.WEST);
        panelTR.setLayout(new GridLayout(4, 1));

        box = new JComboBox<Profile>();
        box.setModel(ccbm);
        box.addActionListener(e -> {
            Profile profileloadoad = (Profile) box.getSelectedItem();
            if (profileloadoad != null) {
                profile = profileloadoad;
                setGUIData(profile);
            }

        });
        panelTR.add(box);
        newProfileDialog = createProfileDialog();
        loadButton = new JButton("Load Profiles");
        loadButton.addActionListener(e -> {
            ccbm.setProfiles(io.loadProfiles());
        });
        saveButton = new JButton("Safe Profiles");
        saveButton.addActionListener(e -> {
            io.saveProfile(ccbm.getProfiles());
        });
        addButton = new JButton("Add Profile");
        addButton.addActionListener(e -> {
            newProfileDialog.setModal(true);
            newProfileDialog.setVisible(true);
        });
        panelTR.add(loadButton);
        panelTR.add(saveButton);
        panelTR.add(addButton);
        panelTop.add(panelTR, BorderLayout.EAST);

        JButton buttonAI = new JButton("AI");
        buttonAI.addActionListener(e -> {
            //TestAI tAI = new TestAI(dayPanels, profile);
            AIHandler aiH = new AIHandler(dayPanels,profile);
            //updateListSize();
            frame.validate();
            frame.repaint();
        });
        panelTC.setLayout(new BorderLayout());
        panelTC.add(buttonAI, BorderLayout.CENTER);
        panelTop.add(panelTC);


        //Bottom panel
        panelBot.setLayout(new GridLayout(1, 4));
        maxmaxMacros(labelMaxCalories, labelMaxProtein, labelMaxCarbs, labelMaxFiber);
        panelBot.add(labelMaxCalories);
        panelBot.add(labelMaxProtein);
        panelBot.add(labelMaxCarbs);
        panelBot.add(labelMaxFiber);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(panelCenter, BorderLayout.CENTER);
        frame.add(panelBot, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void setGUIData(Profile profile) {
        //Center panel
        panelCenter.removeAll();
        panelCenter.setLayout(new GridLayout(1, profile.getDays().size()));
        dayPanels = new DayPanel[profile.getDays().size()];
        for (int i = 0; i < profile.getDays().size(); i++) {
            dayPanels[i] = new DayPanel(profile.getDays().get(i));
            dayPanels[i].updateData(profile.getMaxMacros());
            panelCenter.add(dayPanels[i]);
        }
//        for (DayPanel dayPanel : dayPanels) {
//            dayPanel.addMyEventListener(new MyEventListener() {
//                @Override
//                public void handleMyEvent(MyEvent event) {
//                    maxmaxMacros(labelMaxCalories, labelMaxProtein, labelMaxCarbs, labelMaxFiber);
//                    updateListSize();
//                    frame.validate();
//                    frame.repaint();
//                }
//            });
//        }

        DayPanel dayPanel =dayPanels[profile.getDays().size()-1];
        dayPanel.addMyEventListener(new MyEventListener() {
            @Override
            public void handleMyEvent(MyEvent event) {
                maxmaxMacros(labelMaxCalories, labelMaxProtein, labelMaxCarbs, labelMaxFiber);
                updateListSize();
                frame.validate();
                frame.repaint();
            }
        });

        //Top panel
        for (int i = 0; i < 4; i++) {
            JTextField tf = (JTextField) compArrTL[i][1];
            tf.setText(profile.getMaxMacros()[i] + "");
            tf.addActionListener(new CustomActionListener(compArrTL, dayPanels, profile));
        }
        maxmaxMacros(labelMaxCalories, labelMaxProtein, labelMaxCarbs, labelMaxFiber);
        updateListSize();
        frame.validate();
        frame.repaint();
    }

    public JDialog createProfileDialog() {
        JDialog newProfileDialog = new JDialog();
        newProfileDialog.setTitle("Profiles");
        newProfileDialog.setSize(400, 300);
        newProfileDialog.setLocationRelativeTo(null);
        newProfileDialog.setLayout(new GridLayout(7, 2));

        Component[][] compArrDia = new Component[7][2];
        String[] titles = {"Name: ", "Day count: ", "Max Calories: ", "Max Protein: ", "Max Carbs: ", "Max Fiber: "};

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
        JButton confirmButt = getConfirmButton(compArrDia, newProfileDialog);
        JButton cancelButt = new JButton("cancel");
        cancelButt.addActionListener(e -> newProfileDialog.dispose());

        newProfileDialog.add(confirmButt);
        newProfileDialog.add(cancelButt);
        return newProfileDialog;
    }

    private JButton getConfirmButton(Component[][] compArrDia, JDialog newProfileDialog) {
        JButton confirmButt = new JButton("confirm");
        confirmButt.addActionListener(e -> {
            String name = ((JTextField) compArrDia[0][1]).getText();
            LinkedList<Day> days = createDayList(Integer.parseInt(((JTextField) compArrDia[1][1]).getText()));
            int[] maxMacros = {Integer.parseInt(((JTextField) compArrDia[2][1]).getText()),
                    Integer.parseInt(((JTextField) compArrDia[3][1]).getText()),
                    Integer.parseInt(((JTextField) compArrDia[4][1]).getText()),
                    Integer.parseInt(((JTextField) compArrDia[5][1]).getText())};
            Profile profile = new Profile(name, days, maxMacros);
            ccbm.addProfiles(profile);
            newProfileDialog.dispose();
        });
        return confirmButt;
    }

    private void maxmaxMacros(JLabel labelMaxCalories, JLabel labelMaxProtein, JLabel labelMaxCarbs, JLabel labelMaxFiber) {

        int allDayCal = 0;
        int allDayProt = 0;
        int allDayCarb = 0;
        int allDayFib = 0;

        for (DayPanel dp : dayPanels) {
            Day day = dp.getDay();
            allDayCal += day.getCalories();
            allDayProt += day.getProtein();
            allDayCarb += day.getCarbs();
            allDayFib += day.getFiber();
        }

        int allMaxDayCal = profile.getMaxMacros()[0] * dayPanels.length;
        int allMaxDayProt = profile.getMaxMacros()[1] * dayPanels.length;
        int allMaxDayCarb = profile.getMaxMacros()[2] * dayPanels.length;
        int allMaxDayFib = profile.getMaxMacros()[3] * dayPanels.length;

        labelMaxCalories.setText(allDayCal + "cal / " + allMaxDayCal + "cal");
        labelMaxProtein.setText(allDayProt + "g / " + allMaxDayProt + "g Protein");
        labelMaxCarbs.setText(allDayCarb + "g / " + allMaxDayCarb + "g Carbs");
        labelMaxFiber.setText(allDayFib + "g / " + allMaxDayFib + "g Fiber");
    }

    private void updateListSize() {
        int cnt = 0;
        for (DayPanel dp : dayPanels) {
            if (dp.getDay().getMeals().size() > cnt) {
                cnt = dp.getDay().getMeals().size();
            }
        }
        System.out.println("cnt:"+cnt);
        for (DayPanel dp : dayPanels) {
            @SuppressWarnings("unchecked") JList<Meal> labelMenu = (JList<Meal>) dp.getComponent(1);
            labelMenu.setPreferredSize(new Dimension(0, 20 * cnt));
        }


    }
}
