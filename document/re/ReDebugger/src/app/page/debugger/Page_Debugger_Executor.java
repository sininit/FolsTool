package app.page.debugger;

import app.config;
import app.page.Main;
import app.utils.swing.JOptionPaneUtil;
import top.fols.atri.util.Throwables;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver;
import top.fols.box.reflect.re.Re_ZDebuggerServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Page_Debugger_Executor {
    public Main main;

    public JFrame frame;
    public JPanel Windows;

    private JTextArea 调试页_code;
    private JButton   调试页_执行Button;

    Page_Debugger.FirstTableTraceKey.Element trace;

    public static Page_Debugger_Executor newInstance(Main main, Page_Debugger.FirstTableTraceKey.Element trace) throws Throwable {
        //init windows
        Page_Debugger_Executor windows = new Page_Debugger_Executor();
        windows.main = main;

        JFrame frame = windows.frame = new JFrame(windows.getClass().getSimpleName()+"@"+ config.version );

        frame.setContentPane(windows.Windows);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
        frame.pack();


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setSize(new Dimension((int) (toolkit.getScreenSize().getWidth()/2), frame.getHeight()));
        int x = (int) (toolkit.getScreenSize().getWidth() - frame.getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);


        windows.frame = frame;
        windows.initWindows();

        frame.validate();
        frame.setVisible(true);

        //set fields
        windows.trace = trace;

        return windows;
    }

    public String getCode() { return 调试页_code.getText(); }

    public void initWindows() {
        this.调试页_执行Button.addActionListener(event -> {
            try {
                String code = getCode();
                boolean b = main.Page_Debugger.hasFirstTableStack(trace);
                if (b) {
                    Re_ZDebuggerServer.GetObjectData.ObjectData objectData = main.Page_Debugger.queryExecute(trace.getExecutorObjectID(), code);
                    System.out.println(objectData);
                } else {
                    String message = "trace exited: " + trace;
                    System.out.println(message);
                    JOptionPaneUtil.notify(message);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                JOptionPaneUtil.notify(Throwables.toString(e));
            }
        });
    }
}
