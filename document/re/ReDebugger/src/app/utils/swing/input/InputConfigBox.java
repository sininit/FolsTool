package app.utils.swing.input;

import app.utils.MyConfigurationKeys;
import top.fols.atri.lang.Objects;
import top.fols.atri.lang.Value;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InputConfigBox extends JFrame {
    Objects.CallbackOneParam<Map<String, String>> abstractAction;
    public InputConfigBox setConfirmAbstractAction(Objects.CallbackOneParam<Map<String, String>> abstractAction) {
        this.abstractAction = abstractAction;
        return this;
    }


    public static class Element {
        JPanel item;

        JLabel title;
        JLabel remarks;

        JScrollPane content_scroll;
        JTextArea content;


        public Element(JLabel title, JLabel remarks, JPanel item,

                       JScrollPane content_scroll, JTextArea content) {
            this.title = title;
            this.remarks = remarks;

            this.item = item;

            this.content_scroll = content_scroll;
            this.content = content;
        }

        public JLabel getTitle() { return title; }
        public JLabel getRemarks() { return remarks; }
        public JPanel getItem() { return item; }

        public JTextArea getContent() { return content; }
        public JScrollPane getContentScroll() { return content_scroll; }
    }

    public InputConfigBox(String windowsTitle, MyConfigurationKeys myConfigurationKeys, Map<String, String> data) {
        setTitle(windowsTitle);
//        setModal(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭后销毁对话框
        setLocationRelativeTo(null);

        ToolTipManager.sharedInstance().setDismissDelay(5000);// 设置为5秒

        Map<String, Element> elementMap = new LinkedHashMap<>();
        for (String key: myConfigurationKeys.keySet()) {

            JPanel item = new JPanel(new GridLayout(2, 1));

            JLabel title = new JLabel();
            title.setText(key+": ");

            JLabel remarks = new JLabel();
            remarks.setForeground(Color.BLUE);
            remarks.setText(myConfigurationKeys.getRemarks(key));

            item.add(title);
            item.add(remarks);

            JScrollPane jScrollPane = new JScrollPane();
            JTextArea content = new JTextArea(3, 64);
            String defaultValue = myConfigurationKeys.getDefaultValue(key);
            String dataValue = data.get(key);
            content.setText(null == dataValue?defaultValue:dataValue);
            content.setBorder(new LineBorder(new Color(127,157,185), 1, false));
            content.setToolTipText(myConfigurationKeys.getRemarks(key));

            content.setSize(64, content.getHeight());
            jScrollPane.setViewportView(content);
//            jScrollPane.setHorizontalScrollBarPolicy(
//                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            Element element = new Element(title, remarks, item, jScrollPane, content);
            elementMap.put(key, element);
        }
        JPanel jp = new JPanel(new GridLayout(myConfigurationKeys.size(), 1));

        for (String s : elementMap.keySet()) {
            Element element = elementMap.get(s);

            JPanel panel = new JPanel(new GridLayout(1, 1));
            panel.setBorder(new LineBorder(new Color(127,157,185), 1, false));

            panel.add(element.getItem());
            panel.add(element.getContentScroll());

            jp.add(panel);
        }


        JButton jb = new JButton("确定");
        jb.addActionListener(e -> {
            Map<String, String> kv = new HashMap<>();
            for (String s : elementMap.keySet()) {
                Element element = elementMap.get(s);
                kv.put(s, element.getContent().getText());
            }
            if (null != abstractAction) {
                abstractAction.callback(kv);
            }
            dispose();
        });

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jp);

        JScrollBar sBar = jScrollPane.getVerticalScrollBar();
        sBar.setUnitIncrement(16);


        add(jScrollPane);

        JPanel jp2 = new JPanel(new GridLayout(1, 1));
        jp2.add(jb);
        add(jp2, BorderLayout.SOUTH);

        Value<Boolean> set = Value.wrap(false);
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);

                if (set.get()) {
                } else {
                    set.set(true);
                    double width = 0;
                    double height = 0;
                    for (String key: elementMap.keySet()) {
                        Element element = elementMap.get(key);

                        JLabel jLabel = element.getTitle();
                        JTextArea jTextArea = element.getContent();

                        height = Math.max(height,
                                height + jLabel.getHeight());

                        width = Math.max(width,
                                jLabel.getFontMetrics(jLabel.getFont()).stringWidth(jLabel.getText()) +
                                        jTextArea.getFontMetrics(jTextArea.getFont()).stringWidth(jTextArea.getText()));
                    }
                    Toolkit toolkit = Toolkit.getDefaultToolkit();

                    Dimension dimension = InputConfigBox.this.getSize();
//                    dimension.height += (int) height;
//                    dimension.width = (int) width * 2 + 32;
                    dimension.height = (int) (toolkit.getScreenSize().getHeight() / 2);
                    dimension.width = (int) (toolkit.getScreenSize().getWidth() / 2);
                    InputConfigBox.this.setSize(dimension);

                    int x = (int) (toolkit.getScreenSize().getWidth() - getWidth()) / 2;
                    int y = (int) (toolkit.getScreenSize().getHeight() - getHeight()) / 2;
                    setLocation(x, y);
                }

//                setSize(1280, 400);//对话框的大小
            }
        });

    }

    public InputConfigBox title(String title) {
        setTitle(title);
        return this;
    }



    public void create() {
        setVisible(true);
    }











    public static InputConfigBox newInstance(String title ,
                                             MyConfigurationKeys myConfigurationKeys,
                                             Map<String, String> data) {
        return new InputConfigBox(title, myConfigurationKeys, data);
    }


}
