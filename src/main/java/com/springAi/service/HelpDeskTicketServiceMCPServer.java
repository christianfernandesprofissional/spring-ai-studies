package com.springAi.service;

import com.springAi.entity.HelpDeskTicket;
import com.springAi.model.TicketRequest;
import com.springAi.model.TicketRequestMCPServer;
import com.springAi.repository.HelpDeskTicketRepository;
import com.springAi.repository.HelpDeskTicketRepositoryMCPServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class HelpDeskTicketServiceMCPServer {

    private final HelpDeskTicketRepositoryMCPServer helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequestMCPServer ticketInput) {
        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketInput.issue())
                .username(ticketInput.username())
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
        return helpDeskTicketRepository.save(ticket);
    }

    public List<HelpDeskTicket> getTicketsByUsername(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }


}
