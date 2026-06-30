package com.skybooking.backend.services;

import com.skybooking.backend.dtos.booking.BookingSummaryResponse;
import com.skybooking.backend.dtos.client.ClientProfileResponse;
import com.skybooking.backend.dtos.client.ClientSummaryResponse;
import com.skybooking.backend.dtos.client.UpdateProfileRequest;
import com.skybooking.backend.models.Client;
import com.skybooking.backend.models.Passenger;
import com.skybooking.backend.repositories.ClientRepository;
import com.skybooking.backend.repositories.PassengerRepository;
import com.skybooking.backend.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PassengerRepository passengerRepository;
    private final BookingService bookingService;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public ClientProfileResponse getProfile(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + clientId + " not found"));

        Passenger passenger = passengerRepository.findByClientId(client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger with id " + clientId + " not found"));

        return buildClientProfile(client, passenger);
    }

    @Transactional
    public ClientProfileResponse updateProfile(UpdateProfileRequest dto) {
        Client client = authService.getAuthenticatedClient();

        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setPhone(dto.phone());
        client.setAvatar(dto.avatar());
        clientRepository.save(client);

        Passenger passenger = passengerRepository.findByClientId(client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Pasajero principal no encontrado para el cliente ID: " + client.getId()));

        passenger.setFirstName(dto.firstName());
        passenger.setLastName(dto.lastName());
        passengerRepository.save(passenger);

        return buildClientProfile(client, passenger);
    }

    @Transactional(readOnly = true)
    public Page<ClientSummaryResponse> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(this::buildClientSummary);
    }

    @Transactional(readOnly = true)
    public Page<ClientSummaryResponse> searchClient(String query,  Pageable pageable) {
        return clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query, pageable)
                .map(this::buildClientSummary);
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getClientBooking(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        return bookingService.getBookingByClient(clientId, null, Pageable.unpaged());
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getMyBookings() {
        Client client = authService.getAuthenticatedClient();

        return bookingService.getBookingByClient(client.getId(), null, Pageable.unpaged());
    }

    @Transactional
    public void deactivateClient(Long clientId) {
        Client c = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));
        c.setActive(false);
    }

    @Transactional
    public void activateClient(Long clientId) {
        Client c = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        c.setActive(true);
    }



    private ClientProfileResponse buildClientProfile(Client c, Passenger p) {
        return new ClientProfileResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhone(),
                c.getRole(),
                c.getAvatar(),
                p.getMilesBalance(),
                p.getFrequentFlyerNumber(),
                c.isActive()
        );
    }
    private ClientSummaryResponse buildClientSummary(Client c) {
        return new ClientSummaryResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhone(),
                c.getRole(),
                c.isActive()
        );
    }
}
