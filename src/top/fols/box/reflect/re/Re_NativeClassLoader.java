package top.fols.box.reflect.re;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import top.fols.atri.io.file.Filex;
import top.fols.atri.interfaces.annotations.NotException;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.box.reflect.re.resource.Re_IReResource;
import top.fols.atri.util.DoubleLinkedList;
import top.fols.box.reflect.re.resource.Re_IReResourceFile;

/**
 * 类加载不是必须的 没有加载器 类也能炮
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
public abstract class Re_NativeClassLoader {
	protected final Object lock = new Object();
	protected final Re re;
	protected final Re_NativeClassLoader parent;


	final Map<String, Re_Class>            loaded = new LinkedHashMap<>();	//锁
	final DoubleLinkedList<Re_IReResource> source = new DoubleLinkedList<>(); //锁


	Re_NativeClassLoader(Re re, Re_NativeClassLoader parent) {
		this.re     = re;
		this.parent = parent;
	}

	public static String formatClassName(String name) {
		if (null == name)
			return null;
		if (name.length() == 0)
			return null;

		int st = 0;
		int len = name.length();
		for (int i = 0; i < len; i++) {
			char ch = name.charAt(i);
			if (ch == Re_CodeLoader.PACKAGE_SEPARATOR) {
				st++;
			} else {
				A:{
					for (char c : Re_CodeLoader.SYSTEM_FILE_SEPARATOR) {
						if (ch == c) {
							st++;
							break A;
						}
					}
					break;
				}
			}
		}
		if (st != 0) {
			return name.substring(st, name.length());
		}
		return name;
	}

	/**
	 * @param path 文件路径 支持 \ / 扩展名
	 * @return 将文件路径转换为类名
	 */
	public static String pathToClassName(String path) {
		if (null == path)
			return null;

		int len = path.length();
		String extendName = Filex.getExtensionName(path, Re_CodeLoader.FILE_EXTENSION_SYMBOL);
		if (null != extendName) {
			len = len - extendName.length() - Re_CodeLoader.FILE_EXTENSION_SYMBOL.length();
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < len; i++){
			char ch = path.charAt(i);
			boolean sep = false;
			for (char c : Re_CodeLoader.SYSTEM_FILE_SEPARATOR) {
				if (ch == c){
					sep = true;
					break;
				}
			}
			if (sep) {
				int b_length = stringBuilder.length();
				if (b_length != 0) {
					char last = stringBuilder.charAt(b_length - 1);
					if  (last != ch) {
						stringBuilder.append(Re_CodeLoader.PACKAGE_SEPARATOR);
					}
				}
			} else {
				if (ch == Re_CodeLoader.PACKAGE_SEPARATOR) {
//					throw new UnsupportedOperationException("invalid name: " + path+ Re_CodeLoader.CODE_BLANK_SPACE_CHARS + Re_CodeLoader.CODE_BLANK_SPACE_CHARS +"["+i+"]");
					return null;
				}
				stringBuilder.append(ch);
			}
		}
		if (stringBuilder.length() == 0)
			return null;
		return stringBuilder.toString();
	}

	public static String classNameToPath(String name) {
		return classNameToPath(name, Filex.system_separator);
	}
	public static String classNameToPath(String name, char pathSeparator) {
		if (null == name)
			return null;

		int len = name.length();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < len; i++){
			char ch = name.charAt(i);
			boolean sep = false;
			for (char c : Re_CodeLoader.SYSTEM_FILE_SEPARATOR) {
				if (ch == c){
					sep = true;
					break;
				}
			}
			if (sep) {
//				throw new UnsupportedOperationException("invalid name: " + name+ Re_CodeLoader.CODE_BLANK_SPACE_CHARS + Re_CodeLoader.CODE_BLANK_SPACE_CHARS +"["+i+"]");
				return null;
			} else {
				if (ch == Re_CodeLoader.PACKAGE_SEPARATOR) {
					stringBuilder.append(pathSeparator);
					continue;
				}
				stringBuilder.append(ch);
			}
		}
		if (stringBuilder.length() == 0)
			return null;
		return stringBuilder
				.append(Re_CodeLoader.FILE_EXTENSION_SYMBOL).append(Re_CodeLoader.RE_FILE_EXTENSION_NAME).toString();
	}





	public Re 			  getRe() {
		return this.re;
	}
	public Re_NativeClassLoader getParent() {
		return parent;
	}

	/**
	 * @param name 完全限定名
	 */
	public Re_Class findLoadedClass(String name) {
		synchronized (lock) {
			return loaded.get(name);
		}
	}








	/*
	 * 遍历寻类找源
	 * 越后面添加则等级越高
	 */
	protected Re_IReResourceFile getClassResource(String className) {
		synchronized (lock) {
			for (DoubleLinkedList.Element<Re_IReResource> element = this.source.getFirst(); null != element; element = (DoubleLinkedList.Element<Re_IReResource>) element.getNext()) {
				Re_IReResource resource = element.content();
				Re_IReResourceFile data = resource.findClassResource(className);
				if (null != data) {
					return  data;
				}
			}
			return null;
		}
	}

	public Re_IReResourceFile getFileResource(String filePath) {
		synchronized (lock) {
			for (DoubleLinkedList.Element<Re_IReResource> element = this.source.getFirst(); null != element; element = (DoubleLinkedList.Element<Re_IReResource>) element.getNext()) {
				Re_IReResource resource = element.content();
				Re_IReResourceFile data = resource.getFileResource(filePath);
				if (null != data) {
					return  data;
				}
			}
			return null;
		}
	}

	/**
	 *
	 * @param filePath 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public Long getFileResourceSize(String filePath) {
		synchronized (lock) {
			for (DoubleLinkedList.Element<Re_IReResource> element = this.source.getFirst(); null != element; element = (DoubleLinkedList.Element<Re_IReResource>) element.getNext()) {
				Re_IReResource resource = element.content();
				Long data = resource.getFileSize(filePath);
				if (null != data) {
					return  data;
				}
			}
			return null;
		}
	}
	/**
	 *
	 * @param filePath 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public Long getFileResourceLastModified(String filePath) {
		synchronized (lock) {
			for (DoubleLinkedList.Element<Re_IReResource> element = this.source.getFirst(); null != element; element = (DoubleLinkedList.Element<Re_IReResource>) element.getNext()) {
				Re_IReResource resource = element.content();
				Long data = resource.getFileLastModified(filePath);
				if (null != data) {
					return  data;
				}
			}
			return null;
		}
	}
	/**
	 *
	 * @param filePath 文件路径
	 * @return 为空则代表资源不可读
	 */
	@Nullable
	public InputStream getFileResourceInputStream(String filePath) {
		synchronized (lock) {
			for (DoubleLinkedList.Element<Re_IReResource> element = this.source.getFirst(); null != element; element = (DoubleLinkedList.Element<Re_IReResource>) element.getNext()) {
				Re_IReResource resource = element.content();
				InputStream data = resource.getFileInputStream(filePath);
				if (null != data) {
					return  data;
				}
			}
			return null;
		}
	}







	protected Re_IReResource[] getSources() {
		return this.source.toArray(new Re_IReResource[]{});
	}



	protected boolean removeSourceManager(Re_IReResource resource) {
		synchronized (lock) {
			DoubleLinkedList.Element<Re_IReResource> find = source.indexOf(resource);
			if (null != find) {
				source.remove(find);
				return true;
			}
			return false;
		}
	}
	protected void addSourceManager(Re_IReResource resource) {
		synchronized (lock) {
			DoubleLinkedList.Element<Re_IReResource> find = source.indexOf(resource);
			if (null != find) {
				source.remove(find);
			}
			this.source.addFirst(new DoubleLinkedList.Element<>(resource));//将资源管理器推到第一个
		}
	}
	protected boolean hasSourceManager(Re_IReResource resource) {
		synchronized (lock) {
			return null != source.indexOf(resource);
		}
	}









	/**
	 * 如果已经记载了将会返回已加载结果
	 * 会先向父类记载类 如果不存在再从 本加载器所有资源里找
	 *
	 * *****不要抛出Java异常，如果异常应该是内部异常
	 *
	 *    @param stack 编译代码 以及 初始化类   异常
	 *    @param className 完全限定名
	 */
	Re_Class lookupClass(Re_NativeStack stack, String className)  {
		synchronized (lock) {
			if (stack.isThrow()) return null;

			className = formatClassName(className);

			//对原始名称判断
			Re_Class    re_class = loaded.get(className);
			if (null != re_class) {
				return  re_class;
			}

			if (null != parent) {
				re_class = parent.lookupClass(stack, className);
				if (stack.isThrow()) return null;
				if (null != re_class)
					return  re_class;
			}
//			else {
//				//bootstrap loader...
//				Re_NativeClassLoader bootstrapClassLoader = re.getBootstrapClassLoader();
//				if (null !=    bootstrapClassLoader && this != bootstrapClassLoader) {
//					re_class = bootstrapClassLoader.fromReInnerLoadClass(stack, className);
//					if (stack.isThrow()) return null;
//					if (null != re_class)
//						return  re_class;
//				}
//			}

			//获取类
			Re_IReResourceFile classSource = this.getClassResource(className);
			if (null == classSource) {
				return null;
			}

			//统一名称
			re_class = loaded.get(className = classSource.name());
			if (null != re_class) {
				return  re_class;
			}


			//编译代码
			String code = classSource.asString();
			Re_CodeFile classFile = this.compileReClassFile(stack, classSource.getResourceURL(), code);
			if (stack.isThrow())
				return null;

			//创建类
			Re_Class define;
			define = Re_Class.Unsafes.createReClass(null, className, classFile, null);	//不可出现错误
			define.setReClassLoader(stack, this);	//不可出现错误
			if (stack.isThrow())
				return null;
			className = define.getName();


			//初始化类
			loaded.put(className, define);	//可以导入自己，防止死循环

			this.initReClass(stack, define);

			if (stack.isThrow()) {
				loaded.remove(className);
				return null;
			}

			return define;
		}
	}



	/**
	 * 定义一个顶级类
	 *  @param filePath 文件路径
	 * @param code     代码字节
	 */
	@NotException
	protected abstract Re_CodeFile compileReClassFile(Re_NativeStack stack, String filePath, String code);

	@NotException
	protected abstract void initReClass(Re_NativeStack stack, Re_Class define);




	/**
	 * 从JAVA端执行
	 */
	public Re_Class loadClassOrThrowEx(String className) {
		Re_NativeStack stack = re.newStack();
		Re_Class reClass = lookupClass(stack, className);
		if (null == reClass && !stack.isThrow()) {
			stack.setThrow(Re_Accidents.not_found_reclass(className));
		}
		Re.throwStackException(stack);
		return reClass;
	}
}

