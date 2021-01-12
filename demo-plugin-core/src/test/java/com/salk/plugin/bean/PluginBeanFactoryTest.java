package com.salk.plugin.bean;

import com.salk.plugin.api.IScore;
import com.salk.plugin.dto.Person;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author salkli
 * @date 2021/1/8
 */
class PluginBeanFactoryTest {

    @Test
    @SneakyThrows
    void getScoreInstance() {
        String version = "1.0";
        PluginBeanFactory pluginBeanFactory = PluginBeanFactory.get();
        IScore scorea = pluginBeanFactory.getScoreInstance(version, "scoreb");
        Double score = scorea.score(new Person("B", version));
        System.out.println(version + " =" + score);
        version = "2.0";
        IScore scoreb = pluginBeanFactory.getScoreInstance(version, "scorea");
        Double score2 = scoreb.score(new Person("A", version));
        System.out.println(version + " =" + score2);
        while (true) {
            Thread.sleep(1000);
        }
    }

    @Test
    @SneakyThrows
    void remove() {
        String version = "1.0";
        PluginBeanFactory pluginBeanFactory = PluginBeanFactory.get();
        IScore scoreb = pluginBeanFactory.getScoreInstance(version, "scoreb");
        Double score = scoreb.score(new Person("B", version));
        System.out.println(version + " =" + score);
        version = "2.0";
        IScore scorea = pluginBeanFactory.getScoreInstance(version, "scorea");
        Double score2 = scorea.score(new Person("A", version));
        System.out.println(version + " =" + score2);
        // 测试是否会有不同的实例对象
        IScore scoreaA = pluginBeanFactory.getScoreInstance(version, "scorea");
        Double score3 = scoreaA.score(new Person("A", version));
        // Thread.sleep(20000);
        pluginBeanFactory.remove("2.0");
        // IScore scoreaAa = pluginBeanFactory.getScoreInstance(version, "scorea");
        // Double score35 = scoreaA.score(new Person("A",version));
        // scorea=null;
        // Thread.sleep(20000);
        System.out.println("手动执行gc");
        // System.gc();
        // scorea=null;
        // Thread.sleep(20000);
        // IScore scoreAfterGc = pluginBeanFactory.getScoreInstance(version, "scorea");
        // if(scoreAfterGc==null){
        // System.out.println("score is not undefine");
        // }
    }
}