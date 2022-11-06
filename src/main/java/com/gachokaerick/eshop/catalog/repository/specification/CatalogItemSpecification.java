package com.gachokaerick.eshop.catalog.repository.specification;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CatalogItemSpecification {

    public static Specification<CatalogItem> getSpecification(
        List<Long> ids,
        String name,
        String description,
        String catalogBrand,
        String catalogType,
        String term
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (ids != null) {
                predicates.add(root.get("id").in(ids));
            }

            if (name != null) {
                predicates.add(cb.equal(root.get("name"), name));
            }

            if (description != null) {
                predicates.add(cb.equal(root.get("description"), description));
            }

            if (catalogBrand != null) {
                predicates.add(cb.equal(root.get("catalogBrand").get("brand"), catalogBrand));
            }

            if (catalogType != null) {
                predicates.add(cb.equal(root.get("catalogType").get("type"), catalogType));
            }

            if (term != null) {
                String likeTerm = "%" + term + "%";
                predicates.add(cb.or(cb.like(root.get("name"), likeTerm), cb.like(root.get("description"), likeTerm)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
