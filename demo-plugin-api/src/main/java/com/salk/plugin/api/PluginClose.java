package com.salk.plugin.api;

import com.salk.plugin.dto.Person;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public interface PluginClose {
    /**
     * 插件卸载的时候提供给执行者的关闭接口
     */
    void close();


}
