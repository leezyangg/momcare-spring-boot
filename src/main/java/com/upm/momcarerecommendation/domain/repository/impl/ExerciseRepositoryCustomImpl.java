package com.upm.momcarerecommendation.domain.repository.impl;

import com.upm.momcarerecommendation.domain.entity.Exercise;
import com.upm.momcarerecommendation.domain.repository.ExerciseRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseRepositoryCustomImpl implements ExerciseRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Exercise> findExercisesBasedOnCriteria(Map<String, List<String>> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Exercise> criteriaQuery = criteriaBuilder.createQuery(Exercise.class);
        Root<Exercise> exercise = criteriaQuery.from(Exercise.class);
        List<Predicate> predicates = new ArrayList<>();

        criteria.forEach((field, values) -> {
            if ("impact_level".equals(field)) {
                predicates.add(exercise.get(field).in(values));
            }
            if ("intensity_level".equals(field)) {
                predicates.add(exercise.get(field).in(values));
            }
        });

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Exercise> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
