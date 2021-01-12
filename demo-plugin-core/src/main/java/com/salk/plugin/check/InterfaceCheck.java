package com.salk.plugin.check;

import com.salk.plugin.api.IScore;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author：salkli
 * @date：2021/1/7
 * 接口校验，验证是否实现了该接口
 */
public class InterfaceCheck implements InvalidCheck<JarFile> {
    @Override
    public boolean check(JarFile jarFile) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            Class<? extends JarEntry> aClass = jarEntry.getClass();
            return aClass.isAssignableFrom(IScore.class);
        }
        return false;
    }

}
