
package org.fdroid.v1Ex.model.deprecated;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.fdroid.v1Ex.model.Repo;

@Generated("jsonschema2pojo")
public class Index {

    @SerializedName("repo")
    @Expose
    private Repo repo;
    @SerializedName("apps")
    @Expose
    private List<App> apps = null;
    @SerializedName("packages")
    @Expose
    private List<Package> packages = null;

    public Repo getRepo() {
        return repo;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Index.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("repo");
        sb.append('=');
        sb.append(((this.repo == null)?"<null>":this.repo));
        sb.append(',');
        sb.append("apps");
        sb.append('=');
        sb.append(((this.apps == null)?"<null>":this.apps));
        sb.append(',');
        sb.append("packages");
        sb.append('=');
        sb.append(((this.packages == null)?"<null>":this.packages));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
