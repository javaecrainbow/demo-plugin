package com.salk.plugin.extend;

import java.util.ArrayList;
import java.util.List;

import com.salk.plugin.api.PluginClose;
import org.apache.commons.lang.StringUtils;
import org.ehcache.sizeof.impl.ReflectionSizeOf;

import com.salk.plugin.api.IScore;
import com.salk.plugin.dto.Person;

/**
 * @author：salkli
 * @date：2021/1/6
 */
public class ScoreImplA implements IScore, PluginClose {

    public static final String NAME = "A";
    private  List<byte[]> cool=new ArrayList();

    public static void main(String[] args) {
        ScoreImplA scoreImplA=new ScoreImplA();
        ReflectionSizeOf r=new ReflectionSizeOf();
        System.out.println(r.deepSizeOf(scoreImplA.getCool()));
    }
    public ScoreImplA(){
        System.out.println("init scoreImplA"+ScoreImplA.class.getClassLoader());
        init500M();
    }
    @Override
    public Double score(Person person) {
        if(person==null){
            return Double.valueOf(0);
        }
        System.out.println(person.getVersion());
        //只是为了能把common-lang加载到classpath中
        if(StringUtils.isNotEmpty(person.getName()) && NAME.equals(person.getName())){
            return Double.valueOf(100);
        }
        return Double.valueOf(60);
    }

    private void init500M(){
        for(int i=0;i<500;i++){
            cool.add(new byte[1024*1024]);
        }

    }
    public List<byte[]> getCool(){
        return this.cool;
    }

    @Override
    public void close() {
        this.cool.clear();
    }
}
