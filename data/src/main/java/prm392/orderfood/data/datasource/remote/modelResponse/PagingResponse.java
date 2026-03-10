package prm392.orderfood.data.datasource.remote.modelResponse;

import java.util.List;

public class PagingResponse<T> {
    private List<T> items;
    private int totalItems;
    private int pageIndex;
    private int pageSize;

    public PagingResponse() {
    }

    public PagingResponse(List<T> items, int totalItems, int pageIndex, int pageSize) {
        this.items = items;
        this.totalItems = totalItems;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}