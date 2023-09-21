package app.utils.swing.input;

import app.utils.MyConfigurationKeys;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;

import javax.swing.*;
import java.awt.*;

public class InputListSingleSelect extends JFrame {
//    JFrame jFrame;

    Objects.CallbackOneParam<String> abstractAction;
    public InputListSingleSelect setConfirmAbstractAction(Objects.CallbackOneParam<String> abstractAction) {
        this.abstractAction = abstractAction;
        return this;
    }

    public InputListSingleSelect(String title, MyConfigurationKeys myConfigurationKeys) {
//        this.jFrame = new JFrame();
//        jFrame.setSize(200, 100);
//        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//        this.setUndecorated(true); // 去掉窗口的装饰
//        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//采用指定的窗口装饰风格
        this.setTitle(title);
        this.setSize(320,200);

        JScrollPane scrollPane = new JScrollPane();
        //以数组构造方法创建
        String[] str = myConfigurationKeys.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
        JList<String> jList = new JList<>(str);
        jList.addListSelectionListener(e -> {
            if (!jList.getValueIsAdjusting()) {	//设置只有释放鼠标时才触发
                dispose();
                if (null != abstractAction) {
                    abstractAction.callback(jList.getSelectedValue());
                }
            }
        });
        scrollPane.setViewportView(jList);

        add(scrollPane);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - getHeight()) / 2;
        setLocation(x, y);
    }

    public InputListSingleSelect title(String title) {
        this.setTitle(title);
        return this;
    }


    public void create() {
//        this.setUndecorated(true); // 去掉窗口的装饰
//        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE); //采用指定的窗口装饰风格
        this.setVisible(true);
    }


    public static InputListSingleSelect newInstance(String title, String[] array) {
        return new InputListSingleSelect(title, new MyConfigurationKeys() {{
            for (String s : array) {
                add(s);
            }
        }});
    }
    public static InputListSingleSelect newInstance(String title, MyConfigurationKeys myConfigurationKeys) {
        return new InputListSingleSelect(title, myConfigurationKeys);
    }
}
