package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.vcs.git.GitClient;
import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * @author Andrei Varabyeu
 */
public class GitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitTest.class);

    @Test
    @Ignore
    public void testGit() {
        GitClient gitClient = new GitClient(
                "git@github.com:avarabyeu/jashing.git", "jashing", Collections.singletonList("master"));
        LocalDate now = LocalDate.now();
        Instant before = now.minusMonths(12).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Map<String, Integer> commitsPerUser = gitClient.
                getCommitsPerUser(before.
                        atZone(ZoneId.systemDefault()).toInstant());
        LOGGER.info(Joiner.on(",").withKeyValueSeparator(":").join(commitsPerUser));
        Assert.assertThat("GIT checkout doesn't work properly", commitsPerUser.entrySet(), not(empty()));

    }
}
