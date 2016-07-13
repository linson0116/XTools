package com.linson.xtools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    public static Bitmap getBmp(File file) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }
    public static boolean write(File file, String content) {
        StringBuffer sb = new StringBuffer();
        if (file.exists()) {
            long length = file.length();
            if (length > 1024 * 1024) {
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
            //Log.i("log", content);
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

    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//压缩比率
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 500) {
            baos.reset();
            //options -= 10;
            options = (int) (options * 0.8);
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            if (options <= 9) {
                break;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compressBmpToFile(Bitmap bmp, File file, Context context) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 90;//压缩比率
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);

        SharedPreferences spf = context.getSharedPreferences("xtools", context.MODE_PRIVATE);
        int fileSize = spf.getInt("fileSize", 2000);
        Lu.i("fileSize:" + fileSize);

        while (baos.toByteArray().length / 1024 > fileSize) {
            baos.reset();
            //options -= 10;
            options = (int) (options * 0.9);
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            if (options <= 9) {
                break;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
