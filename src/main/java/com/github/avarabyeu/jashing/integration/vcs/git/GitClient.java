package com.github.avarabyeu.jashing.integration.vcs.git;

import com.github.avarabyeu.jashing.integration.vcs.AbstractVCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * GIT Client
 *
 * @author Andrey Vorobyov
 */
public class GitClient extends AbstractVCSClient implements VCSClient {


    private Repository repository;

    private Git git;


    public GitClient(Path localRepoRoot, String repoName, String repositoryUrl) {
        try {
            File repositoryDir = new File(localRepoRoot.toFile(), repoName);
            if (!repositoryDir.exists()) {

                Git.cloneRepository()
                        .setURI(repositoryUrl)
                        .setDirectory(repositoryDir)
                        .setCloneAllBranches(true)
                        .call();
            }

            // now open the created repository
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            System.out.println(repositoryUrl);
            this.repository = builder.setWorkTree(repositoryDir)
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .setMustExist(true)
                    .build();
            this.git = new Git(repository);

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to) {
        Map<String, Integer> commiters = new HashMap<>();
        Function<RevCommit, Void> mapCommitersFunction = commit -> {
            String name = commit.getAuthorIdent().getName();
            Integer authorCommits = commiters.get(name);
            if (null == authorCommits) {
                commiters.put(name, 1);
            } else {
                commiters.put(name, ++authorCommits);
            }
            return null;
        };
        walkThroughCommits(from, to, mapCommitersFunction);
        return commiters;
    }

    @Override
    public long getCommitsForPeriod(@Nonnull Instant from, @Nullable Instant to) {
        final Long[] commits = new Long[]{0l};
        Function<RevCommit, Void> countCommitsFunction = commit -> {
            commits[0]++;
            return null;
        };
        walkThroughCommits(from, to, countCommitsFunction);
        return commits[0];
    }


    private void walkThroughCommits(@Nonnull Instant from, @Nullable Instant to, Function<RevCommit, ?> function) {
        RevFilter revFilter;
        if (null == to) {
            System.out.println(from.atZone(ZoneId.systemDefault()));
            revFilter = CommitTimeRevFilter.after(from.toEpochMilli());
        } else {
            revFilter = CommitTimeRevFilter.between(from.toEpochMilli(), to.toEpochMilli());
        }

        RevWalk walk = new RevWalk(repository);


        walk.setRevFilter(revFilter);
        walk.setRetainBody(true);
        try {
            ObjectId head = repository.resolve("HEAD");
            walk.markStart(walk.lookupCommit(head));
            walk.forEach(function::apply);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            walk.release();
        }

    }
}
