package de.k3b.fdroid.domain;

import javax.persistence.Column;

/**
 * Same as App but with more search related Properties
 */
@androidx.room.Entity(tableName = "App", foreignKeys = @androidx.room.ForeignKey(entity = Repo.class,
        parentColumns = "id", childColumns = "repoId",
        onDelete = androidx.room.ForeignKey.CASCADE),
        indices = {@androidx.room.Index("id"), @androidx.room.Index({"repoId", "packageName"})}
)

@javax.persistence.Entity
@javax.persistence.Table(name = "App")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class AppForSearch extends App {
    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale name values concatenated for faster search */
    private String searchName;

    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale summary values concatenated for faster search */
    private String searchSummary;

    @Column(length = MAX_LEN_AGGREGATED_DESCRIPTION)
    /** all different locale name description concatenated for faster search */
    private String searchDescription;

    @Column(length = MAX_LEN_AGGREGATED)
    /** all different locale whatsNew values concatenated for faster search */
    private String searchWhatsNew;

    private String searchVersion;
    private String searchSdk;
    private String searchSigner;
    private String searchCategory;

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchSummary() {
        return searchSummary;
    }

    public void setSearchSummary(String searchSummary) {
        this.searchSummary = searchSummary;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public String getSearchWhatsNew() {
        return searchWhatsNew;
    }

    public void setSearchWhatsNew(String searchWhatsNew) {
        this.searchWhatsNew = searchWhatsNew;
    }

    public String getSearchVersion() {
        return searchVersion;
    }

    public void setSearchVersion(String searchVersion) {
        this.searchVersion = searchVersion;
    }

    public String getSearchSdk() {
        return searchSdk;
    }

    public void setSearchSdk(String searchSdk) {
        this.searchSdk = searchSdk;
    }

    public String getSearchSigner() {
        return searchSigner;
    }

    public void setSearchSigner(String searchSigner) {
        this.searchSigner = searchSigner;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }
}
