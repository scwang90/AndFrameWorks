package com.andframe.processor.model;

public class IdQualified {

    public final int id;
    public final String packageName;

    public IdQualified(String packageName, int id) {
        this.id = id;
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "IdQualified{packageName='" + packageName + "', id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdQualified)) return false;
        IdQualified other = (IdQualified) o;
        return id == other.id
                && packageName.equals(other.packageName);
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + id;
        return result;
    }
}
