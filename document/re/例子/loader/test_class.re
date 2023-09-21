println("---------------------------------------------class_test_start");

println("---------------------------------------------------类和类方法 实例测试");


import main;
import test_class;

Student = object();
class ak {
    println(Student);
    Student = 24;
    init() {
        this.Student = 25;
    };
};
println(ak.Student);
println(ak().Student);
println(ak.Student);


//object@99347477
//24
//25
//24

println("---------------------------------------------------");

pa = "pa: 9999";
k = class {
    a=8;
    println(a+1+5, pa); 

    v2   = function(){return(pa)};
    v    = function(){return(a)};

    //实例初始化方法
    init() {
        this.a = 666;
        return(k);
    };
	

    getA = function() {
        println(pa);
        return (static.a)
    };
    getThisA = function(){
        return (this.a)
    };

    class soidahoidiosadia {
		//throw("throw file line test1");
	};
    //throw("throw file line test2");
};
function(){
    //throw("throw file line test3");
    println(pa, k, k.a); 
}();

//14
//pa: 9999
//
//pa: 9999
//class class_test$class_24_380fb434
//8

println("---------------------------------------------------执行类方法：");

println(k.v());
println(pa);
println(k.v2());    
println(k.v2);      

//8
//pa: 9999
//pa: 9999
//function class_test$class_24_380fb434.function_28_668bc3d5 

println("---------------------------------------------------实例测试：");

kinstance = k();
println("0: "+k);
println("1: "+kinstance);
println("2: "+kinstance.a);
println("3: "+kinstance.getA());
println("3: "+kinstance.getThisA());
println("4: "+k.a);

//0: class class_test$class_24_380fb434
//1: class_test$class_24_380fb434@785992331
//2: 666
//pa: 9999
//3: 8
//3: 666
//4: 8

println("---------------------------------------------------类和类方法2");



ab = "ab: "+54;
abstract = function(){
    return (println(ab));
};
abstract();

a = class{
    ab = 6;
    def = function() {
        println(ab);//6
        ab = 4;
        av = 4455;
        r = class {
           println(ab);//4
           ab=5;

           getab = function() {
               println(av);
               return (ab);
           };
        };
        ass=r.getab;
        //4455
        //5
        println(ass());

        return (
            function() {
                return(ab)
            }
        );
    };
    init() {
        this.ab = 8;
        this.assks = 456;

        ab = 74;
        this.get2 = function() {
            println("草:"+this.assks);
            println("草:"+ab);
        };
        this.get2();

        this.abstract = abstract;
    };

    assks = 456789;
    get = function() {
        //从实例创建了下面的   function那么变量域就是  实例
        //从静态方法创建了下面的function那么变量域就是  静态对象
        return(
            function() {
                println("草:"+assks);
                println("草:"+ab);
            }
        );
    };
};
//4
println(eval("a.def()()"));

//ab: 54
//6
//4
//4455
//5
//4
println("---------------------------------------------------");

instance = a();
println(instance);

a.get()();




//草:456
//草:74
//class_test$class_92_dfd3711@391447681
//草:456789
//草:6
println("---------------------------------------------------");

abstract();
instance.abstract();

//ab: 54
//ab: 54
println("---------------------------------------------------");

instance.get()();

//草:456789
//草:6
println("---------------------------------------------------");





that = static;
i=0;
function() {
    while(i<1000000) {
        i=i+1
    };
    println(i);

    that.i = i;
}();
println(i);

//1000000
//1000000

println("---------------------------------------------------");



h = 5;
println(h);

v = 54;
println(":::"+test_class.v);
ab = 2;
a = class {
    ab = 6;
    println(ab);
    println(v);

    v = 7;
    def = function(){
        ab = 4;
        println(v);//7
        r = class{
           println(ab);
           println(v);

           ab = 3;

           println(ab);
           println(v);
        };
        r2 = class{
           println(ab);
           println(v);

           init() {
               this.ab = 24;
               println("hello: " + ab);
               println("hello: " + this.ab);
               println("hello: " + v);
           };
        };
        println("------------");
        r2();
        v = 9;
        return ( function() {
            println(v);
            return(ab);
        });
    };
};
println(a.def()());
println(a.v);

//5
//:::54
//6
//54
//7
//4
//7
//3
//7
//4
//7
//------------
//hello: 4
//hello: 24
//hello: 7
//9
//4
//7
println("---------------------------------------------------");


r2 = class{
   println(ab);
   println(v);

   init() {
       println("hello: " + ab);
       this.ab = 24+5;
       println("hello: " + ab);
       println("hello: " + this.ab);
       println("hello: " + v);

       dfun = function() {
          println(ab);
          println(v);
       };
       v = 54524;
       dfun();
   };
};
r2ins = r2();

//2
//54
//hello: 2
//hello: 2
//hello: 29
//hello: 54
//2
//54524
println("-------------------------mian-start--------------------------");



class private_test{
    static.sa = 444;

    init() {
        this.a = 24;
    };

    class ks {
        static.sa = 55;
        println(sa);
    };

    getA = function() {
        return(this.a)
    };
    getSa = function() {
        return(static.sa)
    };
};

println(private_test().getA());
println(private_test().getSa());

try {
    println(private_test.sa);
} catch(error) {
    println(error)
};

try {
    println(private_test().sa);
} catch(error) {
    println(error)
};
try {
    println(private_test().a);
} catch(error) {
    println(error)
};


//55
//24
//444
//444
//444
//24

println("---------------------------------------------------");
import file;
println(file.k);
file.k = 288884;
println(file.k);

as = 88888;

static.k = 213213213;
println(static.k);

//null
//288884
//213213213

println("---------------------------------------------------24");

class sadsadaa {
    println(k);

    getK = function() {
        return(k)
    };

    class rgerger {
        println(as);

        init() {
            this.a = 19990919;
        };
        static.__test__ = function() {
            return(this.a);
        };


        get_test = function() {
            return(this.__test__());
        };
    };
};
ass = sadsadaa.rgerger();
println(ass.get_test());

//213213213
//88888
//19990919
println("---------------------------------------------class_test_end");
