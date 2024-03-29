package top.fols.atri.io.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

import top.fols.atri.assist.json.JSONArray;
import top.fols.atri.assist.json.JSONObject;
import top.fols.atri.io.util.Streams;
import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Strings;

@SuppressWarnings({"SpellCheckingInspection", "rawtypes"})
public class Filex {
	static Class BASIC_FILE_ATTRIBUTES_CLASS;
	static {
		try {
			BASIC_FILE_ATTRIBUTES_CLASS = BasicFileAttributes.class;
		} catch (Throwable e) {
			BASIC_FILE_ATTRIBUTES_CLASS = null;
		}
	}

	public static long getCreateTime(Object path) {
		File file = toFile(path);
		try {
			@SuppressWarnings("unchecked")
			FileTime t = Files.readAttributes(toPath(file), BASIC_FILE_ATTRIBUTES_CLASS).creationTime();
			return t.toMillis();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}




	public static final String PATH_CURRENT_DIRECTORY_STRING = ".";
	public static final String PATH_CURRENT_PARENT_STRING    = "..";

	public static final char        UNIX_FILE_SEPARATOR_CHAR = '/';
	public static final String      UNIX_FILE_SEPARATOR_STRING = String.valueOf(UNIX_FILE_SEPARATOR_CHAR);

	public static final char        WINDOWS_FILE_SEPARATOR_CHAR = '\\';
	public static final String      WINDOWS_FILE_SEPARATOR_STRING = String.valueOf(WINDOWS_FILE_SEPARATOR_CHAR);


	public static final File FILE_CURRENT_DIRECTORY = new File(Filex.PATH_CURRENT_DIRECTORY_STRING);
	public static final File FILE_CURRENT_PARENT    = new File(Filex.PATH_CURRENT_PARENT_STRING);

	public static File newFileInstance(File path) {
		return null == path ? FILE_CURRENT_DIRECTORY : path;
	}
	public static File newFileInstance(String path) {
		return null == path ? FILE_CURRENT_PARENT : new File(path);
	}




	public static final char     	system_separator  	= File.separatorChar;
	public static final String   	system_separators 	= File.separator;
	static final char[] 	        separatorChars 		= separator_all();

	public static char[] separator_all() {
		Set<Character> separatorCharsTemp;
		separatorCharsTemp = new HashSet<>();
		separatorCharsTemp.add('\\');
		separatorCharsTemp.add('/');
		separatorCharsTemp.add(system_separator);

		char[] chars = new char[separatorCharsTemp.size()];
		int i = 0;
		for (Character character: separatorCharsTemp) {
			chars[i] = character;
			i++;
		}
		return chars;
	}



	/**
	 * filename
	 * @see Filex#PATH_CURRENT_DIRECTORY_STRING
	 * @see Filex#PATH_CURRENT_PARENT_STRING
	 */
	public static boolean namePoint(String name) {
		return PATH_CURRENT_DIRECTORY_STRING.equals(name) || PATH_CURRENT_PARENT_STRING.equals(name);
	}


	/**
	 * cannot exiets
	 * @see Filex#PATH_CURRENT_DIRECTORY_STRING
	 * @see Filex#PATH_CURRENT_PARENT_STRING
	 * @see Filex#separatorChars
	 */
	public static String nameCheck(String name) {
		if (!nameChecked(name)) {
			throw new IllegalArgumentException("name format error: " + name);
		}
		return name;
	}
	public static boolean nameChecked(String name) {
		if (null == name) {
			return false;
		}
		if (PATH_CURRENT_DIRECTORY_STRING.equals(name) || PATH_CURRENT_PARENT_STRING.equals(name)) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			for (char s: separatorChars) {
				if (ch == s) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * path is absolutely
	 * @see Filex#PATH_CURRENT_DIRECTORY_STRING
	 * @see Filex#PATH_CURRENT_PARENT_STRING
	 * @see Filex#separatorChars
	 */
	public static String pathCheck(String name) {
		if (!pathChecked(name)) {
			throw new IllegalArgumentException("name format error: " + name);
		}
		return name;
	}
	public static boolean pathChecked(String name) {
		if (null == name) {
			return false;
		}
		if (PATH_CURRENT_DIRECTORY_STRING.equals(name) || PATH_CURRENT_PARENT_STRING.equals(name)) {
			return false;
		}
		int last = 0, length = name.length(); String str;
		do {
			int next = -1;
			A: for (int i = last; i < length; i++) {
				char ch = name.charAt(i);
				for (char s: separatorChars) {
					if (ch == s) {
						next = i;
						break A;
					}
				}
			}
			if (next == -1) {
				str = name.substring(last, length);
				last = length;
			} else {
				str = name.substring(last, next);
				last = next + 1;
			}
			if (PATH_CURRENT_DIRECTORY_STRING.equals(str) || PATH_CURRENT_PARENT_STRING.equals(str)) {
				return false;
			}
		} while (last < length);
		return true;
	}





	public static final char   FILE_EXTENSION_NAME_SEPARATOR  = '.';
	public static final String FILE_EXTENSION_NAME_SEPARATORS = ""+FILE_EXTENSION_NAME_SEPARATOR;






	public static boolean forCreate(Object path) throws IOException {
		File file = toFile(path);
		return null != file && file.createNewFile();
	}
	public static boolean forMkdir(Object path) {
		File file = toFile(path);
		return null != file && file.mkdir();
	}
	public static boolean forMkdirs(Object path) {
		File file = toFile(path);
		return null != file && file.mkdirs();
	}

	public static FileInputStream  forInput(Object path) throws FileNotFoundException  {
		File file = toFile(path);
		return new FileInputStream(file);
	}
	public static FileOutputStream forOutput(Object path) throws FileNotFoundException {
		return forOutput(path, false);
	}
	public static FileOutputStream forOutput(Object path, boolean append) throws FileNotFoundException {
		File file = toFile(path);
		return new FileOutputStream(file, append);
	}










	public static File toFile(Object path) {
		if (null == path) {	return null; }
		if (path instanceof Filez) {
			return ((Filez)path).innerFile();
		}

		if (path instanceof File)
			return ((File) path);
		if (path instanceof Path)
			return ((Path) path).toFile();

		String fPath = path.toString();
		return new File(fPath);
	}
	public static String toFilePath(Object path) {
		File file = toFile(path);
		if (null == file) { return null; }
		if (path instanceof Filez) {
			return ((Filez) path).getPath();
		}

		if (path instanceof File)
			return ((File) path).getPath();
		if (path instanceof Path)
			return ((Path) path).toFile().getPath();

		return path.toString();
	}
	public static Path toPath(Object path) {
		if (null == path) { return null; }
		if (path instanceof Filez) {
			return ((Filez) path).toPath();
		}

		if (path instanceof File)
			return ((File) path).toPath();
		if (path instanceof Path)
			return ((Path) path);

		return new File(path.toString()).toPath();
	}
	public static String toLocalCanonicalPath(Object path) {
		if (path instanceof Filez) {
			return ((Filez)path).getCanonicalPath();
		} else {
			File file = toFile(path);
			if (null == file) { return null; }
			String rPath;
			try {
				rPath = file.getCanonicalPath();
			} catch (IOException ioException) {
				try {
					rPath = file.getAbsolutePath();
				} catch (SecurityException securityException) {
					rPath = file.getPath();
				}
			}
			return rPath;
		}
	}






	public static long getLastModified(Object path) {
		File   file = toFile(path);
		return file.lastModified();
	}
	public static boolean setLastModified(Object path, long millis) {
		File file = toFile(path);
		try {
			FileTime fileTime = FileTime.fromMillis(millis);
			Files.setLastModifiedTime(toPath(file), fileTime);
			return true;
		} catch (Throwable e) {
			return file.setLastModified(millis);
		}
	}


	public static boolean exists(Object path) {
		File file = toFile(path);
		return null != file && file.exists();
	}


	public static boolean createSymbolicLink(Object to, Path link) throws IOException {
		Files.createSymbolicLink(toPath(to), link);
		return true;
	}

	public static boolean createFile(Object path) {
		try {
			return forCreate(path);
		} catch (IOException e) {
			return false;
		}
	}
	public static boolean createsFile(Object path) {
		File file = toFile(path);
		if (createFile(file)) {
			return true;
		} else {
			createsDir(file.getParentFile());
			return createFile(file);
		}
	}


	public static boolean createDir(Object path) {
		File file = toFile(path);
		if (!(null != file && file.mkdir())) {
			return    file.isDirectory();
		}
		return true;
	}
	public static boolean createsDir(Object path) {
		File file = toFile(path);
		if (!(null != file && file.mkdirs())) {
			return    file.isDirectory();
		}
		return true;
	}

	public static boolean isSymbolicLink(Object path) {
		File file = toFile(path);
		return Files.isSymbolicLink(toPath(file));
	}

	public static boolean isFile(Object path) {
		File file = toFile(path);
		return null != file && file.isFile();
	}

	public static boolean isDir(Object path) {
		File file = toFile(path);
		return null != file && file.isDirectory();
	}


	public static boolean rwx(Object path) {
		File file = toFile(path);

		if (null == file)       { return false; }
		if (!file.canExecute()) { return false; }
		if (!file.canRead())    { return false; }
		if (!file.canWrite())   { return false; }
		return false;
	}





	public static long length(Object path) {
		File file = toFile(path);
		if (null == file) {
			return 0;
		}
		return file.length();
	}
	public static long lengths(Object path) {
		File file = toFile(path);
		if (null == file) {
			return 0;
		}
		if (file.isFile()) {
			return file.length();
		} else {
			long size = 0;
			File[] files = file.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					File filei = files[i];
					size += lengths(filei);
				}
			}
			size += file.length();
			return size;
		}

	}


	public static boolean delete(Object path) {
		File file = toFile(path);
		if (null == file) {
			return false;
		}
		return file.delete();
	}

	public static boolean deletes(Object path) {
		File file = toFile(path);
		if (null == file) {
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		} else if (isSymbolicLink(file)) {
			return file.delete();
		} else {
			boolean result = true;
			File[] files = file.listFiles();
			if (null != files) {
				for (File filei : files) {
					result &= deletes(filei);
				}
			}
			result &= file.delete();
			return result;
		}

	}






	public static boolean pathIsDirectory(String filePath) {
		if ((null!=filePath)) {
			int length = filePath.length();
			if (length > 0){
				char c = filePath.charAt(length - 1);
				for (char separatorChar : separatorChars) {
					if (c == separatorChar) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean pathIsDirectory(String filePath, String pathSeparator) {
		return filePath.endsWith(pathSeparator);
	}

	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath) {
		if ((null != filePath)) {
			if (pathIsDirectory(filePath)) {
				int pathSeparatorLen = 1;
				int dot = Strings.lastIndexOfChar(filePath, separatorChars, filePath.length() - pathSeparatorLen - pathSeparatorLen);
				if (dot > -1) {
					return filePath.substring(0, dot + pathSeparatorLen);
				}
			} else {
				int pathSeparatorLen = 1;
				int dot = Strings.lastIndexOfChar(filePath, separatorChars);
				if (dot > -1) {
					return filePath.substring(0, dot + pathSeparatorLen);
				}
			}
		}
		return null;
	}
	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			if (pathIsDirectory(filePath, pathSeparator)) {
				int pathSeparatorLen = pathSeparator.length();
				int dot = filePath.lastIndexOf(pathSeparator, filePath.length() - pathSeparatorLen - pathSeparatorLen);
				if (dot > -1) {
					return filePath.substring(0, dot + pathSeparatorLen);
				}
			} else {
				int dot = filePath.lastIndexOf(pathSeparator);
				if (dot > -1) {
					return filePath.substring(0, dot + pathSeparator.length());
				}
			}
		}
		return null;
	}







	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath) {
		if ((null != filePath)) {
			if (pathIsDirectory(filePath)) {
				int pathSeparatorLen = 1;
				int dot = Strings.lastIndexOfChar(filePath, separatorChars, filePath.length() - pathSeparatorLen - pathSeparatorLen);
				if (dot > -1) {
					return filePath.substring(dot + pathSeparatorLen, filePath.length() - pathSeparatorLen);
				} else {
					return filePath;
				}
			} else {
				int pathSeparatorLen = 1;
				int dot = Strings.lastIndexOfChar(filePath, separatorChars);
				if (dot > -1) {
					return filePath.substring(dot + pathSeparatorLen, filePath.length());
				} else {
					return filePath;
				}
			}
		}
		return null;
	}
	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			if (pathIsDirectory(filePath, pathSeparator)) {
				int pathSeparatorLen = pathSeparator.length();
				int dot = filePath.lastIndexOf(pathSeparator, filePath.length() - pathSeparatorLen - pathSeparatorLen);
				if (dot > -1) {
					return filePath.substring(dot + pathSeparatorLen, filePath.length() - pathSeparatorLen);
				} else {
					return filePath;
				}
			} else {
				int pathSeparatorLen = pathSeparator.length();
				int dot = filePath.lastIndexOf(pathSeparator);
				if (dot > -1) {
					return filePath.substring(dot + pathSeparatorLen, filePath.length());
				} else {
					return filePath;
				}
			}
		}
		return null;
	}



	/*
	 * 得到扩展名
	 */
	public static String getExtensionName(String filePath) {
		return getExtensionName(filePath, FILE_EXTENSION_NAME_SEPARATORS);
	}
	public static String getExtensionName(String filePath, String extensionNameSeparator) {
		if ((null != filePath) && (filePath.length() > 0)) {
			int dot = filePath.lastIndexOf(extensionNameSeparator);
			int splitCharDot = Strings.lastIndexOfChar(filePath, separatorChars);
			if (splitCharDot > -1) {
				if (dot > -1 && dot > splitCharDot) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			} else {
				if (dot > -1) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			}
		}
		return null;
	}

	public static String getExtensionName(String filePath, String pathSeparator, String extensionNameSeparator) {
		if ((null != filePath) && (filePath.length() > 0)) {
			int dot = filePath.lastIndexOf(extensionNameSeparator);
			int splitCharDot = filePath.lastIndexOf(pathSeparator);
			if (splitCharDot > -1) {
				if (dot > -1 && dot > splitCharDot) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			} else {
				if (dot > -1) {
					return filePath.substring(dot + extensionNameSeparator.length(), filePath.length());
				}
			}
		}
		return null;
	}

	/*
	 * 获得 文件名 不带带后缀
	 */
	public static String getNameNoExtension(String filePath) {
		if ((null != filePath)) {
			int pathSeparatorLen = 1;
			String extensionNameSeparator = Filex.FILE_EXTENSION_NAME_SEPARATORS;
			int dot = Strings.lastIndexOfChar(filePath, separatorChars);
			if (dot > -1) {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > dot) { // dot2 > -1
					return filePath.substring(dot + pathSeparatorLen, dot2);
				} else {// dot2 <= -1
					return filePath.substring(dot + pathSeparatorLen, filePath.length());
				}
			} else {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > -1) {
					return filePath.substring(0, dot2);
				} else {
					return filePath;
				}
			}
		}
		return null;
	}
	/*
	 * 获得 文件名 不带带后缀
	 */
	public static String getNameNoExtension(String filePath, String pathSeparator,
											String extensionNameSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > dot) { // dot2 > -1
					return filePath.substring(dot + pathSeparator.length(), dot2);
				} else {// dot2 <= -1
					return filePath.substring(dot + pathSeparator.length(), filePath.length());
				}
			} else {
				int dot2 = filePath.lastIndexOf(extensionNameSeparator);
				if (dot2 > -1) {
					return filePath.substring(0, dot2);
				} else {
					return filePath;
				}
			}
		}
		return null;
	}





	/**
	 * Check if the file has an invalid path. Currently, the inspection of
	 * a file path is very limited, and it only covers Nul character check.
	 * Returning true means the path is definitely invalid/garbage. But
	 * returning false does not guarantee that the path is valid.
	 *
	 * @return true if the file path is invalid.
	 */
	public static boolean isInvalid(String path) {
		return path.indexOf('\u0000') >= 0;
	}


	public static boolean isStartWithAbsolute(Object path) {
		File file = toFile(path);
		return null != file && file.isAbsolute();
	}


	//dealRelativePath|getCanonicalRelativePath|hasRelativePath|normalizePath|convertFileSeparator|dealRelativePath

	public static String startWithAbsolutePath(char separator, String path) {
		return separator + path;
	}



	/**
	 * is absolute path
	 * 判断是否为绝对路径
	 *
	 * if @param separator for /
	 * "/a"				return true
	 * "/../../"		return true
	 * "./"				return false
	 * "../"			return false
	 */
	public static boolean isStartWithAbsolute(char separator, String path) {
		int n = path.length();
		if (n == 0) return false;
		char c0 = path.charAt(0);
//        char c1 = (n > 1) ? path.charAt(1) : 0;
		if (c0 == separator) {
//            if (c1 == separator) return true;  /* Absolute UNC pathname "\\\\foo" */
			return true;                   /* Drive-relative "\\foo" */
		}
//		if (((c0 >= 'a') && (c0 <= 'z')) || ((c0 >= 'A') && (c0 <= 'Z')) && (c1 == ':')) {
//            if ((n > 2) && (path.charAt(2) == separator))
//                return true;               /* Absolute local pathname "z:\\foo" */
//            return true;                   /* Directory-relative "z:foo" */
//        }
		return false;
	}





	/**
	 * Normalize all system separators
	 * 归一化所有系统分隔符修改为目标分隔符
	 *
	 * if @param separator for /
	 * "\a/\b/\/c\d"		return "/a//b///c/d"
	 *
	 */
	public static String convertFileSeparator(String path, char separator) {
		StringBuilder sb = new StringBuilder(path.length());
		for (int i = 0; i < path.length(); i++) {
			char ch = path.charAt(i);
			for (char s: separatorChars) {
				if (ch == s) {
					ch = separator;
					break;
				}
			}
			sb.append(ch);
		}
		return sb.toString();
	}


	static class Paths {
		/**
		 * 去除重复斜杠 并且将分隔符转为统一格式
		 */
		public static String normalizePath(String path, char toSeparator) {
			return normalizePath(path, true, false, toSeparator);
		}
		/**
		 * 去除重复斜杠 并且将分隔符转为统一格式
		 * @param forceDirectory 如果是false则无意义
		 */
		@SuppressWarnings("CStyleArrayDeclaration")
		public static String normalizePath(String path, boolean isConverSeparator, boolean forceDirectory, char toSeparator) {
			int pathLength = path.length();
			char buffer[]  = new char[pathLength + 1];
			int  bufferLimit = 0;
			if (pathLength > 0) {
				char ch;
				ch = path.charAt(0);

				if (isConverSeparator) {
					for (char s: separatorChars) {
						if (ch == s) {
							ch = toSeparator;
							break;
						}
					}
					buffer[bufferLimit++] = ch;

					for (int i = 1; i < pathLength; i++) {
						ch = path.charAt(i);
						for (char s: separatorChars) {
							if (ch == s) {
								ch = toSeparator;
								break;
							}
						}
						if  (ch == toSeparator) {
							if (bufferLimit == 0 || buffer[bufferLimit - 1] != toSeparator)
								buffer[bufferLimit++] = toSeparator;
						} else {
							buffer[bufferLimit++] = ch;
						}
					}
				} else {
					buffer[bufferLimit++] = ch;
					for (int i = 1; i < pathLength; i++) {
						ch = path.charAt(i);
						if  (ch == toSeparator) {
							if (bufferLimit == 0 || buffer[bufferLimit - 1] != toSeparator)
								buffer[bufferLimit++] = toSeparator;
						} else {
							buffer[bufferLimit++] = ch;
						}
					}
				}
			}
			if (forceDirectory) {
				if (bufferLimit == 0) {
					path = String.valueOf(toSeparator);
				} else {
					if (buffer[bufferLimit - 1] != toSeparator)
						buffer[bufferLimit++] = toSeparator;
					path = new String(buffer, 0, bufferLimit);
				}
				return path;
			} else {
				if (bufferLimit == 0)
					return  "";
				return new String(buffer, 0, bufferLimit);
			}
		}


		/**
		 * 是否存在 . 和 ..
		 */
		public static boolean hasRelativePath(String path, char toSeparator) {
			String pathToUse = normalizePath(path, true, false, toSeparator);
			int pathToUseLength = pathToUse.length();
			if (pathToUseLength == 0)
				return false;
			String separatorString = String.valueOf(toSeparator);
			List<String> pathArray = Strings.split(pathToUse, separatorString);
			int i;
			for (i = pathArray.size() - 1; i >= 0; --i) {
				String element = pathArray.get(i);
				if (PATH_CURRENT_DIRECTORY_STRING.equals(element))
					return true;
				else if(PATH_CURRENT_PARENT_STRING.equals(element))
					return true;
			}
			return false;
		}

		/*
		 * "a/b/.."				return "a/"
		 * "a/../../a/"			return "../a/"
		 * "/../../a/"			return "/a/"
		 * "../../a/"			return "../../a/"
		 * ""					return ""
		 * "/.."				return "/"
		 * ".."                 return ".."
		 *
		 * "."                  return "/"
		 * "/"                  return "/"
		 */
		/**
		 * @param path 路径
		 * @param toSeparator 转换为 分隔符
		 */
		public static String dealRelativePath(String path, char toSeparator) {
			return dealRelativePath(path, false, toSeparator);
		}

		/**
		 * 处理相对路径 [. 和 ..]
		 * @param path 路径
		 * @param forceAbsolutePath 如果为true则强制为绝对路径（首字符为 @param toSeparator ）不会出现 . 和 ..  ，如果是FALSE则无意义
		 * @param toSeparator 转换为 分隔符
		 */
		private static String dealRelativePath(String path, boolean forceAbsolutePath, char toSeparator) {
			String pathToUse = normalizePath(path, true, false, toSeparator);
			int pathToUseLength = pathToUse.length();
			if (pathToUseLength == 0)
				return forceAbsolutePath ? String.valueOf(toSeparator) : "";
			boolean forceAbsolute  = forceAbsolutePath || pathToUse.charAt(0) == toSeparator;
			String separatorString = String.valueOf(toSeparator);

			List<String> pathArray = Strings.split(pathToUse, separatorString);
			List<String> pathElements = new LinkedList<>();
			int tops = 0;

			int i;
			for (i = pathArray.size() - 1; i >= 0; --i) {
				String element = pathArray.get(i);
				if (!PATH_CURRENT_DIRECTORY_STRING.equals(element)) {
					if (PATH_CURRENT_PARENT_STRING.equals(element)) {
						++tops;
					} else if (tops > 0) {
						--tops;
					} else {
						pathElements.add(0, element);
					}
				}
			}
			if (!forceAbsolute) {
				for (i = 0; i < tops; ++i) {
					pathElements.add(0, PATH_CURRENT_PARENT_STRING);
				}
			}
			int pathElementsSize = pathElements.size();
			if (pathElementsSize > 0) {
				StringBuilder sb = new StringBuilder();
				Iterator<String> iter = pathElements.iterator();
				String element = iter.next();
				if (element.length() != 0) {
					if (forceAbsolute)
						sb.append(toSeparator);
					sb.append(element);
				} else if (pathElementsSize == 1) {
					sb.append(toSeparator);
				}
				while (iter.hasNext()) {
					sb.append(toSeparator);
					sb.append(iter.next());
				}
				int sblen = sb.length();
				if (sblen > 0 && sb.charAt(sblen - 1) != toSeparator) {
					int parentSymbolLength = PATH_CURRENT_PARENT_STRING.length();
					if ((pathToUse.charAt(pathToUseLength - 1) == toSeparator) ||
							(pathToUse.regionMatches(pathToUseLength - parentSymbolLength, PATH_CURRENT_PARENT_STRING, 0, parentSymbolLength))) {
						sb.append(toSeparator);//is Directory
					}
				}
				return sb.toString();
			}
			return separatorString;
		}


		/**
		 * 获取绝对路径 不会出现  ../ 和 ./
		 */
		public static String getCanonicalRelativePath(String path, char toSeparator) {
			return dealRelativePath(path, true, toSeparator);
		}
		/**
		 * @param dir		父目录不会被处理相对路径
		 * @param subpath		子路径将会处理为绝对路径 绝对不会超过parent的路径 也就是不会出现 ../ 和 ./
		 * @param toSeparator		格式化为某个类型的分隔符
		 */
		public static String getCanonicalRelativePath(String dir, String subpath, char toSeparator) {
			StringBuilder stringBuilder = new StringBuilder(normalizePath(dir, true, true, toSeparator));
			String relativePath = dealRelativePath(subpath, true, toSeparator);
			return stringBuilder.append(relativePath, 1, relativePath.length()).toString();
		}


		//dealRelativePath|getCanonicalRelativePath|hasRelativePath|normalizePath|convertFileSeparator|dealRelativePath
		@SuppressWarnings("DanglingJavadoc")
		public static void main(String[] args) {
			System.out.println(dealRelativePath("/../../", '/'));
			System.out.println(getCanonicalRelativePath("a/../../a/", "/a/b", '/'));
//        * "a/b/.."				return "a/"
//        * "a/../../a/"			return "../a/"
//        * "/../../a/"			    return "/a/"
//        * "../../a/"			    return "../../a/"
//        * ""					    return ""
//        * "a/b/c/d/e"				return "a/b/c/d/e"
//        * "/.."				    return "/"
//        * ".."                    return "../"
			System.out.println("---------------------------------------");
			System.out.println(dealRelativePath("a/b/..", '/'));
			System.out.println(dealRelativePath("a/../../a/", '/'));
			System.out.println(dealRelativePath("/../../a/", '/'));
			System.out.println(dealRelativePath("../../a/", '/'));
			System.out.println(dealRelativePath("", '/'));
			System.out.println(dealRelativePath("a/b/c/d/e", '/'));
			System.out.println(dealRelativePath("/..", '/'));
			System.out.println(dealRelativePath("..", '/'));
			System.out.println("---------------------------------------");
			/**
			 * /a/
			 * /a/
			 * /a/
			 * /a/
			 * /
			 * /a/b/c/d/e
			 * /
			 * /
			 */
			System.out.println(dealRelativePath("a/b/..", true, '/'));
			System.out.println(dealRelativePath("a/../../a/", true, '/'));
			System.out.println(dealRelativePath("/../../a/", true, '/'));
			System.out.println(dealRelativePath("../../a/", true, '/'));
			System.out.println(dealRelativePath("", true, '/'));
			System.out.println(dealRelativePath("a/b/c/d/e", true, '/'));
			System.out.println(dealRelativePath("/..", true, '/'));
			System.out.println(dealRelativePath("..", true, '/'));
			System.out.println("---------------------------------------");
			/**
			 * a/b/../a
			 * a/b/../
			 * a/../../a/a/b
			 * /../../a/a/b
			 * ../../a/b
			 * /
			 * /
			 * a/b/c/d/e/
			 * /../
			 * ../a/b
			 */
			System.out.println(getCanonicalRelativePath("a/b/..", "a", '/'));
			System.out.println(getCanonicalRelativePath("a/b/..", "", '/'));
			System.out.println(getCanonicalRelativePath("a/../../a/", "/a/b", '/'));
			System.out.println(getCanonicalRelativePath("/../../a/", "a/b", '/'));
			System.out.println(getCanonicalRelativePath("../../a/", "/b", '/'));
			System.out.println(getCanonicalRelativePath("", "/a/..", '/'));
			System.out.println(getCanonicalRelativePath("/", "..", '/'));
			System.out.println(getCanonicalRelativePath("a/b/c/d/e", "/a/../..", '/'));
			System.out.println(getCanonicalRelativePath("/..", "/a/../..", '/'));
			System.out.println(getCanonicalRelativePath("..", "/a/b", '/'));
			System.out.println();
		}
	}
	public static String normalizePath(String path, char separator) {
		return Paths.normalizePath(path, separator);
	}
	public static String dealRelativePath(String path, char separator) {
		return Paths.dealRelativePath(path, separator);
	}
	public static boolean hasRelativePath(String path, char separator) {
		return Paths.hasRelativePath(path, separator);
	}
	public static String getCanonicalRelativePath(String path) {
		return Paths.getCanonicalRelativePath(path, system_separator);
	}
	public static String getCanonicalRelativePath(String path, char separator) {
		return Paths.getCanonicalRelativePath(path, separator);
	}
	public static String getCanonicalRelativePath(String path, String subpath) {
		return Paths.getCanonicalRelativePath(path, subpath, system_separator);
	}
	public static String getCanonicalRelativePath(String path, String subpath, char separator) {
		return Paths.getCanonicalRelativePath(path, subpath, separator);
	}












	public static Path getSymbolicLinkTarget(Object from) throws IOException {
		return Files.readSymbolicLink(toPath(from));
	}


	public static boolean copySymbolicLinkTo(Object from, Object to) throws IOException {
		createSymbolicLink(to, getSymbolicLinkTarget(from));
		return true;
	}
	protected static boolean copyTo(Filez file, Snapshot childrenFileSnapshot, Filez to, boolean ignoredIoError) throws IOException  {
		if (null == to) {
			throw new NullPointerException("to file");
		}
		if (to.exists()) {
			//throw new IOException("existed: " + file);
		}
		boolean result = true;
		if (file.isFile()) {
			to.createsFile();
			try {
				copyAfterClose(file.input(), file.output());
			} catch (IOException e) {
				if (ignoredIoError) {
					result = false;
				} else {
					throw e;
				}
			}
		} else if(isSymbolicLink(file)) {
			try {
				copySymbolicLinkTo(file, to);
			} catch (IOException e) {
				if (ignoredIoError) {
					result = false;
				} else {
					throw e;
				}
			}
		} else if (file.isDirectory()) {
			to.createsDir();

			if (null != childrenFileSnapshot) {
				for (String s : childrenFileSnapshot.keySet()) {
					final Filez fromFiles = file.files(s);
					if (childrenFileSnapshot.isFile(s)) {
						Filez toFiles;
						toFiles = to.files(s);
						toFiles.createsFile();
						try {
							copyAfterClose(fromFiles.input(), toFiles.output());
						} catch (IOException e) {
							if (ignoredIoError) {
								result = false;
							} else {
								throw e;
							}
						}
					} else {
						result = result & copyTo(fromFiles, childrenFileSnapshot.getFileList(s), to.files(s), ignoredIoError);
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static long copyAfterClose(InputStream input, OutputStream output) throws IOException {
		long   copy = Streams.copyFixedLength(input, output, Streams.DEFAULT_BYTE_BUFF_SIZE, Streams.COPY_UNLIMITED_COPY_LENGTH, true);
		output.flush();

		Streams.close(input);
		Streams.close(output);

		return copy;
	}
	/**
	 * deep file list
	 */
	@SuppressWarnings({"UnnecessaryLocalVariable"})
	public static class Snapshot implements Serializable {
		static final long serialVersionUID = 42L;

		String name;
		LinkedHashMap<String, Object> fileLinkedHashMap = new LinkedHashMap<>();//thread unsafe

		private Snapshot(String name) {
			this.name = name;
		}


		private void put(String name, File file) {
			if (null == file) { return; }
			fileLinkedHashMap.put(name, file);
		}
		private void put(String name, Snapshot fileList) {
			if (null ==   fileList) { return; }
			fileLinkedHashMap.put(name, fileList);
		}


		public boolean contains(String name) {
			return fileLinkedHashMap.containsKey(name);
		}
		public boolean remove(String name) {
			return null == fileLinkedHashMap.remove(name);
		}


		public static Snapshot parse(File file) {
			if (null == file) {
				throw new NullPointerException("to file");
			}
			return parse0(new Snapshot(file.getName()),  file);
		}
		//递归搜索文件，假设子文件的子文件中存在一个子文件的链接，是否会造成无限死循环？
		private static Snapshot parse0(Snapshot fileList, File file) {
			if (null == file) {
				throw new NullPointerException("to file");
			}
			File[] fs = file.listFiles();
			for (File f: null == fs ? Filez.EMPTY_FILE_ARRAY: fs) {
				String name = f.getName();
				if (f.isFile()) {
					fileList.put(name, f);
				} else {
					Snapshot fileL = new Snapshot(name);
					fileList.put(name, fileL);
					parse0(fileL, f);
				}
			}
			return fileList;
		}



		public String   getName() {
			return name;
		}
		public String[] listName() {
			return fileLinkedHashMap.keySet().toArray(Finals.EMPTY_STRING_ARRAY);
		}


		public Set<String> keySet() {
			return fileLinkedHashMap.keySet();
		}

		public Object get(String name) {
			Object object = fileLinkedHashMap.get(name);
			return object;
		}

		public boolean isDirectory(String name) {
			return get(name) instanceof Snapshot;
		}
		public boolean isFile(String name) {
			return get(name) instanceof File;
		}

		public Snapshot getFileList(String name) {
			Object object = get(name);
			if (object instanceof Snapshot) { return (Snapshot) object; }
			return null;
		}
		public File 	getFile(String name) {
			Object object = get(name);
			if (object instanceof File)     { return (File) object;     }
			return null;
		}


		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Snapshot)) return false;
			Snapshot snapshot = (Snapshot) o;
			return Objects.equals(name, snapshot.name) &&
				   Objects.equals(fileLinkedHashMap, snapshot.fileLinkedHashMap);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, fileLinkedHashMap);
		}

		@Override
		public String toString() {
			JSONObject to = new JSONObject();

			JSONArray tp = new JSONArray();
			for (Object value: fileLinkedHashMap.values()) {
				if (value instanceof File) {
					value = ((File)value).getName();
				}
				tp.put(null==value?null:value.toString());
			}
			to.put(name, tp);
			return to.toFormatString();
		}
	}
}



