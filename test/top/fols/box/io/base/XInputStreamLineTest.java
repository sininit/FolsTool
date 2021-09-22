package top.fols.box.io.base;


import top.fols.atri.lang.Finals;
import top.fols.atri.lang.Objects;
import top.fols.box.io.digest.XDigestOutputStream;
import top.fols.box.io.os.XFile;
import top.fols.box.time.XTimeConsum;
import top.fols.atri.util.MessageDigests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

public class XInputStreamLineTest {
    public static void main(String[] args) throws Throwable {
        {
            File testFile = new File("/sdcard/_redminote7/PivisionM4.4.3(443006).apk");
            XByteArrayInputStream testStream0 = new XByteArrayInputStream(XFile.readFile(testFile));
            XInputStreamLine testStream = new XInputStreamLine(testStream0, 8192);
            FileOutputStream foss = new FileOutputStream("/sdcard/k");
            XDigestOutputStream md5 = MessageDigests.wrapToStream(MessageDigests.getMessageDigest("md5"));
            XTimeConsum linejsq = XTimeConsum.newAndStart();
            byte[] line;
            int linecount = 0;
            while (null != (line = testStream.readLine(new byte[][]{
                            Finals.Separator.getBytesLineSeparatorR(),
                            Finals.Separator.getBytesLineSeparatorN(),
                            Finals.Separator.getBytesLineSeparatorRN()},
                    true))) {
                linecount++;
                md5.write(line);
                foss.write(line);
                line = null;
            }
            foss.close();
            System.out.println(MessageDigests.toHex(md5.getValue()));
            System.out.println("行: " + linecount);
            System.out.println("耗时: " + linejsq.endAndGetEndLessStart());


            XInputStreamLine ron = new XInputStreamLine(new ByteArrayInputStream("1\n22\n34\n56789\n".getBytes()));
            System.out.println("*" + new String(ron.readLine("\n".getBytes(), true)));
            System.out.println("*" + ron.skip(1));
            System.out.println("*" + ron.read());
            System.out.println("*" + new String(ron.readLine("\n".getBytes(), true)));
            System.out.println("*" + ron.read(new byte[4]));
            System.out.println("*" + ron.read(new byte[4]));
            System.out.println("*" + ron.read(new byte[4]));
            System.out.println();

            System.out.println(new String(ron.readLine("\n".getBytes(), true)));
            System.out.println(new String(ron.readLine("\n".getBytes(), true)));
            System.out.println(new String(ron.readLine("\n".getBytes(), true)));
            System.out.println(new String(ron.readLine("\n".getBytes(), true)));

        }


        {
            byte[] bss = "1,,,2,,3,,4,,5,,6,,7,,8,,9".getBytes();

            XByteArrayInputStream input0 = new XByteArrayInputStream(bss);
            byte[] lines = ",".getBytes();

            XInputStreamLine<XByteArrayInputStream> input = new
                    XInputStreamLine<XByteArrayInputStream>(input0, 1);

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());

            System.out.println("[" + Objects.toString(new String(input.readLine(lines,
                    true), "UTF-8")) + "]");

            System.out.println("pindex: " + input.readLineSeparatorsIndex());
        }

    }
}