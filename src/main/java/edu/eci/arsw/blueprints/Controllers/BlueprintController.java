package edu.eci.arsw.blueprints.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/blueprints")
/**
 * Controller for managing blueprints.
 */
public class BlueprintController {

    private final BlueprintsServices bps;

    /**
     * Constructor for BlueprintController.
     * @param bps the BlueprintsServices instance
     */
    public BlueprintController(BlueprintsServices bps) {
        this.bps = bps;
    }

    /**
     * Get all blueprints.
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity<?> getAllBlueprints() {
        try{
            return ResponseEntity.ok(bps.getAllBlueprints());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Get a blueprint by author.
     * @param author the author of the blueprint
     * @return ResponseEntity
     */
    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintByAuthor(@PathVariable String author){
        try {
            return ResponseEntity.ok(bps.getBlueprintsByAuthor(author));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(404).body(e.getMessage());
            
        }
    }


    /**
     * Get a blueprint by name.
     * @param name the name of the blueprint
     * @return ResponseEntity
     */
    @GetMapping("/blueprint/name/{name}")
    public ResponseEntity<?> getBlueprintByName(@PathVariable String name){
        try {
            return ResponseEntity.ok(bps.getBlueprintsByName(name));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity. status(400).body(e.getMessage());
            
        }
    }
    
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByNameAndAuthor(@PathVariable String author, @PathVariable String bpname){
        try {
            return ResponseEntity.ok(bps.getBlueprintByNameAndAuthor(author, bpname));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity. status(404).body(e.getMessage());
            
        }
    }

    /**
     * Add a new blueprint.
     * @param bp the blueprint to add
     * @return ResponseEntity
     */
    @PostMapping("/blueprint")
    public ResponseEntity<?> addBlueprint(@RequestBody Blueprint bp){
        try {
            bps.addNewBlueprint(bp);
            return ResponseEntity.status(201).body("The Blueprint made by " + bp.getAuthor() + " with name " + bp.getName() + " has been created.");
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Update a blueprint by name.
     * @param name the name of the blueprint
     * @param filter the filter to apply
     * @return ResponseEntity
     */
    @PutMapping("/blueprint/{name}/filter/{filter}")
    public ResponseEntity<?> updateBlueprint(@PathVariable String name, @PathVariable String filter) {
        try {
            bps.updateBlueprint(name, filter);
            Set<Blueprint> bp = bps.getBlueprintsByName(name);
            return ResponseEntity.ok("The Blueprint made by "  + bp.iterator().next().getAuthor() + " with name " + bp.iterator().next().getName() + " has been updated.");
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Update a blueprint by author and name.
     * @param author the author of the blueprint
     * @param bpname the name of the blueprint
     * @param blueprint the updated blueprint
     * @return ResponseEntity
     */
    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint) {
        try {
            bps.updateBlueprint(author, bpname, blueprint);
            return ResponseEntity.ok("Blueprint updated successfully.");
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Delete a blueprint by name.
     * @param name the name of the blueprint to delete
     * @return ResponseEntity
     */
    @DeleteMapping("/blueprint/{name}")
    public ResponseEntity<?> deleteBlueprint(@PathVariable String name) {
        try {
            bps.deleteBlueprint(name);
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok("Blueprint " + name + " has been deleted.");
    }

    
}
