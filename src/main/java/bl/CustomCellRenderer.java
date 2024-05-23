package bl;

import data.Meal;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class CustomCellRenderer extends JLabel implements ListCellRenderer<Meal> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Meal> list, Meal value, int index, boolean isSelected, boolean cellHasFocus) {

        ImageIcon icon = value.getIcon();
        setIcon(icon);
        setText("<html>" + value.getName() + "<br>" + value.getCalories() +" calories "+"<br>"+  + value.getProtein() + "g Protein " + value.getCarbs() + "g Carbs " + value.getFiber() + "g Fiber"+ "</html>");
        setBorder(new EtchedBorder());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

    public CustomCellRenderer() {
        setOpaque(true);
    }
}
