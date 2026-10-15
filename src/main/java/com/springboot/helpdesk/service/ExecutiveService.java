package com.springboot.helpdesk.service;

import com.springboot.helpdesk.dto.request.ExecutiveDto;
import com.springboot.helpdesk.dto.response.ExecutiveTicketInfoDto;
import com.springboot.helpdesk.enums.Role;
import com.springboot.helpdesk.exception.ResourceNotFoundException;
import com.springboot.helpdesk.mapper.ExecutiveMapper;
import com.springboot.helpdesk.model.Executive;
import com.springboot.helpdesk.model.Manager;
import com.springboot.helpdesk.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.springboot.helpdesk.repository.ExecutiveRepository;
import com.springboot.helpdesk.repository.ManagerRepository;
import com.springboot.helpdesk.repository.TicketRepository;
import com.springboot.helpdesk.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ExecutiveService {
    private final ExecutiveRepository executiveRepository;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public void add(@Valid ExecutiveDto executiveDto, String managerUsername) {
        // Step 0: Fetch manager from DB using managerUsername
        Manager manager =  managerRepository.getByUsername(managerUsername);

    // Step 1: Prepare User object and save it in DB
        /*
        Executive
            User user <-- User must go in DB first and then Executive next
        * */
        User user = new User();
        user.setUsername(executiveDto.username());
        user.setPassword(executiveDto.password());
        user.setRole(Role.EXECUTIVE);
        user = userRepository.save(user); // <-- this is a full created user obj with id


        // Step 1.5 Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Step Map ExecutiveDto to Executive entity
        Executive executive = ExecutiveMapper.mapDtoToEntity(executiveDto);

        // Step 3: Attach the User to Executive
        executive.setUser(user);
        executive.setManager(manager);
        // Step 4: Save executive and return
         executiveRepository.save(executive);
    }
    public List<ExecutiveTicketInfoDto> getTicketsByExecutiveUsername(
            String executiveUsername,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.getTicketByExecutiveUsername(
                executiveUsername,
                pageable
        );
    }
}