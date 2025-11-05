package com.wudke.abwicklungsservice.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AbwicklungsRepository extends JpaRepository<AbwicklungsEntity, UUID> {

    List<AbwicklungsEntity> findByLicencePlate(String licensePlate);

    List<AbwicklungsEntity> findByRecipientName(String name);

    List<AbwicklungsEntity> findByRecipientNameAndLicencePlate(String name, String licensePlate);
}
