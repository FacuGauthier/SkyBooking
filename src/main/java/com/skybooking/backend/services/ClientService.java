package com.skybooking.backend.services;

import com.skybooking.backend.dtos.auth.RegisterRequest;
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

    @Transactional(readOnly = true)
    public ClientProfileResponse getProfile(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clientId));

        Passenger passenger = passengerRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Passenger no encontrado con ID: " + clientId));

        return buildClientProfile(client, passenger);
    }

    @Transactional
    public ClientProfileResponse updateProfile(Long clientId, UpdateProfileRequest dto) {
        Client c = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clientId));

        c.setFirstName(dto.firstName());
        c.setLastName(dto.lastName());
        c.setPhone(dto.phone());
        c.setAvatar(dto.avatar());

        Client clientUpdated = clientRepository.save(c);

        Passenger mainPassenger = passengerRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Pasajero principal no encontrado para el cliente ID: " + clientId));

        return buildClientProfile(clientUpdated, mainPassenger);
    }

    @Transactional(readOnly = true)
    public Page<ClientSummaryResponse> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(client -> new ClientSummaryResponse(
                        client.getId(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getRole(),
                        client.isActive()
                ));
    }

    @Transactional(readOnly = true)
    public Page<ClientSummaryResponse> searchClient(String query,  Pageable pageable) {
        return clientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query, pageable)
                .map(c -> new ClientSummaryResponse(
                        c.getId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getRole(),
                        c.isActive()
                ));
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getClientBooking(Long clientId) {
        if(!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + clientId);
        }
        return bookingService.getBookingByClientId(clientId, null, Pageable.unpaged());
    }

    @Transactional
    public void deactivateClient(Long clientId) {
        Client c = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clientId));

        c.setActive(false);
        clientRepository.save(c);
    }

    @Transactional
    public void activateClient(Long clientId) {
        Client c = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clientId));

        c.setActive(true);
        clientRepository.save(c);
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
}
