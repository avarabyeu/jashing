package com.github.avarabyeu.jashing.http;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by avarabyeu on 11/30/15.
 */
public class AnotherTest {

    public static void main(String[] args) throws IOException, URISyntaxException {
        URL statics = Thread.currentThread().getContextClassLoader().getResource("com/google");
        File file = new File(statics.getPath());
        System.out.println(statics.openStream());
        System.out.println("LIST:" + Arrays.toString(file.list()));
        System.out.println(statics);
        String path = AnotherTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        System.out.println(path);
//        ((String) path)
//                .getParentFile().getPath().replace('\\', '/')
        //Utils.enumerationAsStream(statics).forEach(System.out::println);

    }
}
