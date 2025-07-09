package com.example.ecommerce;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenericSearchService {

    private final Map<String, JpaSpecificationExecutor<?>> repoMap;

    private final ApplicationContext context;

    public <T> List<T> search(String entityName, List<SearchFilter> filters) {
        String beanName = toCamelCase(entityName) + "Repository";
        JpaSpecificationExecutor<T> repo = (JpaSpecificationExecutor<T>) repoMap.get(beanName);
        if (repo == null) {
            throw new IllegalArgumentException("No repository found for: " + entityName);
        }
        Specification<T> spec = new MultiFieldSpecification<>(filters);
        return repo.findAll(spec);
    }

    public <T> Page<T> pageableSearch(String entityName, List<SearchFilter> filters, int page, int size) {
        String beanName = toCamelCase(entityName) + "Repository";
        JpaSpecificationExecutor<T> repo = (JpaSpecificationExecutor<T>) repoMap.get(beanName);
        if (repo == null) {
            throw new IllegalArgumentException("No repository found for: " + entityName);
        }
        Pageable pageable = PageRequest.of(page, size);
        Specification<T> spec = new MultiFieldSpecificationGrouped<>(filters);
        return repo.findAll(spec, pageable);
    }

    private String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalize = false;

        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                capitalize = true;
            } else if (capitalize) {
                result.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                result.append(result.length() == 0 ? Character.toLowerCase(c) : c);
            }
        }

        return result.toString();
    }

}
