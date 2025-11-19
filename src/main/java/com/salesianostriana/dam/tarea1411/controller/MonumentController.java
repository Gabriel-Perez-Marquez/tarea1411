package com.salesianostriana.dam.tarea1411.controller;


import com.salesianostriana.dam.tarea1411.model.Monument;
import com.salesianostriana.dam.tarea1411.repository.MonumentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monument")
@Tag(name = "Documentos Controller", description = "Controlador de monumentos, para realizar la gestion de documentos almacenados en la base de datos")
public class MonumentController {

    @Autowired
    private MonumentRepository monumentRepository;

    @GetMapping("")
    @Operation(summary = "Obtener todos los monumentos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monumentos encontrados correctamente", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Monument.class))
            )),
            @ApiResponse(responseCode = "404", description = "Monumentos no encontrados", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<List<Monument>> showAllMonuments(Model model) {
        List<Monument> monuments = monumentRepository.findAll();

        if (monuments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(monuments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un monumentos por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monumento encontrado exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Monument.class)
            )),
            @ApiResponse(responseCode = "404", description = "Monumento no existe", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<String> getMonumentById(@PathVariable Long id) {
        return monumentRepository.findById(id)
                .map(monument -> ResponseEntity.ok(monument.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monument not found"));

    }


    @GetMapping("/create")
    @Operation(summary = "Crear un nuevo monumento")
    @ApiResponse(responseCode = "201", description = "Monumento creado correctamente", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Monument.class)
    ))
    @ApiResponse(responseCode = "404", description = "Error al crear el monumento", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProblemDetail.class)
    ))
    public ResponseEntity<String> createMonument(@RequestBody(
            description = "Detalles del monumento a crear",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = Monument.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "countryCode": "ES",
                                      "countryName": "Spain",
                                      "cityName": "Seville",
                                      "latitude": 37.3891,
                                      "longitude": -5.9845,
                                      "monumentName": "Giralda",
                                      "monumentDescription": "Historic bell tower of the Seville Cathedral.",
                                      "urlImage": "https://example.com/giralda.jpg"
                                    }
                                    """
                    )
            )
    ) Monument monument) {
        try {
            monumentRepository.save(monument);
            return ResponseEntity.status(HttpStatus.CREATED).body("Monument created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating monument");
        }
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Editar un monumento existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monumento modificado con exito", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Monument.class)
            )),
            @ApiResponse(responseCode = "404", description = "Error al modificar el monumento", content = @Content(
                    mediaType = "applicatio/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<String> updateMonument(@PathVariable Long id, Monument monument) {
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
    @Operation(summary = "Eliminar un monumento por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Monumento eliminado con exito", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Monument.class)
            )),
            @ApiResponse(responseCode = "404", description = "Error al eliminar el monumento", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<String> deleteMonument(@PathVariable Long id) {
        if (monumentRepository.existsById(id)) {
            monumentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Monument deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Monument not found");
        }
    }


}