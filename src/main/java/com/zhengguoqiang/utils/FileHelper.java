package com.zhengguoqiang.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhengguoqiang
 */
@Slf4j
public class FileHelper {

    public static List<String> getUniqueLibPath(String... dirs) throws IOException {
        List<String> jarList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();

        for (String dir:dirs){
            List<File> fileList = FileHelper.getFiles(dir, "jar");
            if (fileList != null){
                for (File file : fileList) {
                    if (!fileNameList.contains(file.getName())) {
                        jarList.add(file.getCanonicalPath());
                        fileNameList.add(file.getName());
                    }
                }
            }
        }
        return jarList;
    }

    private static List<File> getFiles(String dir, String... extension){
        File file = new File(dir);
        if (!file.isDirectory()){
            log.info("this is not a directory...");
            return null;
        }else {
            List<File> fileList = new ArrayList<>();
            getFiles(file,fileList,extension);
            return fileList;
        }
    }

    private static void getFiles(File file,List<File> fileList,String... extension){
        File[] files = file.listFiles();
        if (files == null) return;

        for (File file1 : files) {
            if (file1.isDirectory()) {
                getFiles(file1, fileList, extension);
            } else if (file1.isFile()) {
                String fileName = file1.getName().toLowerCase();
                boolean isAdd = false;
                if (extension != null) {
                    for (String ext : extension) {
                        if (fileName.lastIndexOf(ext) == fileName.length() - ext.length()) {
                            isAdd = true;
                            break;
                        }
                    }
                }
                if (isAdd) {
                    fileList.add(file1);
                }
            }
        }
    }
}
