package com.cvte.search.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cvte.search.config.HighLevelRestClientConfig;

public class ESProcessorBulkTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	@SuppressWarnings("unused") 
	public void bulkTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			// Test bulkDocument(index, id, document)
			final ESProcessorBulk esProcessorBulk = new ESProcessorBulk();
			esProcessorBulk.bulkDocument(
					index,
	                "huqiming",
	                new HashMap<String, Object>(){
						private static final long serialVersionUID = 1L;
						{
							put("infotype", "test-index");
							put("name", "胡启");
	        			}
					}, highLevelClient).forEach(bulkItemResponse -> {
			            final DocWriteResponse response = bulkItemResponse.getResponse();
			            switch (bulkItemResponse.getOpType()) {
			                case INDEX:
			                case CREATE:
			                	logger.info("The document was created.");
			                    final IndexResponse indexResponse = (IndexResponse) response;
			                    break;
			                case UPDATE:
			                	logger.info("The document was updated.");
			                    final UpdateResponse updateResponse = (UpdateResponse) response;
			                    break;
			                case DELETE:
			                	logger.info("The document was deleted.");
			                    final DeleteResponse deleteResponse = (DeleteResponse) response;
			                    break;
			            }
			   });
			
			// Test indexDocument
			esProcessorBulk.indexDocument(new IndexRequest(index).id("hu0").source( new HashMap<String, Object>(){
				private static final long serialVersionUID = 1L;
				{
					put("infotype", "test-index");
					put("name", "胡启");
				}
			}), highLevelClient);
			
			
			List<DocWriteRequest<?>> requests = new ArrayList<DocWriteRequest<?>>();
			for(int i=0; i<100; i++) {
				Map<String, Object> document = new HashMap<String, Object>();
				document.put("infotype", "test-index");
				document.put("name", "胡启明" + i);
				IndexRequest indexRequest = new IndexRequest(index).id(String.valueOf(i)).source(document);
				UpdateRequest updateRequest = new UpdateRequest(index, String.valueOf(i)).doc(document);
				DeleteRequest deleteRequest = new DeleteRequest(index, String.valueOf(i));
				requests.add(indexRequest);
				requests.add(updateRequest);
				requests.add(deleteRequest);
			}
			long startTime = System.currentTimeMillis();
	
			// Test bulkDocument(requests, highLevelClient)
			esProcessorBulk.bulkDocument(requests, highLevelClient).forEach(bulkItemResponse -> {
	            final DocWriteResponse response = bulkItemResponse.getResponse();
	            switch (bulkItemResponse.getOpType()) {
	                case INDEX:
	                case CREATE:
	                	logger.info("The document was created.");
	                    final IndexResponse indexResponse = (IndexResponse) response;
	                    break;
	                case UPDATE:
	                	logger.info("The document was updated.");
	                    final UpdateResponse updateResponse = (UpdateResponse) response;
	                    break;
	                case DELETE:
	                	logger.info("The document was deleted.");
	                    final DeleteResponse deleteResponse = (DeleteResponse) response;
	                    break;
	            }
			});
			
			// Test bulkAsyncDocument(requests, highLevelClient)
			esProcessorBulk.bulkAsyncDocument(requests, highLevelClient);
			
			long endTime = System.currentTimeMillis();
			long allTime = endTime - startTime;
			logger.info("批量插入更新总耗时 ：{} ms", allTime);
		}catch (Exception e) {
			logger.error("bulkTest is failed");
		}
	}
}
