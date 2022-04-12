package com.algolia.sample;

import com.algolia.search.SearchClient;
import com.algolia.search.models.indexing.MultipleQueries;
import com.algolia.search.models.indexing.MultipleQueriesRequest;
import com.algolia.search.models.indexing.MultipleQueriesResponse;
import com.algolia.search.models.indexing.Query;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AlgoliaSearcher {

    private static final Logger logger = Logger.getLogger(AlgoliaSearcher.class.getName());

    SearchClient searchClient;

    public AlgoliaSearcher(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    @Bulkhead(name = "multiIndexSearch", type = Bulkhead.Type.THREADPOOL)
    @CircuitBreaker(name = "multiIndexSearch", fallbackMethod = "multiIndexSearchFallback")
    @TimeLimiter(name = "multiIndexSearch", fallbackMethod = "multiIndexSearchFallback")
    public CompletableFuture<String> multiIndexSearch(String queryString) {
        MultipleQueriesRequest request = new MultipleQueriesRequest(constructMultiQueries());
        MultipleQueriesResponse<String> response = searchClient.multipleQueries(request, String.class);
        return CompletableFuture.completedFuture(convertResponse(response)); // get first
    }

    private List<MultipleQueries> constructMultiQueries() {
        return Collections.singletonList(new MultipleQueries("myIndex", new Query()));
    }


    private String convertResponse(MultipleQueriesResponse<String> response) {
        return response.getResults().get(0).getHits().get(0);
    }

    public CompletableFuture<String> multiIndexSearchFallback(String queryString, Throwable throwable) {
        String type = throwable.getClass().toString();
        String localizedMessage = throwable.getLocalizedMessage();
        logger.log(Level.WARNING, "Falling back, error type: {0}, error message: {1}", new String[]{type, localizedMessage});
        return CompletableFuture.completedFuture("Fallback method :)");
    }
}
