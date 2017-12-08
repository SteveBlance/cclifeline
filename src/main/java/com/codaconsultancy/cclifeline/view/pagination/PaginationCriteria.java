package com.codaconsultancy.cclifeline.view.pagination;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class PaginationCriteria {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalRecords;
    private SortBy sortBy;
    private FilterBy filterBy;

    private static final String BLANK = "";
    private static final String SPACE = " ";
    private static final String LIKE_PREFIX = " LIKE '%";
    private static final String LIKE_SUFFIX = "%' ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String BRKT_OPN = " ( ";
    private static final String BRKT_CLS = " ) ";
    private static final String COMMA = " , ";
    public static final String PAGE_NO = "start";
    public static final String PAGE_SIZE = "length";
    public static final String DRAW = "draw";

    public Integer getPageNumber() {
        return (null == pageNumber) ? 0 : pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return (null == pageSize) ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public FilterBy getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(FilterBy filterBy) {
        this.filterBy = filterBy;
    }

    public boolean isFilterByEmpty() {
        if (null == filterBy || null == filterBy.getMapOfFilters() || filterBy.getMapOfFilters().size() == 0) {
            return true;
        }
        return false;
    }

    public boolean isSortByEmpty() {
        if (null == sortBy || null == sortBy.getSortBys() || sortBy.getSortBys().size() == 0) {
            return true;
        }
        return false;
    }

    public String getFilterByClause() {

        StringBuilder fbsb = null;

        if (!isFilterByEmpty()) {
            Iterator<Entry<String, String>> fbit = filterBy.getMapOfFilters().entrySet().iterator();

            while (fbit.hasNext()) {

                Map.Entry<String, String> pair = fbit.next();

                if (null == fbsb) {
                    fbsb = new StringBuilder();
                    fbsb.append(BRKT_OPN);

                    fbsb.append(SPACE)
                            .append(BRKT_OPN)
                            .append(pair.getKey())
                            .append(LIKE_PREFIX)
                            .append(pair.getValue())
                            .append(LIKE_SUFFIX)
                            .append(BRKT_CLS);

                } else {

                    fbsb.append(filterBy.isGlobalSearch() ? OR : AND)
                            .append(BRKT_OPN)
                            .append(pair.getKey())
                            .append(LIKE_PREFIX)
                            .append(pair.getValue())
                            .append(LIKE_SUFFIX)
                            .append(BRKT_CLS);

                }
            }
            fbsb.append(BRKT_CLS);
        }

        return (null == fbsb) ? BLANK : fbsb.toString();
    }

    public String getOrderByClause() {

        StringBuilder sbsb = null;

        if (!isSortByEmpty()) {
            Iterator<Entry<String, SortOrder>> sbit = sortBy.getSortBys().entrySet().iterator();

            while (sbit.hasNext()) {
                Map.Entry<String, SortOrder> pair = sbit.next();
                if (null == sbsb) {
                    sbsb = new StringBuilder();
                    sbsb.append(ORDER_BY).append(pair.getKey()).append(SPACE).append(pair.getValue());
                } else {
                    sbsb.append(COMMA).append(pair.getKey()).append(SPACE).append(pair.getValue());
                }
            }
        }

        return (null == sbsb) ? BLANK : sbsb.toString();
    }


}
