package com.springboot.helpdesk.controller;

import com.springboot.helpdesk.model.CustomerPlan;
import com.springboot.helpdesk.model.Plan;
import com.springboot.helpdesk.service.CustomerPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerPlanController {

    private final CustomerPlanService customerPlanService;

    /*
     API to add customer buys plan record in customer_plan table with all relevant fields
    */
    @PostMapping("/api/customer-plan/add/{customerId}/{planId}")
    public void addCustomerPlan(@PathVariable Long customerId,
                                @PathVariable Long planId,
                                @RequestBody CustomerPlan customerPlan) {

        customerPlanService.addCustomerPlan(
                customerPlan,
                customerId,
                planId
        );
    }

    /*
     * GET API : to fetch all plans based on customer username.
     */
    @GetMapping("/api/customer-plan")
    public List<Plan> getPlansByCustomerUsername(
            @RequestParam("username") String customerUsername) {

        return customerPlanService
                .getPlansByCustomerUsername(customerUsername);
    }
}