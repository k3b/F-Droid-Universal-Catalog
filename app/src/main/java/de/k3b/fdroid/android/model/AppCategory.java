package de.k3b.fdroid.android.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"appId", "categoryId"}, foreignKeys = {
        @ForeignKey(entity = App.class,parentColumns = "id",childColumns = "appId"),
        @ForeignKey(entity = Category.class,parentColumns = "id",childColumns = "categoryId")},
        indices = {@Index("appId"), @Index("categoryId")})
public class AppCategory {
    @NonNull public final Integer appId;
    @NonNull public final Integer categoryId;

    public AppCategory(@NonNull Integer appId, @NonNull Integer categoryId) {
        this.appId = appId;
        this.categoryId = categoryId;
    }
}
