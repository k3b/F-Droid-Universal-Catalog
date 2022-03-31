package org.fdroid.model;

public class PojoCommon {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append('[');
        toString(sb);
        // Formatter.add(sb, "mirrors", this.mirrors);
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    protected void toString(StringBuilder sb) {
    }
}
