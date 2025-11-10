package com.wudke.abwicklungsservice;

import com.wudke.abwicklungsservice.model.BestellEvent;
import com.wudke.abwicklungsservice.model.BezahlEvent;
import com.wudke.abwicklungsservice.model.DruckEvent;
import com.wudke.abwicklungsservice.model.Recipient;
import com.wudke.abwicklungsservice.persistence.AbwicklungsEntity;
import com.wudke.abwicklungsservice.persistence.AbwicklungsRepository;
import com.wudke.abwicklungsservice.persistence.Address;
import com.wudke.abwicklungsservice.persistence.RecipientEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
public class AbwicklungsService {

    @Autowired
    AbwicklungsRepository abwicklungsRepository;

    @Autowired
    CreateShipmentIfReadyCommand createShipmentIfReady;

    //assumes Event consumer calls this service function
    public void handleBestellEvent(BestellEvent bestellEvent) {
        AbwicklungsEntity abwicklungsEntity;
        Optional<AbwicklungsEntity> entity = abwicklungsRepository.findById(bestellEvent.id());
        if (entity.isPresent()) {
            abwicklungsEntity = entity.get();

            if (!isBlank(abwicklungsEntity.getLicencePlate())) {
                log.warn("Already got BestellEvent {}", bestellEvent.id());
                return;
            }

        } else {
            abwicklungsEntity = new AbwicklungsEntity();
            abwicklungsEntity.setId(bestellEvent.id());
        }

        abwicklungsEntity.setLicencePlate(bestellEvent.licensePlate());
        RecipientEntity recipientEntity = new RecipientEntity(bestellEvent.recipient().name(), dtoAddressToPersistence(bestellEvent.recipient().address()));
        abwicklungsEntity.setRecipient(recipientEntity);

        abwicklungsRepository.save(abwicklungsEntity);

        createShipmentIfReady.execute(abwicklungsEntity);
    }

    //assumes Event consumer calls this service function
    public void handleBezahlEvent(BezahlEvent bezahlEvent) {
        Optional<AbwicklungsEntity> optionalAbwicklungsEntity = abwicklungsRepository.findById(bezahlEvent.reference());
        if (optionalAbwicklungsEntity.isPresent()) {
            AbwicklungsEntity abwicklungsEntity = optionalAbwicklungsEntity.get();

            abwicklungsEntity.setPaymentState(bezahlEvent.status());
            abwicklungsRepository.save(abwicklungsEntity);

            createShipmentIfReady.execute(abwicklungsEntity);

        } else {
            AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
            abwicklungsEntity.setId(bezahlEvent.reference());
            abwicklungsEntity.setPaymentState(bezahlEvent.status());
            abwicklungsRepository.save(abwicklungsEntity);
        }
    }

    //assumes Event consumer calls this service function
    public void handleDruckEvent(DruckEvent druckEvent) {
        Optional<AbwicklungsEntity> optionalAbwicklungsEntity = abwicklungsRepository.findById(druckEvent.reference());
        //todo: can the licence plate missmatch? do we need to check and what do we do?
        if (optionalAbwicklungsEntity.isPresent()) {
            AbwicklungsEntity abwicklungsEntity = optionalAbwicklungsEntity.get();

            abwicklungsEntity.setPrintId(druckEvent.id());
            abwicklungsRepository.save(abwicklungsEntity);

            createShipmentIfReady.execute(abwicklungsEntity);

        } else {
            AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
            abwicklungsEntity.setId(druckEvent.reference());
            abwicklungsEntity.setPrintId(druckEvent.id());
            abwicklungsRepository.save(abwicklungsEntity);
        }
    }

    private static Address dtoAddressToPersistence(Recipient.Address dtoAddress) {
        return new Address(dtoAddress.street(), dtoAddress.houseNumber(), dtoAddress.zipCode(), dtoAddress.city());
    }

}
