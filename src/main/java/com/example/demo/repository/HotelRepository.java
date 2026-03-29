package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Search hotels by city
    Page<Hotel> findByCityContainingIgnoreCase(String city, Pageable pageable);

    // Search hotels by price range
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.rooms r WHERE r.basePrice BETWEEN :min AND :max")
    Page<Hotel> findHotelsByPriceRange(double min, double max, Pageable pageable);

    // Top hotels by rating
    @Query("SELECT h FROM Hotel h ORDER BY h.rating DESC")
    Page<Hotel> findTopHotels(Pageable pageable);

    // Advanced search
    @Query("""
        SELECT DISTINCT h FROM Hotel h
        JOIN h.rooms r
        WHERE (:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%')))
        AND (:min IS NULL OR r.basePrice >= :min)
        AND (:max IS NULL OR r.basePrice <= :max)
        AND (:rating IS NULL OR h.rating >= :rating)
    """)
    Page<Hotel> searchHotelsAdvanced(String city, Double min, Double max, Double rating, Pageable pageable);
}