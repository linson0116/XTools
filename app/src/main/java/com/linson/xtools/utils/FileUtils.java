package com.linson.xtools.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/29.
 */
public class FileUtils {
    final static String lineSeparator = System.getProperty("line.separator");

    public static boolean write(File file, String content) {
        StringBuffer sb = new StringBuffer();
        if (file.exists()) {
            long length = file.length();
            if (length > 1024) {
                String pre = new SimpleDateFormat("yyyyMMdd").format(new Date());
                File newFile = new File(file.getParent(), pre + file.getName());
                file.renameTo(newFile);
                file.delete();
            } else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append(lineSeparator);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Lu.v("文件不存在");
            return false;
        }
        try {
            //解决乱码
            Log.i("log", content);
//            OutputStream os = new FileOutputStream(file);
//            os.write((content + lineSeparator).getBytes("utf-8"));
//            os.write(sb.toString().getBytes());
//            os.close();

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write(content + lineSeparator);
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean writeLog(String content) {
        String logName = "XToolsLog.txt";
        Boolean flag = write(logName, content);
        return flag;
    }

    public static boolean write(String fileName, String content) {
        if (getExternalStorage() == null)
            return false;
        File file = new File(getExternalStorage(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return write(file, content);
    }

    public static File getExternalStorage() {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = Environment.getExternalStorageDirectory();
        }
        return file;
    }
}
