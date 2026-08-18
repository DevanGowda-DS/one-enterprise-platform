package com.oneenterprise.orderservice.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * configure a reusable RestClient for calling User Service.
 * the base URL is externalized via application.properties, not hard-coded.
 * a connection/read timeout is applied so Order Service never waits forever
 *        on a slow or unresponsive User Service.
 */
@Configuration
public class RestClientConfig {

    @Value("${user.service.base-url}")
    private String userServiceBaseUrl;

    @Value("${user.service.connect-timeout-ms:2000}")
    private int connectTimeoutMs;

    @Value("${user.service.read-timeout-ms:2000}")
    private int readTimeoutMs;

    @Bean
    public RestClient userRestClient() {
        // Connect timeout is configured on the connection manager (non-deprecated API).
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(
                org.apache.hc.client5.http.config.ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(connectTimeoutMs))
                        .build()
        );

        // Response (read) timeout stays on RequestConfig.
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeoutMs))
                .build();

        var httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return RestClient.builder()
                .baseUrl(userServiceBaseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
