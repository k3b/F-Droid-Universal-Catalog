/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
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

import org.junit.Test;

import java.math.BigInteger;

public class TestDataGeneratorTest {
    @Test
    public void fill_fieldPositonDependent() {
        // 4 generates boolean true. all fields should be filled to value belongig to "4"
        TestClass t = TestDataGenerator.fill(new TestClass(), 4, true);
        assertEquals("b=true, bb=true, by=4, byby=4, i=401, ii=408, l=402, ll=409, bi=407, c=E, cc=E, s=s#4-5, o=null, inherited=413, ", t.toString());
    }

    @Test
    public void fill() {
        // 4 generates boolean true. all fields should be filled to value belongig to "4"
        TestClass t = TestDataGenerator.fill(new TestClass(), 4);
        assertEquals("b=true, bb=true, by=4, byby=4, i=4, ii=4, l=4, ll=4, bi=4, c=E, cc=E, s=s#4, o=null, inherited=4, ", t.toString());
    }

    class TestBaseClass {
        private int inherited;

        @Override
        public String toString() {
            return "inherited=" + inherited + ", ";
        }
    }

    class TestClass extends TestBaseClass {
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

        @Override
        public String toString() {
            String result = "b=" + b + ", " +
                    "bb=" + bb + ", " +
                    "by=" + by + ", " +
                    "byby=" + byby + ", " +
                    "i=" + i + ", " +
                    "ii=" + ii + ", " +
                    "l=" + l + ", " +
                    "ll=" + ll + ", " +
                    "bi=" + bi + ", " +
                    "c=" + c + ", " +
                    "cc=" + cc + ", " +
                    "s=" + s + ", " +
                    "o=" + o + ", " +
                    super.toString();
            return result;
        }
    }

}