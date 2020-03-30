# ElasticSearch-Processor V1.0.0 #

本项目基于ElasticSearch7.4.2的Java API的封装，基于官方提供的elasticsearch-rest-high-level-client API。

本项目假设你已经对ES的基本概念已经有了一个比较全面的认识，带你熟悉各个API见的不同调用及使用。

##	项目基本介绍
ElasticSearch发展以来有4种客户端，分别是：
- Jest client
- Rest client
- Transport client
- Node client

相信大家在项目集成中选择客户端比较纠结，网上各种案例有的用，有的用那种。自己又不了解每个客户端的优劣势，但又想集成最好的，下面给你答案

ES支持两种协议 

       HTTP协议，支持的客户端有Jest client和Rest client
       Native Elasticsearch binary协议，也就是Transport client和Node client

Jest client和Rest client区别

      Jest client非官方支持，在ES5.0之前官方提供的客户端只有Transport client、Node client。在5.0之后官方发布Rest client，并大力推荐

Transport client和Node client区别

       Transport client（7.0弃用）和Node client（2.3弃用）区别：最早的两个客户端，Transport client是不需要单独一个节点。Node client需要单独建立一个节点，连接该节点进行操作，ES2.3之前有独立的API，ES2.3之后弃用该API，推荐用户创建一个节点，并用Transport client连接进行操作

综合：以上就是各个客户端现在的基本情况，可以看出Rest client目前是官方推荐的，强烈建议ES5及其以后的版本使用Java High Level REST Client，此项目是集成7.4.2版本的High Level Rest Client来实现。



##  项目负责人
- 胡启明 huqiming@cvte.com

## 环境依赖 ##

Springboot 2.0.4

ElasticSearch7.4.2

elasticsearch-rest-high-level-client7.4.2

jdk1.8+

##  部署信息

| 使用业务          | 项目名称             | 项目地址 | 部署地址 |
| ----------------- | :-------------------------- | ----------------- | ----------------- |
| 平台              | HIRE全局搜索-简历搜索项目       | https://gitlab.gz.cvte.cn/xdm/hire_search_server_es_restclient | 鲸云 |



## 功能特性
1. 基于ES7.x连接器：
	- com.cvte.search.config.HighLevelRestClientConfig
	- 调用方法
	  - 直接复制此类，注解添加
	  ```java
	  	@Autowired
    	private RestHighLevelClient highLevelClient;
	  ```
	  - 使用creatClient方法
	  ```java
	  	HighLevelRestClientConfig client = new HighLevelRestClientConfig();
	  	RestHighLevelClient highLevelClient = client.creatClient("10.22.21.34", 9201, "http", "hire", "hire");
	  ```
2. Bulk操作API：
    - com.cvte.search.model.ESProcessorBulk
    - 调用方法
	  - 方法：新增-单条数据处理 bulkDocument(final String index,final String id, final Map<String, Object> document)
	  ```java
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
	  ```
	  - 方法：新增-通过IndexRequest 进行插入indexDocument(IndexRequest indexRequest)
	  ```java
	  	esProcessorBulk.indexDocument(new IndexRequest(index).id("hu0").source( new HashMap<String, Object>(){
				private static final long serialVersionUID = 1L;
				{
					put("infotype", "test-index");
					put("name", "胡启");
				}
			}), highLevelClient);
	  ```
	  - 方法：批量数据处理-增删改都可 bulkDocument(List<DocWriteRequest<?>> requests)
	  ```java
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
	  ```
3. Delete API：
    - com.cvte.search.model.ESProcessorDelete
    - 调用方法
    - 方法：删除——通过id及索引名直接删除 deleteDocument(final String index, final String id)
	  ```java
	  		final ESProcessorDelete esProcessorDelete = new ESProcessorDelete();
	        DeleteResponse deleteResponse = esProcessorDelete.deleteDocument(index, "huqimi", highLevelClient);
	        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
	            logger.info("not found doc id:{}", deleteResponse.getId());
	        }
	        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
		        logger.info("deleteResponse Status:{}", deleteResponse.status());
	        }
	  ```
	- 方法：删除——通过分片及查询条件删除  deleteDocumentByQuery(final DeleteByQueryRequest request)
	  ```java
	  final DeleteByQueryRequest request = new DeleteByQueryRequest(index)
                    .setQuery(new TermQueryBuilder("_id", "hu0"));
	        BulkByScrollResponse response = esProcessorDelete.deleteDocumentByQuery(request, highLevelClient);
	  ```
	- 方法：删除——通过DeleteRequest对象进行删除  deleteDocument(DeleteRequest request)
	  ```java
	  		final ESProcessorDelete esProcessorDelete = new ESProcessorDelete();
			DeleteRequest deleteRequest = new DeleteRequest(index, "huqii");	
	        DeleteResponse deleteResponse = esProcessorDelete.deleteDocument(deleteRequest, highLevelClient);
	        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
	            logger.info("not found doc id:{}", deleteResponse.getId());
	        }
	        if (deleteResponse.getResult() == DocWriteResponse.Result.DELETED) {
		        logger.info("deleteResponse Status:{}", deleteResponse.status());
	        }
	  ```
4. Update API：
    - com.cvte.search.model.ESProcessorUpdate
    - 调用方法:
    	- 方法：更新——某个索引下的某个文档  updateDocument(final String index, final String id, final Map<String, Object> document)
	  	```java
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
	  	```
	  	- 方法：更新——某个索引下的某个文档，但当文档不存在时执行插入操作  upsertDocument(final String index, final String id, final Map<String, Object> document)
	  	```java
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
	  	```
	  	- 方法：更新——通过updateDocument对象进行更新  updateDocument(UpdateRequest updateRequest)
	  	```java
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
	  	```
	  	- 方法：更新——异步查询更新  updateByQueryDocument(UpdateByQueryRequest request)
	  	```java
	  	final ESProcessorUpdate esProcessorUpdate = new ESProcessorUpdate();
	        final UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index)
                    .setQuery(new TermQueryBuilder("name", "胡启明-update"))
                    .setBatchSize(100)
                    .setScript(new Script(
        			        ScriptType.INLINE, "painless",
        			        "if (ctx._source. infotype== 'test-update') {ctx._source.infotype='test-update-byquery'}",
        			        Collections.emptyMap()));
	        BulkByScrollResponse response = esProcessorUpdate.updateByQueryDocument(updateByQueryRequest, highLevelClient);
	  	```
4. Search API：
    - com.cvte.search.model.ESProcessorSearch
    - 调用方法
    	- 方法：查询-关键词查询 searchDocument(SearchRequest searchRequest)
    	```java
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
	  ```
	  
## 目录结构描述
```
.
├── README.md
├── logback-spring.xml
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── cvte
│   │   │           └── search
│   │   │               ├── Entity
│   │   │               │   ├── LoggerEntity.java
│   │   │               │   ├── SearchRequestEntity.java
│   │   │               │   ├── SearchResultEntity.java
│   │   │               │   └── UpdateRequestEntity.java
│   │   │               ├── MyWebAppConfigurer.java
│   │   │               ├── SearchbakendApplication.java
│   │   │               ├── WebLogAspect.java
│   │   │               ├── config
│   │   │               │   └── HighLevelRestClientConfig.java
│   │   │               ├── model
│   │   │               │   ├── ESProcessorBulk.java
│   │   │               │   ├── ESProcessorDelete.java
│   │   │               │   ├── ESProcessorSearch.java
│   │   │               │   └── ESProcessorUpdate.java
│   │   │               └── utils
│   │   │                   └── LoggerUtils.java
│   │   ├── main.iml
│   │   └── resources
│   │       ├── application.properties
│   │       └── logback-spring.xml
│   └── test
│       └── java
│           └── com
│               └── cvte
│                   └── search
│                       └── model
│                           ├── ESProcessorBulkTest.java
│                           ├── ESProcessorDeleteTest.java
│                           ├── ESProcessorSearchTest.java
│                           └── ESProcessorUpdateTest.java
└── target
    └── classes
        ├── META-INF
        │   ├── MANIFEST.MF
        │   └── maven
        │       └── com.cvte
        │           └── elasticsearch-processor
        │               ├── pom.properties
        │               └── pom.xml
        ├── application.properties
        └── logback-spring.xml
```

## V1.0.0 版本内容 ##
1. ES7.x connect
2. Bulk API
3. Delete API
4. Update API
5. Search API