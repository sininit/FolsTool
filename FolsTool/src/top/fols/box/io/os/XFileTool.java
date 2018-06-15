package top.fols.box.io.os;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import top.fols.box.annotation.XAnnotations;
import top.fols.box.lang.XString;
import top.fols.box.statics.XStaticBaseType;
import top.fols.box.util.ArrayListUtils;
import top.fols.box.util.XArrays;
import top.fols.box.util.XMap;
import top.fols.box.util.XObjects;

public class XFileTool
{
	private static final String[] formatSize = new String[]{"B","KB","MB","GB","TB","PB","EB","ZB","YB","BB","NB","DB","CB"};
	public static String fileFormatSize(String size)
	{
		return fileFormatSize(size, formatSize);
	}
	public static String fileFormatSize(String size, String[] formatSize)
	{
		return fileFormatSize(size, formatSize, 1024);
	}
	public static String fileFormatSize(String size, String[] formatSize, double PerUnitLength)
	{
		if (formatSize == null || formatSize.length == 0)
			throw new RuntimeException("size unit for null");
		int i = 0;
		if (size == null)
			return 0 + formatSize[i];
		BigDecimal perUnitLengthBigDecimal = new BigDecimal(PerUnitLength);
		if (new BigDecimal(size).compareTo(perUnitLengthBigDecimal) == -1)
			return size + formatSize[i];
		BigDecimal kiloByte = new BigDecimal(size).divide(perUnitLengthBigDecimal, 4, RoundingMode.DOWN);
		BigDecimal tmp;
		while (kiloByte.compareTo(BigDecimal.ZERO) == 1)//>
		{
			if (i + 1 > formatSize.length - 1)
				break;
			i++;
			tmp = kiloByte.divide(perUnitLengthBigDecimal, 4, RoundingMode.DOWN);
			if (tmp.compareTo(BigDecimal.ONE) == -1)
				break;//<1
			kiloByte = tmp;
		}
		return kiloByte.toString() + formatSize[i];
	}


	public static String fileFormatSize(double size)
	{
		return fileFormatSize(size, formatSize);
	}
	public static String fileFormatSize(double size, String[] formatSize)
	{
		return fileFormatSize(size, formatSize, 1024);
	}
	public static String fileFormatSize(double size, String[] formatSize, double PerUnitLength)
	{
		if (formatSize == null || formatSize.length == 0)
			throw new RuntimeException("size unit for null");
		int i = 0;
		if (size < PerUnitLength)
			return Double.toString(size) + formatSize[i];
		double Decimal1024 = PerUnitLength;
		double kiloByte = size / Decimal1024;
		double tmp;
		while (kiloByte > 0)//>0
		{
			if (i + 1 > formatSize.length - 1)
				break;
			i++;
			tmp = kiloByte / Decimal1024;
			if (tmp < 1)
				break;//<1
			kiloByte = tmp;
		}
		return Double.toString(kiloByte) + formatSize[i];
	}




	/*
	 Get Canonical Path 
	 文件获取绝对地址
	 getCanonicalPath("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").equals(new File("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();").getCanonicalPath()); >> true
	 getCanonicalPath("//XSt/*?:]/tt/////.//./././a/b/v//x//a/v**v//n///...//../../();"); >> "/XSt/*?:]/tt/a/b/v/x/a/v**v/();"
	 */
	public static String getCanonicalPath(String pathname)
	{
		if (pathname == null)
			return null;
		String name;
		List<String> Split = XString.split(pathname, File.separator);
		if (Split.size() == 0)
			return pathname;
		StringBuilder path = new StringBuilder();
		for (int i = 0;i < Split.size();i++)
		{
			name = Split.get(i);
			if (name.equals(".."))
			{
				Split.set(i, File.separator);
				for (int i2 = i;i2 >= 0;i2--)
				{
					name = Split.get(i2);
					if (name != null && !name.equals("") && !name.equals(File.separator) && !name.equals(".") && !name.equals(".."))
					{
						Split.set(i2, File.separator);
						break;
					}
				}
			}
			else if (name.equals("."))
			{
				Split.set(i, File.separator);
			}
		}
		for (int i = 0;i < Split.size();i++)
		{
			name = Split.get(i);
			if (name != null && !name.equals("") && !name.equals(File.separator) && !name.equals(".") && !name.equals(".."))
			{
				path.append(File.separator);
				path.append(name);
			}
		}
		return path.toString();
	}


	/*
	 Get Dir File List
	 获取文件夹文件列表

	 Parameter:filePath 路径,recursion 递增搜索,adddir 列表是否添加文件夹
	 */
	public static List<String> getFileList(String filePath, boolean recursion, boolean adddir) throws IOException
    {
		return getFilesList(new File(filePath) , recursion, adddir, null);
    }
	private static List<String> getFilesList(File filePath, boolean recursion, boolean adddir, String rootpath) throws IOException
    {
		List<String> result = new ArrayListUtils<String>();
		if (rootpath == null)
			rootpath = filePath.getCanonicalPath();
		if (!rootpath.endsWith(File.separator))
			rootpath += File.separator;
		File[] files = filePath.listFiles();
		for (File file : files)
		{
			String path = file.getCanonicalPath();
			if (path.startsWith(rootpath))
				path = path.substring(rootpath.length(), path.length());
			if (file.isDirectory())
			{
				if (recursion)
					result.addAll(getFilesList(file.getCanonicalFile(), true, adddir, rootpath));
				if (adddir)
					result.add(path + File.separator);
			}
			else
			{
				result.add(path) ;
			}
		}
		return result;
    }




	private static final Map<String,Boolean> defaultExtensionName;
	final static
	{
		defaultExtensionName = new XMap<Boolean>();
		defaultExtensionName.put("*", true);
	}
	public static String[] getFileList2(File Dir, @XAnnotations("Dir is shown at the top") boolean DirTop, @XAnnotations("extensionName * representative all files")  Map<String,Boolean> extensionName, @XAnnotations("showHideFile.")boolean showHideFile)
	{
		String[] list = new String []{};
		try
		{
			if (extensionName == null)
				extensionName =  defaultExtensionName;

			File[] arrStrings = Dir.listFiles();

			List<String> fileaddres = new ArrayListUtils<String>();
			List<String> fileaddresDir = new ArrayListUtils<String>();

			String file;
			if (!DirTop)
			{
				for (int i = 0; i < arrStrings.length; i++)
				{
					if (arrStrings[i].isDirectory())
					{
						file = arrStrings[i].getName() + File.separator;
						if (!showHideFile && arrStrings[i].isHidden())
							continue;
						fileaddresDir.add(file);
					}
					else
					{
						file = arrStrings[i].getName();
						if (!showHideFile && arrStrings[i].isHidden())
							continue;
						Boolean BooleanObject;
						String extensionFileName = new XFile(file).getExtensionName();
						boolean all = (BooleanObject = extensionName.get("*")) == null ?false: BooleanObject.booleanValue() ;
						boolean by = (BooleanObject = extensionName.get(extensionFileName)) == null ?false: BooleanObject.booleanValue();
						if (by || all && !extensionName.containsKey(extensionFileName))
							fileaddresDir.add(file);
					}
				}
			}
			else
			{
				for (int i = 0; i < arrStrings.length; i++)
				{
					if (arrStrings[i].isDirectory())
					{
						file  = arrStrings[i].getName() + File.separator;
						if (!showHideFile && arrStrings[i].isHidden())
							continue;
						fileaddresDir.add(file);
					}
					else
					{
						file = arrStrings[i].getName();
						if (!showHideFile && arrStrings[i].isHidden())
							continue;
						Boolean BooleanObject;
						String extensionFileName = new XFile(file).getExtensionName();
						boolean all = (BooleanObject = extensionName.get("*")) == null ?false: BooleanObject.booleanValue() ;
						boolean by = (BooleanObject = extensionName.get(extensionFileName)) == null ?false: BooleanObject.booleanValue();
						if (by || all && !extensionName.containsKey(extensionFileName))
							fileaddres.add(file);
					}
				}
			}
			// 使根据指定比较器产生的顺序对指定对象数组进行排序。 
			Object[] fileTmpArray;

			fileTmpArray = fileaddresDir.toArray();
			Arrays.sort(fileTmpArray, Collator.getInstance(java.util.Locale.CHINA));
			if (fileTmpArray.length > 0)
				list = (String[])XArrays.addAll(list, list.length, fileTmpArray, XStaticBaseType.String_class);

			fileTmpArray = fileaddres.toArray();
			Arrays.sort(fileTmpArray, Collator.getInstance(java.util.Locale.CHINA));
			if (fileTmpArray.length > 0)
				list = (String[])XArrays.addAll(list, list.length, fileTmpArray, XStaticBaseType.String_class);

			return list;
		}
		catch (Exception e)
		{
			return list;
		}
	}




	/*
	 is Parent Directory
	 文件是否在父目录

	 Parameter:FilePath 文件,ParentDir 目录
	 isParentDirectory("/sdcard/a/b"  ,  "/../sdcard/") true
	 */
	public static boolean isParentDirectory(File FilePath, File ParentDir) throws IOException
	{
		if (XObjects.isEmpty(FilePath) || XObjects.isEmpty(ParentDir))
			return false;
		String Parent = ParentDir.getCanonicalPath();
		if (!Parent.endsWith(File.separator))
			Parent += File.separator;
		if (FilePath.getCanonicalPath().startsWith(Parent) && ParentDir.exists())
			return true;
		return false;
	}


}
