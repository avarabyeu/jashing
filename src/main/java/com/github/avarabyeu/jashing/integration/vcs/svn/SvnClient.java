package com.github.avarabyeu.jashing.integration.vcs.svn;

import com.github.avarabyeu.jashing.integration.vcs.AbstractVCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSClient;
import com.github.avarabyeu.jashing.integration.vcs.VCSClientException;
import com.google.common.net.UrlEscapers;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnLog;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnRevisionRange;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple SVN client
 *
 * @author Andrey Vorobyov
 */
public class SvnClient extends AbstractVCSClient implements VCSClient {

    private final SvnOperationFactory svnOperationFactory;

    private final SVNURL svnUrl;

    public SvnClient(@Nonnull String url, @Nonnull String name, @Nonnull String password) {
        DAVRepositoryFactory.setup();

        try {
            svnUrl = SVNURL.parseURIEncoded(UrlEscapers.urlFragmentEscaper().escape(url));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
            svnOperationFactory = new SvnOperationFactory();
            svnOperationFactory.setAuthenticationManager(authManager);

        } catch (SVNException e) {
            throw new VCSClientException("Unable to initialize svn client", e);

        }
    }

    public Map<String, Integer> getCommitsPerUser(@Nonnull Instant from, @Nullable Instant to) {
        try {
            Map<String, Integer> commiters = new HashMap<>();
            final SvnLog log = svnOperationFactory.createLog();
            log.setSingleTarget(SvnTarget.fromURL(svnUrl));

            SVNRevision endRevision = to == null ? SVNRevision.HEAD : SVNRevision.create(Date.from(to));
            log.addRange(SvnRevisionRange.create(SVNRevision.create(Date.from(from.atZone(ZoneId.systemDefault()).toInstant())), endRevision));
            log.setDiscoverChangedPaths(false);
            log.setReceiver((svnTarget, svnLogEntry) -> {
                Integer authorCommits = commiters.get(svnLogEntry.getAuthor());
                if (null == authorCommits) {
                    commiters.put(svnLogEntry.getAuthor(), 1);
                } else {
                    commiters.put(svnLogEntry.getAuthor(), ++authorCommits);
                }
            });
            log.run();
            return commiters;
        } catch (SVNException e) {
            throw new VCSClientException("Unable to get revision list for period [" + from + "," + to + "]");
        }

    }


    public long getCommitsForPeriod(@Nonnull Instant from, @Nullable Instant to) {
        try {

            final SvnLog log = svnOperationFactory.createLog();
            log.setSingleTarget(SvnTarget.fromURL(svnUrl));

            SVNRevision endRevision = to == null ? SVNRevision.HEAD : SVNRevision.create(Date.from(to));
            log.addRange(SvnRevisionRange.create(SVNRevision.create(Date.from(from.atZone(ZoneId.systemDefault()).toInstant())), endRevision));
            log.setDiscoverChangedPaths(false);

            final AtomicInteger count = new AtomicInteger(0);
            log.setReceiver((svnTarget, svnLogEntry) -> count.incrementAndGet());
            log.run();
            return count.get();
        } catch (SVNException e) {
            throw new VCSClientException("Unable to get revision list for period [" + from + "," + to + "]");
        }
    }

}
