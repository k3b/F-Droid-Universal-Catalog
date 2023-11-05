/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.domain.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;

public class ExceptionUtilsTest {

    @Test
    public void findParentCauseByType_foundMid() {
        IllegalArgumentException found = new IllegalArgumentException(new RuntimeException());
        Exception root = new RuntimeException(new RuntimeException(found));

        assertEquals(found, ExceptionUtils.findParentCauseByType(root, IllegalArgumentException.class));
    }

    @Test
    public void findParentCauseByType_foundLast() {
        IllegalArgumentException found = new IllegalArgumentException();
        Exception root = new RuntimeException(new RuntimeException(found));

        assertEquals(found, ExceptionUtils.findParentCauseByType(root, IllegalArgumentException.class));
    }

    @Test
    public void findParentCauseByType_notfound() {
        Exception found = new RuntimeException(new RuntimeException());
        Exception root = new RuntimeException(new RuntimeException(found));

        assertNull(ExceptionUtils.findParentCauseByType(root, IllegalArgumentException.class));
    }

    @Test
    public void findFirstParentCause() {
        IllegalArgumentException found = new IllegalArgumentException();
        Exception root = new RuntimeException(new RuntimeException(found));

        assertEquals(found, ExceptionUtils.findFirstParentCause(root));
    }

    @Test
    public void getParentCauseMessage_root_empty() {
        IllegalArgumentException root = new IllegalArgumentException("some message");
        assertEquals("", ExceptionUtils.getParentCauseMessage(root, IllegalArgumentException.class));
    }

    @Test
    public void getParentCauseMessage_notFound_messageWithClassname() {
        Exception found = new IllegalArgumentException("Some message");
        Exception root = new RuntimeException(new RuntimeException(found));
        assertEquals("Caused by IllegalArgumentException: 'Some message'", ExceptionUtils.getParentCauseMessage(root, IOException.class));
    }

    @Test
    public void getParentCauseMessage_found_messageWithoutClassname() {
        Exception found = new IllegalArgumentException("Some message");
        Exception root = new RuntimeException(new RuntimeException(found));
        assertEquals("Caused by: 'Some message'", ExceptionUtils.getParentCauseMessage(root, IllegalArgumentException.class));
    }
}