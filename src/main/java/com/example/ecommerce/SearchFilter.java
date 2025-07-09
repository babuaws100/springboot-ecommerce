package com.example.ecommerce;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFilter {
    private String field;
    private String op; // =, >, <, like
    private Object value;
}

