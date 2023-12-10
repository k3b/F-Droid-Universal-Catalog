/*
 * Copyright (c) 2022-2023 by k3b.
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
package de.k3b.fdroid.domain.entity;

import static de.k3b.fdroid.domain.service.LocalizedService.SEPERATOR_DESCRIPTION;
import static de.k3b.fdroid.domain.service.LocalizedService.SEPERATOR_NAME;
import static de.k3b.fdroid.domain.service.LocalizedService.SEPERATOR_SUMMARY;
import static de.k3b.fdroid.domain.service.LocalizedService.SEPERATOR_WHATS_NEW;
import static de.k3b.fdroid.domain.service.LocalizedService.createLocalePrefixes;
import static de.k3b.fdroid.domain.service.VersionService.SEPERATOR_MIN_MAX;
import static de.k3b.fdroid.domain.util.StringUtil.getLast;

import androidx.room.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.interfaces.IAppDetail;
import de.k3b.fdroid.domain.service.LocalizedService;
import de.k3b.fdroid.domain.util.StringUtil;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Information about an Android App.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = Repo.class,
        parentColumns = "id", childColumns = "resourceRepoId", onDelete = ForeignKey.SET_NULL)},
        indices = {@androidx.room.Index("id"), @androidx.room.Index({"packageName"})}
)
@javax.persistence.Entity
@javax.persistence.Table(name = "App")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
@ExternalDocumentation(description = "Information about an Android [App] that is available in a Repo or Mirror",
        url = ExtDoc.GLOSSAR_URL + "App")
public class App extends AppCommon implements IAppDetail {
    public static final String NOT_FOUND_VALUE = "";

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private Integer resourceRepoId;

    @Column(length = MAX_LEN_AGGREGATED)
    @JsonIgnore
    /** all different locale name values concatenated for faster search */
    private String searchName;

    @Column(length = MAX_LEN_AGGREGATED)
    @JsonIgnore
    /** all different locale summary values concatenated for faster search */
    private String searchSummary;

    @Column(length = MAX_LEN_AGGREGATED_DESCRIPTION)
    @JsonIgnore
    /** all different locale name description concatenated for faster search */
    private String searchDescription;

    @Column(length = MAX_LEN_AGGREGATED)
    @JsonIgnore
    /** all different locale whatsNew values concatenated for faster search */
    private String searchWhatsNew;

    @Schema(description = "Range of available [Version]-s of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Version"),
            example = "0.8.0.191021(44) - 0.8.3.200315(47)")
    private String searchVersion;
    @Schema(description = "Range of available MinSdk-s (= technical term for device compatibility) of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"),
            example = "[14,21,28] - [14,21,28]")
    private String searchSdk;
    @Column(length = MAX_LEN_AGGREGATED)
    private String searchSigner;

    @JsonIgnore
    @Column(length = MAX_LEN_AGGREGATED)
    private String searchCategory;

    @JsonIgnore
    @androidx.room.Ignore
    @javax.persistence.Transient
    private AppSearchParameter appSearchParameter;

    // needed by android-room and jpa
    public App() {
    }

    @androidx.room.Ignore
    public App(String packageName) {
        setPackageName(packageName);
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "resourceRepoId", resourceRepoId);
        super.toStringBuilder(sb);

        toStringBuilder(sb, "searchVersion", this.searchVersion);
        toStringBuilder(sb, "searchSdk", this.searchSdk);
        toStringBuilder(sb, "searchCategory", this.searchCategory);

        toStringBuilder(sb, "searchName", this.searchName, 20);
        toStringBuilder(sb, "searchSummary", this.searchSummary, 20);
        toStringBuilder(sb, "searchDescription", this.searchDescription, 20);
        toStringBuilder(sb, "searchWhatsNew", this.searchWhatsNew, 20);

        toStringBuilder(sb, "searchSigner", this.searchSigner, 14);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getAppId() {
        return getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = maxlen(searchName, MAX_LEN_AGGREGATED);
    }

    public String getSearchSummary() {
        return searchSummary;
    }

    public void setSearchSummary(String searchSummary) {
        this.searchSummary = maxlen(searchSummary, MAX_LEN_AGGREGATED);
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = maxlen(searchDescription, MAX_LEN_AGGREGATED_DESCRIPTION);
    }

    public String getSearchWhatsNew() {
        return searchWhatsNew;
    }

    public void setSearchWhatsNew(String searchWhatsNew) {
        this.searchWhatsNew = maxlen(searchWhatsNew, MAX_LEN_AGGREGATED);
    }

    public String getSearchVersion() {
        return searchVersion;
    }

    public void setSearchVersion(String searchVersion) {
        this.searchVersion = maxlen(searchVersion);
    }

    public String getSearchSdk() {
        return searchSdk;
    }

    public void setSearchSdk(String searchSdk) {
        this.searchSdk = maxlen(searchSdk);
    }

    public String getSearchSigner() {
        return searchSigner;
    }

    public void setSearchSigner(String searchSigner) {
        this.searchSigner = maxlen(searchSigner, MAX_LEN_AGGREGATED);
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = maxlen(searchCategory, MAX_LEN_AGGREGATED);
    }

    private String getFromSearchText(String searchText, String seperator, String notFoundValue) {
        String[] locales = (this.appSearchParameter == null) ? null : this.appSearchParameter.locales;
        if (locales != null && locales.length > 0) {
            String[] localePrefixes = this.appSearchParameter.localePrefixes;
            if (localePrefixes == null) {
                this.appSearchParameter.localePrefixes = localePrefixes = createLocalePrefixes(locales);
            }
            for (String prefix : localePrefixes) {
                String found = LocalizedService.removeLocalePrefix(
                        StringUtil.getFirstWithPrefix(searchText, prefix, seperator, null));
                if (found != null) {
                    return found;
                }
            }
        }
        return LocalizedService.removeLocalePrefix(StringUtil.getFirst(searchText, seperator, notFoundValue));
    }

    @Schema(description = "[App] name calculated from chosen locales/languages.",
            example = "A Photo Manager (Manejador de fotos)")
    public String getLocalizedName() {
        return getFromSearchText(searchName, SEPERATOR_NAME, getPackageName());
    }

    @Schema(description = "Description summary of the [App] calculated from chosen locales/languages.",
            example = "Verwalte lokale Photos: Suchen/Kopieren/Exif bearbeiten/Gallerie/Landkarte.")
    public String getLocalizedSummary() {
        return getFromSearchText(searchSummary, SEPERATOR_SUMMARY, NOT_FOUND_VALUE);
    }

    @Schema(description = "Description of the [App] calculated from chosen locales/languages.",
            example = "Merkmale: Schnelle Bildsuche per Tags(Suchbegriffe), ...")
    public String getLocalizedDescription() {
        return getFromSearchText(searchDescription, SEPERATOR_DESCRIPTION, NOT_FOUND_VALUE);
    }

    @Schema(description = "'Whats New'-info calculated from chosen locales/languages.",
            example = "#168: Bugfix crash in ...")
    public String getLocalizedWhatsNew() {
        return getFromSearchText(searchWhatsNew, SEPERATOR_WHATS_NEW, NOT_FOUND_VALUE);
    }

    @Schema(description = "Most recent (stable) Version of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Version"),
            example = "0.8.3.200315(47)")
    public String getVersion() {
        return getLast(searchVersion, SEPERATOR_MIN_MAX, NOT_FOUND_VALUE);
    }

    @Schema(description = "Most recent (stable) minSdk  (= technical term for device compatibility) of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "minSdk"),
            example = "0.8.3.200315(47)")
    public String getSdk() {
        return getLast(searchSdk, SEPERATOR_MIN_MAX, NOT_FOUND_VALUE);
    }

    /**
     * Database FK to {@link Repo#getId()} : Repo-Server where {@link #getIcon()},
     * {@link Localized#getPhoneScreenshots()}
     * can be downloaded from.
     */
    public Integer getResourceRepoId() {
        return resourceRepoId;
    }

    public void setResourceRepoId(Integer resourceRepoId) {
        this.resourceRepoId = resourceRepoId;
    }

    public AppSearchParameter getAppSearchParameter() {
        return appSearchParameter;
    }

    public void setAppSearchParameter(AppSearchParameter appSearchParameter) {
        this.appSearchParameter = appSearchParameter;
    }
}
