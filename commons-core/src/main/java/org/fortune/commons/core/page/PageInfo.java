package org.fortune.commons.core.page;


import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * 分页信息DTO对象
 */
public class PageInfo {
	/**
	 * 总记录数
	 */
	private int totalCount = 0;

	/**
	 * 每页记录数默认10
	 */
	private int pageSize = 10;

	/**
	 * 总页数
	 */
	private int pageCount;

	/**
	 * 当前页
	 */
	private int pageIndex = 1;
	
	/**
	 * 排序字段
	 */
	private String sortField;
	
	/**
	 * 排序方向,ASC 升序，DESC降序
	 */
	private String sortDirect;

	/**
	 * 数据列表
	 */
	private List dataList;

    /**
     * defaultSortField
     * 默认按第一列排序
     */
    private String defaultSortField="1";

    /**
     * defaultSortDirect
     * 默认降序排列
     */
    private String defaultSortDirect="DESC";

	/**
	 * 默认分页记录数常量
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	public PageInfo(){
	
	}

	/**
	 * 根据分页记录数创建对象
	 * @param pageSize 分页记录数
	 */
	public PageInfo(int pageSize) {
		setPageSize(pageSize);
	}

	/**
	 * 根据分页记录数和当前页码创建对象
	 * @param pageSize 分页记录数
	 * @param pageIndex 页码
	 */
	public PageInfo(int pageSize, int pageIndex) {
		setPageSize(pageSize);
		setPageIndex(pageIndex);
	}

	/**
	 * 根据分页记录数、当前页码和总记录数创建对象
	 * @param pageSize 分页记录数
	 * @param pageIndex 页码
	 * @param totalCount 总记录数
	 */
	public PageInfo(int pageSize, int pageIndex, int totalCount) {
		setPageSize(pageSize);
		setPageIndex(pageIndex);
		setTotalCount(totalCount);
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
		if(this.totalCount==-1){
		    int curPageRowCount=0;
		    if(this.dataList!=null){
		        curPageRowCount=this.dataList.size();
		    }
		    //最后一页，如果没有计算总数的情况下自动计算总数。
		    if(this.pageSize>curPageRowCount){
		      setTotalCount((this.pageIndex-1)*this.pageSize+curPageRowCount);
		    }
		}
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
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

	/**
	 * 设置每页记录数，如果已经有总记录数则计算页数。
	 * 分页记录数不能为0，为0则采用默认分页记录数
	 * @param pageSize pageSize
	 * 
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		if (pageSize == 0){
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
		if (this.totalCount > 0) {
			this.pageCount = this.totalCount / this.pageSize;
			if ((this.totalCount % this.pageSize) > 0) {
				this.pageCount += 1;
			}
		}

	}

	/**
	 * 返回总页数
	 * getTotalCount
	 * @return 总页数
	 * 
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总页数
	 * 
	 * @param totalCount totalCount
	 * 
	 */
	public void setTotalCount(int totalCount) {
		if (totalCount == 0) {
			this.totalCount = 0;
			this.pageCount = 0;
		} else {
			this.totalCount = totalCount;
			if (pageSize == 0){
				this.pageSize = DEFAULT_PAGE_SIZE;
			}
			this.pageCount = this.totalCount / this.pageSize;
			if ((this.totalCount % this.pageSize) > 0) {
				this.pageCount += 1;
			}
		}
	}

    /**
     * getSortField
     * @return 如果没有指定排序字段则返回默认排序字段
     * 
     */
    public String getSortField() {
        if(sortField==null||"".equals(sortField)){
            return this.defaultSortField;
        }
        return sortField;
    }

    public void setSortField(String sortField) {
        //TODO 验证排序字段，防止sql注入
        this.sortField = sortField;
    }

    /**
     * getSortDirect
     * @return 如果没有指定排序方向，则返回默认排序方向
     * 
     */
    public String getSortDirect() {
        if(sortDirect==null||"".equals(sortDirect)){
            return this.defaultSortDirect;
        }
        return sortDirect;
    }

    public void setSortDirect(String sortDirect) {
        if(sortDirect!=null && !sortDirect.equals("")){
            String sortDir = sortDirect.toLowerCase();
            Validate.isTrue(sortDir.equals("asc") || sortDir.equals("desc"), "排序方向必须为 ASC 或者DESC 。");
        }
        this.sortDirect = sortDirect;
    }
    
    public void setDefaultSortField(String sortField){
        //TODO 校验排序字段合法性
        
        this.defaultSortField=sortField;
    }
    
    public void setDefaultSortDirect(String sortDirect){
        if(sortDirect!=null && !sortDirect.equals("")){
            String sortDir = sortDirect.toLowerCase();
            Validate.isTrue(sortDir.equals("asc") || sortDir.equals("desc"), "排序方向必须为 ASC 或者DESC 。");
        }
        this.defaultSortDirect=sortDirect;
    }

}
