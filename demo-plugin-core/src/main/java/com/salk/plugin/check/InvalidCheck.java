package com.salk.plugin.check;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public interface InvalidCheck<T> {
    boolean check(T t);
}
