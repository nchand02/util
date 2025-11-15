package com.prgx.migration.api.util.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Guest response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Integer numOfGuests;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

