
//eval("
//    try {
//        1.value = 1;
//    } catch (e) { println(e); }; ");
//throw;

println("-----------------------------------------------");
import test_vfc;
import test_expression;

import java.lang.Boolean;
import java.lang.String;
import java.lang.Object;
import java.util.List;

array(Object, [0,1,2,3,4,5,6,7,8,9,'a','b','c','d','e','f']);

println(not(8));
ii = "";
h = jasclass;
g = class{};
f = true;
e = jasclass(import java.lang.String);
c = class{}();
b = null;
a = 8;
ss = [66,6];
dd = array([66]);
cc = {"as":{"e":666}, "c":[5]};
setattr(space, array([]), 5);

while (false) {
   function() {
      hhh = time();
      debugger;
   } ();

   sleep(1000);
};





import request;  //boot类

//println((
//    req = request.request({
//       "url": "https://translate.google.cn/m?sl=auto&tl=zh-CN&hl=zh-CN&q=test+to+fail",
//       "headers": "accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//                   accept-encoding:gzip, deflate, br
//                   accept-language:zh-CN,zh;q=0.9
//                   cache-control:max-age=0
//                   cookie:NID=511=XuE-75fip9dUcqkvPi9QsKrEh7G7P85b0xCLv_KaEwDVjIUvFjtjU5bYWdtl-CysqqE9z_OCk4F5JeL6FrpdkKB2uCFrY85C5ctxeHHwLc7NNvVt4kAiWuyGHx5gMewbRZl4i_vlGCj1eTsZM6Opq5HwMDzLg362X7iP41GwLos
//                   upgrade-insecure-requests:1
//                   user-agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"
//    });
//    req.readString().replace("\n", " ")
//));



import test_class;
import main;     //导入自己看看会不会死循环


test = jproxy(["java.lang.Runnable"], {
    run = function() {
        class {
            init() {
                throw(666);
            }
        }();
    }
});

Thread = jimport("java.lang.Thread");
t = Thread(test);
t.start();



class A {
    class IGetName {
        getName = function () {
            return(this.name);
        };
    };
    class IGetSex {
        getSex = function () {
            return(this.sex);
        };
    };
    class AUser {
        init(name) {
            this.name = name;
        };
        getName = function() {
            return(this.name);
        };
    };
};
class User {
    init(n, sex) {
        reflect.invokeInit(A.AUser, [n]);

        this.sex = sex;
    };
    reflect.copyAttr(A.IGetName, A.IGetSex);
    reflect.copyAttr(A.AUser);
};
u = User("梁鑫","男");
println(u.getName());
println(u.getSex());

println(static);





jimport("java.util.concurrent.ConcurrentHashMap");
println(json.stringify(jimport(ConcurrentHashMap)));
println(json.stringify(env));

println(999999999999999999, 11, 4.5, 1, ((1+((597*56)/4)+9-(44/4*4+535+7))<<2)+55);
println("------");

jimport("int[]");
a = -1;
println(a);
a = 3 + -4 + -5;
println(a);
println(1 - -4);
a = int[](1,2,3,4);
println(1 && 2 && null && println("a:"+666));
println(1 || 2 || null || println("b:" + 666));
println(1 | 2 | null | println("d:"+666));
println(1 | 2 | null | println(666+"666"));
println(
    a = 1,
    try {
        a.value = b = c = d = 4
    } catch(err) {
        err
    }
);

println(json.stringify(array([1,2,3,4])));
println(is_runtime_keyword_key("$"));
println(is_runtime_keyword_key("+"));
println(is_str(get_keyword_key(json)));
println(json.stringify(to_list(list_keyword())).replace("\n",""));

a = json.parse("""
{
    "user": {
        "QQ": 784920843,
        "V": [1, 2, 3, 4, 5, 6, 7]
    }
}
""");
println(json.stringify(a));
println(json.stringify(json.parse(json.stringify(a))));



a = {
    "user": {
        "QQ": 784920843,
        "V": [1, 2, 3, 4, 5, 6, 7]
    }
};
println("---------------");
println(try{a.toString()});

//primitive class
dict = object();
dict(0, 555);
dict("pid", 555);

println(dict.toString);


Exception = class {
    init(message) {
        this.message = message;
    };

    R = class soidahoidiosadia {
    };
};
ins = Exception("666");
ins("message", 666);


println(ins);
println(Exception.R);


println(2);
println(1, 2, 3);

println(is_primitive(66));
println(is_primitive(static));
println(is_primitive(a=is_primitive));


println(space);




//闭包！
top = space;
f1 = function (){
    that = space;
    n = 999;
    top.nAdd = function() {
        that.n=n+1
    };
    f2 = function() {
        println(n);
    };
    return (f2);
};
result=f1();
result(); // 999
top.nAdd();
result(); // 1000



jimport("java.lang.System");
import file;

println("----------------------main start-----------------------------");

main = function(p1, p2, p3) {
    println(p1, p2, p3);

    instance = file("D:\\1.txt");
    println("创建实例："+instance);

    instance.write("测试：" + System.currentTimeMillis());

    println("文件读取："+instance.read());

    return('0');
};
main();

println("-------------------------mian-end--------------------------");



