package com.wudke.abwicklungsservice;

import com.wudke.abwicklungsservice.model.BestellEvent;
import com.wudke.abwicklungsservice.model.BezahlEvent;
import com.wudke.abwicklungsservice.model.DruckEvent;
import com.wudke.abwicklungsservice.model.Recipient;
import com.wudke.abwicklungsservice.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbwicklungsServiceTest {

    public static final String LICENSE_PLATE = "some licencePlate";
    public static final String NAME = "some Name";
    public static final String STREET = "some Street";
    public static final String HOUSE_NUMBER = "1";
    public static final String ZIP_CODE = "12345";
    public static final String CITY = "some City";
    public static final UUID SOME_UUID = java.util.UUID.randomUUID();
    @Mock
    AbwicklungsRepository abwicklungsRepositoryMock;

    @Mock
    CreateShipmentIfReadyCommand createShipmentIfReadyMock;


    @InjectMocks
    AbwicklungsService abwicklungsService;

    @Captor
    ArgumentCaptor<AbwicklungsEntity> abwicklungsEntityCaptor;


    @Test
    public void thatHandleNewBestellEventWorks() {
        BestellEvent bestellEvent = getBestellEvent();

        abwicklungsService.handleBestellEvent(bestellEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = assertAbwicklungsEntity();

        verify(createShipmentIfReadyMock).execute(value);
    }

    @Test
    public void thatHandleNewButPartiallyFilledBestellEventWorks() {
        BestellEvent bestellEvent = getBestellEvent();
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setId(SOME_UUID);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.of(abwicklungsEntity));

        abwicklungsService.handleBestellEvent(bestellEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = assertAbwicklungsEntity();

        verify(createShipmentIfReadyMock).execute(value);
    }

    @Test
    public void thatHandleKnownBestellEventWorks() {
        BestellEvent bestellEvent = getBestellEvent();
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setLicencePlate(LICENSE_PLATE);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.of(abwicklungsEntity));

        abwicklungsService.handleBestellEvent(bestellEvent);

        verifyNoMoreInteractions(abwicklungsRepositoryMock);
        verifyNoInteractions(createShipmentIfReadyMock);
    }

    private static @NotNull BestellEvent getBestellEvent() {
        return new BestellEvent(SOME_UUID, LICENSE_PLATE,
                new Recipient(NAME,
                        new Recipient.Address(STREET, HOUSE_NUMBER, ZIP_CODE, CITY)
                )
        );
    }

    @Test
    public void thatHandleBezahlEventWorks() {
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setId(SOME_UUID);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.of(abwicklungsEntity));

        BezahlEvent bezahlEvent = new BezahlEvent(SOME_UUID, PaymentState.SUCCESS);
        abwicklungsService.handleBezahlEvent(bezahlEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = abwicklungsEntityCaptor.getValue();
        assertNotNull(value);
        assertEquals(SOME_UUID, value.getId());
        assertEquals(PaymentState.SUCCESS, value.getPaymentState());

        verify(createShipmentIfReadyMock).execute(any());
    }

    @Test
    public void thatHandleBezahlEventWorksIfItArrivesBeforeBestellEvent() {
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setId(SOME_UUID);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.empty());

        BezahlEvent bezahlEvent = new BezahlEvent(SOME_UUID, PaymentState.SUCCESS);
        abwicklungsService.handleBezahlEvent(bezahlEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = abwicklungsEntityCaptor.getValue();
        assertNotNull(value);
        assertEquals(SOME_UUID, value.getId());
        assertEquals(PaymentState.SUCCESS, value.getPaymentState());

        verifyNoInteractions(createShipmentIfReadyMock);
    }

    @Test
    public void thatHandleDruckEventWorks() {
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setId(SOME_UUID);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.of(abwicklungsEntity));

        UUID printId = UUID.randomUUID();
        DruckEvent druckEvent = new DruckEvent(printId, SOME_UUID, LICENSE_PLATE);
        abwicklungsService.handleDruckEvent(druckEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = abwicklungsEntityCaptor.getValue();
        assertNotNull(value);
        assertEquals(SOME_UUID, value.getId());
        assertEquals(printId, value.getPrintId());

        verify(createShipmentIfReadyMock).execute(any());
    }

    @Test
    public void thatHandleDruckEventWorksIfItArrivesBeforeBestellEvent() {
        AbwicklungsEntity abwicklungsEntity = new AbwicklungsEntity();
        abwicklungsEntity.setId(SOME_UUID);
        when(abwicklungsRepositoryMock.findById(SOME_UUID)).thenReturn(Optional.empty());

        UUID printId = UUID.randomUUID();
        DruckEvent druckEvent = new DruckEvent(printId, SOME_UUID, LICENSE_PLATE);
        abwicklungsService.handleDruckEvent(druckEvent);

        verify(abwicklungsRepositoryMock).save(abwicklungsEntityCaptor.capture());
        AbwicklungsEntity value = abwicklungsEntityCaptor.getValue();
        assertNotNull(value);
        assertEquals(SOME_UUID, value.getId());
        assertEquals(printId, value.getPrintId());

        verifyNoInteractions(createShipmentIfReadyMock);
    }

    private AbwicklungsEntity assertAbwicklungsEntity() {
        AbwicklungsEntity value = abwicklungsEntityCaptor.getValue();
        assertNotNull(value);
        assertEquals(SOME_UUID, value.getId());
        assertEquals(LICENSE_PLATE, value.getLicencePlate());

        RecipientEntity recipient = value.getRecipient();
        assertNotNull(recipient);
        assertEquals(NAME, recipient.getName());

        Address address = recipient.getAddress();
        assertNotNull(address);
        assertEquals(STREET, address.getStreet());
        assertEquals(HOUSE_NUMBER, address.getHouseNumber());
        assertEquals(ZIP_CODE, address.getZipCode());
        assertEquals(CITY, address.getCity());
        return value;
    }
}