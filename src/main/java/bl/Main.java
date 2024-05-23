package bl;
import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        MainGUI mGUI = new MainGUI();
        mGUI.setupGUI();

    }
}