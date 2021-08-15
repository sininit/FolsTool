package top.fols.atri.io;

import top.fols.atri.util.DoubleLinked;
import top.fols.box.io.os.XFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"SpellCheckingInspection", "rawtypes"})
public class Filex {
	static final Class BASIC_FILE_ATTRIBUTES_CLASS;


	static {
		Class aClass;
		try {
			aClass = BasicFileAttributes.class;
		} catch (Throwable e) {
			aClass = null;
		}

		BASIC_FILE_ATTRIBUTES_CLASS = aClass;
	}
    public static long getCreateTime(Object path) {
		File file = toFile(path);
        try {
            @SuppressWarnings("unchecked")
			FileTime t = Files.readAttributes(file.toPath(), BASIC_FILE_ATTRIBUTES_CLASS).creationTime();
            return t.toMillis();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }



	public static final char        UNIX_FILE_SEPARATOR_CHAR = '/';
	public static final String      UNIX_FILE_SEPARATOR_STRING = String.valueOf(UNIX_FILE_SEPARATOR_CHAR);

	public static final char        WINDOWS_FILE_SEPARATOR_CHAR = '\\';
	public static final String      WINDOWS_FILE_SEPARATOR_STRING = String.valueOf(WINDOWS_FILE_SEPARATOR_CHAR);



	static final char[] 	separatorChars 		= separator_all();
	public static char     	system_separator  	= File.separatorChar;
	public static String   	system_separators 	= File.separator;

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


	public static String nameCheck(String name) {
		if (null == name) {
			throw new IllegalArgumentException("name format error: " + name);
		}
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			for (char s: separatorChars) {
				if (ch == s) {
					throw new IllegalArgumentException("name format error: " + name);
				}
			}
		}
		return name;
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
		File file = toFile(path);
		return new FileOutputStream(file);
	}










	public static File toFile(Object path) {
		if (null == path) {	return null; }
		if (path instanceof Filez) {
			return ((Filez)path).innerFile();
		} else if (path instanceof File) {
			return (File) path;
		}
		String fPath = path.toString();
		return new File(fPath);
	}
	public static String    toPath(Object path) {
		File file = toFile(path);
        if (null == file) { return null; }
		if (path instanceof Filez) {
			return ((Filez)path).getPath();
		} else if (path instanceof File) {
			return ((File) path).getPath();
		}
		String fPath = path.toString();
		return fPath;
    }
	public static String toCanonicalPath(Object path) {
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
            Files.setLastModifiedTime(file.toPath(), fileTime);
            return true;
        } catch (Throwable e) {
            return file.setLastModified(millis);
        }
    }


	public static boolean exists(Object path) {
		File file = toFile(path);
		return null != file && file.exists();
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



	public static boolean isFile(Object path) {
		File file = toFile(path);
		return null != file && file.isFile();
	}

	public static boolean isDir(Object path) {
		File file = toFile(path);
		return null != file && file.isDirectory();
	}

	public static boolean isAbsolute(Object path) {
		File file = toFile(path);
		return null != file && file.isAbsolute();
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
        } else {
            boolean result = true;
            File[] files = file.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    File filei = files[i];
                    result &= deletes(filei);
                }
            }
            result &= file.delete();
            return result;
        }

    }




	
	





	
	
	
	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath) {
		return getName(filePath, system_separators);
	}
	/*
	 * 获得 文件名 带后缀
	 */
	public static String getName(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				return filePath.substring(dot + pathSeparator.length(), filePath.length());
			} else {
				return filePath;
			}
		}
		return null;
	}



	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath) {
		return getParent(filePath, system_separators);
	}
	/*
	 * 获得 文件目录
	 */
	public static String getParent(String filePath, String pathSeparator) {
		if ((null != filePath)) {
			int dot = filePath.lastIndexOf(pathSeparator);
			if (dot > -1) {
				return filePath.substring(0, dot + pathSeparator.length());
			}
		}
		return null;
	}

	/*
	 * 得到扩展名
	 */
	public static String getExtensionName(String filePath) {
		return getExtensionName(filePath, system_separators, FILE_EXTENSION_NAME_SEPARATORS);
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
	public static String getNameNoExtension(String fileCanonicalPath) {
		return getNameNoExtension(fileCanonicalPath, system_separators, FILE_EXTENSION_NAME_SEPARATORS);
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










	/**
	 * Normalize all system separators
	 * 归一化所有系统分隔符修改为目标分隔符
	 *
	 * if @param separator for /
	 * "\a/\b/\/c\d"		return "/a//b///c/d"
	 *
	 */
	public static String toFileSystemSeparator(String path, char separator) {
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

	/**
	 * Convert path to standard path(Remove duplicate separators)
	 * If the suffix is ​​separator, it will be deleted (unless separator is the only string)
	 * 将路径转换为标准路径(删除重复的分隔符)
	 * 如果字尾为separator将会删除(除非separator是唯一的字符串)
	 *
	 * if @param separator for /
	 * ""			return ""
	 * "/"			return "/"
	 * "a"			return "a"
	 * "a/"			return "a"
	 * "a/b/c/"		return "a/b/c"
	 * "/a/b/c/"	return "/a/b/c"
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
			if (plast == separator && ppath.length() > 1) {
				/**
				 * path "a/" "a/b/" ...
				 */
				ppath.setLength(ppath.length() - 1);
			} else {
				/**
				 * path ""  "/"  "a"  "/a"  ...
				 */
			}
		}
		return ppath.toString();
	}

	/**
	 * Convert path to standard path(Remove duplicate separators)
	 * If the suffix is ​​separator, it will be deleted (unless separator is the only string)
	 * 将路径转换为标准路径(删除重复的分隔符)
	 * 如果字尾为separator将会删除(除非separator是唯一的字符串)
	 *
	 * if @param separator for /
	 * "",		""		return ""
	 * "/",		""		return "/"
	 * "/a",	""		return "/a"
	 * "/a",	"/"		return "/a"
	 * "a",		"/"		return "a"
	 * "",		"/a"	return "/a"
	 * "/"      "a/b"	return "/a/b"
	 * "/a",	"/b"	return "/a/b"
	 * "a",		"b"		return "a/b"
	 * "a/",	"b"		return "a/b"
	 */
	public static String normalizePath(CharSequence parent, CharSequence subfilepath, char separator) {
		StringBuilder ppath = new StringBuilder(parent.length());

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
			if (plast == separator && ppath.length() > 1) {
				/**
				 * path "a/" "a/b/" ...
				 */
				ppath.setLength(ppath.length() - 1);
			} else {
				/**
				 * path ""  "/"  "a"  "/a"  ...
				 */
			}
		}


		/**
		 * sstart == 4: ////a//////
		 * sstart == 1: /a/////
		 * sstart == 0: a////
		 */
		int sstart = 0;
		int slen = subfilepath.length();
		while (sstart < slen && subfilepath.charAt(sstart) == separator) {
			sstart++;
		}

		/**
		 * There are characters other than delimiters
		 * 存在分隔符以外的字符
		 */
		if (slen - sstart > 0) {
			/**
			 * Determine whether there is content in the character buffer stream
			 * and determine whether the last character of the buffer stream is @param separator If not, add @param separator
			 * 判断字符缓冲流是否存在内容 and 则判断缓冲流最后一个字符是否为 @param separator 如果不是则添加 @param separator
			 */
			if ((ppath.length() > 0 && ppath.charAt(ppath.length() - 1) == separator) == false) {
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

			/**
			 * @param subfilepath a a/ /a/
			 */
			if (slast == separator && ppath.length() > 1) {
				ppath.setLength(ppath.length() - 1);
			}
		} else {
			/**
			 * There are no characters other than the separator,
			 * check whether the character buffer stream is empty,
			 * if it is empty, add a separator
			 * 不存在分隔符以外的字符, 判断一下 字符缓冲流 是否为空，为空则加入一个分隔符
			 */
			if (ppath.length() == 0 && sstart > 0) {
				ppath.append(separator);
			}
		}
		return ppath.toString();
	}

	/**
	 * Handling relative paths that can be handled . or ..
	 * Unrestricted if the prefix is ​​a file separator (Because this is an absolute path)
	 * It will not process drive letters or ~ (Windows DRIVE Letter or Unix ~)
	 *
	 * if @param separator for /
	 * "a/b/.."				return "a/"
	 * "a/../../a/"			return "../a/"
	 * "/../../a/"			return "/a/"
	 * "../../a/"			return "../../a/"
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
			if (DoubleLinked.equalsContent(".", now)) {// 处理 .
				DoubleLinked next = now.getNext();
				DoubleLinked prev = now.getPrev();
				now.remove();
				now = prev;

				if (DoubleLinked.equalsContent(separatorString, next)) {// 实际上必定为true 除非不存在下一个元素
					next.remove();
				}
			} else if (DoubleLinked.equalsContent("..", now)) {// 处理 ..
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
				normalizePath(toFileSystemSeparator(toAbsolutePath(path, separator), separator), separator):
				normalizePath(toAbsolutePath(path, separator), separator);

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
				normalizePath(toFileSystemSeparator(toAbsolutePath(dir, separator), separator), toFileSystemSeparator(subpath, separator), separator):
				normalizePath(toAbsolutePath(dir, separator), subpath, separator);

		String newPath = dealRelativePath(dealPath, separator);

		if (!newPath.startsWith(separatorString)) { // path ""
			newPath = separator + newPath;
		}

		return newPath;
	}





	private static String toAbsolutePath(String path, char separator) {
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
	public static boolean isAbsolute(String path, char separator) {
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


}
