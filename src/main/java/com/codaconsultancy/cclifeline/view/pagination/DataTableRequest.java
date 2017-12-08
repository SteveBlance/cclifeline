package com.codaconsultancy.cclifeline.view.pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTableRequest<T> {

    private String uniqueId;
    private String draw;
    private Integer start;
    private Integer length;
    private String search;
    private boolean regex;
    private List<DataTableColumnSpecs> columns;
    private DataTableColumnSpecs order;
    private boolean isGlobalSearch;
    private int maxParamsToCheck = 0;

    public DataTableRequest(HttpServletRequest request) {
        prepareDataTableRequest(request);
    }


    private void prepareDataTableRequest(HttpServletRequest request) {

        Enumeration<String> parameterNames = request.getParameterNames();

        if (parameterNames.hasMoreElements()) {

            this.setStart(Integer.parseInt(request.getParameter(PaginationCriteria.PAGE_NO)));
            this.setLength(Integer.parseInt(request.getParameter(PaginationCriteria.PAGE_SIZE)));
            this.setUniqueId(request.getParameter("_"));
            this.setDraw(request.getParameter(PaginationCriteria.DRAW));

            this.setSearch(request.getParameter("search[value]"));
            this.setRegex(Boolean.valueOf(request.getParameter("search[regex]")));

            int sortableCol = Integer.parseInt(request.getParameter("order[0][column]"));

            List<DataTableColumnSpecs> columns = new ArrayList<DataTableColumnSpecs>();

            if (!AppUtil.isObjectEmpty(this.getSearch())) {
                this.setGlobalSearch(true);
            }

            maxParamsToCheck = getNumberOfColumns(request);

            for (int i = 0; i < maxParamsToCheck; i++) {
                if (null != request.getParameter("columns[" + i + "][data]")
                        && !"null".equalsIgnoreCase(request.getParameter("columns[" + i + "][data]"))
                        && !AppUtil.isObjectEmpty(request.getParameter("columns[" + i + "][data]"))) {
                    DataTableColumnSpecs colSpec = new DataTableColumnSpecs(request, i);
                    if (i == sortableCol) {
                        this.setOrder(colSpec);
                    }
                    columns.add(colSpec);

                    if (!AppUtil.isObjectEmpty(colSpec.getSearch())) {
                        this.setGlobalSearch(false);
                    }
                }
            }

            if (!AppUtil.isObjectEmpty(columns)) {
                this.setColumns(columns);
            }
        }
    }

    private int getNumberOfColumns(HttpServletRequest request) {
        Pattern p = Pattern.compile("columns\\[[0-9]+\\]\\[data\\]");
        @SuppressWarnings("rawtypes")
        Enumeration params = request.getParameterNames();
        List<String> lstOfParams = new ArrayList<String>();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            Matcher m = p.matcher(paramName);
            if (m.matches()) {
                lstOfParams.add(paramName);
            }
        }
        return lstOfParams.size();
    }

    public PaginationCriteria getPaginationRequest() {

        PaginationCriteria pagination = new PaginationCriteria();
        pagination.setPageNumber(this.getStart());
        pagination.setPageSize(this.getLength());

        SortBy sortBy = null;
        if (!AppUtil.isObjectEmpty(this.getOrder())) {
            sortBy = new SortBy();
            sortBy.addSort(this.getOrder().getData(), SortOrder.fromValue(this.getOrder().getSortDir()));
        }

        FilterBy filterBy = new FilterBy();
        filterBy.setGlobalSearch(this.isGlobalSearch());
        for (DataTableColumnSpecs colSpec : this.getColumns()) {
            if (colSpec.isSearchable()) {
                if (!AppUtil.isObjectEmpty(this.getSearch()) || !AppUtil.isObjectEmpty(colSpec.getSearch())) {
                    filterBy.addFilter(colSpec.getData(), (this.isGlobalSearch()) ? this.getSearch() : colSpec.getSearch());
                }
            }
        }

        pagination.setSortBy(sortBy);
        pagination.setFilterBy(filterBy);

        return pagination;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public List<DataTableColumnSpecs> getColumns() {
        return columns;
    }

    public void setColumns(List<DataTableColumnSpecs> columns) {
        this.columns = columns;
    }

    public DataTableColumnSpecs getOrder() {
        return order;
    }

    public void setOrder(DataTableColumnSpecs order) {
        this.order = order;
    }

    public boolean isGlobalSearch() {
        return isGlobalSearch;
    }

    public void setGlobalSearch(boolean globalSearch) {
        isGlobalSearch = globalSearch;
    }

    public int getMaxParamsToCheck() {
        return maxParamsToCheck;
    }

    public void setMaxParamsToCheck(int maxParamsToCheck) {
        this.maxParamsToCheck = maxParamsToCheck;
    }
}
