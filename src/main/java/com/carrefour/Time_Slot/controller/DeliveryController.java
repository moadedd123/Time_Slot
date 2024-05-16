package com.carrefour.Time_Slot.controller;

import com.carrefour.Time_Slot.model.DeliveryMethod;
import com.carrefour.Time_Slot.model.TimeSlot;
import com.carrefour.Time_Slot.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class DeliveryController {
    private final TimeSlotService timeSlotService;

    public DeliveryController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @Operation(summary = "Get all available delivery methods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the delivery methods",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryMethod[].class))})
    })
    @GetMapping("/delivery-methods")
    public ResponseEntity<DeliveryMethod[]> getDeliveryMethods() {
        return ResponseEntity.ok(DeliveryMethod.values());
    }

    @Operation(summary = "Get available time slots for a specific delivery method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the time slots",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))})
    })
    @GetMapping("/time-slots")
    public ResponseEntity<CollectionModel<EntityModel<TimeSlot>>> getTimeSlots(@RequestParam DeliveryMethod method, @RequestParam LocalDateTime dateTime) {
        List<TimeSlot> timeSlots = timeSlotService.getAvailableTimeSlots(method, dateTime);
        List<EntityModel<TimeSlot>> timeSlotModels = timeSlots.stream()
                .map(timeSlot -> EntityModel.of(timeSlot,
                        linkTo(methodOn(DeliveryController.class).getTimeSlot(timeSlot.getId())).withSelfRel(),
                        linkTo(methodOn(DeliveryController.class).bookTimeSlot(timeSlot.getId())).withRel("book")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(timeSlotModels,
                linkTo(methodOn(DeliveryController.class).getTimeSlots(method, dateTime)).withSelfRel()));
    }

    @Operation(summary = "Get a specific time slot by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the time slot",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class))}),
            @ApiResponse(responseCode = "404", description = "Time slot not found",
                    content = @Content)
    })
    @GetMapping("/time-slots/{id}")
    public ResponseEntity<EntityModel<TimeSlot>> getTimeSlot(@PathVariable Long id) {
        TimeSlot timeSlot = timeSlotService.getTimeSlot(id);
        EntityModel<TimeSlot> timeSlotModel = EntityModel.of(timeSlot,
                linkTo(methodOn(DeliveryController.class).getTimeSlot(id)).withSelfRel(),
                linkTo(methodOn(DeliveryController.class).bookTimeSlot(id)).withRel("book"));

        return ResponseEntity.ok(timeSlotModel);
    }

    @Operation(summary = "Book a specific time slot by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Time slot booked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class))}),
            @ApiResponse(responseCode = "404", description = "Time slot not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Time slot already booked",
                    content = @Content)
    })
    @PostMapping("/book-time-slot/{id}")
    public ResponseEntity<EntityModel<TimeSlot>> bookTimeSlot(@PathVariable Long id) {
        TimeSlot bookedTimeSlot = timeSlotService.bookTimeSlot(id);
        EntityModel<TimeSlot> timeSlotModel = EntityModel.of(bookedTimeSlot,
                linkTo(methodOn(DeliveryController.class).getTimeSlot(id)).withSelfRel());

        return ResponseEntity.created(timeSlotModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(timeSlotModel);
    }
}
