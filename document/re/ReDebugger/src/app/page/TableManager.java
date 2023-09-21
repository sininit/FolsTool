package app.page;

import app.utils.KeyIndexManager;
import app.utils.lock.XlsProperties;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;
import top.fols.atri.reflect.Reflects;
import top.fols.atri.interfaces.interfaces.IInnerMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("UnusedReturnValue")
public class TableManager {
    XlsProperties xlsProperties = new XlsProperties();
    JTable table;
    DefaultTableModel tableModel;


    KeyIndexManager rowKeyIndexManager = new KeyIndexManager();
    String          rowKeyName;
    /**
     * @param rowKeyName keyName 是唯一的
     */
    public TableManager(JTable table, String rowKeyName) {
        this.table = Objects.requireNonNull(table);
        this.tableModel = (DefaultTableModel) table.getModel();
        if (null == rowKeyName || 0 == rowKeyName.length()) {
            throw new NullPointerException("key name");
        }
        this.rowKeyName = rowKeyName;
    }


    public XlsProperties getXlsProperties () { return xlsProperties; }



    public boolean writeToFile(File file) throws IOException {
        return xlsProperties.writeToFile(file);
    }

    /**
     * table
     */
    public String[] getSelectRowPrimaryKey() {
        synchronized (this) {
            int[] rows = this.table.getSelectedRows();
            String[] keys = new String[rows.length];

            int keyIndex = getPrimaryKeyColumnIndex();

            for (int i = 0; i < rows.length; i++) {
                keys[i] = xlsProperties.getValue(rows[i], keyIndex);
            }
            return keys;
        }
    }
    public void select(String[] primaryKey) {
        synchronized (this) {
            List<Integer> indexes = new ArrayList<>();
            int first = -1;
            for (String k: primaryKey) {
                int row = getRowIndex(k);
                if (row != -1) {
                    if (first == -1 || first >= row) {
                        first = row;
                    }
                    indexes.add(row);
                }
            }
            if (first != -1) {
                this.table.changeSelection(first,0,true,true);
                for (Integer integer : indexes) {
                    this.table.addRowSelectionInterval(integer, integer);
                }
            }
        }
    }
    public void unselected(String[] primaryKey) {
        synchronized (this) {
            for (String k: primaryKey) {
                int row = getRowIndex(k);
                if (row != -1) {
                    this.table.removeRowSelectionInterval(row, row);
                }
            }
        }
    }
    public void unselected() {
        synchronized (this) {
            this.table.removeRowSelectionInterval(0, getRowCount()-1);
        }
    }

    /** *********************** */






    public int      getPrimaryKeyColumnIndex() { return xlsProperties.getColumnIndex(rowKeyName); }
    public int      getRowIndex(String primaryKey) {
        return rowKeyIndexManager.getIndex(primaryKey);
    }
    public String   getRowPrimaryKey(int index) {
        synchronized (this) {
            int rowCount = xlsProperties.getRowCount();
            if (index <0 || index >= rowCount) {
                throw new ArrayIndexOutOfBoundsException("index="+index+", row-count="+rowCount);
            }
            int keyIndex = getPrimaryKeyColumnIndex();
            return xlsProperties.getValue(index, keyIndex);
        }
    }
    public String[] getRowPrimaryKeys() {
        synchronized (this) {
            int keyIndex = getPrimaryKeyColumnIndex();
            return xlsProperties.getColumnValues(keyIndex);
        }
    }
    public String getPrimaryKey(Map<String, String> data) {
        return XlsProperties.nonNull(data.get(rowKeyName));
    }
    public String getPrimaryKey() { return rowKeyName; }


    /**
     * @see TableManager#addHideColumn(String)
     */
    public void addColumn(String value0) {
        synchronized (this) {
            if (tableModel.getColumnCount() != xlsProperties.getColumnCount()) {
                throw new UnsupportedOperationException("column count error");
            }
            String value = XlsProperties.nonNull(value0);

            xlsProperties.addColumn(value);
            tableModel.addColumn(value);
        }
    }
    /**
     * addHideHeader 用于 添加隐藏列项, 即 不会添加到JTable里面
     * 必须 先 addHeader添 加完 必须列，
     * 再 addHideHeader 添加隐藏列
     * 添加隐藏项目之后不可再 使用 addHeader
     */
    public int addHideColumn(String header) {
        return xlsProperties.addColumn(header); //可能存在隐藏列 可以考虑再加一个table的  KeyIndexManager
    }



    public int getColumnIndex(String name) {
        return xlsProperties.getColumnIndex(name);
    }


    public void removeAllRow() {
        synchronized (this) {
            rowKeyIndexManager.clear();
            tableModel.setRowCount(0);
            xlsProperties.removeAllRow();
        }
    }
    public void removeAll() {
        synchronized (this) {
            rowKeyIndexManager.clear();
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            xlsProperties.removeAllRowAndHeader();
        }
    }
    public void remove(int index) {
        synchronized (this) {
            int rowCount = xlsProperties.getRowCount();
            if (index <0 || index >= rowCount) {
                throw new ArrayIndexOutOfBoundsException("index="+index+", row-count="+rowCount);
            }

            String pkey = getRowPrimaryKey(index);
            rowKeyIndexManager.remove(pkey);
            tableModel.removeRow(index);
            xlsProperties.removeRow(index);
        }
    }
    public boolean remove(String pkey) {
        synchronized (this) {
            int index = getRowIndex(pkey);
            if (index < 0) {
                return false;
            } else {
                rowKeyIndexManager.remove(pkey);
                tableModel.removeRow(index);
                xlsProperties.removeRow(index);
                return true;
            }
        }
    }



    public boolean updateRow(Map<String, String> data) {
        if (!data.containsKey(rowKeyName)) {
            throw new UnsupportedOperationException("data "+data+", no key value: "+ rowKeyName);
        }
        synchronized (this) {
            String keyValue = XlsProperties.nonNull(data.get(rowKeyName));
            if (!rowKeyIndexManager.contains(keyValue)) {
                throw new UnsupportedOperationException("not found key: "+keyValue);
            }
            int row = getRowIndex(keyValue);

            for (String k: data.keySet()) {
                if (!rowKeyName.equals(k)) {
                    String value = data.get(k);

                    int propertiesColumn = xlsProperties.getColumnIndex(k);
                    if (propertiesColumn < 0) {
                        throw new RuntimeException("not found column: " + k);
                    }
                    xlsProperties.setValue(row, propertiesColumn, value);


                    //可能存在隐藏列 可以考虑再加一个table的  KeyIndexManager
                    int tableColumn = getColumnIndex(k);
                    if (tableColumn >= 0 && tableColumn < tableModel.getColumnCount()) {
                        tableModel.setValueAt(value, row, tableColumn);
                    }
                }
            }
        }
        return true;
    }
    public boolean addRow(Map<String, String> data) {
        if (!data.containsKey(rowKeyName)) {
            throw new UnsupportedOperationException("data "+data+", no key value: "+ rowKeyName);
        }
        synchronized (this) {
            String keyValue = XlsProperties.nonNull(data.get(rowKeyName));
            if (rowKeyIndexManager.contains(keyValue)) {
                throw new UnsupportedOperationException("exists key: "+keyValue);
            }
            rowKeyIndexManager.add(keyValue);

            String[] vs =  xlsProperties.toRowValue(data);
            xlsProperties.addRow(vs);

            //可能存在隐藏列 可以考虑再加一个table的  KeyIndexManager
            String[] vs2 = vs.length < tableModel.getColumnCount() ? Arrays.copyOf(vs, tableModel.getColumnCount()):vs;
            tableModel.addRow(vs2);
        }
        return true;
    }



    public int getRowCount() {
        synchronized (this) {
            return xlsProperties.getRowCount();
        }
    }


    public String getValue(int row, int column) {
        synchronized (this) {
            int rowCount = this.xlsProperties.getRowCount();
            if (row < 0 || row >= rowCount)     { throw new ArrayIndexOutOfBoundsException("set.row="+row+", row-count="+rowCount); }

            int columnCount = this.xlsProperties.getColumnCount();
            if (column < 0 || column >= columnCount)     { throw new ArrayIndexOutOfBoundsException("set.column="+column+", column-count="+columnCount); }

            return xlsProperties.getValue(row, column);
        }
    }
    public String getValue(String primaryKey, String columnName) {
        synchronized (this) {
            int row = getRowIndex(primaryKey);
            if (row == -1)      { throw new UnsupportedOperationException("not found key: "+primaryKey); }

            int column = getColumnIndex(columnName);
            if (column == -1)   { throw new UnsupportedOperationException("not found column: "+columnName); }

            return xlsProperties.getValue(row, column);
        }
    }


    void setValue0(int row, int column, String value) {
        value = XlsProperties.nonNull(value);
        int keyIndex = xlsProperties.getColumnIndex(rowKeyName);
        if (keyIndex == column) {
            if (rowKeyIndexManager.contains(value)) {
                throw new UnsupportedOperationException("exists key: "+ value);
            } else {
                rowKeyIndexManager.set(row, value);
            }
        }
        xlsProperties.setValue(row, column, value);

        //可能存在隐藏列 可以考虑再加一个table的  KeyIndexManager
        if (column < tableModel.getColumnCount()) {
            tableModel.setValueAt(value, row, column);
        }
    }

    public void setValue(int row, int column, String value) {
        synchronized (this) {
            int rowCount = this.xlsProperties.getRowCount();
            if (row < 0 || row >= rowCount)     { throw new ArrayIndexOutOfBoundsException("set.row="+row+", row-count="+rowCount); }

            int columnCount = this.xlsProperties.getColumnCount();
            if (column < 0 || column >= columnCount)     { throw new ArrayIndexOutOfBoundsException("set.column="+column+", column-count="+columnCount); }

            setValue0(row, column, value);
        }
    }

    public void setValue(String primaryKey, String columnName, String value) {
        synchronized (this) {
            int row = getRowIndex(primaryKey);
            if (row == -1)      { throw new UnsupportedOperationException("not found key: "+primaryKey); }

            int column = getColumnIndex(columnName);
            if (column == -1)   { throw new UnsupportedOperationException("not found column: "+columnName); }

            setValue0(row, column, value);
        }
    }



    //可能存在隐藏列 可以考虑再加一个table的  KeyIndexManager
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tableModel.getColumnCount(); i++){
            Object cell = tableModel.getColumnName(i);
            String value = XlsProperties.nonNull(null == cell?null:cell.toString());
            stringBuilder.append(value).append(XlsProperties.Line.SEPARATOR);
        }
        if (stringBuilder.length() > XlsProperties.Line.SEPARATOR.length()) {
            stringBuilder.setLength(stringBuilder.length() - XlsProperties.Line.SEPARATOR.length());
        }
        stringBuilder.append("\n");

        for (int ri = 0; ri< tableModel.getRowCount(); ri++) {
            for (int ci = 0; ci < tableModel.getColumnCount(); ci++) {
                Object cell = tableModel.getValueAt(ri, ci);
                String value = XlsProperties.nonNull(null == cell?null:cell.toString());
                stringBuilder.append(value).append(XlsProperties.Line.SEPARATOR);
            }
            stringBuilder.append("\n");
        }

        if (stringBuilder.length() > "\n".length()) {
            stringBuilder.setLength(stringBuilder.length() - "\n".length());
        }
        return stringBuilder.toString();
    }

    public Map<String, String> getRowMap(String primaryKey) {
        synchronized (this) {
            int row = getRowIndex(primaryKey);
            if (row == -1)      { throw new UnsupportedOperationException("not found key: "+primaryKey); }

            return xlsProperties.getRowValueToMap(row);
        }
    }












    /** *********************** */




    public boolean addRowElement(RowElement element) {
        if (null == element)
            return false;

        Map<String, String> innerMap = element.getInnerMap();
        return this.addRow(innerMap);
    }
    public <T extends RowElement> T getRowAsElement(String primaryKey, Class<T> type) {
        Map<String, String> rowMap = getRowMap(primaryKey);

        //reflect
        T t = Reflects.newInstance(type);
        t.loadMapToVar(rowMap);
        return t;
    }



    public static abstract class RowElement implements IInnerMap<String, String> {
        @Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.PARAMETER, ElementType.TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        public @interface  IVar {
            String name();
        }


        protected Field[] getCurrentFields(){
            return Reflects.accessible(getClass().getDeclaredFields());
        }
        protected Field[] getIVarFields() {
            Field[] declaredFields = getCurrentFields();
            List<Field> fields = new ArrayList<>();
            for (Field declaredField : declaredFields) {
                IVar annotation = declaredField.getAnnotation(IVar.class);
                if (null != annotation) {
                    fields.add(declaredField);
                }
            }
            return fields.toArray(Finals.EMPTY_FIELD_ARRAY);
        }
        protected Map<String, Field> getIVarFieldMap() {
            Field[] declaredFields = getCurrentFields();
            Map<String, Field> fields = new HashMap<>();
            for (Field declaredField : declaredFields) {
                IVar annotation = declaredField.getAnnotation(IVar.class);
                if (null != annotation) {
                    fields.put(annotation.name(), declaredField);
                }
            }
            return fields;
        }



        protected void loadMapToVar(Map<String, String> map) {
            if(null == map) {
                return;
            }
            Map<String, Field> iVarFieldMap = getIVarFieldMap();
            for (String k : map.keySet()) {
                String  v = map.get(k);

                Field findField = iVarFieldMap.get(k);
                if (null != findField) {
                    set(findField, v);
                }
            }
        }

        protected void set(Field field, Object value) {
            try {
                field.set(this, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        protected Object get(Field field) {
            try {
                return field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Map<String, String> getInnerMap() {
            Field[] iVarField = getIVarFields();
            Map<String, String> map = new HashMap<>();
            for (Field declaredField: iVarField) {
                Object value = get(declaredField);
                IVar annotation = declaredField.getAnnotation(IVar.class);

                String k = annotation.name();
                String v = Strings.cast(value);
                map.put(k, v);
            }
            return map;
        }
    }
}
