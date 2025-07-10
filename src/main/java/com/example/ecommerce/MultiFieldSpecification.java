package com.example.ecommerce;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiFieldSpecification<T> implements Specification<T> {

    private final List<SearchFilter> filters;

    public MultiFieldSpecification(List<SearchFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchFilter filter : filters) {
            Path<?> path = root;
            for (String part : filter.getField().split("\\.")) {
                path = path.get(part);
            }
            Predicate predicate = switch (filter.getOp()) {
                case "=" -> cb.equal(path, filter.getValue());
                case ">" -> cb.greaterThan((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
                case "<" -> cb.lessThan((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
                case ">=" -> cb.greaterThanOrEqualTo((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
                case "<=" -> cb.lessThanOrEqualTo((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
                case "like" -> cb.like(path.as(String.class), "%" + filter.getValue() + "%");
                case "like%" -> cb.like(path.as(String.class), filter.getValue() + "%");
                case "%like" -> cb.like(path.as(String.class), "%" + filter.getValue());
                default -> throw new IllegalArgumentException("Unsupported op: " + filter.getOp());
            };

            predicates.add(predicate);
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
