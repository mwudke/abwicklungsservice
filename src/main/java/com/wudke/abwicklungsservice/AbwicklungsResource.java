package com.wudke.abwicklungsservice;

import com.wudke.abwicklungsservice.persistence.AbwicklungsEntity;
import com.wudke.abwicklungsservice.persistence.AbwicklungsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("v1/abwicklungen")
public class AbwicklungsResource {

    /*
     *      AI Generated
     *      prompt: create the missing endpoints
     *      input: I wrote short todos for both endpoints
     *
     *      update: had to fix some things by hand, also noticed that we should use ContainingIgnoreCase instead of strict find by
     */

    @Autowired
    private AbwicklungsRepository abwicklungsRepository;

    // GET /v1/abwicklungen?name=...&licensePlate=...
    @GetMapping
    public ResponseEntity<List<AbwicklungsEntity>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String licensePlate) {

        List<AbwicklungsEntity> result;

        if (name != null && licensePlate != null) {
            result = abwicklungsRepository.findByRecipientNameAndLicencePlate(name, licensePlate);
        } else if (name != null) {
            result = abwicklungsRepository.findByRecipientName(name);
        } else if (licensePlate != null) {
            result = abwicklungsRepository.findByLicencePlate(licensePlate);
        } else {
            result = abwicklungsRepository.findAll();
        }

        return ok(result);
    }

    // GET /v1/abwicklungen/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AbwicklungsEntity> getById(@PathVariable String id) {
       //i needed to add this try catch to handle invalid ids -max
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return notFound().build();
        }

        return abwicklungsRepository.findById(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

}
