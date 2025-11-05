package com.wudke.abwicklungsservice.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbwicklungsRepository extends JpaRepository<AbwicklungsEntity, UUID> {

    Iterable<AbwicklungsEntity> findByLicencePlate(String licensePlate);

    Iterable<AbwicklungsEntity> findByRecipientName(String name);

    Iterable<AbwicklungsEntity> findByRecipientNameAndLicencePlate(String name, String licensePlate);
}
