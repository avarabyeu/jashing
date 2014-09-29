package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.vcs.git.GitClient;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * Created by andrey.vorobyov on 9/27/14.
 */
public class GitTest {

    @Test
    public void testGit() {

        String property = System.getProperty("java.io.tmpdir");
        System.out.println(property);
        GitClient gitClient = new GitClient(FileSystems.getDefault().
                getPath(property), "restendpoint",
                "git@github.com:avarabyeu/restendpoint.git");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minusMonths(30);
        Map<String, Integer> commitsPerUser = gitClient.
                getCommitsPerUser(before.
                        atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(commitsPerUser);

    }
}
