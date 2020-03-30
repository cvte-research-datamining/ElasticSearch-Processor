package com.cvte.search.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ESProcessorBulk {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private RestHighLevelClient highLevelClient;

	
	/**
	 * 新增-单条数据处理
	 * @param String index
	 * @param String id
	 * @param Map<String, Object> document
	 * @return BulkResponse
	 */
	public BulkResponse bulkDocument(final String index,final String id, final Map<String, Object> document) {
		try {
            final BulkRequest bulkRequest = new BulkRequest()
                    .add(new IndexRequest(index).id(id).source(document));
            return highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 新增-单条数据处理，增加请求参数
	 * @param String index
	 * @param String id
	 * @param Map<String, Object> document
	 * @param RestHighLevelClient highLevelClient
	 * @return BulkResponse
	 */
	public BulkResponse bulkDocument(final String index,final String id, 
			final Map<String, Object> document, RestHighLevelClient highLevelClient) {
		try {
            final BulkRequest bulkRequest = new BulkRequest()
                    .add(new IndexRequest(index).id(id).source(document));
            return highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	
	
	/**
	 * 新增-通过IndexRequest 进行插入
	 * @param IndexRequest indexRequest
	 * @throws IOException
     * @return IndexResponse
	 */
	public IndexResponse indexDocument(IndexRequest indexRequest) {
		try {
			return highLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		} catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 新增-通过IndexRequest 进行插入，增加请求参数
	 * @param IndexRequest indexRequest
	 * @param RestHighLevelClient highLevelClient
	 * @throws IOException
     * @return IndexResponse
	 */
	public IndexResponse indexDocument(IndexRequest indexRequest, RestHighLevelClient highLevelClient) {
		try {
        	return highLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		} catch (final IOException e) {
	        throw new RuntimeException(e);
	    }
    }
	
	
	
	/**
     *批量数据处理-增删改都可
     *@param DocWriteRequest requests
     * ->IndexRequest
     * ->UpdateRequest
     * ->DeleteRequest
     * @return BulkResponse
     */
	public BulkResponse bulkDocument(List<DocWriteRequest<?>> requests) {
		try {
	        BulkRequest bulkRequest = new BulkRequest();
	        for (DocWriteRequest<?> writeRequest : requests) {
	            bulkRequest.add(writeRequest);
	        }
	        return highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
     *批量数据处理-增删改都可
     *@param DocWriteRequest requests
     * ->IndexRequest
     * ->UpdateRequest
     * ->DeleteRequest
	 * @param RestHighLevelClient highLevelClient
     * @return BulkResponse
     */
	public BulkResponse bulkDocument(List<DocWriteRequest<?>> requests, RestHighLevelClient highLevelClient) {
		try {
	        BulkRequest bulkRequest = new BulkRequest();
	        for (DocWriteRequest<?> writeRequest : requests) {
	            bulkRequest.add(writeRequest);
	        }
	        return highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	
	
	/**
     * 异步批量数据处理-增删改都可
     *@param  DocWriteRequest requests
     * ->IndexRequest
     * ->UpdateRequest
     * ->DeleteRequest
     */
	public void bulkAsyncDocument(List<DocWriteRequest<?>> requests) {
        bulkAsyncListenerDocument(requests, actionListener());
    }
	
	/**
     * 异步批量数据处理-增删改都可，增加请求参数
     *@param  DocWriteRequest requests
     * ->IndexRequest
     * ->UpdateRequest
     * ->DeleteRequest
	 * @param RestHighLevelClient highLevelClient
     * 
     */
	public void bulkAsyncDocument(List<DocWriteRequest<?>> requests, RestHighLevelClient highLevelClient) {
        bulkAsyncListenerDocument(requests, actionListener(), highLevelClient);
    }
	
	
    public void bulkAsyncListenerDocument(List<DocWriteRequest<?>> requests, ActionListener<BulkResponse> actionListener) {
        BulkRequest bulkRequest = new BulkRequest();
        for (DocWriteRequest<?> writeRequest : requests) {
            bulkRequest.add(writeRequest);
        }
        highLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, actionListener);
    }
    
    public void bulkAsyncListenerDocument(List<DocWriteRequest<?>> requests, ActionListener<BulkResponse> actionListener, RestHighLevelClient highLevelClient) {
        BulkRequest bulkRequest = new BulkRequest();
        for (DocWriteRequest<?> writeRequest : requests) {
            bulkRequest.add(writeRequest);
        }
        highLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, actionListener);
    }

    
    private ActionListener<BulkResponse> actionListener() {
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    logger.error("Increased resource failure causes：{}", bulkResponse.buildFailureMessage());
                }else {
                    logger.info("Increased resource sucesses");
                }
            }
            @Override
            public void onFailure(Exception e) {
                logger.error("Asynchronous batch increases data exceptions：{}", e.getLocalizedMessage());
            }
        };
        return listener;
    }
}
