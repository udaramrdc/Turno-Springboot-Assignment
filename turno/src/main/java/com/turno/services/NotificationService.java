package com.turno.services;

import com.turno.entities.AgentEntity;
import com.turno.entities.CustomerEntity;
import com.turno.entities.LoanEntity;

public interface NotificationService {
    void notifyAgentAndManager(AgentEntity agent, LoanEntity loan);
    void notifyCustomer(CustomerEntity customer, LoanEntity loan);
}