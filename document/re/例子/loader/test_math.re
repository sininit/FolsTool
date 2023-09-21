jimport("int[]");
println("---------------------------------------------------循环10000000次");

println( (start=time()) );
i = 0;
while(i < 10000000) {
    i=i+1
};
println("耗时：" + (time() - start));
println(i);


println("---------------------------------------------------Map");
a = object();
a.a = 666;
a.b = int[](1,2,3,4);
a.jimport = jimport;
println(a);

ints = a.jimport("int[]");
println(ints);

println(1,2,3,4,5,6,7,8,9);

println("---------------------------------------------------");




//编译本文件在 snapdragon 865上可能需要1毫秒
//如果需要甚至可以实现不读取全部, 直接 一行一行的执行，这样就能无视长度 不需要等待就能执行
//
//将符号数量记录 后面再判断是否加载符号

//println("7",  if(throw(return(4)), a) );
//throw(return(1, return(2, (4), eval("5"))), 3);
//return();

//可以直接修改但是 如果你改了我鄙视你
//Integer.MAX_VALUE = 44;
//println(Integer.MAX_VALUE);

//class java.lang.Long
println((null = 999999999999999999).getClass());

//999999999999999999 11 4.5 1 :31183
println(999999999999999999, 11, 4.5, 1, ((1+((597*56)/4)+9-(44/4*4+535+7)) <<2)  + 55   );

//2
println(if(1){2}else{3});

a = (1;2);
//2
println(a);

//1 2
println(
    (a=1);
    (b=2);
);
println("-"*10);


try {
    eval("

        eval(\"

              //eval(throw(44));
                a=5;

                throw(0);




        \")
    ")
} catch(error) {
    println(error)
};


//throw(777);


//5
println(a);
println("----------------");



a = array([0+0,1+1,2+1,3+1,4+1,5+1,6+1,7+1,8+1,9+1]);
//10
println(a());
//2   // a[1]
println(a(1));
//哈哈哈
println(a(0, "哈哈哈"));
//[哈哈哈, 2, 3, 4, 5, 6, 7, 8, 9, 10]
println(a);
//666
jimport("java.lang.Integer");
println(Integer.parseInt("666"));

//class java.lang.String
jimport("java.lang.Class");
println(Class.forName("java.lang.String"));
println("----------------");

//false
println(1 && 2 && null && println("a:"+666));
//1
println(1 || 2 || null || println("b:"+666));
//d:666
//true
println(1 | 2 | null | println("d:"+666));
//666666
//true
println(1 | 2 | null | println(666+"666"));
//--------------------
println("-"*20);

println("------");
//5
println(1 - (-4));
println("------");

//error: Error: 	base[open] not a method, type[null]
//Trace:
//	at: <source:line>
try { w=open() } catch(err) {
    println("error: "+err),
    try{ w.write(true), err.println() }
} finally {
    try{ w.close() }
};

//1 2  4.0 5.5 6 7 true false null 3 z
//Hello World! z
println("Hello World!", println(1, 2L,  3c,  4f,   5.5d,  6s,   7b,   true,    false,  null, '3', "z"));



println("--------------");
//x=true k=true
//x=false k=true

x=true; k=false;
while(x) {
    if(k) {
        x=false
    } else {
        k=true
    };
    println("x="+x, "k="+k)
};
println("--------------");


//true
println(not(false) || println(123456));//注释

//0 1 2 3 4 5 6 7 8 9
for((i=0, j=10), i < j, i=i+1) {
    println(i)
};

//444
println(444);
if (x){
}else{
   x = true
};
//true
println(x);

//10
println(""+(1+4+5));


//throw(0);
