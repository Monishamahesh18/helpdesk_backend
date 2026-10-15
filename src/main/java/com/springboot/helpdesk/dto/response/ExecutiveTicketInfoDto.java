package com.springboot.helpdesk.dto.response;

import com.springboot.helpdesk.enums.Priority;
import com.springboot.helpdesk.enums.Status;

import java.time.Instant;

public record ExecutiveTicketInfoDto(
        Long ticketId,
        Long executiveId,
        String executiveName,
        String subject,
        Priority priority,
        Status status,
        Instant createdAt,
        Long customerId,
        String customerName
) {
}