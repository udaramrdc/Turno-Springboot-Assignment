package com.turno.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "agents")
@Data
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    // Self-referencing relationship for manager
    @Column(name = "manager_id")
    private Integer managerId;
    
    @Column(name = "is_available")
    private Boolean isAvailable;
    
}
