package org.example.hotel_soap.service;

import org.example.hotel_soap.model.Reservation;
import org.example.hotel_soap.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;

    @Value("${hotel.reservation.max-days}")
    private int maxReservationDays;

    @Value("${hotel.reservation.min-days}")
    private int minReservationDays;

    public Reservation createReservation(Long clientId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        validateReservationDates(checkInDate, checkOutDate);
        // Check if the room exists
        roomService.getRoom(roomId); // This will throw an exception if the room doesn't exist
        Reservation reservation = new Reservation(clientId, roomId, checkInDate, checkOutDate);
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    public Reservation updateReservation(Long id, Long clientId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        validateReservationDates(checkInDate, checkOutDate);
        Reservation reservation = getReservation(id);
        reservation.setClientId(clientId);
        reservation.setRoomId(roomId);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {
        long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (daysBetween < minReservationDays || daysBetween > maxReservationDays) {
            throw new IllegalArgumentException("Reservation must be between " + minReservationDays + " and " + maxReservationDays + " days");
        }
    }
}

