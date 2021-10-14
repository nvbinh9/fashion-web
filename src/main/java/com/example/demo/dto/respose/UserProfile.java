package com.example.demo.dto.respose;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {
	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private LocalDate createdAt;
	private String email;

}
