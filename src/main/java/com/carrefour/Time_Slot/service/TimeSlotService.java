package com.carrefour.Time_Slot.service;

import com.carrefour.Time_Slot.model.DeliveryMethod;
import com.carrefour.Time_Slot.model.TimeSlot;
import com.carrefour.Time_Slot.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeSlotService {
    private final TimeSlotRepository repository;

    public TimeSlotService(TimeSlotRepository repository) {
        this.repository = repository;
    }

    public List<TimeSlot> getAvailableTimeSlots(DeliveryMethod method, LocalDateTime dateTime) {
        return repository.findByDeliveryMethodAndStartTimeAfter(method, dateTime);
    }

    public TimeSlot getTimeSlot(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid time slot ID"));
    }

    public TimeSlot bookTimeSlot(Long id) {
        TimeSlot timeSlot = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid time slot ID"));
        if (timeSlot.isBooked()) {
            throw new IllegalStateException("Time slot already booked");
        }
        timeSlot.setBooked(true);
        return repository.save(timeSlot);
    }
}
