package fr.ulity.core_v3.utils;

public class Version implements Comparable<Version> {
    private final String version;

    public Version(String version) {
        if (version == null)
            throw new IllegalArgumentException("Version can not be null");
        if (!version.matches("[0-9]+(\\.[0-9]+)*"))
            throw new IllegalArgumentException("Invalid version format");
        this.version = version;
    }

    public final String get() {
        return this.version;
    }

    public int compareTo(Version that) {
        if (that == null)
            return 1;
        String[] thisParts = get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {

            int thisPart = (i < thisParts.length) ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = (i < thatParts.length) ? Integer.parseInt(thatParts[i]) : 0;

            if (thisPart < thatPart) return -1;
            else if (thisPart > thatPart) return 1;
        }
        return 0;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        return (compareTo((Version) that) == 0);
    }
}
