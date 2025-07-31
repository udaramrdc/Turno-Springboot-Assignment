package com.turno.services;

import org.springframework.stereotype.Service;

import com.turno.entities.AgentEntity;
import com.turno.entities.CustomerEntity;
import com.turno.entities.LoanEntity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImp implements NotificationService {

	// in ideal implementation this messages metadata is sent to SQS/Azure bus kind of storage and which is being processed by a separate dedicated microservice
	@Override
	public void notifyAgentAndManager(AgentEntity agent, LoanEntity loan) {
		log.info("Notification sent to Agent '{}' for loan #{}", agent.getName(), loan.getId());
        if (agent.getManagerId() != null) {
            log.info("Notification also sent to Manager (ID: {})", agent.getManagerId());
        }
	}

	@Override
	public void notifyCustomer(CustomerEntity customer, LoanEntity loan) {
		log.info("SMS sent to Customer '{}' for '{}' loan #{}", customer.getName(), loan.getApplicationStatus(),loan.getId());	
	}

}
