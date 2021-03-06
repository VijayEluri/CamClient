/*
 * Copyright (c) 2013, Matthew Bertolini
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of CamClient nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mattbertolini.camclient.net.urlconnection.support;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Matt Bertolini
 */
public class ContentTypeTest {
    @Test
    public void testValueOfNoCharset() {
        ContentType html = ContentType.valueOf("text/html");
        assertEquals("text", html.getType());
        assertEquals("html", html.getSubtype());
        assertNull(html.getCharset());
        assertEquals("ISO-8859-1", html.getCharsetOrDefault());
    }

    @Test
    public void testValueOfWildcard() {
        ContentType wildcard = ContentType.valueOf("*/*");
        assertEquals("*", wildcard.getType());
        assertEquals("*", wildcard.getSubtype());
        assertNull(wildcard.getCharset());
        assertEquals("ISO-8859-1", wildcard.getCharsetOrDefault());
    }

    @Test
    public void testValueOfWithCharset() {
        ContentType json = ContentType.valueOf("application/json; charset=UTF-8");
        assertEquals("application", json.getType());
        assertEquals("json", json.getSubtype());
        assertEquals("UTF-8", json.getCharset());
        assertEquals("UTF-8", json.getCharsetOrDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromHeaderNullInput() {
        ContentType.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromHeaderInvalidInput() {
        ContentType.valueOf("not valid input.");
    }

    @Test
    public void testWithCharset() {
        ContentType contentType = ContentType.create("application", "json");
        assertEquals("application", contentType.getType());
        assertEquals("json", contentType.getSubtype());
        assertNull(contentType.getCharset());
        assertEquals("ISO-8859-1", contentType.getCharsetOrDefault());
        contentType = contentType.withCharset("UTF-8");
        assertEquals("application", contentType.getType());
        assertEquals("json", contentType.getSubtype());
        assertEquals("UTF-8", contentType.getCharset());
        assertEquals("UTF-8", contentType.getCharsetOrDefault());
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ContentType.class).verify();
    }
}
