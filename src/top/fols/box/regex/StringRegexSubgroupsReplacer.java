package top.fols.box.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"StringOperationCanBeSimplified"})
public class StringRegexSubgroupsReplacer {
    final Map<Integer, String> replaceList = new HashMap<>();
    String oldContent;
    Pattern pattern; Matcher matcher; int groupCount;
    int modCount;

    public StringRegexSubgroupsReplacer(String content, String pattern) {
        this(content, Regexs.compile(pattern));
    }
    public StringRegexSubgroupsReplacer(String content, Pattern pattern) {
        this.oldContent = content.toString();
        this.pattern = Objects.requireNonNull(pattern, "pattern");

        this.matcher = pattern.matcher(content);
        if (!this.matcher.find())	{
            throwable = "no found match";
            return;
        }
        this.groupCount = this.matcher.groupCount();
        if (this.groupCount <= 0) {
            throwable = "no found subgroup";
            return;
        }
        int st = this.matcher.start();
        int ed = this.matcher.end();
        if (ed - st != content.length()) {
            throwable = "no full match";
            return;
        }
        modCount++;
    }


    String throwable;
    public boolean isMatch() {
        return null == throwable;
    }
    void checkMatch() {
        if (null != throwable)
            throw new UnsupportedOperationException(throwable);
    }




    public String  oldContent() { return oldContent; }
    public Pattern pattern()    { return pattern;    }







    public String getSubgroup(int i) {
        if (isMatch()) {
            return matcher.group(i);
        } else {
            return null;
        }
    }

    public int getGroupCount() {
        checkMatch();
        return groupCount;
    }
    public StringRegexSubgroupsReplacer removeSubgroup(int i) {
        checkMatch();
        setSubgroup(i, "");
        return this;
    }
    public StringRegexSubgroupsReplacer setSubgroup(int i, String v) {
        checkMatch();
        replaceList.put(i, v);
        modCount++;
        return this;
    }

    public boolean isChanged() {
        return replaceList.size() != 0;
    }

    public String build() {
        if (isMatch()) {
            StringBuilder sb = new StringBuilder();
            String gs;

            int g = 1;
            sb.append(oldContent, 0, matcher.start(g));
            for (; g <= groupCount; g++) {
                String ms =  replaceList.get(g);
                if (null != ms)
                    gs = ms;
                else
                    gs = matcher.group(g);

                sb.append(gs);

                int next = g + 1;
                if (next <= groupCount) {
                    int st = matcher.end(g);
                    int ed = matcher.start(next);
                    sb.append(oldContent, st, ed);
                }
            }
            sb.append(oldContent, matcher.end(groupCount), oldContent.length());
            return sb.toString();
        } else {
            return null;
        }
    }

    int    lastToStringModCount;
    String lastToString;
    String fastString() {
        if (lastToStringModCount == modCount) {
            return lastToString;
        } else {
            String build = build();
            lastToStringModCount = modCount;
            lastToString = build;
            return build;
        }
    }
    @Override
    public String toString() {
        // TODO: Implement this method
        return fastString();
    }
}



