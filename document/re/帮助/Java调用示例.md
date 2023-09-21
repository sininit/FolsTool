```

Java调用示例 （Example）

System.out.println(666)
 ``` java
 import top.fols.box.reflect.re.Re;
 
 Re re = new Re();
 re.exec("$(0).out.println($(1))", Re.jimport(System.class), 666);
 
 or
 
 Re re = new Re();
 re.exec("System = $(0)", Re.jimport(System.class));
 re.exec("System.out.println($(0))", 666);
 ```

System.out
 ``` java
 Re re = new Re();
 re.exec("System = $(0)", Re.jimport(System.class));
 System.out.println(re.exec("System.out") == System.out);
 ```

``` java
java 反射
    System.out.println(re.exec("jclasses(Main).Size.__value__"));
    
    System.out.println(re.exec("jfield(System).out.charOut.__value__.get(System.out)"));
    System.out.println(re.exec("jfield(System).out.__value__"));
    System.out.println(re.exec("jfield(System).out.formatter($(0)).__value__", Formatter.class));
    
    re.exec("System.out.println( jmethod(System).console().writer().__value__  )");
    re.exec("System.out.println( jmethod(System).exit(arguments(0)).__value__   )", int.class);
    re.exec("System.out.println( jmethod(arguments(0)).trim().__value__ )", "test base");
    re.exec("System.out.println( jmethod(System).console().writer().__value__");
    re.exec("System.out.println( jmethod(String).trim(returnType=String).__value__");
    
    re.exec("System.out.println( (jconstructor(System)())  )");
    re.exec("System.out.println( (jconstructor(String)(String))  )");
```