package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.vcs.git.GitClient;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

/**
 * @author Andrei Varabyeu
 */
public class GitTest {

    @Test
    public void testGit() {
        GitClient gitClient = new GitClient(
                "git@github.com:avarabyeu/restendpoint.git", "restendpoint");
        LocalDate now = LocalDate.now();
        Instant before = now.minusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Map<String, Integer> commitsPerUser = gitClient.
                getCommitsPerUser(before.
                        atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(commitsPerUser);

    }
}
