println("****************test_vfc_start****************");

import java.lang.String;
import java.lang.System;

newClass = class {
    final this   var tv1 = this;

    a = function () {
        return "a";
    };
    c = function c2() {
        return "c2";
    };
    final static var sv1 = 8;
    final static var sv2: str = "9";

    final this   var name;
          this   var tv2;
    struct var tv = {};



    b = struct function version() {
        return "20230116";
    };

    final init(name: String) {
        this.name = name;
    };
};
newClassInstance = newClass("LiangXin");
try{newClassInstance.sv1=999;};
try{newClassInstance.sv2=999;};
println(newClass.sv1);
println(newClass.sv2);
println(newClass.a());
//8
//9
//a


try{newClass.version=null;};
println(newClass.version);
try{newClassInstance.version=null;};
println(newClassInstance.version);
println(newClassInstance.version());
//re-function test_vfc$class_4_b7dd107.version
//re-function test_vfc$class_4_b7dd107.version
//20230116

try{newClassInstance.tv1=null;};
println(newClassInstance.tv1);
println(newClassInstance.tv2);
try{newClassInstance.name=null;};
println(newClassInstance.name);
//test_vfc$class_4_b7dd107@1693847660
//null
//LiangXin

println(newClass.tv);
println(newClassInstance.tv);



import int[];
import java.lang.Character;
function test(nums: int[], target: int): boolean {
    var nh: [int, Character] = 9;
    var hashmap: object = {};

    var test;
    var test = 8;
    
    println("test: " + test);
    
    
    return true;
};
test(int[](0,1,2,3,4,5,6,7,8,9), 10);

println("****************test_vfc_end****************");