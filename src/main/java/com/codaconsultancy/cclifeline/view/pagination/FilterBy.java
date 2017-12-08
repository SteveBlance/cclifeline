package com.codaconsultancy.cclifeline.view.pagination;

import java.util.HashMap;
import java.util.Map;

public class FilterBy {

    private Map<String, String> mapOfFilters;
    private boolean globalSearch;

    public FilterBy() {
        if (null == mapOfFilters) {
            mapOfFilters = new HashMap<>();
        }
    }

    public Map<String, String> getMapOfFilters() {
        return mapOfFilters;
    }

    public void setMapOfFilters(Map<String, String> mapOfFilters) {
        this.mapOfFilters = mapOfFilters;
    }

    public void addFilter(String filterColumn, String filterValue) {
        mapOfFilters.put(filterColumn, filterValue);
    }

    public boolean isGlobalSearch() {
        return globalSearch;
    }

    public void setGlobalSearch(boolean globalSearch) {
        this.globalSearch = globalSearch;
    }

}

