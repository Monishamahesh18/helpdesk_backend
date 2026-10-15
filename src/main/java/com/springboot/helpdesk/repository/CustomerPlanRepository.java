package com.springboot.helpdesk.repository;

import com.springboot.helpdesk.model.CustomerPlan;
import com.springboot.helpdesk.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPlanRepository extends JpaRepository<CustomerPlan, Long> {

    @Query("""
            select cp.plan
            from CustomerPlan cp
            JOIN cp.customer c
            JOIN c.user u
            where u.username = ?1
            """)
    List<Plan> getPlansByCustomerUsername(String customerUsername);
}