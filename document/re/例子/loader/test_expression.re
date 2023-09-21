println("****************test_expression/start****************");

//没有实现
//++++++++++---
// a = {};
// t = space.a["math"] = 9;
// t2 = a["math"] = 10;
// t3 = a["math"];
// t4 = a["math"]["kk"];
// ["a"]=888;
// println(
    // (
        // class {
          // init(name) { this.name = name };
          // function getName() { return this.name; };
        // } ("lx")
    // ) ["getName"]()
// );
// println([0][0]);
// a[][] [0];
// a[0] [1]=9;
// throw exception("err");
//++++++++++---







hello = class Hello {
    init () {};

    t = 5 + function a(param) {
        for (i=0; i < 10; i=i+1) {
            println(i);
        };
        i = 0;
        while (i < 10) {
            i = i + 1;
            break;
            return true;
        };
        obj = { a=2; b=3; };
        foreach(k, v, obj) {
            println("k="+k+", v="+v);
        };

        try {
            throw(66);
        } catch(e) {
            println(e);
        } finally {
        };
        a = try { readFile(file) };


        if (true) {
            println([true,  0]);
        } else {
            if (false) {
                println([true,  1]);
            } else {
                println([false, 2]);
            }
        };
        if(1==1) {
            println("1==1    success");
        } else {
            println("1==1   failed");
        };
        if (1==2) {
            println("1==2 .  failed");
        } else {
            println("1==2   success");
        };
        if(1==3) {
            println("1==3 .  failed");
        } else if(true) {
            println("1==3 .  success");
        } else{
            println("1==3   failed");
        };
        if(1==3) {
            println("1==3 .  failed");
        } else if(false){
            println("1==3 .  failed");
        } else{
            println("1==3  success");
        };

        based ({}) {
            a = 8;
        };
    } ();
} ();

println("****************test_expression/end****************");