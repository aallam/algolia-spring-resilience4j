package com.algolia.sample;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchConfig;
import com.algolia.search.StatefulHost;
import com.algolia.search.models.common.CallType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.EnumSet;
import java.util.List;

@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    SearchClient searchClient() {
        List<StatefulHost> hosts = List.of(new StatefulHost("expired.badssl.com", EnumSet.of(CallType.READ)));
        SearchConfig configuration =
                new SearchConfig.Builder("latency", "927c3fe76d4b52c5a2912973f35a3077")
                        .setHosts(hosts)
                        .build();
        return DefaultSearchClient.create(configuration);
    }
}
