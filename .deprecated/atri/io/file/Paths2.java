public class Paths2{
    /**
     * Convert path to standard path(Remove duplicate separators)
     * If the suffix is separator, it will be deleted (unless separator is the only string)
     * 将路径转换为标准路径(删除重复的分隔符)
     * 如果字尾为separator将会删除(除非separator是唯一的字符串)
     *
     * if @param separator for /
     * ""			return ""
     * "/"			return "/"
     * "a"			return "a"
     * "a/"			return "a"
     * "a//b//c"	return "a/b/c"
     * "a//b//c/"	return "a/b/c/"
     * "//a//b//c/"	return "/a/b/c/"
     */
    public static String normalizePath(CharSequence path, char separator) {
        StringBuilder ppath = new StringBuilder(path.length());

        int plen = path.length();
        if (plen > 0) {
            char plast = path.charAt(0);
            ppath.append(plast);
            int i;
            for (i = 1;i < plen;i++) {
                char ch = path.charAt(i);
                if (ch == separator) {
                    if (plast == separator) {
                        continue;
                    }
                }
                ppath.append(plast = ch);
            }
        }
        return ppath.toString();
    }

    /**
     * Convert path to standard path(Remove duplicate separators)
     * If the suffix is separator, it will be deleted (unless separator is the only string)
     * 将路径转换为标准路径(删除重复的分隔符)
     * 如果字尾为separator将会删除(除非separator是唯一的字符串)
     *
     * if @param separator for /
     * "",		""		return ""
     * "/",		""		return "/"
     * "/a",	""		return "/a"
     * "/a",	"/"		return "/a/"
     * "a",		"/"		return "a/"
     * "",		"/a"	return "/a"
     * "/"      "a/b"	return "/a/b"
     * "/a",	"/b"	return "/a/b"
     * "a",		"b"		return "a/b"
     * "a/",	"b"		return "a/b"
     */
    public static String normalizePath(CharSequence parent, CharSequence subfilepath, char separator) {
        StringBuilder ppath = new StringBuilder(parent.length());

        boolean parentEndSeparator;
        int plen = parent.length();
        if (plen > 0) {
            char plast = parent.charAt(0);
            ppath.append(plast);
            int i;
            for (i = 1;i < plen;i++) {
                char ch = parent.charAt(i);
                if (ch == separator) {
                    if (plast == separator) {
                        continue;
                    }
                }
                ppath.append(plast = ch);
            }
            parentEndSeparator = plast == separator;
        } else {
            parentEndSeparator = false;
        }


		/*
		 sstart == 4: ////a//////
		 sstart == 1: /a/////
		 sstart == 0: a////
		 */
        int sstart = 0;
        int slen = subfilepath.length();
        while (sstart < slen && subfilepath.charAt(sstart) == separator) {
            sstart++;
        }


        if (slen - sstart <= 0) {
            if (sstart > 0) {
                ppath.append(separator);
            }
        } else {
			/*
			 There are characters other than delimiters
			 存在分隔符以外的字符
			 */
            if (!parentEndSeparator) {
                ppath.append(separator);
            }

            char slast = subfilepath.charAt(sstart);
            for (int i = sstart;i < slen;i++) {
                char ch = subfilepath.charAt(i);
                if (ch == separator) {
                    if (slast == separator) {
                        continue;
                    }
                }
                ppath.append(slast = ch);
            }
        }
        return ppath.toString();
    }



    /**
     * Handling relative paths that can be handled . or ..
     * Unrestricted if the prefix is a file separator (Because this is an absolute path)
     * It will not process drive letters or ~ (Windows DRIVE Letter or Unix ~)
     *
     * if @param separator for /
     * "a/b/.."				return "a/"
     * "a/../../a/"			return "../a/"
     * "/../../a/"			return "/a/"
     * "../../a/"			return "../../a/"
     * "/../../a/"			return "/a/"
     * ""					return ""
     * "/.."				return "/"
     * ".."                 return ".."
     */
    public static String dealRelativePath(String path, char separator) {
        int length = path.length();
        if (length == 0) {
            return "";
        }
        String separatorString = String.valueOf(separator);

        DoubleLinked<String> p = new DoubleLinked<>(null);
        DoubleLinked<String> bottom = p;
        DoubleLinked<String> top = p;

        int lastIndex = 0;
        for (int i = 0; i < length; i++) {
            char ch = path.charAt(i);
            if (ch == separator) {
                String t = path.substring(lastIndex, i);
                DoubleLinked<String> element;

                if (i > 0) {
                    element = new DoubleLinked<String>(t);
                    top.addNext(element);
                    top = element;
                }

                element = new DoubleLinked<String>(separatorString);
                top.addNext(element);
                top = element;

                lastIndex = i + 1;
            }
        }
        if (lastIndex != length) {
            String t = path.substring(lastIndex, length);
            DoubleLinked<String> element = new DoubleLinked<String>(t);
            top.addNext(element);
            top = element;
        }
        /**
         * after >>
         * /a/b/c//  =>  {null, /, a, /, b, /, c, /, /}
         * a/b/c//  =>  {null, a, /, b, /, c, /, /}
         * a/b/c  =>  {null, a, /, b, /, c}
         */




        DoubleLinked now = bottom.getNext();
        DoubleLinked limit = null;
        while (true) {
            if (DoubleLinked.equalsContent(PATH_CURRENT_DIRECTORY_STRING, now)) {// 处理 .
                DoubleLinked next = now.getNext();
                DoubleLinked prev = now.getPrev();
                now.remove();
                now = prev;

                if (DoubleLinked.equalsContent(separatorString, next)) {// 实际上必定为true 除非不存在下一个元素
                    next.remove();
                }
            } else if (DoubleLinked.equalsContent(PATH_CURRENT_PARENT_STRING, now)) {// 处理 ..
                DoubleLinked prev = now.getPrev();//分隔符 或者为 null

                DoubleLinked first = bottom.getNext();
                if (prev == bottom) { // 前面不存在分隔符
                    /*   ..   */
                    DoubleLinked next = now.getNext();
                    limit = DoubleLinked.equalsContent(separatorString, next) ?next: limit; // 实际上必定为true 除非不存在下一个元素
                    now = next;
                    continue;
                } if (prev == first) { // 分隔符是在第一个元素 此为绝对路径
                    /*   /..   */
                    DoubleLinked next = now.getNext();
                    DoubleLinked last = now.getPrev();
                    now.remove();
                    now = last;

                    if (DoubleLinked.equalsContent(separatorString, next)) {// 实际上必定为true 除非不存在下一个元素
                        next.remove();
                    }
                    limit = first;
                    continue;
                } else {
                    if (prev == limit) {// 无法返回上一层
                        DoubleLinked next = now.getNext();
                        limit = DoubleLinked.equalsContent(separatorString, next) ?next: limit;// 实际上必定为true 除非不存在下一个元素
                        now = next;
                        continue;
                    }
                }

                //   a/.. /a/b/../ /a/..  ../a/../ ../../  ../../a/../..
                // 必定不是分隔符 或者为 null
                DoubleLinked prev_prev = null == prev ? null : prev.getPrev();
                prev_prev = prev_prev == bottom ? null : prev_prev;

                if (null != prev) {
                    prev.remove();
                }
                if (null != prev_prev) {
                    prev_prev.remove();
                }

                DoubleLinked next = now.getNext();
                DoubleLinked last = now.getPrev();
                now.remove();
                now = last;

                if (DoubleLinked.equalsContent(separatorString, next)) { // 实际上必定为true 除非不存在下一个元素
                    next.remove();
                }
            }

            if (!(null != now && null != (now = now.getNext()))) {
                break;
            }
        }

        DoubleLinked absbottom = bottom.getNext();
        bottom.remove();

        if (null == absbottom) {
            return "";
        } else {
            StringBuilder right = new StringBuilder();
            DoubleLinked x = absbottom;
            do {
                right.append(x);
            } while (null != (x = x.getNext()));
//			if (right.length() > 1 && right.charAt(right.length() - 1) == separator) { right.setLength(right.length() - 1); }
            return right.toString();
        }
    }
    @SuppressWarnings({"rawtypes", "UnnecessaryLocalVariable"})
    public static boolean hasRelativePath(String path, char separator) {
        int length = path.length();
        if (length == 0) {
            return false;
        }
        String separatorString = String.valueOf(separator);

        DoubleLinked<String> p = new DoubleLinked<>(null);
        DoubleLinked<String> bottom = p;
        DoubleLinked<String> top = p;

        int lastIndex = 0;
        for (int i = 0; i < length; i++) {
            char ch = path.charAt(i);
            if (ch == separator) {
                String t = path.substring(lastIndex, i);
                DoubleLinked<String> element;

                if (i > 0) {
                    element = new DoubleLinked<>(t);
                    top.addNext(element);
                    top = element;
                }

                element = new DoubleLinked<>(separatorString);
                top.addNext(element);
                top = element;

                lastIndex = i + 1;
            }
        }
        if (lastIndex != length) {
            String t = path.substring(lastIndex, length);
            DoubleLinked<String> element = new DoubleLinked<>(t);
            top.addNext(element);
            top = element;
        }
        /*
         * after >>
         * /a/b/c//  =>  {null, /, a, /, b, /, c, /, /}
         * a/b/c//  =>  {null, a, /, b, /, c, /, /}
         * a/b/c  =>  {null, a, /, b, /, c}
         */
        DoubleLinked now = bottom.getNext();
        do {
            if (DoubleLinked.equalsContent(PATH_CURRENT_DIRECTORY_STRING, now)) {// 处理 .
                return true;
            } else if (DoubleLinked.equalsContent(PATH_CURRENT_PARENT_STRING, now)) {// 处理 ..
                return true;
            }
        } while (null != now && null != (now = now.getNext()));
        return false;
    }






    public static String getCanonicalRelativePath(String path) {
        return getCanonicalRelativePath(path, true, system_separator);
    }
    public static String getCanonicalRelativePath(String path, char separator) {
        return getCanonicalRelativePath(path, true, separator);
    }
    /**
     * Nothing to do with the file system
     * Treat the path as an absolute path
     * The delimiter at the end of the suffix will be deleted.
     * The prefix of the return path must be the @param delimiter
     * 与文件系统无关
     * 将路径作为绝对路径处理
     * 字尾的 @param separator 将被删除 返回路径字首必定为 @param separator
     *
     * @param path
     * @param changeAllFileSystemSeparator if ture will format @param path file
     *                     separator to @param separator
     * @param separator
     * @return
     */
    public static String getCanonicalRelativePath(String path, boolean changeAllFileSystemSeparator, char separator) {
        String separatorString = String.valueOf(separator);

        String dealPath = changeAllFileSystemSeparator ?
                normalizePath(convertFileSeparator(startWithAbsolutePath(separator, path), separator), separator):
                normalizePath(startWithAbsolutePath(separator, path), separator);
        String newPath = dealRelativePath(dealPath, separator);

        if (!newPath.startsWith(separatorString)) { // path ""
            newPath = separator + newPath;
        }

        return newPath;
    }

    public static String getCanonicalRelativePath(String dir, String subpath) {
        return getCanonicalRelativePath(dir, subpath, true, system_separator);
    }
    public static String getCanonicalRelativePath(String dir, String subpath, char separator) {
        return getCanonicalRelativePath(dir, subpath, true, separator);
    }
    public static String getCanonicalRelativePath(String dir, String subpath, boolean changeAllFileSystemSeparator, char separator) {
        String separatorString = String.valueOf(separator);

        String dealPath = changeAllFileSystemSeparator ?
                normalizePath(convertFileSeparator(startWithAbsolutePath(separator, dir), separator), convertFileSeparator(subpath, separator), separator):
                normalizePath(startWithAbsolutePath(separator, dir), subpath, separator);
        String newPath = dealRelativePath(dealPath, separator);

        if (!newPath.startsWith(separatorString)) { // path ""
            newPath = separator + newPath;
        }

        return newPath;
    }

}