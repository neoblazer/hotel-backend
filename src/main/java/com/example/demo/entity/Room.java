package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"bookings"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double basePrice;

    @Column(nullable = false)
    private int totalRooms;

    private String imageUrl;    // ← required by RoomDTO
    private String amenities;   // ← stored as comma-separated: "WiFi,AC,TV"
    private Double rating;      // ← required by RoomDTO
    private Integer capacity;   // ← required by RoomDTO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;
}