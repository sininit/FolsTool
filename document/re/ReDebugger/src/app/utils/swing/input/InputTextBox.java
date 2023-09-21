package app.utils.swing.input;

import app.utils.swing.LineNumberHeaderView;
import top.fols.atri.lang.Objects;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InputTextBox extends JFrame {
    protected final JTextArea textArea1;



    Objects.CallbackOneParam<String> abstractAction;
    public InputTextBox setConfirmAbstractAction(Objects.CallbackOneParam<String> abstractAction) {
        this.abstractAction = abstractAction;
        return this;
    }
    Runnable closeCallback;
    public InputTextBox setCloseCallback(Runnable closeCallback) {
        this.closeCallback = closeCallback;
        return this;
    }







    public InputTextBox title(String content) {
        this.setTitle(content);
        return this;
    }





    public InputTextBox content(String content) {
        content(this.textArea1, content);
        return this;
    }
    public InputTextBox append(String content) {
        append(this.textArea1, content);
        return this;
    }
    public InputTextBox println(String content) {
        try {
            return this.append(content + "\n");
        } finally {
            System.out.println(content);
        }
    }



    public static void content(JTextArea jTextArea, Object text) {
        jTextArea.setText(null == text?"": Objects.get_String(text));

        int newLength = jTextArea.getText().length();
        jTextArea.setSelectionStart(newLength);
        jTextArea.setSelectionEnd(newLength);
    }
    public static void append(JTextArea jTextArea, Object text) {
//        if (jTextArea.getText().length() > 1024L * 1024L * 4L) {
//            jTextArea.setText(null);
//        }
        int start  = jTextArea.getSelectionStart();
        int end    = jTextArea.getSelectionEnd();
        int length = jTextArea.getText().length();

        String content = (null == text?"": Objects.get_String(text));
        jTextArea.append(content);

        int newLength = jTextArea.getText().length();

        if (start == length || end == length) {
            jTextArea.setSelectionStart(newLength);
            jTextArea.setSelectionEnd(newLength);
        }
    }

    public String  content() {
        return this.textArea1.getText();
    }


    public void close() {
        this.dispose();
    }


    public InputTextBox size(int w, int h) {
        this.setSize(new Dimension(w, h));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = (int) (toolkit.getScreenSize().getWidth() - this.getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - this.getHeight()) / 2;
        this.setLocation(x, y);
        return this;
    }





    public InputTextBox() throws HeadlessException {
        JScrollPane contentScrollPane = new JScrollPane();
        JTextArea content = new JTextArea(3, 64);
        content.setBorder(new LineBorder(new Color(127, 157, 185), 1, false));

        content.setSize(64, content.getHeight());
        contentScrollPane.setViewportView(content);
//            contentScrollPane.setHorizontalScrollBarPolicy(
//                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);



        JPanel jp = new JPanel(new GridLayout(1, 1));
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setBorder(new LineBorder(new Color(127, 157, 185), 1, false));

        panel.add(contentScrollPane);

        jp.add(panel);


        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jp);

        JScrollBar sBar = jScrollPane.getVerticalScrollBar();
        sBar.setUnitIncrement(16);

        add(jScrollPane);



        JButton jb = new JButton("确定");
        jb.addActionListener(e -> {
            String v = content.getText();
            if (null != abstractAction) {
                abstractAction.callback(v);
            }
            dispose();
        });

        JPanel jp2 = new JPanel(new GridLayout(1, 1));
        jp2.add(jb);
        add(jp2, BorderLayout.SOUTH);


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.setSize(new Dimension((int) (toolkit.getScreenSize().getWidth()/2), (int) (toolkit.getScreenSize().getHeight()/2)));

        int x = (int) (toolkit.getScreenSize().getWidth() - this.getWidth()) / 2;
        int y = (int) (toolkit.getScreenSize().getHeight() - this.getHeight()) / 2;
        this.setLocation(x, y);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    closeCallback.run();
                } catch (Throwable ignored) {}
            }
        });

        LineNumberHeaderView lineNumberHeaderView = new LineNumberHeaderView();

        contentScrollPane.setRowHeaderView(lineNumberHeaderView);
        content.setFont(LineNumberHeaderView.DEFAULT_FONT);


        this.textArea1 = content;
    }




    public void create() {
        setVisible(true);
    }







    public static InputTextBox newInstance() {
        return new InputTextBox();
    }
}
