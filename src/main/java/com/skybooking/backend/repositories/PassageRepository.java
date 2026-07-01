package com.skybooking.backend.repositories;

import com.skybooking.backend.models.Passage;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.TravelClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {
}
