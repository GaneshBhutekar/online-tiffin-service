package com.cdac.onlineTiffinService.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.cdac.onlineTiffinService.model.MenuItem;
import com.cdac.onlineTiffinService.model.FoodCategory;

import jakarta.persistence.criteria.Predicate;

public class MenuSpecification {

    public static Specification<MenuItem> filter(

            Long kitchenId,

            FoodCategory category,

            Boolean available,

            BigDecimal minPrice,

            BigDecimal maxPrice

    ){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("kitchen").get("id"),
                            kitchenId
                    )
            );

            if(category != null){

                predicates.add(

                        cb.equal(
                                root.get("foodCategory"),
                                category
                        )

                );

            }

            if(available != null){

                predicates.add(

                        cb.equal(
                                root.get("available"),
                                available
                        )

                );

            }

            if(minPrice != null){

                predicates.add(

                        cb.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice
                        )

                );

            }

            if(maxPrice != null){

                predicates.add(

                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice
                        )

                );

            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }

}