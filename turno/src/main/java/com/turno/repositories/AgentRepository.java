package com.turno.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.turno.entities.AgentEntity;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Integer>{

	@Query("SELECT a FROM AgentEntity a WHERE a.id NOT IN (SELECT l.assignedAgentId FROM LoanEntity l WHERE l.applicationStatus = 'UNDER_REVIEW' AND l.assignedAgentId IS NOT NULL)")
    List<AgentEntity> findAvailableAgents();
	
}
