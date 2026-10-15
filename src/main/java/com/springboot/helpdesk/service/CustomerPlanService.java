package com.springboot.helpdesk.service;

import com.springboot.helpdesk.exception.ResourceNotFoundException;
import com.springboot.helpdesk.model.Customer;
import com.springboot.helpdesk.model.CustomerPlan;
import com.springboot.helpdesk.model.Plan;
import com.springboot.helpdesk.repository.CustomerPlanRepository;
import com.springboot.helpdesk.repository.CustomerRepository;
import com.springboot.helpdesk.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerPlanService {

    private final CustomerPlanRepository customerPlanRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;

    public void addCustomerPlan(CustomerPlan customerPlan,
                                Long customerId,
                                Long planId) {

        // Fetch Customer from DB
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer id Invalid"));

        // Fetch Plan from DB
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan id Invalid"));

        // Attach Customer and Plan to CustomerPlan
        customerPlan.setCustomer(customer);
        customerPlan.setPlan(plan);

        // Save CustomerPlan
        customerPlanRepository.save(customerPlan);
    }

    public List<Plan> getPlansByCustomerUsername(String customerUsername) {

        return customerPlanRepository
                .getPlansByCustomerUsername(customerUsername);
    }
}