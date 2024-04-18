/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.reviewpages.internal.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.contrib.reviewpages.ReviewPagesManager;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.query.QueryException;

import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.plugin.scheduler.AbstractJob;
import com.xpn.xwiki.web.Utils;

/**
 * Scheduler job that notifies the reviewers group of the review pages that have their review date reached. The
 * scheduler job is executed every day at 8:00 AM.
 * <p>
 * Note that the "Job execution context user" property of this scheduler JOB is set to XWiki.XWikiGuest This done on
 * purpose in order to make sure that notifications will be sent regardless of configuration of the filters : "System
 * Filter" and "Own Events Filter". For example, if the job execution context user is set to superadmin the notification
 * will not be sent when the "System Filter" filter is enabled, so, initializing the job context user with the guest
 * user will ensure that the notifications will not be hidden.
 *
 * @version $Id$
 * @since 1.0
 */
public class ReviewPagesNotifierSchedulerJob extends AbstractJob implements Job
{
    @Override
    protected void executeJob(JobExecutionContext jobContext)
    {
        Logger logger = LoggerFactory.getLogger(ReviewPagesNotifierSchedulerJob.class);
        logger.debug("Review Published Documents Notifier Scheduler Job started ...");
        ReviewPagesManager reviewPagesManager = Utils.getComponent(ReviewPagesManager.class);
        try {
            List<DocumentReference> documentReferences = reviewPagesManager.getDocumentsToReview();
            if (!documentReferences.isEmpty()) {
                for (DocumentReference documentReference : documentReferences) {
                    try {
                        reviewPagesManager.notifyReviewers(documentReference);
                        logger.debug(String.format("The reviewers of the document [%s] have been notified.",
                            documentReference));
                    } catch (XWikiException e) {
                        logger.error(String.format("An error appeared when notifying reviewers of the document [%s].",
                            documentReference), e);
                    }
                }
            } else {
                logger.debug("There are no documents ready to review.");
            }
        } catch (QueryException e) {
            logger.error("An error appeared when executing the Review Published Documents Notifier Scheduler Job.", e);
        }
        logger.debug("Review Published Documents Notifier Scheduler Job finished ...");
    }
}
