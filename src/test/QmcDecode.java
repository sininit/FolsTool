package test;

import top.fols.box.io.os.XFile;
import top.fols.box.util.XArrayPieceIndexManager;

import java.io.*;

/**
 * * @ProjectName: flactomp3 * @Package: PACKAGE_NAME * @ClassName:
 * algorithm.Decode * @Author: 吴成昊 * @Description: * @Date: 2019/4/17 17:26
 * * @Version: 0.1
 */
public class QmcDecode {
    private static class MaskCacl {
        private int x = -1;
        private int y = 8;
        private int dx = 1;
        private int index = -1;
        private int[][] seedMap = {{0x4a, 0xd6, 0xca, 0x90, 0x67, 0xf7, 0x52},
                {0x5e, 0x95, 0x23, 0x9f, 0x13, 0x11, 0x7e}, {0x47, 0x74, 0x3d, 0x90, 0xaa, 0x3f, 0x51},
                {0xc6, 0x09, 0xd5, 0x9f, 0xfa, 0x66, 0xf9}, {0xf3, 0xd6, 0xa1, 0x90, 0xa0, 0xf7, 0xf0},
                {0x1d, 0x95, 0xde, 0x9f, 0x84, 0x11, 0xf4}, {0x0e, 0x74, 0xbb, 0x90, 0xbc, 0x3f, 0x92},
                {0x00, 0x09, 0x5b, 0x9f, 0x62, 0x66, 0xa1}};

        public int NextMask() {
            int ret;
            index++;
            if (x < 0) {
                dx = 1;
                y = ((8 - y) % 8);
                ret = ((8 - y) % 8);
                ret = 0xc3;
            } else if (x > 6) {
                dx = -1;
                y = 7 - y;
                ret = 0xd8;
            } else {
                ret = seedMap[y][x];
            }
            x += dx;
            if (index == 0x8000 || (index > 0x8000 && (index + 1) % 0x8000 == 0)) {
                return NextMask();
            }
            return ret;
        }
    }

    private static File getNewExtensionNamePath(String filepath, String newExtensionName) {
        File orgFile = new File(filepath);
        File file = new File(XFile.getParent(orgFile.getAbsolutePath()) + XFile.getNameNoExtension(filepath) + "."
                + newExtensionName);
        return file;
    }

    public static void decodeQmcFile(String filePath) throws FileNotFoundException, IOException {

        String newExName = null;
        String orgExName = XFile.getExtensionName(filePath);
        if ("qmcflac".equals(orgExName)) {
            newExName = "flac";
        } else if ("qmc3".equals(orgExName)) {
            newExName = "mp3";
        } else if ("qmc0".equals(orgExName)) {
            newExName = "mp3";
        } else {
            throw new IOException(
                    "cannot decode file type: " + orgExName + ", can decode type: " + "{qmcflac, qmc3, qmc0}");
        }

        MaskCacl decode = new MaskCacl();

        File file = new File(filePath);
        long fileLength = file.length();

        File fileDecodePath = QmcDecode.getNewExtensionNamePath(filePath, newExName);

        FileInputStream fis = new FileInputStream(filePath);
        FileOutputStream fos = new FileOutputStream(fileDecodePath);

        int buflen = 8192;

        XArrayPieceIndexManager arrayPieceIndexManager = new XArrayPieceIndexManager(fileLength, buflen);

        byte[] buffer = new byte[buflen];
        for (int i = 0; i < arrayPieceIndexManager.getPieceCount(); i++) {
            int read = fis.read(buffer, 0, (int) arrayPieceIndexManager.getPieceSize(i));

            for (int i2 = 0; i2 < read; i2++) {
                buffer[i2] = (byte) (decode.NextMask() ^ buffer[i2]);
            }
            fos.write(buffer, 0, read);
        }
        fos.flush();
        fos.close();
        fis.close();

        buffer = null;

    }
}
