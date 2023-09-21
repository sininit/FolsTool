package app.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.text.NumberFormat;

public class PoiUtils {
    public static class ErrorCell {
        private byte id;
        public ErrorCell(byte id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ErrorCell{" +"id=" + id + '}';
        }
    }


    public static Object getCellValue(Cell cell) {
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

}
