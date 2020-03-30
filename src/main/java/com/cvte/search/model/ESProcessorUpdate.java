package com.cvte.search.model;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ESProcessorUpdate {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private RestHighLevelClient highLevelClient;
	
	/**
     * 更新——某个索引下的某个文档
     * @param index
     * @param id
     * @param Map<String, Object> document
     * @return
     */
    public UpdateResponse updateDocument(final String index, final String id, final Map<String, Object> document) {
        try {
            final UpdateRequest updateRequest = new UpdateRequest(index, id)
                    .doc(document);

            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("update Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新——某个索引下的某个文档，添加请求参数
     * @param index
     * @param id
     * @param Map<String, Object> document
     * @param RestHighLevelClient highLevelClient
     * @return
     */
    public UpdateResponse updateDocument(final String index, final String id, 
    		final Map<String, Object> document, RestHighLevelClient highLevelClient) {
        try {
            final UpdateRequest updateRequest = new UpdateRequest(index, id)
                    .doc(document);

            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("update Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 更新——某个索引下的某个文档，但当文档不存在时执行插入操作
     * @param index
     * @param id
     * @param Map<String, Object> document
     * @return
     */
    public UpdateResponse upsertDocument(final String index, final String id, final Map<String, Object> document) {
        try {
            final UpdateRequest updateRequest = new UpdateRequest(index, id)
                    .doc(document)
                    .upsert(document);

            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("upsert Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新——某个索引下的某个文档，但当文档不存在时执行插入操作，添加请求参数
     * @param index
     * @param id
     * @param Map<String, Object> document
     * @param RestHighLevelClient highLevelClient
     * @return
     */
    public UpdateResponse upsertDocument(final String index, final String id, 
    		final Map<String, Object> document,  RestHighLevelClient highLevelClient) {
        try {
            final UpdateRequest updateRequest = new UpdateRequest(index, id)
                    .doc(document)
                    .upsert(document);
            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            logger.error("upsert Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 更新——通过updateDocument对象进行更新
     * @param updateRequest
     * @return
     */
    public UpdateResponse updateDocument(UpdateRequest updateRequest) {
        try {
            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UpdateRequest Exception:{}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新——通过updateDocument对象进行更新，添加请求参数
     * @param updateRequest
     * @param RestHighLevelClient highLevelClient
     * @return
     */
    public UpdateResponse updateDocument(UpdateRequest updateRequest, RestHighLevelClient highLevelClient) {
        try {
            return highLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UpdateRequest Exception:{}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 更新——异步查询更新
     *
     * @param UpdateByQueryRequest request
     */
    public BulkByScrollResponse updateByQueryDocument(UpdateByQueryRequest request) {
        try {
            return highLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("updateByQuery Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 更新——异步查询更新，添加请求参数
     *
     * @param UpdateByQueryRequest request
     * @param RestHighLevelClient highLevelClient
     */
    public BulkByScrollResponse updateByQueryDocument(UpdateByQueryRequest request, RestHighLevelClient highLevelClient) {
        try {
            return highLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("updateByQuery Exception {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
