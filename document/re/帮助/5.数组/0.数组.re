
array(        [0,1,2,3,4,5,6,7,8,9,'a','b','c','d','e','f']);//创建Object数组
array(Object, [0,1,2,3,4,5,6,7,8,9,'a','b','c','d','e','f']);//创建Object数组

arrayof(Number, len);             //创建指定长度 一维数组
arrayof(Number, [1, 2, 3]);       //创建多维数组
arrayof(Number, int[](1, 2, 3));  //创建多维数组




数组的创建方法：（这里的 Number[] 其实就是一个参数名... 并没有实际意义）
    Number[] = jimport("java.lang.Number[]");
    Number[](data...);          //创建数组





a();     //长度
a(0);    //获取第1个数据
a(0,1);  //设置第一个数据
