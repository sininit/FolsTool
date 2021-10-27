package top.fols.atri.reflect;

public class PublicReflectCache extends ReflectCache {
	@Override public ClassesList 	 getClassesList(Class cls) 			 	{ return super.getClassesList(cls);      }
	@Override public ConstructorList getConstructorsList(Class cls)  		{ return super.getConstructorsList(cls); }
	@Override public FieldList 		 getFieldsList(Class cls)				{ return super.getFieldsList(cls);       }
	@Override public FieldList 		 getFieldsList(Class cls, String name)  { return super.getFieldsList(cls, name); }
	@Override public MethodList 	 getMethodsList(Class p1)   			{ return super.getMethodsList(p1);   }
	@Override public MethodList 	 getMethodsList(Class p1, String p2)    { return super.getMethodsList(p1, p2);   }




}
