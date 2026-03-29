package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Hotel;
import com.example.demo.service.HotelService;
import com.example.demo.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotels", description = "Hotel management and search")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Hotel>> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotel created", hotelService.createHotel(hotel)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Hotel>> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotel updated", hotelService.updateHotel(id, hotel)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotel deleted", "Deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<HotelDTO>>> getHotels(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotels fetched", hotelService.getAllHotels(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelDTO>> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotel fetched", hotelService.convertToDTO(hotel)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<HotelDTO>>> searchHotels(
            @RequestParam String location, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotels found", hotelService.searchHotels(location, pageable)));
    }

    @GetMapping("/price-filter")
    public ResponseEntity<ApiResponse<Page<HotelDTO>>> filterByPrice(
            @RequestParam double min, @RequestParam double max, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotels found", hotelService.findHotelsByPriceRange(min, max, pageable)));
    }

    @GetMapping("/advanced-search")
    public ResponseEntity<ApiResponse<Page<HotelDTO>>> advancedSearch(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) Double rating,
            Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Hotels found", hotelService.searchAdvanced(city, min, max, rating, pageable)));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<Page<HotelDTO>>> getTopHotels(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Top hotels fetched", hotelService.getTopHotels(pageable)));
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getHotelRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Rooms fetched", roomService.getRoomsByHotel(hotelId)));
    }
}