package com.turno.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgentDecisionRequest {
    @NotNull
    private String decision; // "APPROVE" or "REJECT"
}
