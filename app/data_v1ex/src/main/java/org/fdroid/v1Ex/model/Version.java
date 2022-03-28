
package org.fdroid.v1Ex.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Version {

    @SerializedName("added")
    @Expose
    private long added;
    @SerializedName("apkName")
    @Expose
    private String apkName;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("hashType")
    @Expose
    private String hashType;
    @SerializedName("minSdkVersion")
    @Expose
    private long minSdkVersion;
    @SerializedName("sig")
    @Expose
    private String sig;
    @SerializedName("signer")
    @Expose
    private String signer;
    @SerializedName("size")
    @Expose
    private long size;
    @SerializedName("srcname")
    @Expose
    private String srcname;
    @SerializedName("targetSdkVersion")
    @Expose
    private long targetSdkVersion;
    @SerializedName("versionCode")
    @Expose
    private long versionCode;
    @SerializedName("versionName")
    @Expose
    private String versionName;
    @SerializedName("nativecode")
    @Expose
    private List<String> nativecode = null;

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType) {
        this.hashType = hashType;
    }

    public long getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(long minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSrcname() {
        return srcname;
    }

    public void setSrcname(String srcname) {
        this.srcname = srcname;
    }

    public long getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(long targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public List<String> getNativecode() {
        return nativecode;
    }

    public void setNativecode(List<String> nativecode) {
        this.nativecode = nativecode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Version.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("added");
        sb.append('=');
        sb.append(this.added);
        sb.append(',');
        sb.append("apkName");
        sb.append('=');
        sb.append(((this.apkName == null)?"<null>":this.apkName));
        sb.append(',');
        sb.append("hash");
        sb.append('=');
        sb.append(((this.hash == null)?"<null>":this.hash));
        sb.append(',');
        sb.append("hashType");
        sb.append('=');
        sb.append(((this.hashType == null)?"<null>":this.hashType));
        sb.append(',');
        sb.append("minSdkVersion");
        sb.append('=');
        sb.append(this.minSdkVersion);
        sb.append(',');
        sb.append("sig");
        sb.append('=');
        sb.append(((this.sig == null)?"<null>":this.sig));
        sb.append(',');
        sb.append("signer");
        sb.append('=');
        sb.append(((this.signer == null)?"<null>":this.signer));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(this.size);
        sb.append(',');
        sb.append("srcname");
        sb.append('=');
        sb.append(((this.srcname == null)?"<null>":this.srcname));
        sb.append(',');
        sb.append("targetSdkVersion");
        sb.append('=');
        sb.append(this.targetSdkVersion);
        sb.append(',');
        sb.append("versionCode");
        sb.append('=');
        sb.append(this.versionCode);
        sb.append(',');
        sb.append("versionName");
        sb.append('=');
        sb.append(((this.versionName == null)?"<null>":this.versionName));
        sb.append(',');
        sb.append("nativecode");
        sb.append('=');
        sb.append(((this.nativecode == null)?"<null>":this.nativecode));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
