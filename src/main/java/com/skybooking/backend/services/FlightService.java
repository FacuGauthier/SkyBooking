package com.skybooking.backend.services;

import com.skybooking.backend.dtos.airline.AirlineResponse;
import com.skybooking.backend.dtos.airline.AirlineSummaryResponse;
import com.skybooking.backend.dtos.airport.AirportResponse;
import com.skybooking.backend.dtos.airport.AirportSummaryResponse;
import com.skybooking.backend.dtos.flight.*;
import com.skybooking.backend.dtos.plane.PlaneResponse;
import com.skybooking.backend.models.Airline;
import com.skybooking.backend.models.Airport;
import com.skybooking.backend.models.Flight;
import com.skybooking.backend.models.Plane;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.FlightStatus;
import com.skybooking.backend.models.enums.TravelClass;
import com.skybooking.backend.repositories.AirportRepository;
import com.skybooking.backend.repositories.FlightRepository;
import com.skybooking.backend.repositories.PassageRepository;
import com.skybooking.backend.repositories.PlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final PassageRepository passageRepository;
    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;

    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES =
            List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);
    private final Map<TravelClass, BigDecimal> CLASS_MULTIPLIERS = Map.of(
            TravelClass.ECONOMY, BigDecimal.valueOf(1.0),
            TravelClass.PREMIUM_ECONOMY, BigDecimal.valueOf(1.4),
            TravelClass.BUSINESS, BigDecimal.valueOf(2.5),
            TravelClass.FIRST_CLASS, BigDecimal.valueOf(4.0)
    );

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest dto) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if(dto.departureDate() != null) {
            startDateTime = dto.departureDate().atStartOfDay();
            endDateTime = dto.departureDate().atTime(LocalTime.MAX);
        }

        List<Flight> flights = flightRepository.searchFlightsDynamic(
                normalize(dto.originCode()),
                normalize(dto.destinationCode()),
                startDateTime,
                endDateTime,
                normalize(dto.airlineName()),
                dto.maxPrice(),
                dto.travelClass().name()
        );

        return flights.stream()
                .map(this::buildFlightSearch)
                .toList();
    }

    public FlightDetailResponse getFlightDetail(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + flightId));

        return buildFlightDetail(flight);
    }

    public OccupiedSeatsResponse getOccupiedSeats(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + flightId));

        List<String> occupiedSeats = passageRepository.findOccupiedSeatsByFlightId(flightId);

        return new OccupiedSeatsResponse(
                flightId, occupiedSeats
        );
    }

    public Integer getAvailableSeatsCount(Long flightId, TravelClass travelClass) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + flightId));

        return getAvailableSeatsCounts(flight, travelClass);
    }

    @Transactional
    public FlightDetailResponse createFlight(FlightRequest dto) {
        Plane plane = planeRepository.findById(dto.planeId())
                .orElseThrow(() -> new IllegalArgumentException("Plane no encontrado con ID: " + dto.planeId() ));
        Airport origin = airportRepository.findById(dto.originAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Aeropuerto no encontrado con ID: " + dto.originAirportId()));
        Airport destination = airportRepository.findById(dto.originAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Aeropuerto no encontrado con ID: " + dto.destinationAirportId()));

        if(origin.getId().equals(destination.getId())) throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");

        if(!dto.departureTime().isBefore(dto.arrivalTime())) throw new IllegalArgumentException("La fecha de salida debe ser anterior a la fecha de llegada.");

        if(flightRepository.existsOverlappingFlight(dto.planeId(),dto.departureTime(),dto.arrivalTime())) throw new IllegalArgumentException("El avión ya tiene un vuelo asignado que se superpone con el horario indicado.");

        Flight flight = new Flight();
        flight.setFlightNumber(dto.flightNumber());
        flight.setOriginAirport(origin);
        flight.setDestinationAirport(destination);
        flight.setDepartureTime(dto.departureTime());
        flight.setArrivalTime(dto.arrivalTime());
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setBasePrice(BigDecimal.valueOf(dto.basePrice()));
        flight.setPlane(plane);
        flight.setStops(dto.stops());
        Flight saved = flightRepository.save(flight);

        return buildFlightDetail(saved);
    }

    @Transactional
    public FlightDetailResponse updateFlight(Long flightId, FlightRequest dto) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + flightId));

        if(passageRepository.existsByFlight_IdAndBooking_Status(flightId, BookingStatus.CONFIRMED)){
            throw new IllegalStateException("No se puede modificar un vuelo que ya tiene reservas confirmadas.");
        }

        Plane plane = planeRepository.findById(dto.planeId())
                .orElseThrow(() -> new IllegalArgumentException("Plane no encontrado con ID: " + dto.planeId() ));
        Airport origin = airportRepository.findById(dto.originAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Aeropuerto no encontrado con ID: " + dto.originAirportId()));
        Airport destination = airportRepository.findById(dto.originAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Aeropuerto no encontrado con ID: " + dto.destinationAirportId()));

        if(origin.getId().equals(destination.getId())) throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");

        if(!dto.departureTime().isBefore(dto.arrivalTime())) throw new IllegalArgumentException("La fecha de salida debe ser anterior a la fecha de llegada.");

        if(flightRepository.existsOverlappingFlightExcluding(plane.getId(),dto.departureTime(),dto.arrivalTime(), flightId)) throw new IllegalArgumentException("El avión ya tiene otro vuelo asignado que se superpone con el horario indicado.");

        flight.setFlightNumber(dto.flightNumber());
        flight.setOriginAirport(origin);
        flight.setDestinationAirport(destination);
        flight.setDepartureTime(dto.departureTime());
        flight.setArrivalTime(dto.arrivalTime());
        flight.setBasePrice(BigDecimal.valueOf(dto.basePrice()));
        flight.setPlane(plane);
        flight.setStops(dto.stops() != null ? dto.stops() : 0);
        Flight updated = flightRepository.save(flight);

        return buildFlightDetail(updated);
    }

    @Transactional
    public void deleteFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado con ID: " + flightId));

        if(passageRepository.existsByFlight_IdAndBooking_StatusIn(flightId, ACTIVE_BOOKING_STATUSES)){
            throw new IllegalStateException("No se puede eliminar un vuelo con reservas activas (pendientes o confirmadas).");
        }

        flightRepository.delete(flight);
    }



    public BigDecimal calculatePrice(BigDecimal basePrice, TravelClass travelClass) {
        if(basePrice == null) throw new IllegalArgumentException("basePrice no puede ser nulo.");
        if(travelClass == null) throw new IllegalArgumentException("travelClass no puede ser nulo.");

        BigDecimal multiplier = CLASS_MULTIPLIERS.get(travelClass);
        if(multiplier == null) throw new IllegalArgumentException("No existe multiplicador definido para la clase: " + travelClass);

        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    private FlightDetailResponse buildFlightDetail(Flight flight) {
        AirportResponse origin = buildAirportResponse(flight.getOriginAirport());
        AirportResponse destination = buildAirportResponse(flight.getDestinationAirport());

        long minutes = ChronoUnit.MINUTES.between(
                flight.getDepartureTime(),
                flight.getArrivalTime()
        );

        String duration = "%dh %dm".formatted(minutes / 60, minutes % 60);

        int availableSeatsEconomy = getAvailableSeatsCounts(flight, TravelClass.ECONOMY);
        int availableSeatsBusiness =  getAvailableSeatsCounts(flight, TravelClass.BUSINESS);

        PlaneResponse plane = buildPlaneResponse(flight.getPlane());

        AirlineResponse airline = buildAirlineResponse(flight.getAirline());

        return new FlightDetailResponse(
                flight.getId(),
                normalize(flight.getFlightNumber()),
                origin,
                destination,
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                duration,
                flight.getStatus(),
                calculatePrice(flight.getBasePrice(), TravelClass.ECONOMY).doubleValue(),
                calculatePrice(flight.getBasePrice(), TravelClass.BUSINESS).doubleValue(),
                availableSeatsEconomy,
                availableSeatsBusiness,
                plane,
                airline,
                flight.getStops()
        );
    }
    private AirportResponse buildAirportResponse(Airport airport) {
        return new AirportResponse(
                airport.getId(),
                normalize(airport.getName()),
                normalize(airport.getIataCode()),
                normalize(airport.getIcaoCode()),
                normalize(airport.getCity()),
                normalize(airport.getCountry()),
                normalize(airport.getTimezone()),
                airport.getLatitude(),
                airport.getLongitude()
        );
    }
    private AirlineResponse buildAirlineResponse(Airline airline) {
        return new AirlineResponse(
                airline.getId(),
                normalize(airline.getName()),
                normalize(airline.getIataCode()),
                normalize(airline.getIcaoCode()),
                normalize(airline.getCountry()),
                normalize(airline.getWebsite()),
                normalize(airline.getLogoUrl())
        );
    }
    private PlaneResponse buildPlaneResponse(Plane plane) {
        return new PlaneResponse(
                plane.getId(),
                normalize(plane.getRegistration()),
                normalize(plane.getModel()),
                plane.getBusinessSeats(),
                plane.getEconomySeats(),
                plane.getTotalCapacity(),
                plane.getManufactureYear(),
                plane.getAirline() != null ? plane.getAirline().getId() : null,
                plane.getAirline() != null ? plane.getAirline().getName() : null
        );
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? "" : value;
    }

    private FlightSearchResponse buildFlightSearch(Flight flight) {
        long minutes = ChronoUnit.MINUTES.between(
                flight.getDepartureTime(),
                flight.getArrivalTime()
        );

        String duration = "%dh %dm".formatted(minutes / 60, minutes % 60);

        int availableSeats = getAvailableSeatsCounts(flight, TravelClass.ECONOMY) + getAvailableSeatsCounts(flight, TravelClass.BUSINESS);

        AirportSummaryResponse origin = buildAirportSummary(flight.getOriginAirport());
        AirportSummaryResponse destination = buildAirportSummary(flight.getOriginAirport());
        AirlineSummaryResponse airline = buildAirlineSummary(flight.getAirline());

        return new FlightSearchResponse(
                flight.getId(),
                flight.getFlightNumber(),
                origin,
                destination,
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                duration,
                flight.getStatus(),
                calculatePrice(flight.getBasePrice(),TravelClass.ECONOMY).doubleValue(),
                calculatePrice(flight.getBasePrice(),TravelClass.BUSINESS).doubleValue(),
                availableSeats,
                flight.getPlane().getModel(),
                airline,
                flight.getStops()
        );
    }
    private AirportSummaryResponse buildAirportSummary(Airport airport) {
        return new AirportSummaryResponse(
                airport.getId(),
                airport.getIataCode(),
                airport.getCity(),
                airport.getCountry()
        );
    }
    private AirlineSummaryResponse buildAirlineSummary(Airline airline) {
        return new AirlineSummaryResponse(
                airline.getId(),
                airline.getName(),
                airline.getIataCode(),
                airline.getLogoUrl()
        );
    }
    private int getAvailableSeatsCounts(Flight flight, TravelClass travelClass) {
        int totalSeats = travelClass.equals(TravelClass.BUSINESS)
                ? flight.getPlane().getBusinessSeats()
                : flight.getPlane().getEconomySeats();

        int occupiedSeats = passageRepository.countByFlight_IdAndTravelClassAndBooking_StatusNot(
                flight.getId(), travelClass, BookingStatus.CANCELLED);

        return totalSeats - occupiedSeats;
    }
}
