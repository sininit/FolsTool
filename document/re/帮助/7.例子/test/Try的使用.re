//无聊的try测试

println(function(){
    //8
    //try{return (8)}catch(e){}finally{};
    //9
    //try{throw(1)}catch(e){return(8)}finally{return (9)};
    //9
    //try{null}catch(e){return(8)}finally{return (9)};
    //8
    //try{throw(1)}catch(e){return(8)}finally{};
    //9
    //try{throw(1)}catch(e){}finally{return (9)};

    //Error: 	2
    //try{
    //    throw(1)
    //}catch(e){
    //    throw(2)
    //}finally{
    //    ""
    //};

    //1
    //try{throw(1)}catch(e){throw(2)}finally{return(1)};
    //2
    //try{throw(1)}catch(e){}finally{return(2)};
    //2
    //try{}catch(e){}finally{return(2)};
}());
