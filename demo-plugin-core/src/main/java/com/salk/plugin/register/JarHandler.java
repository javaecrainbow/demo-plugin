package com.salk.plugin.register;

import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public class JarHandler implements FileHandler {
    public static final String JAR = "jar";
    private String dirPath;

    public JarHandler(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public Map<String, List<File>> collector() {
        Map<String, List<File>> result = new HashMap(20);
        loopFiles(getDirPath(), new ArrayList(),result);
        return result;
    }

    private void loopFiles(String dirPath, List<File> collector,Map<String, List<File>> result) {
        File file = new File(dirPath);
        Assert.isTrue(file.exists(), "File is not exist");
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            for (File temp : files) {
                // 版本规则
                //if (temp.getName().matches("[0-9|.]+")) {
                    loopFiles(temp.getPath(), collector,result);
                    //result.put(temp.getName(), collector);
                //}
            }
            if (file.getName().matches("[0-9|.]+")) {
                List<File> temp=new ArrayList<>();
                temp.addAll(collector);
                result.put(file.getName(),temp);
                collector.clear();
            }
        } else {
            if (file.getName().endsWith(JAR)) {
                collector.add(file);
            }
        }
    }

    @Override
    public String getDirPath() {
        return this.dirPath;
    }
}
