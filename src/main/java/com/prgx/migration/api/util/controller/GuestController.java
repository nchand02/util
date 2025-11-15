package com.prgx.migration.api.util.controller;

import com.prgx.migration.api.util.dto.request.GuestRequest;
import com.prgx.migration.api.util.dto.response.GuestResponse;
import com.prgx.migration.api.util.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Guest management operations
 */
@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

    private final GuestService guestService;

    /**
     * Get all guests for the authenticated user
     */
    @GetMapping
    public ResponseEntity<List<GuestResponse>> getAllGuests(@AuthenticationPrincipal Long userId) {
        logger.info("GET /api/guests - User: {}", userId);
        List<GuestResponse> guests = guestService.getAllGuestsByUserId(userId);
        return ResponseEntity.ok(guests);
    }

    /**
     * Get a specific guest by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        logger.info("GET /api/guests/{} - User: {}", id, userId);
        GuestResponse guest = guestService.getGuestById(id, userId);
        return ResponseEntity.ok(guest);
    }

    /**
     * Create a new guest
     */
    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(
            @Valid @RequestBody GuestRequest request,
            @AuthenticationPrincipal Long userId) {
        logger.info("POST /api/guests - User: {}, Guest: {}", userId, request.getName());
        GuestResponse guest = guestService.createGuest(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(guest);
    }

    /**
     * Update an existing guest
     */
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody GuestRequest request,
            @AuthenticationPrincipal Long userId) {
        logger.info("PUT /api/guests/{} - User: {}, Guest: {}", id, userId, request.getName());
        GuestResponse guest = guestService.updateGuest(id, request, userId);
        return ResponseEntity.ok(guest);
    }

    /**
     * Delete a guest
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteGuest(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        logger.info("DELETE /api/guests/{} - User: {}", id, userId);
        guestService.deleteGuest(id, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Guest deleted successfully");
        response.put("id", id);

        return ResponseEntity.ok(response);
    }

    /**
     * Get guest count for the authenticated user
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getGuestCount(@AuthenticationPrincipal Long userId) {
        logger.info("GET /api/guests/count - User: {}", userId);
        long count = guestService.getGuestCount(userId);

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }
}

