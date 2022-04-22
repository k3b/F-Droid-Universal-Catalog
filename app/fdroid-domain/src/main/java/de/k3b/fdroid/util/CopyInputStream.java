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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * reading and processing {@link InputStream} and also
 * copying it-s content to a {@link OutputStream}.
 * <p>
 * Example: Simultanious parsing a zip from https and saving it as a file.
 */
public class CopyInputStream extends FilterInputStream {
    // MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to
    // use when skipping.
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    private final OutputStream out;

    public CopyInputStream(InputStream in, OutputStream out) {
        super(in);
        this.out = out;
    }

    public int read() throws IOException {
        int c = super.read();
        if (c != -1) out.write(c);
        return c;
    }

    public int read(byte[] b, int offset, int len) throws IOException {
        int result = super.read(b, offset, len);
        if (result != -1) out.write(b, offset, len);
        return result;
    }

    /**
     * inspired by {@link InputStream#skip(long)}
     */
    public long skip(long n) throws IOException {
        long remaining = n;
        int nr;

        if (n <= 0) {
            return 0;
        }

        int size = (int) Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
        byte[] skipBuffer = new byte[size];
        while (remaining > 0) {
            nr = read(skipBuffer, 0, (int) Math.min(size, remaining));
            if (nr < 0) {
                break;
            }
            out.write(skipBuffer, 0, nr);
            remaining -= nr;
        }

        return n - remaining;
    }

    public void close() throws IOException {
        out.close();
        super.close();
    }
}
