package com.cvte.search.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cvte.search.config.HighLevelRestClientConfig;

public class ESProcessorUpdateTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void updateTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorUpdate esProcessorUpdate = new ESProcessorUpdate();
	        UpdateResponse response = esProcessorUpdate.updateDocument(
	        		index,
	                "huqimi",
	                new HashMap<String, Object>(){
						private static final long serialVersionUID = 1L;
						{
							put("infotype", "test-update");
							put("name", "胡启明-update");
	        			}
					}, highLevelClient);
	        if (response.getResult() == DocWriteResponse.Result.UPDATED) {
	        	logger.info("The document was updated.");
            } else if (response.getResult() == DocWriteResponse.Result.NOOP) {
            	logger.info("The document was not impacted.");
            }
		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
//	@Test
	public void upsertTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorUpdate esProcessorUpdate = new ESProcessorUpdate();
	        UpdateResponse response = esProcessorUpdate.upsertDocument(
	        		index,
	                "huqi",
	                new HashMap<String, Object>(){
						private static final long serialVersionUID = 1L;
						{
							put("infotype", "test-update");
							put("name", "胡启明-update");
	        			}
					}, highLevelClient);
	        if (response.getResult() == DocWriteResponse.Result.UPDATED) {
	        	logger.info("The document was updated.");
            } else if (response.getResult() == DocWriteResponse.Result.CREATED) {
	        	logger.info("The document was created.");
            }else if (response.getResult() == DocWriteResponse.Result.NOOP) {
            	logger.info("The document was not impacted.");
            }
		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
//	@Test
	public void updateDocumentTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorUpdate esProcessorUpdate = new ESProcessorUpdate();
	        Map<String, Object> document = new HashMap<String, Object>();
			document.put("infotype", "test-update-doc");
			document.put("name", "胡启明-update-doc");
			UpdateRequest updateRequest = new UpdateRequest(index, "huqimimi").doc(document);
	        UpdateResponse response = esProcessorUpdate.updateDocument(updateRequest, highLevelClient);
	        if (response.getResult() == DocWriteResponse.Result.UPDATED) {
	        	logger.info("The document was updated.");
            }else if (response.getResult() == DocWriteResponse.Result.NOOP) {
            	logger.info("The document was not impacted.");
            }
		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
	@Test
	public void updateByQueryDocumentTest() {
		try {
			HighLevelRestClientConfig client = new HighLevelRestClientConfig();
			RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "", "");
			String index = "test";
			
	        final ESProcessorUpdate esProcessorUpdate = new ESProcessorUpdate();
	        final UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index)
                    .setQuery(new TermQueryBuilder("name", "胡启明-update"))
                    .setBatchSize(100)
                    .setScript(new Script(
        			        ScriptType.INLINE, "painless",
        			        "if (ctx._source. infotype== 'test-update') {ctx._source.infotype='test-update-byquery'}",
        			        Collections.emptyMap()));
	        BulkByScrollResponse response = esProcessorUpdate.updateByQueryDocument(updateByQueryRequest, highLevelClient);
	        logger.info("The document was {}.", response.getStatus());
		} catch (Exception e) {
			logger.error("updateTest is failed");
		}
	}
	
}
