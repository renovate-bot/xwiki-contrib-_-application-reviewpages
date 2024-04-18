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
package org.xwiki.contrib.reviewpages.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.reviewpages.ReviewPagesManager;
import org.xwiki.contrib.reviewpages.notifications.events.DocumentReviewDateReachedTargetableEvent;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.observation.ObservationManager;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryManager;
import org.xwiki.user.UserReferenceSerializer;
import org.xwiki.velocity.internal.XWikiDateTool;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;

/**
 * Default implementation of the {@link ReviewPagesManager} role.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Singleton
public class DefaultReviewPagesManager implements ReviewPagesManager
{
    /**
     * Default event source.
     */
    static final String EVENT_SOURCE = "org.xwiki.contrib:application-reviewpages-api";

    @Inject
    private XWikiDateTool dateTool;

    @Inject
    private UserReferenceSerializer<String> serializer;

    @Inject
    @Named("current")
    private DocumentReferenceResolver<String> resolver;

    @Inject
    private Logger logger;

    @Inject
    private ObservationManager observationManager;

    @Inject
    private Provider<XWikiContext> xcontextProvider;

    @Inject
    private QueryManager queryManager;

    @Override
    public List<DocumentReference> getDocumentsToReview() throws QueryException
    {
        List<DocumentReference> results = new ArrayList<>();

        String queryString = "from doc.object(ReviewPages.ReviewData) as reviewData where reviewData.reviewDate = "
            + ":today";

        Query query = queryManager.createQuery(queryString, Query.XWQL);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date today;
        try {
            today = formatter.parse(formatter.format(new Date()));
            query.bindValue("today", today);
            for (Object docFullName : query.execute()) {
                results.add(resolver.resolve((String) docFullName));
            }
            return results;
        } catch (ParseException e) {
            logger.debug("Failed to parse the query date");
        }
        return Collections.emptyList();
    }

    @Override
    public void notifyReviewers(DocumentReference documentReference) throws XWikiException
    {
        XWikiContext context = xcontextProvider.get();

        XWikiDocument document = context.getWiki().getDocument(documentReference, context);
        Set<String> target = new HashSet<>();
        // Notify only the document creator for this version.
        target.add(serializer.serialize(document.getAuthors().getCreator()));
        DocumentReviewDateReachedTargetableEvent event = new DocumentReviewDateReachedTargetableEvent(target);
        // Fire the event
        observationManager.notify(event, EVENT_SOURCE, document);
    }
}
