package com.salk.plugin.api;

import com.salk.plugin.dto.Person;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public interface IScore {
    /**
     * 对用户进行评价
     * @param person
     * @return String
     */
    Double score(Person person);


}
