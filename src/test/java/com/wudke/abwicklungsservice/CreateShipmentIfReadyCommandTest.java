package com.wudke.abwicklungsservice;

import com.wudke.abwicklungsservice.client.CreateShipmentDto;
import com.wudke.abwicklungsservice.client.ShipmentRecipientDto;
import com.wudke.abwicklungsservice.client.VersandServiceClient;
import com.wudke.abwicklungsservice.persistence.AbwicklungsEntity;
import com.wudke.abwicklungsservice.persistence.Address;
import com.wudke.abwicklungsservice.persistence.PaymentState;
import com.wudke.abwicklungsservice.persistence.RecipientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateShipmentIfReadyCommandTest {

    public static final UUID ID = UUID.randomUUID();
    public static final UUID PRINT_ID = UUID.randomUUID();
    public static final String NAME = "some name";
    public static final String STREET = "some street";
    public static final String HOUSE_NUMBER = "23";
    public static final String ZIP_CODE = "12345";
    public static final String CITY = "some city";

    @Mock
    private VersandServiceClient versandServiceClientMock;

    @InjectMocks
    private CreateShipmentIfReadyCommand command;

    @Test
    public void thatWhenReady_callsCreateShipment() {
        AbwicklungsEntity entity = createEntity();

        command.execute(entity);

        ArgumentCaptor<CreateShipmentDto> captor = ArgumentCaptor.forClass(CreateShipmentDto.class);
        verify(versandServiceClientMock, times(1)).createShipment(captor.capture());

        CreateShipmentDto dto = captor.getValue();
        assertEquals(ID.toString(), dto.parcelId());

        ShipmentRecipientDto rec = dto.recipient();
        assertNotNull(rec);
        assertEquals(NAME, rec.name());
        assertEquals(STREET, rec.street());
        assertEquals(HOUSE_NUMBER, rec.houseNumber());
        assertEquals(ZIP_CODE, rec.zipCode());
        assertEquals(CITY, rec.city());
    }

    @Test
    public void thatWhenPaymentNotSuccess_doesNotCallCreateShipment() {
        AbwicklungsEntity entity = createEntity();
        entity.setPaymentState(PaymentState.FAILURE);

        command.execute(entity);

        verify(versandServiceClientMock, never()).createShipment(any());
    }

    @Test
    public void thatWhenPaymentNull_doesNotCallCreateShipment() {
        AbwicklungsEntity entity = createEntity();
        entity.setPaymentState(null);

        command.execute(entity);

        verify(versandServiceClientMock, never()).createShipment(any());
    }

    @Test
    public void thatWhenPrintIdMissing_doesNotCallCreateShipment() {
        AbwicklungsEntity entity = createEntity();
        entity.setPrintId(null);

        command.execute(entity);

        verify(versandServiceClientMock, never()).createShipment(any());
    }

    private AbwicklungsEntity createEntity() {
        AbwicklungsEntity entity = new AbwicklungsEntity();

        entity.setId(ID);
        entity.setPaymentState(PaymentState.SUCCESS);
        entity.setPrintId(PRINT_ID);

        RecipientEntity recipient = new RecipientEntity();
        recipient.setName(NAME);

        Address addr = new Address();
        addr.setStreet(STREET);
        addr.setHouseNumber(HOUSE_NUMBER);
        addr.setZipCode(ZIP_CODE);
        addr.setCity(CITY);
        recipient.setAddress(addr);

        entity.setRecipient(recipient);

        return entity;
    }
}