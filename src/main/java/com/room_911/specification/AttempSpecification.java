package com.room_911.specification;

import com.room_911.entity.Attemp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttempSpecification {

    @PersistenceContext
    EntityManager entityManager;

    public List<Attemp> filterAttemps(LocalDate dateInicio, LocalDate dateFinal, String employeeId){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Attemp> criteriaQuery = criteriaBuilder.createQuery(Attemp.class);
        Root<Attemp> root = criteriaQuery.from(Attemp.class);

        List<Predicate> predicates = new ArrayList<>();

        if (employeeId != null && !employeeId.isBlank()){
            predicates.add(criteriaBuilder.equal(root.get("employee").get("employeeId"), employeeId));
        }

        if(dateInicio != null && dateFinal != null){
            LocalDateTime startDateTime = dateInicio.atStartOfDay();
            LocalDateTime finalDateTime = dateFinal.atTime(LocalTime.MAX);
            predicates.add(criteriaBuilder.between(root.get("date"), startDateTime, finalDateTime));
        } else if (dateInicio != null) {
            LocalDateTime startDateTime = dateInicio.atStartOfDay();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDateTime));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("date")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
