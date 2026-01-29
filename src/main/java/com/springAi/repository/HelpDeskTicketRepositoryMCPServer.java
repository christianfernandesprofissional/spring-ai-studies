package com.springAi.repository;

import com.springAi.entity.HelpDeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpDeskTicketRepositoryMCPServer extends JpaRepository<HelpDeskTicket, Long> {

    List<HelpDeskTicket> findByUsername(String username);

}
