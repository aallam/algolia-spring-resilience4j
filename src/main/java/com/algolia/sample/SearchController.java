package com.algolia.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    AlgoliaSearcher searcher;

    public SearchController(AlgoliaSearcher searcher) {
        this.searcher = searcher;
    }

    @GetMapping("/multi")
    public CompletableFuture<String> multiIndexSearch() {
        return searcher.multiIndexSearch("");
    }
}
