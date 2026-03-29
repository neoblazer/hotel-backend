package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
    private Double rating;
    private String imageUrl;
    private String description;  // ← NEW field (was missing in old version)
}