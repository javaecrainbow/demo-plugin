package com.salk.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author：salkli
 * @date：2021/1/6
 */
@Data
@AllArgsConstructor
public class Person {
    /**
     * 名称
     */
    private String name;
    /**
     * 版本号
     */
    private String version;
}
