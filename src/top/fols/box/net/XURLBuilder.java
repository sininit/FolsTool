package top.fols.box.net;

import java.io.Serializable;
import top.fols.box.net.XURL;
import top.fols.box.net.XURLParam;


public class XURLBuilder implements Serializable{
    private static final long serialVersionUID = 1L;
    
    String protocol;
    String user;
    String host;
    String port;
    String path;
    XURLParam param;
    String ref;



    //protocol://user@host:port/dir/filename?param=value&multiplyParam=value#ref
    public XURLBuilder(String url) {
        XURL xurl = new XURL(url);
        
        this.protocol = xurl.getProtocol();
        this.user = xurl.getUser();
        this.host = xurl.getHost();
        this.port = xurl.getPort();
        this.path = xurl.getFilePath();
        this.param = null == xurl.getParam() ?null: xurl.param();
        this.ref = xurl.getRef();
    }
    public XURLBuilder() {
        super();
    }




    public XURLBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String protocol() {
        return protocol;
    }

    public XURLBuilder removeProtocol() {
        this.protocol = null;
        return this;
    }




    public XURLBuilder user(String user) {
        this.user = user;
        return this;
    }

    public String user() {
        return user;
    }

    public XURLBuilder removeUser() {
        this.user = null;
        return this;
    }




    public XURLBuilder host(String host) {
        this.host = host;
        return this;
    }

    public String host() {
        return host;
    }

    public XURLBuilder removeHost() {
        this.host = null;
        return this;
    }




    public XURLBuilder port(int port) {
        this.port = String.valueOf(port);
        return this;
    }

    public XURLBuilder port(String port) {
        this.port = String.valueOf(Integer.parseInt(port));
        return this;
    }

    public int portInt() {
        return Integer.parseInt(port);
    }

    public String port() {
        return port;
    }

    public XURLBuilder removePort() {
        this.port = null;
        return this;
    }



    public XURLBuilder path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return path;
    }

    public XURLBuilder removePath() {
        this.path = null;
        return this;
    }




    public XURLBuilder param(XURLParam param) {
        this.param = param;
        return this;
    }

    public XURLBuilder param(String param) {
        return this.param(null == param ?null: new XURLParam(param));
    }

    public String param() {
        return null == this.param ?null: this.param.toFormatString();
    }




    private XURLParam param0() { return null == this.param ?this.param = new XURLParam(): this.param; }

    public XURLBuilder addParam(String key, String value) {
        this.param0().putNE(key, value);
        return this;
    }
    public String getParam(String key) {
        return null == this.param ?null: this.param.getND(key);
    }
    public XURLBuilder removeParam(String key) {
        this.param0().remove(key);
        return this;
    }
    public boolean containsParam(String key) {
        return null == this.param ?false: this.param.containsKey(key);
    }
    public XURLBuilder removeParam() {
        this.param = null;
        return this;
    }




    public XURLBuilder ref(String raf) {
        this.ref = raf;
        return this;
    }

    public String ref() {
        return ref;
    }

    public XURLBuilder removeRef() {
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
