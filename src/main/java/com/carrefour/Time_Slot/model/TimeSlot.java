package com.carrefour.Time_Slot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot extends RepresentationModel<TimeSlot>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DeliveryMethod deliveryMethod;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;

    public boolean isBooked(){
    	return isBooked;
    }

    public void setBooked(boolean isBooked){
    	this.isBooked = isBooked;
    }

}