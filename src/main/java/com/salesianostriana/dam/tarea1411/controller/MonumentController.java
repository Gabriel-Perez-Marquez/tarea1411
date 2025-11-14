package com.salesianostriana.dam.tarea1411.controller;


import com.salesianostriana.dam.tarea1411.model.Monument;
import com.salesianostriana.dam.tarea1411.repository.MonumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monument")
public class MonumentController {

    @Autowired
    private MonumentRepository monumentRepository;

    @GetMapping("")
    public ResponseEntity<List<Monument>> showAllMonuments(Model model) {
        List<Monument> monuments = monumentRepository.findAll();

        if (monuments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(monuments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMonumentById(@PathVariable Long id) {
        return monumentRepository.findById(id)
                .map(monument -> ResponseEntity.ok(monument.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monument not found"));

    }


    @GetMapping("/create")
    public ResponseEntity<String> createMonument(Monument monument){
        try {
            monumentRepository.save(monument);
            return ResponseEntity.status(HttpStatus.CREATED).body("Monument created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating monument");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMonument (@PathVariable Long id, Monument monument){
        try {
            if (!monumentRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monument not found");
            }
            monument.setId(id);
            monumentRepository.save(monument);
            return ResponseEntity.ok("Monument updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating monument");
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMonument(@PathVariable Long id) {
        if (monumentRepository.existsById(id)) {
            monumentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Monument deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monument not found");
        }
    }




}