package com.salk.plugin.register;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public interface FileHandler {
    Map<String, List<File>> collector();

    String getDirPath();
}
