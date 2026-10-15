package com.springboot.helpdesk.service;

import com.springboot.helpdesk.dto.request.TicketDto;
import com.springboot.helpdesk.dto.response.ExecutiveTicketInfoDto;
import com.springboot.helpdesk.dto.response.TicketInfoDto;
import com.springboot.helpdesk.dto.response.TicketRespDto;
import com.springboot.helpdesk.dto.response.TicketStatusCustomerCountDto;
import com.springboot.helpdesk.enums.Priority;
import com.springboot.helpdesk.enums.Status;
import com.springboot.helpdesk.exception.OwnershipException;
import com.springboot.helpdesk.exception.ResourceNotFoundException;
import com.springboot.helpdesk.mapper.TicketMapper;
import com.springboot.helpdesk.model.Customer;
import com.springboot.helpdesk.model.Executive;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.springboot.helpdesk.model.Ticket;
import com.springboot.helpdesk.repository.CustomerRepository;
import com.springboot.helpdesk.repository.ExecutiveRepository;
import com.springboot.helpdesk.repository.TicketRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ExecutiveRepository executiveRepository;
    public void add(String customerUsername, TicketDto ticketDto) {
        // Step 1: Fetch Customer Object from DB using given customerId
        Customer customer =  customerRepository.getByUsername(customerUsername);
        System.out.println("i am at line 38");
        // Step 2: Map TicketDto to Ticket entity
        Ticket ticket = TicketMapper.convertDtoToEntity(ticketDto);
        System.out.println("i am at line 41");
        // Step 3: Attach customer to Ticket
        ticket.setCustomer(customer);
        System.out.println("i am at line 44");
        // Step 3.5: Assign Status as Open to Ticket
        ticket.setStatus(Status.OPEN);
        System.out.println("i am at line 47");
        // Step 4: Insert Ticket in DB
        ticketRepository.save(ticket);
    }

    public void assignExecutive(Long executiveId, Long ticketId, String managerUsername) {
        // Step 1: Fetch executive from DB using executiveId
        Executive executive = executiveRepository.findById(executiveId)
                .orElseThrow(() -> new ResourceNotFoundException("executive not found"));


        // Step 1.5 Does this executive belong to the team of logged-in manager?
        if(! (executive.getManager().getUser().getUsername().equals(managerUsername))){
            throw new OwnershipException("Operation not allowed! contact admin");
        }

        // Step 2: Fetch ticket from DB using ticketId
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        // Step 3: Attach executive to ticket
        ticket.setExecutive(executive);

        // Step 4: Save ticket
        ticketRepository.save(ticket);
    }

    public List<TicketInfoDto> getTicketsByCustomerId(Long customerId, int page, int size) {

        // Validate customerId
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Id Invalid"));


        // Create an Instance of Pageable interface using page and size values thru PageRequest
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.getTicketByCustomerId(customerId, pageable);
    }

    public List<TicketInfoDto> getTicketsByCustomerUsername(String customerUsername, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.getTicketByCustomerUsername(customerUsername, pageable);
    }

    public List<ExecutiveTicketInfoDto> getTicketsByExecutiveUsername(String executiveUsername, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.getTicketByExecutiveUsername(executiveUsername, pageable);
    }

    public List<TicketStatusCustomerCountDto> getTicketStatusCustomerCount() {
        return ticketRepository.getTicketStatusCustomerCount();
    }

    public List<TicketRespDto> getTicketsByPriority(Priority priority) {
        List<Ticket> list = ticketRepository.findByPriority(priority);
        // Map List<Ticket> to List<TicketDto> using Mapper
        return list
                .stream()
                .map(TicketMapper::convertEntityToDto)
                .toList();
    }

    public List<TicketRespDto> getTicketsByStatus(Status status) {
        List<Ticket> list = ticketRepository.findByStatus(status);

        // Map List<Ticket> to List<TicketRespDto> using Mapper
        return list
                .stream()
                .map(TicketMapper::convertEntityToDto)
                .toList();
    }

    public List<TicketRespDto> getTicketsByPriorityAndStatus(
            Priority priority, Status status) {
        List<Ticket> list =
                ticketRepository.findByPriorityAndStatus(priority, status);

        // Map List<Ticket> to List<TicketRespDto> using Mapper
        return list
                .stream()
                .map(TicketMapper::convertEntityToDto)
                .toList();
    }


    public List<TicketRespDto> dynamicFilterTicket(
            Priority priority,
            Status status,
            LocalDate fromDate,
            LocalDate toDate) {

        return ticketRepository.dynamicFilterTicket(
                priority,
                status,
                fromDate == null ? null :
                        fromDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                toDate == null ? null :
                        toDate.plusDays(1)
                                .atStartOfDay(ZoneOffset.UTC).toInstant()
        );
    }

} // yyyy-MM-ddThh:mm:sec.nano

/*
void m1(){
    if(this == true)
        throw new ResourceNotFoundException("Customer id Invalid");
}

()->new ResourceNotFoundException("Customer id Invalid")
()->{
    if(this == true)
        throw new ResourceNotFoundException("Customer id Invalid");
}
* */