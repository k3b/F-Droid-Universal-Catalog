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

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * fill testdata to objects via Java reflection:
 * All primitive fields get a reproducable value based on baseValue
 * inspired by https://tuhrig.de/create-random-test-objects-with-java-reflection/
 */
public class TestDataGenerator {

    private static final String NUMBER2_TYPES = ",Boolean,Byte,Integer,Long,BigInteger,";
    private static final String NUMBER_TYPES = ",bool,byte,int,long,";

    // char,Character,
    public static <T> T fill(T instance, int baseValue) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getRandomValueForField(instance, field, baseValue);
            if (value != null) {
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException ignore) {
                    // cannot set value so ignore it
                }
            }
        }
        return instance;
    }

    private static Object getRandomValueForField(Object instance, Field field, int baseValue) {
        Class<?> type = field.getType();

        // Note that we must handle the different types here! This is just an
        // example, so this list is not complete! Adapt this to your needs!
        if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[baseValue % enumValues.length];
        } else if (type.equals(Byte.class)) {
            return Byte.valueOf((byte) baseValue);
        } else if (type.equals(Integer.class)) {
            return Integer.valueOf(baseValue);
        } else if (type.equals(Long.class)) {
            return Long.valueOf(baseValue);
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(baseValue);

        } else if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return Character.valueOf((char) ('A' + baseValue));
        } else if (type.isAssignableFrom(Number.class) || NUMBER_TYPES.contains("," + type.getSimpleName() + ",")) {
            return (byte) baseValue;
        } else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return (baseValue % 2 == 0);
        } else if (type.equals(String.class)) {
            return field.getName() + "#" + baseValue;
        }
        return null;
    }
}