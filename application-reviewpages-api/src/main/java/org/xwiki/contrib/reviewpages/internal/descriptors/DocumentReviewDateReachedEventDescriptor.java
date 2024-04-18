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
package org.xwiki.contrib.reviewpages.internal.descriptors;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.reviewpages.notifications.events.DocumentReviewDateReachedTargetableEvent;
import org.xwiki.eventstream.RecordableEventDescriptor;

/**
 * Descriptor related to the event {@link DocumentReviewDateReachedTargetableEvent}.
 *
 * @version $Id$
 * @since 1.0
 */
@Component
@Singleton
@Named("org.xwiki.contrib.reviewpages.notifications.events.DocumentReviewDateReachedTargetableEvent")
public class DocumentReviewDateReachedEventDescriptor implements RecordableEventDescriptor
{
    @Override
    public String getEventType()
    {
        return DocumentReviewDateReachedTargetableEvent.class.getCanonicalName();
    }

    @Override
    public String getDescription()
    {
        return "ReviewPages.event.reviewDateReached.description";
    }

    @Override
    public String getApplicationName()
    {
        return "ReviewPages.application";
    }

    @Override
    public String getApplicationIcon()
    {
        return "check";
    }
}
