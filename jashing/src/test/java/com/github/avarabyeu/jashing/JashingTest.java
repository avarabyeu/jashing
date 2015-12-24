package com.github.avarabyeu.jashing;

import com.github.avarabyeu.jashing.core.Jashing;

/**
 * Created by andrei_varabyeu on 12/23/15.
 */
public class JashingTest {
    public static void main(String[] args) {
        Jashing.builder().withPort(8282).build().bootstrap();
    }
}
