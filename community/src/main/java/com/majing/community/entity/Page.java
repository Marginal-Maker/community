package com.majing.community.entity;

/**
 * @author majing
 * @date 2023-08-03 10:11
 * @Description 分页组件相关的属性
 */
public class Page {
    private Integer current;
    private Integer limit;
    private Integer rows;
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public Integer getPageNum(){
        if(rows % limit == 0){
            return rows / limit;
        }
        else {
            return rows / limit + 1;
        }
    }
    public Integer getFrom(){
        return Math.max(current - 2, 1);
    }
    public Integer getTo(){
        return Math.min(getPageNum(), current + 2);
    }
    public Integer getOffSet(){
        return (current - 1) * limit;
    }

}
