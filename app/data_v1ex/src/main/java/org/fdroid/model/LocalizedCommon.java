package org.fdroid.model;

import org.fdroid.util.Formatter;

public class LocalizedCommon extends PojoCommon {
    private String name;
    private String summary;
    private String description;
    private String icon;
    private String video;
    private String whatsNew;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    protected void toString(StringBuilder sb) {
        super.toString(sb);
        Formatter.add(sb, "name", this.name);
        Formatter.add(sb, "summary", this.summary);
        Formatter.add(sb, "description", this.description);
        Formatter.add(sb, "icon", this.icon);
        Formatter.add(sb, "video", this.video);
        Formatter.add(sb, "whatsNew", this.whatsNew);
    }

}
