package com.prgx.migration.api.util.service;

import com.prgx.migration.api.util.dto.request.GuestRequest;
import com.prgx.migration.api.util.dto.response.GuestResponse;
import com.prgx.migration.api.util.exception.ResourceNotFoundException;
import com.prgx.migration.api.util.exception.UnauthorizedException;
import com.prgx.migration.api.util.model.Guest;
import com.prgx.migration.api.util.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Guest entity operations
 */
@Service
@RequiredArgsConstructor
public class GuestService {

    private static final Logger logger = LoggerFactory.getLogger(GuestService.class);

    private final GuestRepository guestRepository;

    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuestsByUserId(Long userId) {
        logger.debug("Fetching all guests for user: {}", userId);
        return guestRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id, Long userId) {
        logger.debug("Fetching guest with id: {} for user: {}", id, userId);
        Guest guest = guestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
        return convertToResponse(guest);
    }

    @Transactional
    public GuestResponse createGuest(GuestRequest request, Long userId) {
        logger.debug("Creating new guest for user: {}", userId);

        Guest guest = Guest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .numOfGuests(request.getNumOfGuests() != null ? request.getNumOfGuests() : 1)
                .userId(userId)
                .build();

        Guest savedGuest = guestRepository.save(guest);
        logger.info("Guest created successfully with id: {}", savedGuest.getId());
        return convertToResponse(savedGuest);
    }

    @Transactional
    public GuestResponse updateGuest(Long id, GuestRequest request, Long userId) {
        logger.debug("Updating guest with id: {} for user: {}", id, userId);

        Guest guest = guestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        guest.setName(request.getName());
        guest.setEmail(request.getEmail());
        guest.setPhone(request.getPhone());
        guest.setNumOfGuests(request.getNumOfGuests() != null ? request.getNumOfGuests() : 1);

        Guest updatedGuest = guestRepository.save(guest);
        logger.info("Guest updated successfully with id: {}", updatedGuest.getId());
        return convertToResponse(updatedGuest);
    }

    @Transactional
    public void deleteGuest(Long id, Long userId) {
        logger.debug("Deleting guest with id: {} for user: {}", id, userId);

        Guest guest = guestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        guestRepository.delete(guest);
        logger.info("Guest deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public long getGuestCount(Long userId) {
        return guestRepository.countByUserId(userId);
    }

    private GuestResponse convertToResponse(Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .numOfGuests(guest.getNumOfGuests())
                .userId(guest.getUserId())
                .createdAt(guest.getCreatedAt())
                .updatedAt(guest.getUpdatedAt())
                .build();
    }
}

