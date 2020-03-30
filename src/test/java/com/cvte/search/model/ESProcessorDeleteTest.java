package com.cvte.search.model;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cvte.search.config.HighLevelRestClientConfig;

public class ESProcessorDeleteTest {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void deleteTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorDelete esProcessorDelete = new ESProcessorDelete();
	        DeleteResponse deleteResponse = esProcessorDelete.deleteDocument(index, "huqimi", highLevelClient);
	        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
	            logger.info("not found doc id:{}", deleteResponse.getId());
	        }
	        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
		        logger.info("deleteResponse Status:{}", deleteResponse.status());
	        }

		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
	@Test
	public void deleteDocumentByQueryTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String[] index = {"test"};
			
	        final ESProcessorDelete esProcessorDelete = new ESProcessorDelete();
	        final DeleteByQueryRequest request = new DeleteByQueryRequest(index)
                    .setQuery(new TermQueryBuilder("_id", "hu0"));
	        BulkByScrollResponse response = esProcessorDelete.deleteDocumentByQuery(request, highLevelClient);
	        logger.info("The document was {}.", response.getStatus());

		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
	@Test
	public void deleteDocumentTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorDelete esProcessorDelete = new ESProcessorDelete();
			DeleteRequest deleteRequest = new DeleteRequest(index, "huqii");	
	        DeleteResponse deleteResponse = esProcessorDelete.deleteDocument(deleteRequest, highLevelClient);
	        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
	            logger.info("not found doc id:{}", deleteResponse.getId());
	        }
	        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
		        logger.info("deleteResponse Status:{}", deleteResponse.status());
	        }
		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
}
