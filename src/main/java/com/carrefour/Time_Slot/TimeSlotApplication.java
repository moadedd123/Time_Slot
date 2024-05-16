package com.carrefour.Time_Slot;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Delivery Service API", version = "1.0", description = "API for managing delivery methods and time slots"))
public class TimeSlotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeSlotApplication.class, args);
	}

}
