package com.example.demo.dto.respose;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UserDateAuditPayload extends DateAuditPayload {
	private Long createdBy;

	private Long updatedBy;

}
