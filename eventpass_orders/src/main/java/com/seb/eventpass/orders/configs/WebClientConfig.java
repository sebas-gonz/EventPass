package com.seb.eventpass.orders.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Value("${microservices.events.url:http://localhost:8080}")
    private String eventsEventsUrl;

    @Value("${microservices.tickets.url:http://localhost:8080}")
    private String ticketsEventsUrl;

    @Bean(name = "eventWebClient")
    public WebClient eventWebClient() {
        WebClient.Builder builder  = WebClient.builder();
        HttpClient  httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );
        return builder
                .baseUrl(eventsEventsUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean(name = "ticketWebClient")
    public WebClient ticketWebClient() {
        WebClient.Builder builder  = WebClient.builder();
        HttpClient  httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));
        return builder
                .baseUrl(ticketsEventsUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
