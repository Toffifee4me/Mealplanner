package BL;

import java.awt.*;

public class CustomGridLAyout extends GridLayout {
    private int[] rowHeight;

    public CustomGridLAyout(int rows, int cols) {
        super(rows, cols);
    }

    public void setRowHeight(int row, int height) {
        if (rowHeight == null) {
            rowHeight = new int[getRows()];
        }
        rowHeight[row] = height;
    }

    @Override
    public void layoutContainer(Container parent) {
        int nComps = parent.getComponentCount();
        if (nComps == 0)
            return;

        int[] xOffsets = new int[getColumns()];
        int[] yOffsets = new int[getRows()];
        int[] colWidths = new int[getColumns()];
        int[] rowHeights = new int[getRows()];
        int help = 0;

        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            int row = i / getColumns();
            int col = i % getColumns();
            Dimension d = c.getPreferredSize();
            help = parent.getComponent(1).getPreferredSize().height;
            help = Math.max(help, (parent.getHeight() - help) / (getRows() - 1));
            colWidths[col] = Math.max(parent.getWidth(), d.width);
            if (i == 1) {
                rowHeights[i] = help;
            } else {
                rowHeights[i] = Math.max((parent.getHeight() - help) / (getRows() - 1), d.height);
            }

        }


        // calculate cell positions
        for (int i = 0; i < getColumns(); i++)
            for (int j = 0, y = 0; j < getRows(); j++) {
                yOffsets[j] = y;
                y += rowHeights[j];
            }

        for (int i = 0; i < getRows(); i++)
            for (int j = 0, x = 0; j < getColumns(); j++) {
                xOffsets[j] = x;
                x += colWidths[j];
            }

        // set the bounds of each component
        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            int row = i / getColumns();
            int col = i % getColumns();
            int x = xOffsets[col];
            int y = yOffsets[row];
            int width = colWidths[col];
            int height = rowHeights[row];
            c.setBounds(x, y, width, height);
        }
    }
}
