/*
 * Copyright (C) 2016 The Android Open Source Project
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
 * Marks a class as a RoomDatabase.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Database {
    /**
     * The list of entities included in the database. Each entity turns into a table in the
     * database.
     *
     * @return The list of entities in the database.
     */
    Class<?>[] entities();

    /**
     * The list of database views included in the database. Each class turns into a view in the
     * database.
     *
     * @return The list of database views.
     */
    Class<?>[] views() default {};

    /**
     * The database version.
     *
     * @return The database version.
     */
    int version();

    /**
     * You can set the annotation processor argument ({@code room.schemaLocation}) to tell Room to
     * export the database schema into a folder. Even though it is not mandatory, it is a good
     * practice to have version history of your schema in your codebase and you should commit the
     * schema files into your version control system (but don't ship them with your app!).
     * <p>
     * When {@code room.schemaLocation} is set, Room will check this variable and if it is set to
     * {@code true}, the database schema will be exported into the given folder.
     * <p>
     * {@code exportSchema} is {@code true} by default but you can disable it for databases when
     * you don't want to keep history of versions (like an in-memory only database).
     *
     * @return Whether the schema should be exported to the given folder when the
     * {@code room.schemaLocation} argument is set. Defaults to {@code true}.
     */
    boolean exportSchema() default true;

    /**
     * List of AutoMigrations that can be performed on this Database.
     * <p>
     * See {@link AutoMigration} for example code usage.
     * <p>
     * For more complicated cases not covered by {@link AutoMigration}, runtime defined
     * androidx.room.migration.Migration Migration added with
     * androidx.room.RoomDatabase.Builder#addMigrations addMigrations can still be used.
     *
     * @return List of AutoMigration annotations.
     */
    AutoMigration[] autoMigrations() default {};
}
