package top.fols.atri.net;

import java.io.Serializable;

import top.fols.box.net.XURL;

public class URLBuilder implements Serializable{
    private static final long serialVersionUID = 1L;
    
    String protocol;
    String user;
    String host;
    String port;
    String path;
    URLParams param;
    String ref;



    //protocol://user@host:port/dir/filename?param=value&multiplyParam=value#ref
    public URLBuilder(String url) {
        XURL xurl = new XURL(url);
        
        this.protocol = xurl.getProtocol();
        this.user = xurl.getUser();
        this.host = xurl.getHost();
        this.port = xurl.getPort();
        this.path = xurl.getFilePath();
        this.param = null == xurl.getParam() ?null: xurl.param();
        this.ref = xurl.getRef();
    }
    public URLBuilder() {
        super();
    }




    public URLBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String protocol() {
        return protocol;
    }

    public URLBuilder removeProtocol() {
        this.protocol = null;
        return this;
    }




    public URLBuilder user(String user) {
        this.user = user;
        return this;
    }

    public String user() {
        return user;
    }

    public URLBuilder removeUser() {
        this.user = null;
        return this;
    }




    public URLBuilder host(String host) {
        this.host = host;
        return this;
    }

    public String host() {
        return host;
    }

    public URLBuilder removeHost() {
        this.host = null;
        return this;
    }




    public URLBuilder port(int port) {
        this.port = String.valueOf(port);
        return this;
    }

    public URLBuilder port(String port) {
        this.port = port;
        return this;
    }

    public int portInt() {
        return Integer.parseInt(port);
    }
    public String port() {
        return port;
    }

    public URLBuilder removePort() {
        this.port = null;
        return this;
    }



    public URLBuilder path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return path;
    }

    public URLBuilder removePath() {
        this.path = null;
        return this;
    }




    public URLBuilder param(URLParams param) {
        this.param = param;
        return this;
    }

    public URLBuilder param(String param) {
        return this.param(null == param ?null: new URLParams(param));
    }
    public String       param() {
        return null == this.param ?null: this.param.toString();
    }

    public URLParams    paramObject() { return null == this.param ?this.param = new URLParams(): this.param; }

    public URLBuilder addParam(String key, String value) {
        this.paramObject().putData(key, value);
        return this;
    }
    public String getParam(String key) {
        return null == this.param ?null: this.param.getData(key);
    }
    public URLBuilder removeParam(String key) {
        this.paramObject().remove(key);
        return this;
    }
    public boolean containsParam(String key) {
        return null == this.param ?false: this.param.containsKey(key);
    }
    public URLBuilder removeParam() {
        this.param = null;
        return this;
    }




    public URLBuilder ref(String raf) {
        this.ref = raf;
        return this;
    }

    public String ref() {
        return ref;
    }

    public URLBuilder removeRef() {
        this.ref = null;
        return this;
    }




    public String build() { 
        return XURL.createURL(protocol(), user(), host(), port(), path(), param(), ref());
    }


    @Override
    public String toString() {
        return this.build();
    }
    
    
    
}
