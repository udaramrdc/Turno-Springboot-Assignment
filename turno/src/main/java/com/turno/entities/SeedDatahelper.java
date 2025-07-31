package com.turno.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.turno.repositories.AgentRepository;
import com.turno.repositories.CustomerRepository;
import com.turno.repositories.LoanRepository;

import jakarta.annotation.PostConstruct;

@Component
public class SeedDatahelper {

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private LoanRepository loanRepository;

	@PostConstruct
	public void seedData() {
		seedAgentData();
		seedCustomerData();
		seedLoansData();
	}

	private void seedAgentData() {
		AgentEntity manager = new AgentEntity();
		manager.setName("Amit Manager");
		manager.setPhone("9999990000");

		manager = agentRepository.save(manager);

		// Create Agents under this manager
		AgentEntity agent1 = new AgentEntity();
		agent1.setName("Rahul Agent");
		agent1.setPhone("9999990001");
		agent1.setManagerId(manager.getId());

		AgentEntity agent2 = new AgentEntity();
		agent2.setName("Sneha Agent");
		agent2.setPhone("9999990002");
		agent2.setManagerId(manager.getId());

		agentRepository.saveAll(List.of(agent1, agent2));

		AgentEntity seniorAgent = new AgentEntity();
		seniorAgent.setName("Meena Senior");
		seniorAgent.setPhone("9999990003");

		seniorAgent = agentRepository.save(seniorAgent);

		AgentEntity juniorAgent = new AgentEntity();
		juniorAgent.setName("Anil Junior");
		juniorAgent.setPhone("9999990004");
		juniorAgent.setManagerId(seniorAgent.getId());

		agentRepository.save(juniorAgent);

	}

	private void seedCustomerData() {
		List<CustomerEntity> customers = List.of(new CustomerEntity(null, "Ravi Kumar", "9876543210"),
				new CustomerEntity(null, "Priya Sharma", "9876501234"),
				new CustomerEntity(null, "Amit Patel", "9876512345"),
				new CustomerEntity(null, "Neha Verma", "9876523456"),
				new CustomerEntity(null, "Manish Singh", "9876534567"));

		customerRepository.saveAll(customers);
	}

	private void seedLoansData() {
		List<CustomerEntity> customers = customerRepository.findAll();
		List<LoanEntity> loans = List.of(
				new LoanEntity(null, 50000.0, "PERSONAL", "APPROVED_BY_SYSTEM", LocalDateTime.now().minusDays(10),
						customers.get(0).getId(), null),
				new LoanEntity(null, 200000.0, "HOME", "UNDER_REVIEW", LocalDateTime.now().minusDays(5),
						customers.get(1).getId(), null),
				new LoanEntity(null, 300000.0, "BUSINESS", "APPLIED", LocalDateTime.now().minusDays(2),
						customers.get(2).getId(), null),
				new LoanEntity(null, 150000.0, "AUTO", "REJECTED_BY_SYSTEM", LocalDateTime.now().minusDays(1),
						customers.get(0).getId(), null),
				new LoanEntity(null, 180000.0, "PERSONAL", "APPROVED_BY_AGENT", LocalDateTime.now(),
						customers.get(1).getId(), 2));

		loanRepository.saveAll(loans);
	}
}
