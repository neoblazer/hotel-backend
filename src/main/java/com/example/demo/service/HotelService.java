package com.example.demo.service;

import com.example.demo.dto.HotelDTO;
import com.example.demo.entity.Hotel;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updated) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotel.setName(updated.getName());
        hotel.setCity(updated.getCity());
        hotel.setState(updated.getState());
        hotel.setRating(updated.getRating());
        hotel.setImageUrl(updated.getImageUrl());
        hotel.setDescription(updated.getDescription());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found");
        }
        hotelRepository.deleteById(id);
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
    }

    public Page<HotelDTO> getAllHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<HotelDTO> searchHotels(String city, Pageable pageable) {
        return hotelRepository.findByCityContainingIgnoreCase(city, pageable).map(this::convertToDTO);
    }

    public Page<HotelDTO> findHotelsByPriceRange(double min, double max, Pageable pageable) {
        return hotelRepository.findHotelsByPriceRange(min, max, pageable).map(this::convertToDTO);
    }

    public Page<HotelDTO> getTopHotels(Pageable pageable) {
        return hotelRepository.findTopHotels(pageable).map(this::convertToDTO);
    }

    public Page<HotelDTO> searchAdvanced(String city, Double min, Double max, Double rating, Pageable pageable) {
        return hotelRepository.searchHotelsAdvanced(city, min, max, rating, pageable).map(this::convertToDTO);
    }

    public HotelDTO convertToDTO(Hotel hotel) {
        return new HotelDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getCity(),
                hotel.getState(),
                hotel.getRating(),
                hotel.getImageUrl(),
                hotel.getDescription()
        );
    }
}