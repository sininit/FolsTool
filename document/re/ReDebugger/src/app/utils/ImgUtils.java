package app.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImgUtils {
    public static class ImageSize{
        private final int width;
        private final int height;
        private ImageSize(int w, int h) {
            this.width = w;
            this.height = h;
        }

        public int getWidth(){
            return this.width;
        }
        public int getHeight(){
            return this.height;
        }

        @Override
        public String toString() {
            return this.width+"x"+this.height;
        }
    }




//    public static ImageSize getImageSize2(File file) {
//        BufferedImage bi = null;
//        int width = -1, height = -1;
//        try {
//            bi = ImageIO.read(file);
//            width = bi.getWidth();
//            height = bi.getHeight();
//        } catch (IOException e) {
//        } finally {
//            bi = null;
//        }
//        ImageSize instance = new ImageSize(width, height);
//        return instance;
//    }




    /**
     * 获取图片的分辨率
     */
    public static ImageSize getImageSize(File path) {
        int width = -1, height = -1;
        if (null!=path) {
            String suffix = getFileSuffix(path.getPath());
            //解码具有给定后缀的文件
            Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
            //System.out.println(ImageIO.getImageReadersBySuffix(suffix));
            if (iter.hasNext()) {
                ImageReader reader = iter.next();
                try {
                    ImageInputStream stream = new FileImageInputStream(path);
                    reader.setInput(stream);
                    width = reader.getWidth(reader.getMinIndex());
                    height = reader.getHeight(reader.getMinIndex());
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    reader.dispose();
                }
            }
        }
        return new ImageSize(width, height);
    }
    /**
     * 获得图片的后缀名
     */
    private static String getFileSuffix(final String path) {
        String result = null;
        if (path != null) {
            result = "";
            if (path.lastIndexOf('.') != -1) {
                result = path.substring(path.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        //System.out.println("getFileSuffix:" + result);
        return result;
    }















}
