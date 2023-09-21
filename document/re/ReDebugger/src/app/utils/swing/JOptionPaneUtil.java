package app.utils.swing;

import javax.swing.*;

public class JOptionPaneUtil {
    public static boolean confirm(String title, String message) {
        //确认是否要送件
        int opt = JOptionPane.showConfirmDialog(null,
                message, title,
                JOptionPane.YES_NO_OPTION);
        return opt == JOptionPane.YES_OPTION;
    }

    public static void notify(String message) {
        notify("通知", message);
    }
    public static void notify(String title, String message) {
        //确认是否要送件
        JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
    }

}
