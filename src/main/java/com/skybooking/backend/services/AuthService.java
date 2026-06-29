package com.skybooking.backend.services;

import com.skybooking.backend.dtos.auth.AuthResponse;
import com.skybooking.backend.dtos.auth.RegisterRequest;
import com.skybooking.backend.models.Client;
import com.skybooking.backend.models.Passenger;
import com.skybooking.backend.models.enums.Role;
import com.skybooking.backend.repositories.ClientRepository;
import com.skybooking.backend.repositories.PassengerRepository;
import com.skybooking.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientRepository clientRepository;
    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest dto) {
        // 1. Validar que el email no esté registrado previamente
        if (clientRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya se encuentra registrado.");
        }

        // 2. Crear el Client
        Client c = new Client();
        c.setFirstName(dto.firstName());
        c.setLastName(dto.lastName());
        c.setEmail(dto.email());
        c.setPhone(dto.phone());
        c.setPasswordHash(passwordEncoder.encode(dto.password()));
        c.setRole(Role.CLIENT);
        c.setActive(true);

        Client clientSaved = clientRepository.save(c);

        // 3. Crear el Passenger principal vinculado al Client
        Passenger p = new Passenger();
        p.setFirstName(dto.firstName());
        p.setLastName(dto.lastName());
        p.setClient(clientSaved);
        p.setMilesBalance(0);

        // Generar frequentFlyerNumber único ("SK-" + UUID corto)
        String shortUUID = UUID.randomUUID().toString().replace("-","").substring(0,7).toUpperCase();
        p.setFrequentFlyerNumber(shortUUID);
        p.setDocumentType(dto.documentType());
        p.setDocumentNumber(dto.documentNumber());
        p.setBirthDate(dto.birthDate());

        Passenger passengerSaved = passengerRepository.save(p);

        // 4. Generar el JWT
        UserDetails userDetails = User.builder()
                .username(clientSaved.getEmail())
                .password(clientSaved.getPasswordHash())
                .roles(clientSaved.getRole().name())
                .build();
        String token = jwtUtil.generateToken(userDetails);

        // 5. Retornar el AuthResponse
        return new AuthResponse(
                token,
                clientSaved.getId().toString(),
                clientSaved.getFirstName(),
                clientSaved.getLastName(),
                clientSaved.getEmail(),
                clientSaved.getPhone(),
                clientSaved.getRole(),
                clientSaved.getAvatar(),
                passengerSaved.getMilesBalance(),
                passengerSaved.getFrequentFlyerNumber()
        );
    }


}
