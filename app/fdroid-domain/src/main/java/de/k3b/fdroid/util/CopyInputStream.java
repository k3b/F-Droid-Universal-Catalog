/*
 * Copyright (c) 2022 by k3b.
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

package de.k3b.fdroid.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * reading and processing {@link InputStream} and also
 * copying it-s content to a {@link OutputStream}.
 * <p>
 * Example: Simultanious parsing a zip from https and saving it as a file.
 */
public class CopyInputStream extends InputStream {
    // MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to
    // use when skipping.
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    private final InputStream in;
    private final OutputStream out;

    public CopyInputStream(InputStream in, OutputStream out) {
        if (in == null || out == null) throw new NullPointerException();
        this.in = in;
        this.out = out;
    }

    /* In original InputStream every byte goes through read() */
    public int read() throws IOException {
        int c = in.read();
        if (c != -1) out.write(c);
        return c;
    }

    public void close() throws IOException {
        // if this.in is closed without reading to the end:
        // read (and copy) until the end.
        do {
        } while (skip(MAX_SKIP_BUFFER_SIZE) == MAX_SKIP_BUFFER_SIZE);

        out.flush();
        out.close();
        in.close();
    }
}
