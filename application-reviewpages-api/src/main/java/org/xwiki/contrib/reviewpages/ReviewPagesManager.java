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
package org.xwiki.contrib.reviewpages;

import java.util.List;

import org.xwiki.component.annotation.Role;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.query.QueryException;

import com.xpn.xwiki.XWikiException;

/**
 * Review pages manager allowing to maintain list of wiki pages that needs to be reviewed from time to time, together
 * with a date for the next review for each page.
 *
 * @version $Id$
 * @since 1.0
 */
@Role
public interface ReviewPagesManager
{
    /**
     * Get the list of documents ready to review.
     *
     * @return a list of DocumentReferences.
     * @throws QueryException in case of issue
     */
    List<DocumentReference> getDocumentsToReview() throws QueryException;

    /**
     * Notify the reviewers of a document that its review date has been reached.
     *
     * @param documentReference the published document reference
     */
    void notifyReviewers(DocumentReference documentReference) throws XWikiException;
}
