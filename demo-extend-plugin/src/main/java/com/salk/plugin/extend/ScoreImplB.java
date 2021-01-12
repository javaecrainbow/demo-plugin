package com.salk.plugin.extend;

import com.salk.plugin.api.IScore;
import com.salk.plugin.dto.Person;
import org.apache.commons.lang.StringUtils;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public class ScoreImplB implements IScore {
    public static final String NAME = "B";

    @Override
    public Double score(Person person) {
        if (person == null) {
            return Double.valueOf(0);
        }
        //只是为了能把common-lang加载到classpath中
        if (StringUtils.isNotEmpty(person.getName()) && NAME.equals(person.getName())) {
            return Double.valueOf(100);
        }
        return Double.valueOf(60);
    }
}
