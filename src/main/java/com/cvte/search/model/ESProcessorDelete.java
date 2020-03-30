package com.cvte.search.model;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ESProcessorDelete {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private RestHighLevelClient highLevelClient;
	
	 /**
     * 删除——通过id及索引名直接删除
     * @param index
     * @param id
     * @return DeleteResponse
     */
    public DeleteResponse deleteDocument(final String index, final String id) {
    	try{
            final DeleteRequest request = new DeleteRequest(index, id);
            return highLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("deleteDocument Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 删除——通过id及索引名直接删除，增加在请求参数
     * @param index
     * @param id
     * @param RestHighLevelClient highLevelClient
     * @return DeleteResponse
     */
    public DeleteResponse deleteDocument(final String index, final String id, RestHighLevelClient highLevelClient) {
        try{
            final DeleteRequest request = new DeleteRequest(index, id);
            return highLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("deleteDocument Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 删除——通过分片及查询条件删除 
     * @param indices
     * @param query
     * @return BulkByScrollResponse
     */
    public BulkByScrollResponse deleteDocumentByQuery(final DeleteByQueryRequest request) {
        try {
            return highLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 删除——通过分片及查询条件删除 
     * @param indices
     * @param query
     * @param RestHighLevelClient highLevelClient
     * @return BulkByScrollResponse
     */
    public BulkByScrollResponse deleteDocumentByQuery(final DeleteByQueryRequest request, RestHighLevelClient highLevelClient) {
        try {
            return highLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 删除——通过DeleteRequest对象进行删除
     * @param request
     * @return
     */
    public Boolean deleteDocument(DeleteRequest request) throws IOException {
    	try {
	        DeleteResponse deleteResponse = highLevelClient.delete(request, RequestOptions.DEFAULT);
	        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
	            logger.info("not found doc id:{}", deleteResponse.getId());
	            return false;
	        }
	        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
	            return true;
	        }
	        logger.info("deleteResponse Status:{}", deleteResponse.status());
	        return false;
    	} catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 删除——通过DeleteRequest对象进行删除
     * @param request
     * @param RestHighLevelClient highLevelClient
     * @return
     */
    public DeleteResponse deleteDocument(DeleteRequest request, RestHighLevelClient highLevelClient) throws IOException {
    	try {
	        return highLevelClient.delete(request, RequestOptions.DEFAULT);
    	} catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
