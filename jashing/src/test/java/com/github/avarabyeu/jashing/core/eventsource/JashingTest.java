package com.github.avarabyeu.jashing.core.eventsource;

import com.github.avarabyeu.jashing.core.Jashing;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andrey.vorobyov on 2/12/15.
 */
public class JashingTest {

    @Ignore
    @Test
    public void testJashing() {
        //new Jashing().registerModule(new VcsModule()).start();
        new Jashing().start();

    }
}
