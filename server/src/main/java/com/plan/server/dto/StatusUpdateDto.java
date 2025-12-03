package com.plan.server.dto;

import com.plan.server.enums.Status;
import lombok.Data;

@Data
public class StatusUpdateDto {
    private Status status;
}