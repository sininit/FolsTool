package top.fols.box.util;

import top.fols.atri.io.util.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static final char UNLIMITED_LINE_SEPARATOR = '\0';
    private char valueSeparator;
    private char lineSeparator;

    private List<List<String>> lists = new ArrayList<>();



    public CSVReader() {
        this.valueSeparator = ',';
        this.lineSeparator  = UNLIMITED_LINE_SEPARATOR;
    }
    public CSVReader(char valueSeparator, char lineSeparator) {
        if (valueSeparator == lineSeparator) {
            throw new UnsupportedOperationException("value.separator == line.separator");
        }
        if (lineSeparator == UNLIMITED_LINE_SEPARATOR) {
            throw new UnsupportedOperationException("line.separator unable");
        }
        this.valueSeparator = valueSeparator;
        this.lineSeparator  = lineSeparator;
    }

    public void read(String csvFile) {
        try (StringReader stringReader = new StringReader(csvFile)) {
            read(stringReader);
        };
    }
    @SuppressWarnings("StatementWithEmptyBody")
    public void read(Reader reader) {
        char[] chars;
        try {
            chars = Streams.toString(reader).toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        char read;
        boolean isJoinValue = false;
        StringBuilder stringBuilder = new StringBuilder();
        List<List<String>> lines = new ArrayList<>();
        List<String> line = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            read = chars[i];

            if (isJoinValue) {
                if (read == '"') {
                    int pos = i + 1;
                    if (pos < chars.length) {
                        char aChar = chars[pos];
                        if (aChar == '"') {
                            stringBuilder.append('"');
                            i = pos;
                            continue;
                        }
                    }
                    isJoinValue = false;
                    String string = stringBuilder.toString();
                    line.add(string);
                } else {
                    stringBuilder.append(read);
                }
            } else {
                if (read == '"') {
                    isJoinValue = true;
                    stringBuilder.setLength(0);
                } else if (valueSeparator == read) {
                } else if (lineSeparator  == read) {
                    if (line.size() > 0) {
                        lines.add(line);
                        line = new ArrayList<>();
                    }
                } else if (lineSeparator == UNLIMITED_LINE_SEPARATOR && (read == '\r' || read == '\n')) {
                    int pos = i;
                    while (pos + 1 < chars.length) {
                        char aChar = chars[pos + 1];
                        if (!(aChar == '\r' || aChar == '\n')) {
                            break;
                        }
                        pos++;
                    }
                    if (line.size() > 0) {
                        lines.add(line);
                        line = new ArrayList<>();
                    }
                    i = pos;
                } else if (read == ' ') {
                } else {
                    throw new RuntimeException("unsuppert char: " + read);
                }
            }
        }
        if (line.size() > 0) {
            lines.add(line);
        }


        this.lists = lines;
    }

    public char getValueSeparator() {return valueSeparator;}
    public char getLineSeparator() {return lineSeparator;}
    public List<List<String>> getLists() {return lists;}





    public static void main(String[] args) throws FileNotFoundException {
//        String csvFile = "C:\\Users\\78492\\Desktop\\20230713Export.csv";
//
//        CSVReader csvReader = new CSVReader();
//        csvReader.read(new FileReader(csvFile));
//        List<List<String>> lists1 = csvReader.getLists();
//
//        MyXls myXls = new MyXls();
//        for (List<String> strings : lists1) {
//            Object[] array = strings.toArray();
//            if ("��".equals(array[8]))
//                array[8] = null;
//            myXls.addRowAsString(array);
//        }
//        myXls.writeToFile(new File("C:\\Users\\78492\\Desktop\\20230713Export.xlsx"));
    }
}
