package com.androidlibrary.permission;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LinkedME06 on 16/7/17.
 */
public class Permission {

    //写入并读取文件内容
    private String writeStorage(){
        writeToFile();
        return readFromFile();
    }

    private void writeToFile(){
        File f = Environment.getExternalStorageDirectory();//获取SD卡目录
        File fileDir = new File(f,"test.txt");
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileDir);
            os.write("123u1oi2401293840981".getBytes());
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String readFromFile() {
        String fileContent = "No content!";
        File f = Environment.getExternalStorageDirectory();//获取SD卡目录
        File fileDir = new File(f, "test.txt");
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = new FileInputStream(fileDir);
            bos = new ByteArrayOutputStream();
            byte[] array = new byte[1024];
            int len = -1;
            while ((len = is.read(array)) != -1) {
                bos.write(array, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bos != null){
            fileContent = bos.toString();
        }
        return fileContent;
    }
}
