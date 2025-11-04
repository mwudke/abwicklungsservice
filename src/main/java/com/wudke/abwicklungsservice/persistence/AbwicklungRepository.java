package com.wudke.abwicklungsservice.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbwicklungRepository extends JpaRepository<AbwicklungEntity, String> {

}
