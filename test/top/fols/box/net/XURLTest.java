package top.fols.box.net;

import top.fols.atri.net.XURL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class XURLTest {
    public static void main(String[] args) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        String urlStr = "http://tester:123456@www.baidu.com?sb?a=b&b=c&c=d#abc";
        URL url = new URL(urlStr);
        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();
        int defaultPort = url.getDefaultPort();
        String query = url.getQuery();
        String ref = url.getRef();
        String user = url.getUserInfo();
        String authority = url.getAuthority();
        String file = url.getFile();
        // Object content = url.getContent();
        System.out.println(url.getPath());

        System.out.println("______");
        String testUrl;
        testUrl = "http://tester:123456@www.baidu.com/s/k?s/sb?a=b  #abc/";
        // testUrl = "http://127.0.0.1:7777/_HM4X/1qw/r/t/b";
        // testUrl = "http://127.0.0.1:7777?/post=4/_HM4X/1qw/r/t/b";
        // testUrl = "http://127.0.0.1:7777/l/..?path=%2F_PhoneFile%2F/listmode=1";
        testUrl = "http://tester:123456@www.baidu.com//a///b///c/..//?sb//../..///?a=b&b=c&c=d//../#cxk";

        System.out.println(testUrl);
        XURL tsx = new XURL(testUrl).absoluteUrl();
        System.out.println(tsx);
        Method[] ms = XURL.class.getMethods();
        for (Method m : ms) {
            if (m.getParameterTypes().length == 0) {
                System.out.println(m.getName() + " ==> " + m.invoke(tsx));
            }
        }

        System.out.println("_________");
    }
}