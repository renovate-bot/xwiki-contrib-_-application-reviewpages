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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
// import org.junit.Rule;
import org.xwiki.panels.test.po.ApplicationsPanel;
import org.xwiki.test.ui.AbstractTest;
import org.xwiki.test.ui.SuperAdminAuthenticationRule;
import org.xwiki.test.ui.po.ViewPage;

/**
 * UI Tests for the ReviewPages application - Stub.
 */
public class ReviewPagesTest extends AbstractTest
{

    @Rule
    public SuperAdminAuthenticationRule authenticationRule = new SuperAdminAuthenticationRule(getUtil(), getDriver());
    

    @Test
    public void testViewPages() throws InterruptedException
    {
        ApplicationsPanel applicationPanel = ApplicationsPanel.gotoPage();
        ViewPage vp = applicationPanel.clickApplication("Review Pages");
        Assert.assertEquals("ReviewPages", vp.getMetaDataValue("space"));
        Assert.assertEquals("WebHome", vp.getMetaDataValue("page"));
    }
}
