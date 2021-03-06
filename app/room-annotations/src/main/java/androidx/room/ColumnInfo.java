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

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows specific customization about the column associated with this field.
 * <p>
 * For example, you can specify a column name for the field or change the column's type affinity.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface ColumnInfo {
    /**
     * Constant to let Room inherit the field name as the column name. If used, Room will use the
     * field name as the column name.
     */
    String INHERIT_FIELD_NAME = "[field-name]";
    /**
     * Undefined type affinity. Will be resolved based on the type.
     *
     * @see #typeAffinity()
     */
    int UNDEFINED = 1;
    /**
     * Column affinity constant for strings.
     *
     * @see #typeAffinity()
     */
    int TEXT = 2;
    /**
     * Column affinity constant for integers or booleans.
     *
     * @see #typeAffinity()
     */
    int INTEGER = 3;
    /**
     * Column affinity constant for floats or doubles.
     *
     * @see #typeAffinity()
     */
    int REAL = 4;
    /**
     * Column affinity constant for binary data.
     *
     * @see #typeAffinity()
     */
    int BLOB = 5;
    /**
     * Collation sequence is not specified. The match will behave like {@link #BINARY}.
     *
     * @see #collate()
     */
    int UNSPECIFIED = 1;
    /**
     * Collation sequence for case-sensitive match.
     *
     * @see #collate()
     */
    int BINARY = 2;
    /**
     * Collation sequence for case-insensitive match.
     *
     * @see #collate()
     */
    int NOCASE = 3;
    /**
     * Collation sequence for case-sensitive match except that trailing space characters are
     * ignored.
     *
     * @see #collate()
     */
    int RTRIM = 4;
    /**
     * Collation sequence that uses system's current locale.
     *
     * @see #collate()
     */
    @RequiresApi(21)
    int LOCALIZED = 5;
    /**
     * Collation sequence that uses Unicode Collation Algorithm.
     *
     * @see #collate()
     */
    @RequiresApi(21)
    int UNICODE = 6;
    /**
     * A constant for {@link #defaultValue()} that makes the column to have no default value.
     */
    String VALUE_UNSPECIFIED = "[value-unspecified]";

    /**
     * Name of the column in the database. Defaults to the field name if not set.
     *
     * @return Name of the column in the database.
     */
    String name() default INHERIT_FIELD_NAME;

    /**
     * The type affinity for the column, which will be used when constructing the database.
     * <p>
     * If it is not specified, the value defaults to {@link #UNDEFINED} and Room resolves it based
     * on the field's type and available TypeConverters.
     * <p>
     * See <a href="https://www.sqlite.org/datatype3.html">SQLite types documentation</a> for
     * details.
     *
     * @return The type affinity of the column. This is either {@link #UNDEFINED}, {@link #TEXT},
     * {@link #INTEGER}, {@link #REAL}, or {@link #BLOB}.
     */
    @SuppressWarnings("unused") @SQLiteTypeAffinity int typeAffinity() default UNDEFINED;

    /**
     * Convenience method to index the field.
     * <p>
     * If you would like to create a composite index instead, see: {@link Index}.
     *
     * @return True if this field should be indexed, false otherwise. Defaults to false.
     */
    boolean index() default false;

    /**
     * The collation sequence for the column, which will be used when constructing the database.
     * <p>
     * The default value is {@link #UNSPECIFIED}. In that case, Room does not add any
     * collation sequence to the column, and SQLite treats it like {@link #BINARY}.
     *
     * @return The collation sequence of the column. This is either {@link #UNSPECIFIED},
     * {@link #BINARY}, {@link #NOCASE}, {@link #RTRIM}, {@link #LOCALIZED} or {@link #UNICODE}.
     */
    @Collate int collate() default UNSPECIFIED;

    /**
     * The default value for this column.
     * <pre>
     *   {@literal @}ColumnInfo(defaultValue = "No name")
     *   public String name;
     *
     *  {@literal @}ColumnInfo(defaultValue = "0")
     *   public int flag;
     * </pre>
     * <p>
     * Note that the default value you specify here will <em>NOT</em> be used if you simply
     * insert the {@link Entity} with {@link Insert @Insert}. In that case, any value assigned in
     * Java/Kotlin will be used. Use {@link Query @Query} with an <code>INSERT</code> statement
     * and skip this column there in order to use this default value.
     * </p>
     * <p>
     * NULL, CURRENT_TIMESTAMP and other SQLite constant values are interpreted as such. If you want
     * to use them as strings for some reason, surround them with single-quotes.
     * </p>
     * <pre>
     *   {@literal @}ColumnInfo(defaultValue = "NULL")
     *   {@literal @}Nullable
     *   public String description;
     *
     *   {@literal @}ColumnInfo(defaultValue = "'NULL'")
     *   {@literal @}NonNull
     *   public String name;
     * </pre>
     * <p>
     * You can also use constant expressions by surrounding them with parentheses.
     * </p>
     * <pre>
     *   {@literal @}CoumnInfo(defaultValue = "('Created at' || CURRENT_TIMESTAMP)")
     *   public String notice;
     * </pre>
     *
     * @return The default value for this column.
     * @see #VALUE_UNSPECIFIED
     */
    String defaultValue() default VALUE_UNSPECIFIED;

    /**
     * The SQLite column type constants that can be used in {@link #typeAffinity()}
     */
    @IntDef({UNDEFINED, TEXT, INTEGER, REAL, BLOB})
    @Retention(RetentionPolicy.CLASS)
    @interface SQLiteTypeAffinity {
    }

    @IntDef({UNSPECIFIED, BINARY, NOCASE, RTRIM, LOCALIZED, UNICODE})
    @Retention(RetentionPolicy.CLASS)
    @interface Collate {
    }
}
