package app.utils.swing;

import app.page.TableManager;
import com.sun.awt.AWTUtilities;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JTableUtil {
    public static Operate operate(JTable table) {
        return new Operate(table);
    }
    public static class Operate {
        protected JTable instance;
        protected DefaultTableModel defaultTableModel;
        protected DefaultTableColumnModel defaultTableColumnModel;

        protected Operate(JTable instance) {
            this.instance = instance;
            this.defaultTableModel = (DefaultTableModel) instance.getModel();
            this.defaultTableColumnModel = (DefaultTableColumnModel) instance.getColumnModel();
        }

        public JTable instance() { return instance; }

        public Operate addColumn(String... name) {
            for (String n: name) this.addColumn0(n);
            return this;
        }
        public Operate addColumn(Collection<String> name) {
            for (String n: name)
                this.addColumn0(n);
            return this;
        }
        private void addColumn0(String name) throws RuntimeException {
            if (this.searchColumn(name) != -1) {
                throw new RuntimeException("contains: " + name);
            }
            this.defaultTableModel.addColumn(name);
        }
        private int searchColumn(String name) {
            int clen = this.column();
            for (int i = 0; i<clen; i++) {
                if (Objects.equals(name, this.getColumnName(i))) {
                    return i;
                }
            }
            return -1;
        }




        public void setColumnWidth(int index, int width){ this.defaultTableColumnModel.getColumn(index).setPreferredWidth(width); }
        public void setColumnWidths(int[] width) {
            for (int i = 0; i < Math.min(column(), width.length); i ++) {
                setColumnWidth(i, width[i]);
            }
        }

        public int getColumnWidth(int index){
            return this.defaultTableColumnModel.getColumn(index).getPreferredWidth();
        }
        public int[] getColumnWidths() {
            int tableColumnCount = column();
            int[] tableColumnWidths = new int[tableColumnCount];
            for (int i = 0; i < tableColumnCount; i++) {
                tableColumnWidths[i] = getColumnWidth(i);
            }
            return tableColumnWidths;
        }

        public int row(){ return this.defaultTableModel.getRowCount(); }
        public int column(){ return this.defaultTableModel.getColumnCount(); }
        public String getColumnName(int index) { return index >= this.column()? null:this.defaultTableModel.getColumnName(index); }
        public int getColumnIndex(String name) { return this.searchColumn(name); }





        public int addLine() {
            return this.addLine(Finals.EMPTY_OBJECT_ARRAY);
        }
        public int addLine(Map<String, String> values) {
            int row = this.defaultTableModel.getRowCount();
            this.defaultTableModel.addRow(Finals.EMPTY_OBJECT_ARRAY);
            for (String columnName: values.keySet()) {
                String value = values.get(columnName);

                int cindex = this.getColumnIndex(columnName);
                this.defaultTableModel.setValueAt(value, row, cindex);
            }
            return row;
        }
        public int addLine(Object... values) {
            int row = this.defaultTableModel.getRowCount();
            this.defaultTableModel.addRow(Finals.EMPTY_OBJECT_ARRAY);
            int cindex = 0;
            for (Object value: values) {
                this.defaultTableModel.setValueAt(value, row, cindex);
                if (++cindex == values.length) {
                    break;
                }
            }
            return row;
        }


        public Object get(int row, int column) {
            return this.defaultTableModel.getValueAt(row, column);
        }
        public Operate set(int row, int column, Object value) {
            this.defaultTableModel.setValueAt(null==value?"":String.valueOf(value), row, column);
            return this;
        }


        public Operate removeAllRow() {
            for (int i = this.row() -1; i >= 0; i--) {
                this.removeRow(i);
            }
            return this;
        }
        public Operate removeRow(int row) {
            this.defaultTableModel.removeRow(row);
            return this;
        }



        public Operate removeAllColumn() {
            while (this.column() > 0) {
                this.removeColumn(0);
            }
            return this;
        }
        public Operate removeColumn(int column) {
            int columnCount = this.defaultTableColumnModel.getColumnCount() - 1;
            if (columnCount >= 0) {
                TableColumn tableColumn = this.defaultTableColumnModel.getColumn(column);
                this.defaultTableColumnModel.removeColumn(tableColumn);
                this.defaultTableModel.setColumnCount(columnCount);
            }
            return this;
        }
    }




    public static class ATableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
//            System.out.println(row+"/"+column);
//            Object value = getValueAt(row, column);
//            System.out.println(value);
            return false;
        }
    }

    public static class ValueEditorView extends JFrame {
        public ValueEditorView() {
            // 窗口标题
//            this.setTitle("SF");

            //窗口图标
//            Image icon = Toolkit.getDefaultToolkit().getImage("images/windows.png");
//            this.setIconImage(icon);

            //窗口边框隐藏
            this.setUndecorated(true);

            //绘制窗口
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle bounds = new Rectangle(screenSize);
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());

//            bounds.x += insets.left;
//            bounds.y += insets.top;
//            bounds.width -= insets.left + insets.right;
//            bounds.height -= insets.top + insets.bottom;
            bounds.width = 128;
            bounds.height = 128;
            this.setBounds(bounds);

            //显示窗口
            this.setVisible(true);
        }
    }
    public static void showValueEditorView(JTable jTable, MouseEvent evt, int row, int col, IValueChanger valueChanger) {
        ValueEditorView valueEditorView = new ValueEditorView();

        //com.sun.awt.AWTUtilities必須使用jdk1.6版本，0.1f表示透明度，越小透明度越大
        AWTUtilities.setWindowOpacity(valueEditorView, 0.9f);

//                      valueEditor.setIconImage(frame.getIconImage());
//                      Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
//                      JRootPane rootPane = frame.getRootPane();

        //单元格的位置 在整个视图中      无视滑块
        Rectangle cellRect = jTable.getCellRect(row, col, true);

        //点击的单元格在屏幕中的绝对位置
        //int screenX = (int) evt.getLocationOnScreen().getX();
        //int screenY = (int) evt.getLocationOnScreen().getY();

        //点击的单元格绝对位置 - 单元格x0, y0绝对位置
        long cellRectPointOffsetX = (long) (evt.getX() - cellRect.getX());
        long cellRectPointOffsetY = (long) (evt.getY() - cellRect.getY());

        cellRect.height += 128;
        cellRect.width += 0;
        cellRect.x = (int) (evt.getLocationOnScreen().getX() - cellRectPointOffsetX);
        cellRect.y = (int) (evt.getLocationOnScreen().getY() - cellRectPointOffsetY);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //防止超出屏幕
        if (cellRect.x + cellRect.width > screenSize.width) {
            cellRect.width -= ((cellRect.x + cellRect.width) - screenSize.width);
        }

        valueEditorView.setBounds(cellRect);

        JScrollPane jScrollPane = new JScrollPane();

        Object value = valueChanger.getValue(row, col);
        String old   = String.valueOf(value);

        JTextArea jTextArea = new JTextArea();
        jTextArea.setBounds(jScrollPane.getBounds());
        jTextArea.setWrapStyleWord(true);
        jTextArea.setText(old);

        jScrollPane.setViewportView(jTextArea);
        valueEditorView.add(jScrollPane);

        FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                String n = jTextArea.getText();
                if (!old.equals(n)) {
                    valueChanger.setValue(row, col, old, n);
                }
                valueEditorView.dispose();
            }
        };

        valueEditorView.addFocusListener(focusAdapter);
        jTextArea.addFocusListener(focusAdapter);


        jTextArea.setSelectionStart(0);
        jTextArea.setSelectionEnd(0);

        jTextArea.requestFocus();
    }



    public static void fitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();

        Enumeration<?> columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier()
                            , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }






    public static JTableUtil getInstance(JTable instance) {
        JTableUtil util = map.get(instance);
        if (null == util) {
            map.put(instance, util = new JTableUtil(instance));
            util.init();
        }
        return util;
    }



    protected static final Map<JTable, JTableUtil> map = new WeakHashMap<>();

    protected InitTableParam param;
    protected JTable instance;
    protected DefaultTableModel defaultTableModel;
    protected DefaultTableColumnModel defaultTableColumnModel;


    JTableUtil(JTable instance) {
        this.instance = instance;
    }

    void init() {
        instance.getTableHeader().setReorderingAllowed(false);
        instance.setPreferredScrollableViewportSize(new Dimension(400, 80));
        instance.setModel(new ATableModel());

//        instance.getModel().addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    int column = e.getColumn();
//                    int firstRow = e.getFirstRow();
//                    int lastRow = e.getLastRow();
//                    if (firstRow >= 0 && lastRow >= 0) {
//                        System.out.println(instance.getValueAt(lastRow, column));
//                    }
//                }
//            }
//        });


        //初始化列表框
        instance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      instance.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());

        defaultTableModel = (DefaultTableModel) instance.getModel();
        defaultTableColumnModel = (DefaultTableColumnModel) instance.getColumnModel();


        for (int i = defaultTableModel.getRowCount() -1; i >= 0; i--) {
            defaultTableModel.removeRow(i);
        }

        while (defaultTableModel.getColumnCount() > 0) {
            int columncount = this.defaultTableColumnModel.getColumnCount() - 1;

            TableColumn tableColumn = this.defaultTableColumnModel.getColumn(columncount);
            this.defaultTableColumnModel.removeColumn(tableColumn);
            this.defaultTableModel.setColumnCount(columncount);
        }
    }

    public void showValueEditorView(MouseEvent evt, int row, int col) {
        showValueEditorView(instance, evt, row, col, param);
    }

    public void fitTableColumns() {
        fitTableColumns(this.instance);
    }







    public static abstract class InitTableParam implements IValueChanger {
        public final JTable      jTable;
        public final String      keyName;
        public final boolean     directEditor;


        /**
         *
         * @param jTable       表
         * @param keyName      主键
         * @param directEditor 双击单击编辑
         */
        public InitTableParam(JTable jTable, String keyName, boolean directEditor) {
            this.jTable = jTable;
            this.keyName = keyName;
            this.directEditor = directEditor;
        }



        //后生成
        public JTableUtil           jTableUtil;
        //后生成
        public JTableUtil.Operate   operate;
        //后生成
        public TableManager         tableManager;


        public abstract void loadTableManagerData() throws Throwable;

        public abstract void readStyle();
        public abstract void saveStyle();
        public abstract void mouseClicked(InitTableParam initTableParam, MouseEvent evt);


        @Override
        public abstract Object  getValue(int row, int col);
        @Override
        public abstract void    setValue(int row, int col, Object oldValue, Object value);
    }
    public interface IValueChanger {
        Object  getValue(int row, int col);
        void    setValue(int row, int col, Object oldValue, Object value);
    }



    public static void initTable(InitTableParam param) throws Throwable {
        JTable jTable = param.jTable;

        param.jTableUtil = JTableUtil.getInstance(jTable);
        param.jTableUtil.param = param;

        //双击直接显示编辑器
        if (param.directEditor) {
            jTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {//双击
                        if (!(evt.getSource() instanceof JTableHeader)) {
                            JTable jTable = (JTable) evt.getSource();
                            int row = jTable.rowAtPoint(evt.getPoint());
                            int col = jTable.columnAtPoint(evt.getPoint());
//                          System.out.println(row + ", " + col);
                            param.jTableUtil.showValueEditorView(evt, row, col);
                        }
                    }
                }
            });
        }

        param.operate = JTableUtil.operate(jTable);

        MouseListener tableMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                param.mouseClicked(param, evt);
            }
        };
        jTable.getTableHeader().addMouseListener(tableMouseListener);
        jTable.addMouseListener(tableMouseListener);


        param.tableManager = new TableManager(jTable, param.keyName);
        param.loadTableManagerData();
        param.readStyle();

        jTable.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) {
                param.saveStyle();
            }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });

    }

}
