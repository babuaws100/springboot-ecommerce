package com.example.ecommerce;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private String entity;
    private int page;
    private int size;
    private List<SearchFilter> filters;
}

