package com.hugh.base.service.web;

import java.util.List;

public class PageResult<T> {
    private long total;
    private List<T> items;
    public PageResult(long total, List<T> items){ this.total=total; this.items=items; }
    public long getTotal(){ return total; } public List<T> getItems(){ return items; }
}
