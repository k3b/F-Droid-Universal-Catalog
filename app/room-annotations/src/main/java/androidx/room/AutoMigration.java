/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.room;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Declares an automatic migration on a Database.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AutoMigration {
    /**
     * Version of the database schema to migrate from.
     *
     * @return Version number of the database to migrate from.
     */
    int from();

    /**
     * Version of the database schema to migrate to.
     *
     * @return Version number of the database to migrate to.
     */
    int to();

    /**
     * User implemented custom auto migration spec.
     *
     * @return The auto migration specification or none if the user has not implemented a spec
     */
    Class<?> spec() default Object.class;
}
