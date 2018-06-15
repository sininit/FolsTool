package top.fols.box.lang;
import top.fols.box.statics.XStaticBaseType;

public class XClassUtils
{
	public static boolean isInstance(Object obj, Class cls)
	{
		if (cls == null)
			return false;
		else if (cls.isPrimitive())//not base class
		{
			if (obj == null)
				return false;
			if (cls == XStaticBaseType.byte_class)
				return obj instanceof Byte;
			else if (cls == XStaticBaseType.long_class)
				return obj instanceof Long;
			else if (cls == XStaticBaseType.double_class)
				return obj instanceof Double;
			else if (cls == XStaticBaseType.char_class)
				return obj instanceof Character;
			else if (cls == XStaticBaseType.int_class)
				return obj instanceof Integer;
			else if (cls == XStaticBaseType.boolean_class)
				return obj instanceof Boolean;
			else if (cls == XStaticBaseType.float_class)
				return obj instanceof Float;
			else if (cls == XStaticBaseType.short_class)
				return obj instanceof Short;
			return false;
		}
		else
		{
			if (obj == null)
				return true;
			if (cls == XStaticBaseType.Byte_class)
				return obj instanceof Byte;
			else if (cls == XStaticBaseType.Long_class)
				return obj instanceof Long;
			else if (cls == XStaticBaseType.Double_class)
				return obj instanceof Double;
			else if (cls == XStaticBaseType.Character_class)
				return obj instanceof Character;
			else if (cls == XStaticBaseType.Integer_class)
				return obj instanceof Integer;
			else if (cls == XStaticBaseType.Boolean_class)
				return obj instanceof Boolean;
			else if (cls == XStaticBaseType.Float_class)
				return obj instanceof Float;
			else if (cls == XStaticBaseType.Short_class)
				return obj instanceof Short;
			return cls.isInstance(obj);//((String)null)
		}
	}
	
}
