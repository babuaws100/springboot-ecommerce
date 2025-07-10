package com.example.ecommerce;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Collectors;

public class MultiFieldSpecificationGrouped<T> implements Specification<T> {

    private final List<SearchFilter> filters;

    public MultiFieldSpecificationGrouped(List<SearchFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        // Group filters by field name
        Map<String, List<SearchFilter>> grouped = filters.stream()
                .collect(Collectors.groupingBy(SearchFilter::getField));

        List<Predicate> allFieldPredicates = new ArrayList<>();

        for (Map.Entry<String, List<SearchFilter>> entry : grouped.entrySet()) {
            String field = entry.getKey();
            List<SearchFilter> sameFieldFilters = entry.getValue();

            Path<?> path = resolvePath(root, field);

            List<Predicate> orPredicates = sameFieldFilters.stream()
                    .map(filter -> buildPredicate(cb, path, filter))
                    .toList();

            // Combine filters on the same field using OR
            allFieldPredicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
        }

        // Combine all grouped field predicates using AND
        return cb.and(allFieldPredicates.toArray(new Predicate[0]));
    }

    private Path<?> resolvePath(Path<?> root, String field) {
        Path<?> path = root;
        for (String part : field.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Path<?> path, SearchFilter filter) {
        Object value = filter.getValue();
        return switch (filter.getOp()) {
            case "=" -> cb.equal(path, value);
            case ">" -> cb.greaterThan((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
            case "<" -> cb.lessThan((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
            case ">=" -> cb.greaterThanOrEqualTo((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
            case "<=" -> cb.lessThanOrEqualTo((Path<? extends Comparable<Object>>) path, (Comparable<Object>) filter.getValue());
            case "like" -> cb.like(path.as(String.class), "%" + value + "%");
            case "like%" -> cb.like(path.as(String.class), value + "%");
            case "%like" -> cb.like(path.as(String.class), "%" + value);
            default -> throw new IllegalArgumentException("Unsupported op: " + filter.getOp());
        };
    }
}
