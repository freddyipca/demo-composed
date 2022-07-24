package com.example.democomposed;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class DemoComposedApplication {

    @Bean
    public Function<KStream<String, String>, KStream<String, String>> foo() {
        return input -> input.peek((s, s2) -> log.info("FOO the number is: " + s2));
    }

    @Bean
    public Function<KStream<String, String>, KStream<String, String>> fee() {
        return input -> input.peek((s, s2) -> log.info("FEE the number is: " + s2));
    }

    @Bean
    public Function<KStream<String, String>, KStream<String, Long>> bar() {
        return input -> input
                .mapValues(value -> Long.parseLong(value) +  10)
                .peek((s, s2) -> log.info("BAR the number is: " + s2));
    }

    @Bean
    public Function<KStream<String, String>, KStream<String, Long>> composedOne() {
        return foo().andThen(bar());
    }

    @Bean
    public Function<KStream<String, String>, KStream<String, Long>> composedTwo() {
        return fee().andThen(bar());
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoComposedApplication.class, args);
    }

}
