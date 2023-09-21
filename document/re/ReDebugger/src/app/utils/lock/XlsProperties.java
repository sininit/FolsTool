




package app.utils.lock;

import app.utils.KeyIndexManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsProperties {
    XSSFWorkbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet();
    public XlsProperties() { }



    public static final String FILE_EXTENSION = "xlsx";

    Head head;
    Head getOrInitColumn() { return null == head ? head = new Head() :head; }


    static int rowOffset = 1;

    public List<String> getColumn() {
        synchronized (this) {
            return this.getOrInitColumn().list();
        }
    }



    public int addColumn(String value0) {
        synchronized (this) {
            String value = nonNull(value0);

            int col = this.getOrInitColumn().add(value);

            Row row = this.getOrInitPoiHeader();
            Cell cell = row.createCell(col);
            cell.setCellValue(value);

            setPoiColumnColor(cell, IndexedColors.GREY_25_PERCENT.index);
            return col;
        }
    }
    public void setColumn(int index, String value0) {
        synchronized (this) {
            if (index < 0 || index >= getColumnCount()) { throw new ArrayIndexOutOfBoundsException("index="+index+", size="+ getColumnCount()); }
            String value = nonNull(value0);

            Row row = this.getOrInitPoiHeader();
            Cell cell = row.getCell(index);
            if (null == cell) {
                throw new NullPointerException("head column: "+index);
            }

            cell.setCellValue(value);
            head.keyIndexManager.set(index, value);
        }
    }
    public int getColumnIndex(String name) {
        return this.getOrInitColumn().keyIndexManager.getIndex(name);
    }

    public int getColumnCount() {
        return this.getOrInitColumn().keyIndexManager.size();
    }
    public boolean hasColumn(String name) {
        return this.getOrInitColumn().keyIndexManager.contains(nonNull(name));
    }

    CellStyle headerStyle;
    public void setPoiColumnColor(Cell cell , short color) {
        if (null != cell) {
            CellStyle cellStyle = headerStyle;
            if (null == cellStyle){
                cellStyle = workbook.createCellStyle();
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setFillForegroundColor(color);//IndexedColors.AQUA.index
                headerStyle = cellStyle;
            }
            cell.setCellStyle(cellStyle);
        }
    }


    @Override
    public String toString() {
        return toString(false);
    }
    public String toString(boolean oneRowOnly) {
        StringBuilder stringBuilder = new StringBuilder();
        int rowCount = rowOffset + getRowCount();
        for (int i = 0; i< rowCount; i++) {
            Row row = sheet.getRow(i);
            if (null == row) { continue; }
            int cellCount = getCellCount(row);
            for (int i2 = 0; i2 < cellCount; i2++) {
                Cell cell = row.getCell(i2);
                String value = nonNull(null == cell?null:cell.getStringCellValue());
                if (oneRowOnly) {
                    value = value.replaceAll("\n", " ");
                }
                stringBuilder.append(value).append(Line.SEPARATOR);
            }
            if (stringBuilder.length() > Line.SEPARATOR.length()) {
                stringBuilder.setLength(stringBuilder.length() - Line.SEPARATOR.length());
            }
            stringBuilder.append("\n");
        }
        if (stringBuilder.length() > "\n".length()) {
            stringBuilder.setLength(stringBuilder.length() - "\n".length());
        }
        return stringBuilder.toString();
    }


    static int getRowCount(Sheet sheet) {
        return sheet.getLastRowNum() >= 1 && null != sheet.getRow(sheet.getLastRowNum())?sheet.getLastRowNum():0;
    }

    public int getRowCount() {
        synchronized (this) {
            return getRowCount(sheet);
        }
    }


    public static int getCellCount(Row row) {
        return row.getLastCellNum();
    }

    public int addRow() { return addRow((String[])null); }
    public int addRow(String... values) {
        synchronized (this) {
            int size = this.getRowCount();

            Row row = sheet.createRow(rowOffset + size);
            if (null != values) {
                for (int i = 0; i<values.length; i++) {
                    String value = nonNull(values[i]);

                    Cell cell = row.createCell(i);
                    cell.setCellValue(value);
                }
            }
            return size;
        }
    }





    public boolean removeRow(int index) {
        if (index < 0) { throw new ArrayIndexOutOfBoundsException("index="+index); }
        synchronized (this) {
            if (index >= getRowCount()) { throw new ArrayIndexOutOfBoundsException("index="+index+", size="+getRowCount()); }
            removeRow(sheet, index + rowOffset);
            return true;
        }
    }
    public void removeAllRowAndHeader() {
        synchronized (this) {
            this.getOrInitColumn().keyIndexManager.clear();
            for (int i = rowOffset + getRowCount(); i >= 0; i--) {
                Row row = sheet.getRow(i);
                if (null != row) {
                    sheet.removeRow(row);
                }
            }
            this.getOrInitPoiHeader();
        }
    }
    public void removeAllRow() {
        synchronized (this) {
            for (int i = rowOffset + getRowCount(); i >= rowOffset; i--) {
                Row row = sheet.getRow(i);
                if (null != row) {
                    sheet.removeRow(row);
                }
            }
        }
    }






    public void setValue(int row, int column, String value) {
        synchronized (this) {
            value = nonNull(value);

            int rowCount = this.getRowCount();
            if (row < 0 || row >= rowCount)     { throw new ArrayIndexOutOfBoundsException("set.row="+row+", row-count="+rowCount); }

            Row row1 = sheet.getRow(row + rowOffset);
            Cell cell = row1.getCell(column);
            if (null == cell) {
//              throw new ArrayIndexOutOfBoundsException("set.column="+column+", lastCell="+row1.getLastCellNum());
                cell = row1.createCell(column);
            }

            cell.setCellValue(value);
        }
    }

    public String getValue(int row, int column) {
        synchronized (this) {
            int rowCount = this.getRowCount();
            if (row < 0 || row >= rowCount)     { throw new ArrayIndexOutOfBoundsException("set.row="+row+", row-count="+rowCount); }

            Row row1 = sheet.getRow(row + rowOffset);
            Cell cell = row1.getCell(column);
            if (null == cell) { throw new ArrayIndexOutOfBoundsException("set.column="+column+", lastCell="+row1.getLastCellNum()); }

            return cell.getStringCellValue();
        }
    }
    public String[] getColumnValues(int column) {
        synchronized (this) {
            int rowCount = this.getRowCount();
            String[] values = new String[rowCount];
            for (int row = 0; row < rowCount; row++){
                Row row1 = sheet.getRow(row + rowOffset);
                try {
                    Cell cell = row1.getCell(column);
                    values[row] = cell.getStringCellValue();
                } catch (NullPointerException e){
                    throw e;
                }
            }
            return values;
        }
    }

    public static String nonNull(String value) {
        return null == value?"":value;
    }





    public String[] toRowValue(Map<String, String> valueMap) {
        String[] vs = new String[getColumnCount()];
        for (String key : valueMap.keySet()) {
            int column = getColumnIndex(key);
            if (column >= 0) {
                vs[column] = XlsProperties.nonNull(valueMap.get(key));
            }
        }
        return vs;
    }

    public Map<String, String> getRowValueToMap(int row) {
        synchronized (this) {
            int rowCount = this.getRowCount();
            if (row < 0 || row >= rowCount) {
                throw new ArrayIndexOutOfBoundsException("set.row=" + row + ", row-count=" + rowCount);
            }

            List<String> colName = this.getOrInitColumn().list();
            Row row1 = sheet.getRow(row + rowOffset);

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i< colName.size(); i++) {
                String name = colName.get(i);
                Cell cell = row1.getCell(i);
                String value = nonNull(null==cell?null:cell.getStringCellValue());
                map.put(name, value);
            }
            return map;
        }
    }
    public String[] getRowValue(int row) {
        synchronized (this) {
            int rowCount = this.getRowCount();
            if (row < 0 || row >= rowCount) {
                throw new ArrayIndexOutOfBoundsException("set.row=" + row + ", row-count=" + rowCount);
            }

            List<String> colName = this.getOrInitColumn().list();
            Row row1 = sheet.getRow(row + rowOffset);

            String[] values = new String[colName.size()];
            for (int i = 0; i< colName.size(); i++){
                Cell cell = row1.getCell(i);
                String value = nonNull(null==cell?null:cell.getStringCellValue());
                values[i] = value;
            }
            return values;
        }
    }



    /**
     * Remove a row by its index
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum)
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);//将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null)
                sheet.removeRow(removingRow);
        }
    }



    public boolean writeToFile(File file) throws IOException {
        return ExecutorLock.writeToFile(this.workbook, file);
    }





    public static Object getCellValue(Cell cell) {
        if (null == cell) return null;
        Object cellValue;
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.NUMERIC) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);//true时的格式：1,234,567,890
            double action = cell.getNumericCellValue();
            cellValue = nf.format(action);
        } else if (cellType == CellType.STRING) {
            cellValue = cell.getStringCellValue();
        } else if (cellType == CellType.BOOLEAN) {
            cellValue = cell.getBooleanCellValue();
        } else if (cellType == CellType.FORMULA) {
            cellValue = cell.getCellFormula();
        } else if (cellType == CellType.BLANK) {
            cellValue = null;
        } else if (cellType == CellType.ERROR) {
            cellValue = null;
        } else {
            cellValue = null;
        }
        return cellValue;
    }
    public static String getCellStringValue(Cell cell) {
        Object cellValue = getCellValue(cell);
        return null == cellValue ? null : String.valueOf(cellValue);
    }


    public static XlsProperties readFile(File file) throws IOException {
        Workbook workbook = ExecutorLock.readWorkBookFile(file);
        XlsProperties xlsProperties = new XlsProperties();
        if (null == workbook) {
            return xlsProperties;
        }

        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        if (null == header) {
            throw new NullPointerException("table header");
        }
        for (int i = 0; i< getCellCount(header); i++) {
            Cell cell = header.getCell(i);
            String value = nonNull(getCellStringValue(cell));

            xlsProperties.addColumn(value);
        }

        int rowCount = getRowCount(sheet);
        for (int i = 0; i< rowCount; i++) {
            Row row = sheet.getRow(i + rowOffset);
            String[] cellValue = new String[getCellCount(row)];
            for (int ci = 0; ci < cellValue.length; ci++){
                cellValue[ci] = nonNull(getCellStringValue(row.getCell(ci)));
            }
            xlsProperties.addRow(cellValue);
        }
        return xlsProperties;
    }









    Row getOrInitPoiHeader() {
        Row row = sheet.getRow(0);
        if (null == row) {
            Row row1 = sheet.createRow(0);
            return row1;
        } else {
            return row;
        }
    }






    public static class Head extends Line {
        public Head() {}
        KeyIndexManager keyIndexManager = new KeyIndexManager();

        void requireNo(String key) {
            if (contains(key)) {
                throw new UnsupportedOperationException("contains: "+key+", object: "+this);
            }
        }

        public boolean contains(String key) {
            return keyIndexManager.contains(key);
        }

        @Override
        public int add(String value) {
            this.requireNo(value);
            int size = keyIndexManager.size();
            keyIndexManager.add(value);
            return size;
        }

        public void remove(String key) {
            keyIndexManager.remove(key);
        }

        public List<String> list() {
            return keyIndexManager.getList();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : this.list())
                stringBuilder.append(value).append(SEPARATOR);
            if (stringBuilder.length() > SEPARATOR.length())
                stringBuilder.setLength(stringBuilder.length()-SEPARATOR.length());
            return stringBuilder.toString();
        }
    }
    public static class Line {
        List<String> values;
        public static final String SEPARATOR = "\t";
        public Line(String... args) {
            this.values = new ArrayList<>();
            if (null != args){
                for (String value: args){
                    this.values.add(null == value?"":value);
                }
            }
        }

        public int add(String values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : this.values)
                stringBuilder.append(value).append(SEPARATOR);
            if (stringBuilder.length() > SEPARATOR.length())
                stringBuilder.setLength(stringBuilder.length()-SEPARATOR.length());
            return stringBuilder.toString();
        }
    }










    public static void main(String[] args) throws IOException {
        XlsProperties xlsProperties = new XlsProperties();
        xlsProperties.addColumn("ID");
        xlsProperties.addColumn("账号");
        System.out.println(xlsProperties.getRowCount());

        xlsProperties.addRow("1", "2");
        System.out.println(xlsProperties.getRowCount());

        xlsProperties.addRow("3", "4");
        System.out.println(xlsProperties.getRowCount());

        System.out.println("\'" + xlsProperties.toString() + "\'");

        xlsProperties.removeAllRowAndHeader();

        System.out.println("\'" + xlsProperties.toString() + "\'");

        System.out.println("----------------------------------------");

        xlsProperties.addColumn("ID2");
        xlsProperties.addColumn("账号2");

        xlsProperties.addRow("5", "6");
        xlsProperties.addRow("7", "8");

        System.out.println(xlsProperties.getRowCount());

        xlsProperties.removeRow(1);

        System.out.println(xlsProperties.getRowCount());

        xlsProperties.addRow("9", "10");

        System.out.println(xlsProperties.getRowCount());

        System.out.println("\'" + xlsProperties.toString() + "\'");


        System.out.println("----------------------------------------");


        xlsProperties.setColumn(0, "不会吧不会吧");

        xlsProperties.setValue(0, 0, "hhh");
        xlsProperties.setValue(0, 1, "hhh2");


        xlsProperties.setValue(1, 0, "hhh3");
        xlsProperties.setValue(1, 1, "hhh4");


        System.out.println("\'" + xlsProperties.toString() + "\'");


        xlsProperties.writeToFile(new File("c:\\1." + XlsProperties.FILE_EXTENSION));


        System.out.println("----------------------------------------");
        System.out.println("read: " + XlsProperties.readFile(new File("c:\\1." + XlsProperties.FILE_EXTENSION)));


        System.out.println("----------------------------------------");


    }
}
