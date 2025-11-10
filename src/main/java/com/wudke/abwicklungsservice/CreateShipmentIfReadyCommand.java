package com.wudke.abwicklungsservice;


import com.wudke.abwicklungsservice.client.CreateShipmentDto;
import com.wudke.abwicklungsservice.client.VersandServiceClient;
import com.wudke.abwicklungsservice.model.Recipient;
import com.wudke.abwicklungsservice.persistence.AbwicklungsEntity;
import com.wudke.abwicklungsservice.persistence.Address;
import com.wudke.abwicklungsservice.persistence.PaymentState;
import com.wudke.abwicklungsservice.persistence.RecipientEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CreateShipmentIfReadyCommand {

    @Autowired
    VersandServiceClient versandServiceClient;


    public void execute(AbwicklungsEntity abwicklungsEntity) {
        if (PaymentState.SUCCESS.equals(abwicklungsEntity.getPaymentState()) && abwicklungsEntity.getPrintId() != null) {
            log.info("Abwicklung {} is ready for shipment", abwicklungsEntity.getId());
            CreateShipmentDto createShipmentDto = new CreateShipmentDto(abwicklungsEntity.getId(), recipientEntityToDto(abwicklungsEntity.getRecipient()));
            versandServiceClient.createShipment(createShipmentDto);
        }
    }

    private static Recipient recipientEntityToDto(RecipientEntity entity) {
        Address entityAddress = entity.getAddress();

        Recipient.Address recipientAddress = new Recipient.Address(
                entityAddress.getStreet(),
                entityAddress.getHouseNumber(),
                entityAddress.getZipCode(),
                entityAddress.getCity()
        );

        return new Recipient(entity.getName(), recipientAddress);
    }

}
