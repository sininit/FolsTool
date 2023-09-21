package top.fols.box.util;

import top.fols.atri.io.file.Filex;

public class Zips {

	public static final char   SEPARATOR  = '/';
	public static final String SEPARATORS = "" + SEPARATOR;

	public static final String ROOT_DIRECTORY = "";


	 static boolean pathIsFile(String path) {
		return !pathIsDirectory(path);
	}
	 static boolean pathIsDirectory(String path) {
		return  path.endsWith(SEPARATORS);
	}

	/**
	 * 不处理.. 和 .
	 */
	public static String formatPathToDirectory(String path) {
		return formatPath(path, false);
	}
	public static String formatPathToFile(String path) {
		return formatPath(path, true);
	}
	public static String formatPath(String path, Boolean isFile) {
		path = Filex.convertFileSeparator(path, SEPARATOR);
		path = Filex.normalizePath(path, SEPARATOR);

		if (path.startsWith(SEPARATORS)) {
			path = path.substring(SEPARATORS.length(), path.length());
		}

		if (path.length() == 0) {
			if (isFile == null || !isFile) return ROOT_DIRECTORY;
			
			throw new UnsupportedOperationException("empty file name");
		}

		if (isFile == null) return path;
		if (pathIsFile(path)) {
			if (isFile)
				return path;
			else
				return path + SEPARATORS;
		} else {
			if (isFile)
				return path.substring(0, path.length() - SEPARATORS.length());
			else
				return path;
		}
	}
	public static String formatPath(String subpath) {
		subpath = Filex.convertFileSeparator(subpath, SEPARATOR);
		subpath = Filex.normalizePath(subpath, SEPARATOR);

		if (subpath.startsWith(SEPARATORS)) {
			subpath = subpath.substring(SEPARATORS.length(), subpath.length());
		}

//		if (subpath.length() == 0)
//			throw new UnsupportedOperationException("empty file name");

		return subpath;
	}

}
