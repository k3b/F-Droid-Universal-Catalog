/*
 * Copyright (c) 2023 by k3b.
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

@SuppressWarnings("unused")
public class ExceptionUtils {
    public static String getParentCauseMessage(Throwable throwable, Class<?> exceptionTypeClass) {
        Throwable cause = findParentCauseByType(throwable, exceptionTypeClass);
        if (cause != null && cause != throwable) return "Caused by: '" + cause.getMessage() + "'";

        cause = findFirstParentCause(throwable);
        if (cause != null && cause != throwable) {
            return "Caused by "
                    + cause.getClass().getSimpleName() + ": '" + cause.getMessage() + "'";
        }
        return "";
    }

    public static Throwable findParentCauseByTypeOrFirst(Throwable throwable, Class<?> exceptionTypeClass) {
        Throwable cause = findParentCauseByType(throwable, exceptionTypeClass);
        if (cause == null) cause = findFirstParentCause(throwable);
        return cause;
    }

    public static Throwable findParentCauseByType(Throwable throwable, Class<?> exceptionTypeClass) {
        Throwable rootCause = throwable;
        while (rootCause != null) {
            if (rootCause.getClass().equals(exceptionTypeClass)) return rootCause;

            if (rootCause == rootCause.getCause()) return null; // avoid endless recursion
            rootCause = rootCause.getCause();
        }
        return null;
    }

    public static Throwable findFirstParentCause(Throwable throwable) {
        if (throwable == null) return null;
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
