package com.github.avarabyeu.jashing.utils;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Unit tests for {@link com.github.avarabyeu.jashing.utils.InstanceOfMap}
 * @author Andrei Varabyeu
 */
public class InstanceOfMapTest {

    @Test
    public void testBuilder(){
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        HashMap<Object, Object> hashMap = new HashMap<>();
        InstanceOfMap<Object> instanceOfMap = InstanceOfMap.builder().fromList(Lists.<Map<?, ?>>newArrayList(treeMap, hashMap));

        Assert.assertThat(instanceOfMap, hasEntry(TreeMap.class, treeMap));
        Assert.assertThat(instanceOfMap, hasEntry(HashMap.class, hashMap));
    }

    @Test
    public void testInstanceOf(){
        InstanceOfMap<Object> instanceOfMap = InstanceOfMap.builder().fromList(Lists.<Map<?, ?>>newArrayList(new TreeMap<>()));
        Assert.assertThat(instanceOfMap.getInstanceOf(Map.class), notNullValue());
    }
}
