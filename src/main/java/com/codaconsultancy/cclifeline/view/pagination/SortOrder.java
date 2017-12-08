package com.codaconsultancy.cclifeline.view.pagination;

public enum SortOrder {

    ASC("ASC"),
    DESC("DESC");

    private final String value;

    SortOrder(String v) {
        value = v;
    }

    public static SortOrder fromValue(String v) {
        for (SortOrder c : SortOrder.values()) {
            if (c.name().equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
