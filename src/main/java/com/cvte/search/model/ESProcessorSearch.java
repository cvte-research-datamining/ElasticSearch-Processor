package com.cvte.search.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cvte.search.Entity.SearchResultEntity;

public class ESProcessorSearch {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private RestHighLevelClient highLevelClient;
	
	 /**
     * 查询
     * @param searchRequest
     * @return
     */
    public SearchResultEntity searchDocument(SearchRequest searchRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchResponse searchResponse;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            searchResponse.getTook().getMillis();
            long totalHits = searchResponse.getHits().getTotalHits().value;
            long took = searchResponse.getTook().getMillis();
            for (SearchHit document : searchHit) {
                Map<String, Object> item = document.getSourceAsMap();
                if (item == null) {
                    continue;
                }
                Map<String, HighlightField> highlightFields = document.getHighlightFields();
                if (!highlightFields.isEmpty()) {
                    for (String key : highlightFields.keySet()) {
                        Text[] fragments = highlightFields.get(key).fragments();
                        if (item.containsKey(key)) {
                            item.put(key, fragments[0].string());
                        }
                        String[] fieldArray = key.split("[.]");
                        if (fieldArray.length > 1) {
                            item.put(fieldArray[0], fragments[0].string());
                        }
                    }
                }
                list.add(item);
            }
            Map<String, Map<String, Long>> aggregations = getAggregation(searchResponse.getAggregations());
            return new SearchResultEntity(totalHits, list, took, aggregations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SearchResultEntity();
    }
    
    /**
     * 
     * @param searchRequest
     * @param highLevelClient
     * @return
     */
    public SearchResultEntity searchDocument(SearchRequest searchRequest, RestHighLevelClient highLevelClient) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchResponse searchResponse;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            searchResponse.getTook().getMillis();
            long totalHits = searchResponse.getHits().getTotalHits().value;
            long took = searchResponse.getTook().getMillis();
            for (SearchHit document : searchHit) {
                Map<String, Object> item = document.getSourceAsMap();
                if (item == null) {
                    continue;
                }
                Map<String, HighlightField> highlightFields = document.getHighlightFields();
                if (!highlightFields.isEmpty()) {
                    for (String key : highlightFields.keySet()) {
                        Text[] fragments = highlightFields.get(key).fragments();
                        if (item.containsKey(key)) {
                            item.put(key, fragments[0].string());
                        }
                        String[] fieldArray = key.split("[.]");
                        if (fieldArray.length > 1) {
                            item.put(fieldArray[0], fragments[0].string());
                        }
                    }
                }
                list.add(item);
            }
            Map<String, Map<String, Long>> aggregations = getAggregation(searchResponse.getAggregations());
            return new SearchResultEntity(totalHits, list, took, aggregations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SearchResultEntity();
    }
    
    private Map<String, Map<String, Long>> getAggregation(Aggregations aggregations) {
        if (aggregations == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Map<String, Long>> result = new HashMap<>();
        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        aggregationMap.forEach((k, v) -> {
            Map<String, Long> agg = new HashMap<>();
            List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) v).getBuckets();
            for (Terms.Bucket bucket : buckets) {
                agg.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
            result.put(k, agg);
        });
        return result;
    }
    
    
    
    /**
     * 个数查询
     *
     * @param countRequest
     * @return
     */
    public Long countDocument(CountRequest countRequest) {
        try {
            CountResponse countResponse = highLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            logger.error("CountResponse Exceptions：{}", e.getLocalizedMessage());
            return 0L;
        }
    }
}
