package com.ontheway.bean;

/**
 * 分页查询类 
 * 分页查询开始索引、开始分页等从 0 开始索引
 * @author Administrator
 *
 */
public class Page {
    public boolean IsASC = true;
    public String Order = "";
    public int FirstResult = 0;
    public int MaxResult = 0;
    public static final int PAGEMODE_PAGING = 1;
    public static final int PAGEMODE_ADDITIAONAL = 2;
    

    public Page(){}

    /**
     * 构造一个新的分页查询示例索引是从0开始
     * 
     * @param sizeOrmax 分页大小 或 追加查询大小
     * @param curOrbegin 分页起始页 或 追加起始页
     * 默认 追加：PAGEMODE_ADDITIAONAL 
     */
    public Page(int max,int begin)
    {
        this.FirstResult = begin;
        this.MaxResult = max;
    }
    /**
     * 构造一个新的分页查询示例索引是从0开始
     * 
     * @param sizeOrmax 分页大小 或 追加查询大小
     * @param curOrbegin 分页起始页 或 追加起始页
     * @param order 排序列
     * 默认 追加：PAGEMODE_ADDITIAONAL 
     */
    public Page(int max,int begin,String order)
    {
    	this.Order = order;
        this.FirstResult = begin;
        this.MaxResult = max;
    }
    /**
     * 构造一个新的分页查询示例
     * @param sizeOrmax 分页大小 或 追加查询大小
     * @param curOrbegin 分页起始页 或 追加起始页
     * @param order 排序列
     * @param isAsc 是否升序
     */
    public Page(int max,int begin,String order,boolean isAsc)
    {
    	this.Order = order;
        this.FirstResult = begin;
        this.MaxResult = max;
    	this.IsASC = isAsc;
    }
    /**
     * 构造一个新的分页查询示例
     * 
     * @param order 排序列
     * @param isAsc 是否升序
     */
    public Page(String order,String asc)
    {
    	if(order != null && !order.equals("")){
        	this.Order = order;
        	this.IsASC = "asc".equals(asc)||"ASC".equals(asc);
    	}
    }

    /**
     * 
     * @Description: 转换成单页条数
     * @Version: V1.0, 2015-2-2 下午5:02:53
     * @return
     */
    public int getPageSize(){
    	return MaxResult;
    }
    /**
     * @Description: 转换成当前页码
     * @Version: V1.0, 2015-2-2 下午5:02:17
     * @return （从0开始）
     */
    public int getcurrentPage(){
    	return FirstResult / MaxResult;
    }

    public String getOrder() {
    	if(Order == null){
    		return "";
    	}
		return Order;
	}
    
    public String getAsc(){
    	if(Order == null || Order.equals("")){
    		return "";
    	}
		return IsASC?"asc":"desc";
    }

	public String toOrderString() {
		// TODO Auto-generated method stub
    	if(Order == null || Order.equals("")){
    		return "";
    	}
		return " order by " + Order + (IsASC?" asc":" desc");
	}
}
