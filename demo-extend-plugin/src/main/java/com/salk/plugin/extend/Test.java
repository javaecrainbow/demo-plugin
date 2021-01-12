package com.salk.plugin.extend;

import com.salk.plugin.dto.Person;
import org.apache.commons.lang.StringUtils;
import org.ehcache.sizeof.impl.ReflectionSizeOf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：salkli
 * @date：2021/1/7
 */
public class Test {
    public static final String NAME = "A";
    private List<String> cool=new ArrayList();

    public static void main(String[] args) {
        Test test=new Test();
        ReflectionSizeOf r=new ReflectionSizeOf();
        System.out.println(r.deepSizeOf(test.getCool()));
    }
    public Test(){
        init500M();
    }

    private void init500M(){
        for(int i=0;i<10000;i++){
            cool.add(new String("1"));
        }

    }
    public List<String> getCool(){
        return this.cool;
    }
}
