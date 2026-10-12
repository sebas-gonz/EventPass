package com.seb.eventpass.payment.configs;

import com.seb.eventpass.payment.filters.JwtAuthenticationFilter;
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
@RequiredArgsConstructor
public class WebClientConfig {
    @Value("${microservices.orders.url:http://localhost:8081}")
    private String ordersUrl;


    @Bean(name = "orderWebClient")
    public WebClient eventWebClient() {
        WebClient.Builder builder = WebClient.builder();
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );
        return builder
                .baseUrl(ordersUrl)
                .filter(relayJwtFilter())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
