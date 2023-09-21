
---
说明 （help）
``` 
        前提
            本语言是个小工具，调用函数就是语法, 请注意它的执行速度并不快
        语法
            除了注释 每段代码必须用; 或者,分割无论是任何代码，
            本解析器语法只支持 var . = () 任何 表达式函数 只是转换为函数 而已
        关键词 (Re.keyword) 仅在编译前修改，
            注释 
                只支持单行注释 //
            
            返回  
                最重要的语法, 如果返回了应该不在继续执行了, 谁先执行就是什么操作, 比如return(throw(0)); 就是throw(0); throw(return(0)); 就是返回 0, 除了try函数（）
                [throw()]   抛出异常
                [return()]  返回
            
            内置转换语法表达式函数
                class A {}
                init() {}
                function a  {}
                while(true) {}
                for(i=0;i<10;i=i+1) {}
                foreach(k,v, obj) {}
                try{}catch{}finally{}
                if{}else{}
                
                eval("a=1"); 动态执行字符串代码
            数据转换
                [str()] [int()] [long()] [char()] [float()] [double()] [short()] [byte()] [boolean()] 将字符串或者对象转为对应类型值
                
            常量声明 
                    int long char float double short byte  boolean  boolean null  char str
                a = (1, 2L,  3c,  4f,   5.5d,  6s,   7b,   true,    false,  null, ‘3’, "");
                a == "";
                
            算数表达式 Re_Primitive_Util_Math
                可以进行算术运算, 
                加载器是把运算符前面的整体和后面的整体转换为方法而已     注意它是没有优先级的  不过你可以自己加括号
                比如
                1+1         > +(1, 1 )
                1+5+9+10    > +(+(+(1,5),9),10)
                但是你不能主动声明+(1,1)
                
                支持的运算符号有很多种
                &   &&
                |   ||
                ^
                >>
                <<
                >>>
                !=  就是!equals
                ==  就是 equals
                === 相当于java中的==
                !== 相当于java中的==
                
                +
                -
                *
                /
                %
                
                <
                >
                <= 
                >=
                
                ~ 操作符 不支持 因为只有一个参数我懒得弄了直接搞成了方法  ~(8) 这样执行就行