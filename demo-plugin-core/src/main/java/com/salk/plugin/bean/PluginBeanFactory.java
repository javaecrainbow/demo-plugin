package com.salk.plugin.bean;

import com.salk.plugin.api.IScore;
import com.salk.plugin.api.PluginClose;
import com.salk.plugin.check.InterfaceCheck;
import com.salk.plugin.check.InvalidCheck;
import com.salk.plugin.classloader.PluginClassLoader;
import com.salk.plugin.register.FileHandler;
import com.salk.plugin.register.JarHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.jar.JarFile;

/**
 * @author：salkli @date：2021/1/6
 */
public class PluginBeanFactory {

    private Map<String, PluginClassLoader> classLoaders = new HashMap();

    private FileHandler fileHandler;
    private static volatile PluginBeanFactory PLUGIN_BEAN_FACTORY;

    public static final String DIR = "D:\\ctest\\plugin";

    public static PluginBeanFactory get() {
        if (PLUGIN_BEAN_FACTORY == null) {
            synchronized (PluginBeanFactory.class) {
                if (PLUGIN_BEAN_FACTORY == null) {
                    PLUGIN_BEAN_FACTORY = new PluginBeanFactory();
                }
            }
        }
        return PLUGIN_BEAN_FACTORY;
    }

    private PluginBeanFactory() {
        fileHandler = new JarHandler(DIR);
        registerClassLoader();
    }

    Map<String, IScore> instances = new ConcurrentHashMap<>();

    public IScore getScoreInstance(String version, String pluginName) throws Exception {
        PluginClassLoader pluginClassLoader = classLoaders.get(version);
        if (instances.get(getInstanceKey(version, pluginName)) == null) {
            synchronized (getInstanceKey(version, pluginName)) {
                if (instances.get(getInstanceKey(version, pluginName)) == null) {
                    IScore iScore = (IScore)pluginClassLoader.loadClass(getClassFullName(pluginName)).newInstance();
                    instances.put(getInstanceKey(version, pluginName), iScore);

                }
            }
        }
        return instances.get(getInstanceKey(version, pluginName));
    }

    private String getInstanceKey(String version, String pluginName) {
        return version + "||" + pluginName;
    }

    public Object getObj(String version, String pluginName) throws Exception {
        PluginClassLoader pluginClassLoader = classLoaders.get(version);
        //
        return pluginClassLoader.loadClass(getClassFullName(pluginName)).newInstance();
    }

    public void remove(String version) throws Exception {
        PluginClassLoader pluginClassLoader = classLoaders.get(version);
        if (pluginClassLoader != null) {
            try {
                pluginClassLoader.close();
            } catch (IOException e) {
                throw e;
            }
        }
        removeInstance(version);

    }

    private void removeInstance(String version) {
        instances.entrySet().forEach(item -> {
            String key = item.getKey();
            IScore value = item.getValue();
            if (key.startsWith(version + "||")) {
                if (PluginClose.class.isAssignableFrom(value.getClass())) {
                    ((PluginClose)value).close();
                }
            }
        });
        instances.entrySet().removeIf(item -> {
            return item.getKey().startsWith(version + "||");
        });
    }

    private String getClassFullName(String pluginName) {
        return "scorea".equals(pluginName) ? "com.salk.plugin.extend.ScoreImplA" : "com.salk.plugin.extend.ScoreImplB";
    }

    /**
     * 注册到classloader
     *
     * @return
     */
    private void registerClassLoader() {
        Map<String, List<File>> collector = fileHandler.collector();
        collector.entrySet().stream().forEach(item -> {
            List<File> value = item.getValue();
            InvalidCheck<JarFile> jarEntryInvalidCheck = new InterfaceCheck();
            PluginClassLoader pluginClassLoader = new PluginClassLoader(jarEntryInvalidCheck, value);
            classLoaders.put(item.getKey(), pluginClassLoader);
        });
    }

}
