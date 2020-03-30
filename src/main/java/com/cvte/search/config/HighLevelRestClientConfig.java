package com.cvte.search.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * ES7连接器
 * @author huqiming
 *
 */
@Configuration
public class HighLevelRestClientConfig {
	
    @Value("${es.connect.host}")
    private String host;

    @Value("${es.connect.port}")
    private Integer port;
    
    @Value("${es.connect.schema}")
    private String schema;

    @Value("${es.connect.username}")
    private String username;

    @Value("${es.connect.password}")
    private String password;
    
    //静态变量
    private static int connectTimeOut = 1000; // 连接超时时间
    private static int socketTimeOut = 30000; // 连接超时时间
    private static int connectionRequestTimeOut = 500; // 获取连接的超时时间
    private static int maxConnectTotal = 100; // 最大连接数
    private static int maxConnectPerRoute = 100; // 最大路由连接数


    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost hosts = new HttpHost(host, port, schema);
        return RestClient.builder(hosts);
    }
    
    public  RestHighLevelClient getClient(@Autowired RestClientBuilder restClientBuilder)
    {
    	restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectTotal);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            final CredentialsProvider credentialsProvider=new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username, password)); //用户名和密码用于鉴权
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        return new RestHighLevelClient(restClientBuilder);
    }
    
    public RestHighLevelClient creatClient(String host, int port, String schema, String username, String password) {
    	RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(host, port, schema));
    	restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(100);
            httpClientBuilder.setMaxConnPerRoute(100);
            final CredentialsProvider credentialsProvider=new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username, password)); //用户名和密码用于鉴权
            return httpClientBuilder;
        });
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(1000);
            requestConfigBuilder.setSocketTimeout(30000);
            requestConfigBuilder.setConnectionRequestTimeout(500);
            return requestConfigBuilder;
        });
        return new RestHighLevelClient(restClientBuilder);
    	
    }
}
