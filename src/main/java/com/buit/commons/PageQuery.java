package com.buit.commons;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.annotations.ApiModelProperty;

/**
* @ClassName: PageQuery
* @Description: 分页信息基础类 <br>
* @author 神算子
* @date 2020年4月26日 下午3:39:16
 */
public class PageQuery  implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="当前页码数")
	@JsonProperty(access = Access.WRITE_ONLY)
	private int pageNum=1;
	@ApiModelProperty(value="每页多少行")
	@JsonProperty(access = Access.WRITE_ONLY)
	private int pageSize=10;
	@ApiModelProperty(value="排序列名",hidden = true)
	@JsonProperty(access = Access.WRITE_ONLY)  
	private String  sortColumns;
	@ApiModelProperty(value="排序状态",hidden = true)
	private String  sortType;
	
	public String getSortColumns() {
		return sortColumns;
	}
	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}


	@Override
	public String toString() {
		return "PageQuery{" +
				"pageNum=" + pageNum +
				", pageSize=" + pageSize +
				", sortColumns='" + sortColumns + '\'' +
				", sortType='" + sortType + '\'' +
				'}';
	}
}