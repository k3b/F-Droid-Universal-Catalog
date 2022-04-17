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

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.math.BigInteger;

public class TestDataGeneratorTest {
    @Test
    public void fill() {
        // 4 generates boolean true. all fields should be filled to value belongig to "4"
        TestClass t = TestDataGenerator.fill(new TestClass(), 4);
        assertEquals("b=true, bb=true, by=4, byby=4, i=4, ii=4, l=4, ll=4, bi=4, c=E, cc=E, s=s#4, o=null", t.toString());
    }

    class TestClass {
        private int i;
        private long l;
        private char c;
        private byte by;
        private String s;
        private boolean b;
        private BigInteger bi;

        private Integer ii;
        private Long ll;
        private Boolean bb;
        private Character cc;
        private Byte byby;

        private TestDataGeneratorTest o;

        @NonNull
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("b=").append(b).append(", ");
            result.append("bb=").append(bb).append(", ");
            result.append("by=").append(by).append(", ");
            result.append("byby=").append(byby).append(", ");
            result.append("i=").append(i).append(", ");
            result.append("ii=").append(ii).append(", ");
            result.append("l=").append(l).append(", ");
            result.append("ll=").append(ll).append(", ");
            result.append("bi=").append(bi).append(", ");
            result.append("c=").append(c).append(", ");
            result.append("cc=").append(cc).append(", ");
            result.append("s=").append(s).append(", ");
            result.append("o=").append(o);
            return result.toString();
        }
    }

}