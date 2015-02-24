package com.github.avarabyeu.jashing.core.eventsource;

import com.google.common.base.Joiner;
import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Andrei Varabyeu
 */
public class ClasspathScannerTest {

    @Test
    public void testScanner() throws IOException {
        Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("com.github.avarabyeu.jashing.eventsource");

        List<Class<?>> eventSourceCandidates = new ArrayList<>();
        for (ClassPath.ClassInfo classInfo : classes) {

            Class<?> clazz = classInfo.load();
            if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && EventSource.class.isAssignableFrom(clazz)) {
                eventSourceCandidates.add(clazz);
            }

        }

        System.out.println(Joiner.on("\n").join(eventSourceCandidates));


    }
}
