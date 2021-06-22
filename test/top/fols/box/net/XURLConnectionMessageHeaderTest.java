package top.fols.box.net;

import top.fols.atri.lang.Strings;
import top.fols.atri.net.MessageHeader;
import top.fols.atri.net.URLConnections;
import top.fols.box.io.XStream;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XURLConnectionMessageHeaderTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        URLConnection urlc =
                new URLConnections.GetRequest("http://www.baidu.com").getURLConnection();

        urlc.addRequestProperty("Host", "1");
        urlc.addRequestProperty("Host", "2");
        //urlc.setRequestProperty("Host", "3");


        System.out.println(Strings.join(urlc.getRequestProperties(), "\n"));
        System.out.println();

        urlc.setRequestProperty("   Host  ", "new");
        urlc.setRequestProperty("   host  ", "new");
        System.out.println(Strings.join(urlc.getRequestProperties(), "\n"));
        System.out.println();

        System.out.println(Strings.join(new URLConnections.GetRequest("https://m.baidu.com/?from=844b&vit=fps").getURLConnection().getHeaderFields().get("set-cookie"), "\n"));
        System.out.println();
        System.out.println(Strings.join(new URLConnections.GetRequest("https://m.baidu.com/?from=844b&vit=fps").getURLConnection().getHeaderField("set-cookie"), "\n"));
        System.out.println();
        System.out.println();



        System.out.println("===========");




        MessageHeader xbk = (MessageHeader) XStream.ObjectTool.toObject(
                XStream.ObjectTool.toByteArray(
                        new MessageHeader()
                                .putAll(
                                        "Aaaa : 1" + "\n"
                                                + "aaa: 2" + "\n"
                                )
                                . addAll(
                                        "AAAA: 3" + "\n"
                                                + "Aaa: 4" + "\n"
                                )
                                . putAll(
                                        "AAAA : 5" + "\n"
                                                + "AAA: 6" + "\n"
                                )
                                . put((String) null, "hhh")
                                . put("", "hhh")
                                . putAll(new HashMap<String, List<String>>()
                                         {{
                                             put("", new ArrayList<String>());
                                         }}
                                )
                ));
        System.out.println(xbk);

    }

}