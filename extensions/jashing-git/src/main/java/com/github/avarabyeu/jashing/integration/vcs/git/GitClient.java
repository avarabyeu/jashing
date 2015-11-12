package com.github.avarabyeu.jashing.integration.vcs.git;

import com.github.avarabyeu.jashing.integration.vcs.AbstractVCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSClientException;
import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * GIT Client
 *
 * @author Andrey Vorobyov
 */
public class GitClient extends AbstractVCSClient implements VCSClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitClient.class);

    private static final String HEAD_REVISION = "HEAD";

    private static final Supplier<File> JASHING_TEMP_DIR = Suppliers.memoize(() -> {
        File jashingTempDir = new File(StandardSystemProperty.JAVA_IO_TMPDIR.value() + File.separator + "jashing");
        //noinspection ResultOfMethodCallIgnored
        jashingTempDir.mkdir();
        return jashingTempDir;
    });

    private Git git;

    public GitClient(String repositoryUrl, String repoName, Collection<String> branches) {
        try {
            File repositoryDir = new File(JASHING_TEMP_DIR.get(), repoName);
            if (!repositoryDir.exists()) {

                LOGGER.info("Cloning git repository with name {}", repoName);
                CloneCommand cloneCommand = Git.cloneRepository()
                        .setURI(repositoryUrl)
                        .setDirectory(repositoryDir);
                if (null != branches && !branches.isEmpty()) {
                    cloneCommand.setBranchesToClone(branches);
                }
                this.git = cloneCommand.call();
            } else {
                // now open the created repository
                FileRepositoryBuilder builder = new FileRepositoryBuilder();
                Repository repository = builder.setWorkTree(repositoryDir)
                        .readEnvironment() // scan environment GIT_* variables
                                //.findGitDir() // scan up the file system tree
                        .setMustExist(true)
                        .build();

                this.git = new Git(repository);
            }

        } catch (GitAPIException | IOException e) {
            LOGGER.error("Unable to init GIT repository", e);
        }
    }


    @Override
    public Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to) {
        fetch();
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
        fetch();
        final AtomicLong commits = new AtomicLong();
        Function<RevCommit, Void> countCommitsFunction = commit -> {
            commits.incrementAndGet();
            return null;
        };
        walkThroughCommits(from, to, countCommitsFunction);
        return commits.get();
    }


    private synchronized void walkThroughCommits(@Nonnull Instant from, @Nullable Instant to, Function<RevCommit, ?> function) {
        RevFilter revFilter;
        if (null == to) {
            revFilter = CommitTimeRevFilter.after(from.toEpochMilli());
        } else {
            revFilter = CommitTimeRevFilter.between(from.toEpochMilli(), to.toEpochMilli());
        }

        RevWalk walk = new RevWalk(git.getRepository());

        walk.setRevFilter(revFilter);
        walk.setRetainBody(true);
        try {
            ObjectId head = git.getRepository().resolve(HEAD_REVISION);
            walk.markStart(walk.lookupCommit(head));
            walk.sort(RevSort.COMMIT_TIME_DESC);
            walk.forEach(function::apply);
        } catch (IOException e) {
           throw new VCSClientException("Cannot walk through commits", e);
        } finally {
            walk.dispose();
        }

    }

    private synchronized void fetch() {
        try {
            LOGGER.info("Pulling updates of GIT repository [{}]", git.getRepository().getDirectory().getAbsolutePath());
            git.fetch().call();
            PullResult pullResult = git.pull().call();
            LOGGER.info("Fetched from [{}]. Result [{}]", pullResult.getFetchedFrom(), pullResult.isSuccessful());
        } catch (GitAPIException e) {
            LOGGER.error(MessageFormatter.format("Cannot PULL repository [{}]", git.getRepository().getDirectory().getAbsolutePath()).getMessage(), e);
        }
    }
}
