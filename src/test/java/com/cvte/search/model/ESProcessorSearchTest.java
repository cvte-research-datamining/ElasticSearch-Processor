package com.cvte.search.model;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cvte.search.Entity.SearchResultEntity;
import com.cvte.search.config.HighLevelRestClientConfig;

public class ESProcessorSearchTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void searchDocumentTest() {
		HighLevelRestClientConfig client = new HighLevelRestClientConfig();
		RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
		String[] index = {"test"};
		
        final ESProcessorSearch esProcessorSearch = new ESProcessorSearch();

        SearchRequest searchRequest=new SearchRequest(index);
        String query = "胡启明";
        String infotype = "test-update";
        String[] highlightFieldArray={"name"};
        String[] queryFieldArray={"name"};
        String[] fieldListArray = {"name", "infotype"};
        int start = 0;
        int row = 10;
        String[] includeFields = fieldListArray;
        String[] excludeFields = {"name"};

        //增加高亮处理
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        for(String field:highlightFieldArray)
        {
            HighlightBuilder.Field highlightText = new HighlightBuilder.Field(field);
            highlightText.preTags("<em>");
            highlightText.postTags("</em>");
            highlightBuilder.field(highlightText);
        }

        //multimatch查询多字段，filter过滤infotype类型
        QueryBuilder  qb= QueryBuilders.boolQuery()
                .must(QueryBuilders.multiMatchQuery(query,queryFieldArray))
                .filter(QueryBuilders.termQuery("infotype",infotype))
        		.should(QueryBuilders.matchQuery("c_name", query)).boost(2.0f);


        //Add the SearchSourceBuilder to the SeachRequest
        searchRequest.indices(index)
                .source(new SearchSourceBuilder().query(qb)
                		.from(start)
                        .size(row)
                        .fetchSource(includeFields, excludeFields)
                        .highlighter(highlightBuilder)
                        .aggregation(AggregationBuilders.terms("infotype_count").field("infotype").size(10).order(BucketOrder.key(true))));
        SearchResultEntity searchResultEntity = esProcessorSearch.searchDocument(searchRequest, highLevelClient);
        System.out.println(searchResultEntity.getDocuments());
        
	}

}
