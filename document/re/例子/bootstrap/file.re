//get_type(data);
//instanceof();

jimport("java.io.File");
jimport("java.io.FileWriter");
jimport("java.io.FileReader");
jimport("java.io.FileInputStream");
jimport("java.io.FileOutputStream");
jimport("java.io.InputStreamReader");
jimport("java.lang.reflect.Modifier");
jimport("java.lang.StringBuilder");


init(path) {
    this.path = path;
};


read = function (charset) {
    if (null == charset) {
        rstream = InputStreamReader(FileInputStream(this.path))
    } else {
        rstream = InputStreamReader(FileInputStream(this.path), charset)
    };
    
    buf  = arrayof(char, 8192);
    read = -1;
    sb = StringBuilder();

    while((read = rstream.read(buf)) != -1) {
        sb.append(buf,0,read);
    };
    return( str(sb) );
};



function append(data, charset){
    return( this.write(data, charset, true) );
};
function write(data, charset, append) {
    bytes = null;
    fos = null;

    if (is_str(data)) {
        if (null == charset) {
            bytes = data.getBytes();
        } else {
            bytes = data.getBytes(charset);
        };
    } else {
        if (jinstanceof(data, byte[])) {
            bytes = data;
        }
    };

    if (null == bytes) {
        throw("cannot write data");
    };
    
    if(append) {
        fos = FileOutputStream(this.path, true)
    } else {
        fos = FileOutputStream(this.path)
    };
    
    fos.write(bytes);
    fos.close();
    
    return( this );
};



methods = jasclass(File).getMethods();
foreach(k, method, methods) {
    name = method.getName();

    f = function() {
        cache = method;
        if (Modifier.isStatic(cache.getModifiers())) {
            return(
              function() {
                  return(cache.invoke(null, arguments));
              }
            );
        } else {
            return(
              function() {
                  return(cache.invoke(File(this.path), arguments));
              }
            );
        };
    } ();
    //setattr(static, name, f);
};