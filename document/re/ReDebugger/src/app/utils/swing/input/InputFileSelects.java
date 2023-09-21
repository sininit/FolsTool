package app.utils.swing.input;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InputFileSelects {
    public static File selectFileChooser(String description, String[] extensions) {
        //弹出文件选择框
        JFileChooser chooser = new JFileChooser();
        //后缀名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
        int option = chooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION)  {	//假如用户选择了保存
            File dir = chooser.getCurrentDirectory();
            File file = chooser.getSelectedFile();
            String fname = chooser.getName(file);	//从文件名输入框中获取文件名
            return file;
        }
        return null;
    }
    public static File selectDirectoryChooser(String description, String[] extensions) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = jFileChooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectFile = jFileChooser.getSelectedFile();
            if (selectFile.isDirectory()) {
                if (!selectFile.canRead() || !selectFile.canWrite()) {
                    JOptionPane.showMessageDialog(null, "没有权限：" + selectFile);
                    return null;
                }
            }
            return selectFile;
        }
        return null;
    }



    public static void openFileFromDesktop(File file) {
        try { Desktop.getDesktop().open(file); } catch (IOException ignored) {}
    }


    public static File saveFileChooser(String name, String description, String[] extensions) {
        //弹出文件选择框
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(name));
        chooser.setFocusable(true);
        //后缀名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
        chooser.setFileFilter(filter);
        //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
        int option = chooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
            File dir = chooser.getCurrentDirectory();
            File file = chooser.getSelectedFile();
            String fname = chooser.getName(file);	//从文件名输入框中获取文件名
            return file;
        }
        return null;
    }
}
