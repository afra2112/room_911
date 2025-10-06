package com.room_911.specification;

import com.room_911.entity.Attemp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class AttempSpecification {

    @PersistenceContext
    private EntityManager entityManager;


    public Page<Attemp> filterAttemps(LocalDate dateInicio, LocalDate dateFinal, String employeeId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Attemp> query = cb.createQuery(Attemp.class);
        Root<Attemp> root = query.from(Attemp.class);

        List<Predicate> predicates = buildPredicates(cb, root, dateInicio, dateFinal, employeeId);

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get("date")));

        List<Attemp> contenido = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        Long total = countAttemps(cb, dateInicio, dateFinal, employeeId);

        return new PageImpl<>(contenido, pageable, total);
    }

    public List<Attemp> filterAttemps(LocalDate dateInicio, LocalDate dateFinal, String employeeId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Attemp> query = cb.createQuery(Attemp.class);
        Root<Attemp> root = query.from(Attemp.class);

        List<Predicate> predicates = buildPredicates(cb, root, dateInicio, dateFinal, employeeId);

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get("date")));

        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Attemp> root,
                                            LocalDate dateInicio, LocalDate dateFinal, String employeeId) {
        List<Predicate> predicates = new ArrayList<>();

        if (employeeId != null && !employeeId.isBlank()) {
            predicates.add(cb.equal(root.get("employee").get("employeeId"), employeeId));
        }

        if (dateInicio != null && dateFinal != null) {
            predicates.add(cb.between(root.get("date"),
                    dateInicio.atStartOfDay(),
                    dateFinal.atTime(LocalTime.MAX)));
        } else if (dateInicio != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), dateInicio.atStartOfDay()));
        }

        return predicates;
    }

    private Long countAttemps(CriteriaBuilder cb, LocalDate dateInicio, LocalDate dateFinal, String employeeId) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Attemp> countRoot = countQuery.from(Attemp.class);

        List<Predicate> predicates = buildPredicates(cb, countRoot, dateInicio, dateFinal, employeeId);

        countQuery.select(cb.count(countRoot))
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}