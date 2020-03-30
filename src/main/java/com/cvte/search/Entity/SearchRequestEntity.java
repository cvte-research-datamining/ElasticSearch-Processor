package com.cvte.search.Entity;

public class SearchRequestEntity {
	
	/**
	 * 索引
	 */
	private String[] index;
	
	/**
	 * 查询字段定义
	 */
	private String[] queryFieldArray;
	
	/**
	 * 高亮字段定义
	 */
	private String[] highLightFieldArray;
	
	/**
	 * 返回字段定义
	 */
	private String[] fieldListArray;
	
	/**
	 * 返回过滤字段定义
	 */
	private String[] excludeFields;

	public String[] getIndex() {
		return index;
	}

	public void setIndex(String[] index) {
		this.index = index;
	}

	public String[] getQueryFieldArray() {
		return queryFieldArray;
	}

	public void setQueryFieldArray(String[] queryFieldArray) {
		this.queryFieldArray = queryFieldArray;
	}

	public String[] getHighLightFieldArray() {
		return highLightFieldArray;
	}

	public void setHighLightFieldArray(String[] highLightFieldArray) {
		this.highLightFieldArray = highLightFieldArray;
	}

	public String[] getFieldListArray() {
		return fieldListArray;
	}

	public void setFieldListArray(String[] fieldListArray) {
		this.fieldListArray = fieldListArray;
	}

	public String[] getExcludeFields() {
		return excludeFields;
	}

	public void setExcludeFields(String[] excludeFields) {
		this.excludeFields = excludeFields;
	}
	
	public SearchRequestEntity(String[] index,
			String[] queryFieldArray,
			String[] highLightFieldArray,
			String[] fieldListArray,
			String[] excludeFields) {
		this.index = index;
		this.queryFieldArray = queryFieldArray;
		this.highLightFieldArray = highLightFieldArray;
		this.fieldListArray = fieldListArray;
		this.excludeFields = excludeFields;
	}

}
