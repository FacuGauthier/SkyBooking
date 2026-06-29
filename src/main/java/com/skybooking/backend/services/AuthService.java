package com.skybooking.backend.services;

import com.skybooking.backend.dtos.auth.AuthResponse;
import com.skybooking.backend.dtos.auth.ChangePasswordRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Client c = buildClient(dto);

        Client clientSaved = clientRepository.save(c);

        // 3. Crear el Passenger principal vinculado al Client
        Passenger p = buildPassenger(dto, clientSaved);

        Passenger passengerSaved = passengerRepository.save(p);

        // 4. Generar el JWT
        String token = jwtUtil.generateToken(clientSaved.getId(), clientSaved.getRole(), clientSaved.getEmail());

        // 5. Retornar el AuthResponse
        return buildAuthResponse(token, passengerSaved, clientSaved);
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

        // 5. Generar el JWT
        String token = jwtUtil.generateToken(client.getId(), client.getRole(), client.getEmail());

        // 6. Retornar el AuthResponse
        return buildAuthResponse(token,passenger,client);
    }

    @Transactional(readOnly = true)
    public Client getAuthenticatedClient() {
        // 1. Extraer el username (email) usando la clase de utilidad JwtUtil
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()) throw new IllegalArgumentException("No hay usuario autenticado.");

        Object principal = auth.getPrincipal();

        if(!(principal instanceof UserDetails)) throw new IllegalStateException("No hay usuario autenticado.");

        String email = auth.getName();

        // 2. Buscar el cliente en la base de datos
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    @Transactional
    public void changePassword(Long clientId, ChangePasswordRequest dto) {
        // 1. Buscar al cliente por ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // 2. Validar que la contraseña actual ingresada coincida con el hash de la base de datos
        if (!passwordEncoder.matches(dto.currentPassword(), client.getPasswordHash())) {
            throw new BadCredentialsException("La contraseña actual es incorrecta.");
        }

        // 3. Validar que la nueva contraseña no sea exactamente igual a la anterior (Regla de negocio opcional recomendada)
        if (passwordEncoder.matches(dto.newPassword(), client.getPasswordHash())) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
        }

        // 4. Encriptar la nueva contraseña y actualizar la entidad
        client.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        clientRepository.save(client);
    }



    private Client buildClient(RegisterRequest dto) {
        Client c = new Client();

        c.setFirstName(dto.firstName());
        c.setLastName(dto.lastName());
        c.setEmail(dto.email());
        c.setPhone(dto.phone());
        c.setPasswordHash(passwordEncoder.encode(dto.password()));
        c.setRole(Role.CLIENT);
        c.setActive(true);

        return c;
    }
    private Passenger buildPassenger(RegisterRequest dto, Client clientSaved) {
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

        return p;
    }
    private AuthResponse buildAuthResponse(String token, Passenger passenger, Client client) {
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
