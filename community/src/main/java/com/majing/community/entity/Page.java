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
    /**
     * 获取总页数，如果余数不为0，则+1
     * @param
     * @return java.lang.Integer
     * @created at 2023/9/11 16:38
    */
    public Integer getPageNum(){
        if(rows % limit == 0){
            return rows / limit;
        }
        else {
            return rows / limit + 1;
        }
    }
    /**
     * 页面列表的起始位置，一般展示5页，不足时从首页开始
     * @param
     * @return java.lang.Integer
     * @created at 2023/9/11 16:40
    */
    public Integer getFrom(){
        return Math.max(current - 2, 1);
    }
    /**
     * 页面列表的结束位置，一般展示5页，不足时以尾页结尾
     * @param
     * @return java.lang.Integer
     * @created at 2023/9/11 16:41
    */
    public Integer getTo(){
        return Math.min(getPageNum(), current + 2);
    }
    /**
     * 求每一页的数据的起始位置-1，数据库limit(m, n)查询m+1-->n
     * @param
     * @return java.lang.Integer
     * @created at 2023/9/11 16:42
    */
    public Integer getOffSet(){
        return (current - 1) * limit;
    }

}
