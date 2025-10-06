package com.room_911.specification;

import com.room_911.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeSpecification {
    @PersistenceContext
    EntityManager entityManager;

    public boolean isOnlyNumber(String query){
        try {
            Integer.parseInt(query);
            return true;
        }catch (NumberFormatException exception){
            return false;
        }
    }

    public List<Employee> filterEmployees(String query, Long department){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            if (isOnlyNumber(query)){
                predicates.add(criteriaBuilder.equal(root.get("employeeId"), query));
            }else {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query.toLowerCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + query.toLowerCase() + "%")
                        )
                );
            }
        }

        if(department != null && department != 0){
            predicates.add(criteriaBuilder.equal(root.get("productionDepartment").get("departmentId"), department));
        }

        criteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
