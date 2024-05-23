package bl;

import data.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomActionListener implements ActionListener {

    JTextField tfCal;
    JTextField tfProt;
    JTextField tfCarb;
    JTextField tfFib;
    DayPanel[] dayPanels;

    public CustomActionListener(Component[][] compArr, DayPanel[] dayPanels, Profile profile) {
        this.tfCal = (JTextField) compArr[0][1];
        this.tfProt = (JTextField) compArr[1][1];
        this.tfCarb = (JTextField) compArr[2][1];
        this.tfFib = (JTextField) compArr[3][1];
        this.dayPanels=dayPanels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] arr = new int[4];
        arr[0] = Integer.parseInt(tfCal.getText());
        arr[1] = Integer.parseInt(tfProt.getText());
        arr[2] = Integer.parseInt(tfCarb.getText());
        arr[3] = Integer.parseInt(tfFib.getText());
        for (DayPanel daypanel : dayPanels) {
            daypanel.updateData(arr);
        }
    }
}
