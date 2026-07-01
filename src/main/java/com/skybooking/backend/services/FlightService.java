package com.skybooking.backend.services;

import com.skybooking.backend.dtos.airline.AirlineSummaryResponse;
import com.skybooking.backend.dtos.airport.AirportSummaryResponse;
import com.skybooking.backend.dtos.flight.FlightSearchRequest;
import com.skybooking.backend.dtos.flight.FlightSearchResponse;
import com.skybooking.backend.models.Flight;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.TravelClass;
import com.skybooking.backend.repositories.FlightRepository;
import com.skybooking.backend.repositories.PassageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public BigDecimal calculatePrice(BigDecimal basePrice, TravelClass travelClass) {
        if(basePrice == null) throw new IllegalArgumentException("basePrice no puede ser nulo.");
        if(travelClass == null) throw new IllegalArgumentException("travelClass no puede ser nulo.");

        BigDecimal multiplier = CLASS_MULTIPLIERS.get(travelClass);
        if(multiplier == null) throw new IllegalArgumentException("No existe multiplicador definido para la clase: " + travelClass);

        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
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

        AirportSummaryResponse origin = new  AirportSummaryResponse(
                flight.getOriginAirport().getId(),
                flight.getOriginAirport().getIataCode(),
                flight.getOriginAirport().getCity(),
                flight.getOriginAirport().getCountry()
        );

        AirportSummaryResponse destination = new  AirportSummaryResponse(
                flight.getDestinationAirport().getId(),
                flight.getDestinationAirport().getIataCode(),
                flight.getDestinationAirport().getCity(),
                flight.getDestinationAirport().getCountry()
        );

        AirlineSummaryResponse airline = new AirlineSummaryResponse(
                flight.getAirline().getId(),
                flight.getAirline().getName(),
                flight.getAirline().getIataCode(),
                flight.getAirline().getLogoUrl()
        );

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
    private int getAvailableSeatsCounts(Flight flight, TravelClass travelClass) {
        int totalSeats = travelClass.equals(TravelClass.BUSINESS)
                ? flight.getPlane().getBusinessSeats()
                : flight.getPlane().getEconomySeats();

        int occupiedSeats = passageRepository.countByFlightIdAndTravelClassAndBookingStatusNot(
                flight.getId(), travelClass, BookingStatus.CANCELLED);

        return totalSeats - occupiedSeats;
    }
}
