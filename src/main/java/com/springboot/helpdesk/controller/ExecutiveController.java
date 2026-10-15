package com.springboot.helpdesk.controller;

import com.springboot.helpdesk.dto.request.ExecutiveDto;
import com.springboot.helpdesk.dto.response.ExecutiveTicketInfoDto;
import com.springboot.helpdesk.model.Executive;
import com.springboot.helpdesk.service.ExecutiveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExecutiveController {

    private final ExecutiveService executiveService;

    /*
    DTO (name,email,contact)
    validation
    Map DTO to Entity
    Insert Entity IN Db
    * */
    @PostMapping("/api/executive/add")
    public void insertExecutive(@Valid @RequestBody ExecutiveDto executiveDto,
                                Principal principal){
        executiveService.add(executiveDto, principal.getName()); // principal.getName() give us logged manager username
    }
    @GetMapping("/api/executive/ticket")
    public List<ExecutiveTicketInfoDto> getTicketsByExecutiveUsername(
            @RequestParam("username") String executiveUsername,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {

        return executiveService.getTicketsByExecutiveUsername(
                executiveUsername,
                page,
                size
        );
    }
}