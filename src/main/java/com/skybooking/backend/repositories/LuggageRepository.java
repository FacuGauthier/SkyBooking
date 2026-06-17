package com.skybooking.backend.repositories;

import com.skybooking.backend.models.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuggageRepository extends JpaRepository<Luggage, Long> {
}
