package com.seb.eventpass.orders.configs;

import com.seb.eventpass.orders.filters.JwtAuthenticationFilter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Value("${microservices.events.url:http://localhost:8080}")
    private String eventsEventsUrl;

    @Value("${microservices.tickets.url:http://localhost:8083}")
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
                .filter(relayJwtFilter())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean(name = "ticketWebClient")
    public WebClient ticketWebClient() {
        WebClient.Builder builder  = WebClient.builder();
        HttpClient  httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(60))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS)));
        return builder
                .baseUrl(ticketsEventsUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(relayJwtFilter())
                .build();
    }

    private ExchangeFilterFunction relayJwtFilter(){
        return (request , next) -> {
            return ReactiveSecurityContextHolder.getContext()
                    .map(context -> context.getAuthentication())
                    .map(authentication -> ClientRequest.from(request)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.getCredentials())
                            .build())
                    .defaultIfEmpty(request)
                    .flatMap(next::exchange);
        };
    }
}
