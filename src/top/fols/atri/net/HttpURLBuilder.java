package top.fols.atri.net;

import java.io.Serializable;

import static top.fols.atri.net.HttpURL.fromHostToStandardizationHost;

@SuppressWarnings("UnusedReturnValue")
public class HttpURLBuilder implements Serializable{
    private static final long serialVersionUID = 1L;
    
    String protocol;
    String user;
    String host;
    String port;
    String path;
    HttpURLParam param;
    String ref;



    //protocol://user@host:port/dir/filename?param=tip&multiplyParam=tip#ref
    public HttpURLBuilder(String url) {
        HttpURL xurl = new HttpURL(url);
        
        this.protocol = xurl.getProtocol();
        this.user = xurl.getUser();
        this.host = xurl.getHost();
        this.port = xurl.getPort();
        this.path = xurl.getFilePath();
        this.param = null == xurl.getParam() ?null: xurl.param();
        this.ref = xurl.getRef();
    }
    public HttpURLBuilder() {
        super();
    }




    public HttpURLBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
    public String protocol() {
        return protocol;
    }

    public HttpURLBuilder removeProtocol() {
        this.protocol = null;
        return this;
    }




    public HttpURLBuilder user(String user) {
        this.user = user;
        return this;
    }

    public String user() {
        return user;
    }

    public HttpURLBuilder removeUser() {
        this.user = null;
        return this;
    }




    public HttpURLBuilder host(String host) {
        this.host = fromHostToStandardizationHost(host);
        return this;
    }
    public String host() {
        return host;
    }

    public HttpURLBuilder removeHost() {
        this.host = null;
        return this;
    }




    public HttpURLBuilder port(int port) {
        this.port = String.valueOf(port);
        return this;
    }

    public HttpURLBuilder port(String port) {
        this.port = port;
        return this;
    }

    public int portInt() {
        return Integer.parseInt(port);
    }
    public String port() {
        return port;
    }

    public HttpURLBuilder removePort() {
        this.port = null;
        return this;
    }



    public HttpURLBuilder path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return path;
    }

    public HttpURLBuilder removePath() {
        this.path = null;
        return this;
    }




    public HttpURLBuilder param(HttpURLParam param) {
        this.param = param;
        return this;
    }

    public HttpURLBuilder param(String param) {
        return this.param(null == param ?null: new HttpURLParam(param));
    }
    public String       param() {
        return null == this.param ?null: this.param.toString();
    }

    public HttpURLParam paramObject() { return null == this.param ?this.param = new HttpURLParam(): this.param; }

    public HttpURLBuilder setParam(String key, String value) {
        this.paramObject().put(key, value);
        return this;
    }
    public String getParam(String key) {
        return null == this.param ?null: this.param.getDecodeValue(key);
    }
    public HttpURLBuilder removeParam(String key) {
        this.paramObject().remove(key);
        return this;
    }
    public boolean containsParam(String key) {
        return null != this.param && this.param.containsKey(key);
    }
    public HttpURLBuilder removeParam() {
        this.param = null;
        return this;
    }




    public HttpURLBuilder ref(String raf) {
        this.ref = raf;
        return this;
    }

    public String ref() {
        return ref;
    }

    public HttpURLBuilder removeRef() {
        this.ref = null;
        return this;
    }




    public String build() { 
        return HttpURL.createURL(protocol(), user(), host(), port(), path(), param(), ref());
    }


    @Override
    public String toString() {
        return this.build();
    }
    
    
    
}
