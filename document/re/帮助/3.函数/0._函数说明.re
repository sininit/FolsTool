 当然里面也有 常量
Re的函数都是Re_IObject


Re_IObject
函数只有一种
可控参数函数


可控制参数执行函数:
    比如
    (x=1, x=2, x=3);
    这时候一共有 三个参数 你可以在Re_IObject具体实现里 控制哪个执行那个不执行
    这样就可以做到选择执行, 比如if是判断参数一是否为非空且不为false参数二
    这样x就被赋值为2了并返回参数二的执行结果，  第三个参数跳过了

注意如果要自己实现Re_Object 需要特别注意 如果执行了executor的getExpressionValue 必须要 判断executor.isReturn如果为true应该立即停止你的函数的执行过程并返回executor.getReturn