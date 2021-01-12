package com.salk.plugin.classloader;

import com.salk.plugin.check.InterfaceCheck;
import com.salk.plugin.check.InvalidCheck;
import com.salk.plugin.register.FileHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;

/**
 * @author：salkli
 * @date：2021/1/6
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    private FileHandler fileHandler;

    private Map<String, List<URL>> cacheURL = new ConcurrentHashMap<>();

    private InvalidCheck<JarFile> jarEntryInvalidCheck;

    public PluginClassLoader(InvalidCheck invalidCheck,List<File> files) {
        super(new URL[]{}, findParentClassLoader());
        jarEntryInvalidCheck = invalidCheck;
        addToURL(files);
    }

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

    }

    public static ClassLoader findParentClassLoader() {
        ClassLoader classLoader = PluginClassLoader.class.getClassLoader();
        if (classLoader == null) {
            return classLoader.getParent();
        }
        return classLoader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            try {
                return PluginClassLoader.class.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException ex) {
              throw ex;
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    private void addToURL(List<File> files) {
            files.stream().forEach(file -> {
                try {
                    URL jarUrl = new URL("jar:file:/" + file.getAbsolutePath() + "!/");
                    try {
                        URLConnection uc = jarUrl.openConnection();
                        if (uc instanceof JarURLConnection) {
                            uc.setUseCaches(true);
                            JarFile jarFile = ((JarURLConnection) uc).getJarFile();
                            if (jarEntryInvalidCheck.check(jarFile)) {
                                log.error("{} jar file include invalid class", jarFile.getName());
                            }
                            ((JarURLConnection) uc).getManifest();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    super.addURL(jarUrl);
                } catch (MalformedURLException e) {
                    log.error(e.getMessage(),e);
                }
            });
    }

}
