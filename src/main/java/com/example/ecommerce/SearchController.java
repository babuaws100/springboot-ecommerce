package com.example.ecommerce;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final GenericSearchService searchService;

    @PostMapping("")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        List<?> result = searchService.search(request.getEntity(), request.getFilters());
        return ResponseEntity.ok(result);
    }

    @PostMapping("paged")
    public ResponseEntity<?> pageableSearch(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.pageableSearch(request.getEntity(), request.getFilters(), request.getPage(), request.getSize()));
    }
}
