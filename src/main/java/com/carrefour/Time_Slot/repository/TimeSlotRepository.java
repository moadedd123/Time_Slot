package com.carrefour.Time_Slot.repository;

import com.carrefour.Time_Slot.model.DeliveryMethod;
import com.carrefour.Time_Slot.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDeliveryMethodAndStartTimeAfter(DeliveryMethod deliveryMethod, LocalDateTime startTime);
}