package com.skybooking.backend.services;

import com.skybooking.backend.dtos.auth.AuthResponse;
import com.skybooking.backend.dtos.auth.LoginRequest;
import com.skybooking.backend.dtos.auth.RegisterRequest;
import com.skybooking.backend.models.Client;
import com.skybooking.backend.models.Passenger;
import com.skybooking.backend.models.enums.Role;
import com.skybooking.backend.repositories.ClientRepository;
import com.skybooking.backend.repositories.PassengerRepository;
import com.skybooking.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest dto) {
        // 1. Buscar al cliente por email
        Client client = clientRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas o el usuario no existe."));

        // 2. Verificar si el usuario está activo
        if(!client.isActive()) throw new IllegalArgumentException("La cuenta se encuentra desactivada.");

        // 3. Validar la contraseña usando el PasswordEncoder
        if(!passwordEncoder.matches(dto.password(), client.getPasswordHash())) throw new BadCredentialsException("Credenciales incorrectas o el usuario no existe.");

        // 4. Recuperar el Passenger principal vinculado a este Client
        Passenger passenger = passengerRepository.findByClientId(client.getId())
                .orElseThrow(() -> new IllegalStateException("Error de consistencia: No se encontró un perfil de pasajero para este cliente."));

        // 5. Generar las UserDetails para el JWT
        UserDetails user = User.builder()
                .username(client.getEmail())
                .password(client.getPasswordHash())
                .roles(client.getRole().name())
                .build();
        String token = jwtUtil.generateToken(user);

        // 6. Construir y retornar el AuthResponse completo
        return new AuthResponse(
                token,
                client.getId().toString(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhone(),
                client.getRole(),
                client.getAvatar(),
                passenger.getMilesBalance(),
                passenger.getFrequentFlyerNumber()
        );
    }
}
