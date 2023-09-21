package top.fols.atri.io.other;

import top.fols.atri.interfaces.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"RedundantCast", "unused"})
public class LineEditor {

    private int offsetLine;
    private int lastLine;
    private final Map<Integer, StringBuilder> map = new HashMap<>();

    private final String separator;

    static public final String DEFAULT_SEPARATOR = "\n";
    static final int UNSET = -1;



    public LineEditor() {
        this(UNSET, DEFAULT_SEPARATOR);
    }
    public LineEditor(int offsetLine) {
        this(offsetLine, DEFAULT_SEPARATOR);
    }

    public LineEditor(String separator) {
        this(UNSET, separator);
    }
    public LineEditor(int   offLine, String separator) {
        if (offLine <= 0 && offLine != UNSET)
            throw new RuntimeException("error grow: " + offLine);

        offsetLine = lastLine = offLine;

        this.separator       = separator;
    }




    public boolean isEmpty() {
        if (UNSET == offsetLine)
            return true;

        StringBuilder stringBuilder;
        return offsetLine == lastLine && (null == (stringBuilder = map.get(offsetLine)) || stringBuilder.length() == 0);
    }


    public String getSeparator() {
        return separator;
    }


    public boolean isSet(){
        return UNSET != offsetLine;
    }

    public int getOffsetLine() {
        if (UNSET == offsetLine)
            return UNSET;
        return offsetLine;
    }
    public int getLastLine() {
        if (UNSET == offsetLine)
            return UNSET;
        return lastLine;
    }



    private StringBuilder editLine0(int line) {
        Integer k = (Integer) line;

        StringBuilder cache = map.get(k);
        if (null == cache)
            map.put(k, cache = new StringBuilder());

        return cache;
    }

    public boolean hasLine(int line) {
        return UNSET != offsetLine &&
                line >= offsetLine && line <= lastLine;
    }



    public LineEditor nextLine() {
        if (UNSET == offsetLine) {
            append(1, "");
            return this;
        }else {
            append(lastLine + 1, "");
            return this;
        }
    }




    int grow(int line) {
        if (UNSET == offsetLine)
            offsetLine = lastLine = (line = Math.max(line, 1));

        if (line <= 0)
            line = lastLine;
        else if (line > lastLine)
            lastLine = line;

        return line;
    }

    public LineEditor setLineContent(int line, String content) {
        line = grow(line);

        if (null == content)
            return this;

        if (content.length() != 0)
            if (content.contains(separator))
                throw new RuntimeException("contains separator: "+"["+separator+"]");

        StringBuilder stringBuilder = editLine0(line);
        stringBuilder.setLength(0);
        stringBuilder.append(content);
        return this;
    }




    /**
     * 返回新的行
     */
    public int append(String content) {
        return append(getLastLine(), content);
    }
    /**
     * 返回新的行
     * 如果表里已存在line，并且content是多行
     *   假设line的下一行存在内容则会抛出异常
     */
    public int append(int line, String content) {
        int oLine = line = grow(line);

        if (null == content)
            return line;

        if (content.length() != 0) {
            int last = 0;
            int current;
            boolean readEnd = false;
            while (true) {
                if ((current = content.indexOf(separator, last)) == -1)
                    if (readEnd) {
                        break;
                    } else {
                        current = content.length();
                        readEnd = true;
                    }
                String lineContent = content.substring(last, current);

                StringBuilder stringBuilder = editLine0(line);
                if (stringBuilder.length() != 0)
                    if (line != oLine) //next line
                        throw new RuntimeException("already exits content line: " + line);

                stringBuilder.append(lineContent);

                last = current + separator.length();
                line += (readEnd ? 0 :1);
            }
            lastLine = Math.max(line, lastLine);
        }
        return line;
    }









    public String getLineContent(int line) {
        if (!hasLine(line))
            return null;

        Integer integer = (Integer) line;
        StringBuilder cache = map.get(integer);
        if (null ==   cache)
            return "";
        else
            return cache.toString();
    }



    public String toFile() {
        if (UNSET == offsetLine) //no edit
            return "";

        StringBuilder line = new StringBuilder();
        for (int off = 1; off <= lastLine; off++)
            line.append(getLineContent(off)).append(separator);

        if (line.length() > separator.length())
            line.setLength(line.length() - separator.length());

        return line.toString();
    }

    /**
     * trim string
     */
    @NotNull
    @Override
    public String toString() {
        if (UNSET == offsetLine) //no edit
            return "";

        if (offsetLine == lastLine) {
            return getLineContent(offsetLine);
        } else {
            StringBuilder line = new StringBuilder();

            for (int off = offsetLine; off <= lastLine; off++)
                line.append(getLineContent(off)).append(separator);

            if (line.length() > separator.length())
                line.setLength(line.length() - separator.length());

            return line.toString();
        }
    }
}

