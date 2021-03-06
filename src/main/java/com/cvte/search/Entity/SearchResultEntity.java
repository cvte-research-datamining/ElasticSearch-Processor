package com.cvte.search.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SearchResultEntity {
	/**
     * 命中总数
     */
    public Long totalHits;

    /**
     * 返回文档集合
     */
    public List<Map<String,Object>> documents;

    /**
     * 查询耗时 单位毫秒
     */
    public Long took;


    /**
     * 聚合结果
     */
    public Map<String,Map<String,Long>> aggregations;

    public SearchResultEntity(Long totalHits,
                        List<Map<String, Object>> documents,
                        Long took,
                        Map<String,Map<String,Long>> aggregations) {
        this.totalHits = totalHits;
        this.documents = documents;
        this.took = took;
        this.aggregations = aggregations;
    }

    public SearchResultEntity() {
        this.totalHits=0L;
        this.documents =new ArrayList<>();
        this.took=0L;
    }


	public Long getTotalHits() {
		return totalHits;
	}


	public List<Map<String, Object>> getDocuments() {
		return documents;
	}


	public Long getTook() {
		return took;
	}


	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}
    
    
}
