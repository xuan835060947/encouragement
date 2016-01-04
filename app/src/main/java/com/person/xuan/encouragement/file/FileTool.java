package com.person.xuan.encouragement.file;

import android.os.Environment;

import com.person.xuan.encouragement.util.ShareValueUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class FileTool {
    private static final String mFilePath = ShareValueUtil.EXTERNAL_STORAGEP_FILE_PATH;

    public static void addLine(final String fileName, final String data) {
        new Thread() {
            @Override
            public void run() {
                File dir = Environment.getExternalStoragePublicDirectory(mFilePath);
                File file = new File(dir, fileName);
                try {
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(data + '\n');
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void getLines(final String fileName, OnGetLine onGetLine) {
        File dir = Environment.getExternalStoragePublicDirectory(mFilePath);
        File file = new File(dir, fileName);
        String content = null;
        try {
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                for (int i = 0; (content = bufferedReader.readLine()) != null; i++) {
                    onGetLine.onGetLine(content, i);
                }
                onGetLine.onGetLine(null, -1);
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(final String fileName, final String fileContent) {
        new Thread() {
            @Override
            public void run() {
                File dir = Environment.getExternalStoragePublicDirectory(mFilePath);
                File file = new File(dir, fileName);
                try {
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(fileContent);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static String getStringFromFile(String fileName) {
        File dir = Environment.getExternalStoragePublicDirectory(mFilePath);
        File file = new File(dir, fileName);
        String content = null;
        try {
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                content = bufferedReader.readLine();
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public interface OnGetLine {
        void onGetLine(String line, int lineNum);
    }

}
