package com.github.avarabyeu.jashing.integration.vcs.git;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.StreamSupport;

/**
 * Created by avarabyeu on 11/27/15.
 */
public class GitTest {

    private static final Supplier<File> JASHING_TEMP_DIR = Suppliers.memoize(() -> {
        File jashingTempDir = new File("jashing-temp");
        //noinspection ResultOfMethodCallIgnored
        jashingTempDir.mkdir();
        return jashingTempDir;
    });

    public static void main(String[] args) throws GitAPIException, IOException {
        String repoName = "reportportal";
        File repositoryDir = new File(JASHING_TEMP_DIR.get(), repoName);
        Git git;
        if (!repositoryDir.exists()) {

            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI("git@git.epam.com:epmc-tst/reportportal.git")
                    .setDirectory(repositoryDir);

            git = cloneCommand.call();
        } else {
            // now open the created repository
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setWorkTree(repositoryDir)
                    .readEnvironment() // scan environment GIT_* variables
                    //.findGitDir() // scan up the file system tree
                    .setMustExist(true)
                    .build();

            git = new Git(repository);
        }
        System.out.println("Cloned...");

        StreamSupport.stream(git.log().all().call().spliterator(), false)
                .filter(revCommit -> {
                    return Instant.ofEpochSecond(revCommit.getCommitTime()).atZone(ZoneId.systemDefault()).toLocalDate()
                            .isEqual(
                                    LocalDate.now());
                }).forEach(r -> {
            System.out.println(r.getName());
            System.out.println(r.getAuthorIdent().getName());
        });

    }
}
