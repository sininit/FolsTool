package app.utils.lock;

import app.utils.LockThread;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.fols.atri.io.file.Filex;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ExecutorLock {
    public static final LockThread FILE_LOCK = new LockThread();
    public static char[] fileToChars(File file, Charset charset) throws IOError {
        return ExecutorLock.FILE_LOCK.executeAndJoins((lock) -> {
            int length = Math.toIntExact(file.length());
            char[] chars = new char[length];

            int read;
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader stream = new InputStreamReader(fis, charset)) {
                if (length != (read = stream.read(chars))) {
                    return Arrays.copyOf(chars, read);
                }
                return chars;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static boolean charsToFile(File file, char[] chars, Charset charset) throws IOError {
        return ExecutorLock.FILE_LOCK.executeAndJoins((lock) -> {
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter stream = new OutputStreamWriter(fos, charset)) {
                stream.write(chars);
                stream.flush();
                return true;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param file
     * @return
     * @throws IOException
     */
    public static final String EXCEL_2003_FILE_EXTENSION = "xls";
    public static final String EXCEL_2007_FILE_EXTENSION = "xlsx";
    public static Workbook readWorkBookFile(File file) throws IOError {
        if (file.length() == 0) {
            return null;
        }
        String fileName = file.getName();
        String fileType = Filex.getExtensionName(fileName);

        return ExecutorLock.FILE_LOCK.executeAndJoins((lock) -> {
            try  {
                FileInputStream inStr;
                try {
                    inStr = new FileInputStream(file);
                } catch (FileNotFoundException e){
                    return null;
                }
                Workbook wb;
                if (EXCEL_2003_FILE_EXTENSION.equals(fileType)) {
                    wb = new HSSFWorkbook(inStr);  //2003-
                } else if (EXCEL_2007_FILE_EXTENSION.equals(fileType)){
                    wb = new XSSFWorkbook(inStr);  //2007+
                } else {
                    throw new IOException("read data error");
                }
                return wb;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static boolean writeToFile(Workbook workbook, File file) throws IOError {
        return ExecutorLock.FILE_LOCK.executeAndJoins((lock) -> {
            try  {
                FileOutputStream fos = new FileOutputStream(file);
                workbook.write(fos);
                fos.flush();
                fos.close();
                return true;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

}