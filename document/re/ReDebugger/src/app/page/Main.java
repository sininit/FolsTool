package app.page;

import app.Application;
import app.config;
import app.page.debugger.Page_Debugger;
import app.utils.TimeUtils;
import app.utils.reflects.ReflectUtils;
import app.utils.swing.input.InputFileSelects;
import top.fols.atri.util.Throwables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"DanglingJavadoc", "NonAsciiCharacters"})
public class Main {
    public JFrame frame;
    public JPanel Windows;

    public JMenuBar bar;
    public JTabbedPane tabbed;

    public JLabel status;

    public JPanel JPanel_首页;
    public JPanel JPanel_DebuggerPage;

    /** ------------------------------------------------------------- */
    public Main main;
    private transient boolean init = false;
    /** ------------------------------------------------------------- */;
    public transient Page_Debugger Page_Debugger = new Page_Debugger();
    /** ------------------------------------------------------------- */




    public JScrollPane          首页_jScrollPane_Log;
    public JTextArea            首页_log_textArea1;





    private JPanel 调试页_地址栏;
    private JPanel 调试页_侧边操作栏;
    private JPanel 调试页_StackID栏;


    public JButton      调试页_链接Button;
    public JTextField   调试页_地址textField;
    public JButton      调试页_断开Button;




    public JLabel       调试页_StackIDTextField;

    public JLabel       调试页_状态_链接;
    public JButton      调试页_恢复Button;



    public JScrollPane          调试页_TableTracePane;
    public JTable               调试页_TableTrace;


    public JPanel               调试页_TableObjectJPanel;
    public JScrollPane          调试页_TableObjectPane;
    public JTable               调试页_TableObject;


    public List<IWindowsInstance> windowsInstances = new ArrayList<IWindowsInstance>() {{
        Field[] fields = Main.class.getFields();
        for (Field field: fields){
            try {
                Object o = field.get(Main.this);
                if (o instanceof IWindowsInstance) {
                    IWindowsInstance v = (IWindowsInstance) o;
                    add(v);
                }
            } catch (Throwable e) {e.printStackTrace();}
        }
    }};

    public static Main newInstance() throws Throwable {
        //init windows
        Main windows = new Main();
        windows.main = windows;

        JFrame frame = windows.frame = new JFrame(windows.getClass().getSimpleName()+"@"+ config.version );

        windows.bar = new JMenuBar();

        windows.status.setText("");
        windows.status.setText(windows.status.getText()+"启动时间："+ TimeUtils.getTime_yyyy_MM_dd_HH_mm_ss_toString(System.currentTimeMillis()));
        windows.status.setText(windows.status.getText()+"  ");
        windows.status.setText(windows.status.getText()+"路径："+ Application.directory());


        frame.setContentPane(windows.Windows);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame.setSize(new Dimension((int) (toolkit.getScreenSize().getWidth()/2), frame.getHeight()));
        int x = (int) (toolkit.getScreenSize().getWidth() - frame.getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        windows.frame = frame;


        windows.initWindows();
        for (IWindowsInstance IWindowsInstance : windows.windowsInstances) {
            ReflectUtils.setFieldValue(windows, IWindowsInstance).initWindows();
        }

        frame.setJMenuBar(windows.bar);
        frame.validate();
        frame.setVisible(true);


        return windows;
    }



    public void readStyle() {
        //windows size
        {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Rectangle size = Application.Setting.main_size.getGson();
            if (null != size) {
                if (size.x < 0) { size.x = 0; }
                if (size.y < 0) { size.y = 0; }
                if (size.x > toolkit.getScreenSize().getWidth()) { size.x = 0; }
                if (size.y > toolkit.getScreenSize().getHeight()) { size.y = 0; }
                frame.setBounds(size);
            }
        }
        {
            try {
                int select = Application.Setting.select.getGson();
                this.tabbed.setSelectedIndex(select);
            } catch (Throwable ignored) {
            }
        }
    }
    public void saveStyle() {
        //windows size
        {
            Application.Setting.main_size.setGson(frame.getBounds());
        }
        {
            Application.Setting.select.setGson(this.tabbed.getSelectedIndex());
        }
    }

    public boolean save() {
        boolean result = true;
        try {
            saveStyle();
        } catch (Throwable e) {
            e.printStackTrace();
            Application.logln(Throwables.toString(e));
            result = false;
        }
        return result;
    }



    public void initWindowsMenu() {
        {
            JSeparator separator;
            JMenu jMenu;
            JMenuItem item;
            jMenu = new JMenu("文件");


            item = new JMenuItem("立即保存");
            item.addActionListener(e -> {
                Application.logln(Application.saveStatus() ? "保存成功" : "保存失败");
            });
            jMenu.add(item);


            JCheckBoxMenuItem 自动保存 = (JCheckBoxMenuItem) (item = new JCheckBoxMenuItem("自动保存"));
            item.addActionListener(e -> {
                Application.AutoSave.threadAutoSave(自动保存.isSelected());
            });
            jMenu.add(item);
            自动保存.setSelected(Application.Setting.auto_save.getBoolean());


            separator = new JSeparator();
            jMenu.add(separator);


            item = new JMenuItem("打开数据目录");
            item.addActionListener(e -> {
                InputFileSelects.openFileFromDesktop(Application.directory());
            });
            jMenu.add(item);

            separator = new JSeparator();
            jMenu.add(separator);

            this.bar.add(jMenu);
        }


//        {
//            JSeparator separator;
//            JMenu jMenu;
//            JMenuItem item;
//            jMenu = new JMenu("设置");
//
//
//            item = new JMenuItem(账号页.class.getSimpleName());
//            item.addActionListener(e -> {
//                账号页.SoftwareSetting.settingConfiguration.edit(账号页.class.getSimpleName());
//            });
//            jMenu.add(item);
//
//            separator = new JSeparator();
//            jMenu.add(separator);
//
//
//            this.bar.add(jMenu);
//        }


    }




    public void initWindows() {
        if (!this.init) {
            this.initWindowsMenu();
            this.readStyle();
            this.init = true;
        }
    }

}
