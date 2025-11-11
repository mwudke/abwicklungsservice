package com.wudke.abwicklungsservice.client;

import org.springframework.stereotype.Component;

@Component
public class VersandServiceClient {

    public void createShipment(CreateShipmentDto createShipmentDto) {
        //TODO: impl. client, also consider what to do on errors...
        //POST /shipment
        //202 - Shipment was created
        //409 - Shipment was already created
        //400 - Validation error

    }
}
