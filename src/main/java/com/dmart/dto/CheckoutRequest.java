package com.dmart.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CheckoutRequest {

    @NotBlank(message = "Fulfillment type is required")
    private String fulfillmentType;

    private LocalDateTime scheduledDate;

    private String deliveryAddress;

    public CheckoutRequest() {
    }

    public String getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(String fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}