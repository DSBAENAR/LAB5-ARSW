/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.services.filters.BlueprintFilter;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service for managing blueprints.
 * @author dsbaenar
 * @author hcadavid
 */
@Service
public class BlueprintsServices {
   
    private final BlueprintsPersistence bpp;
    private BlueprintFilter bpf;


    private final BlueprintFilter redundancyFilter;
    private final BlueprintFilter subsamplingFilter;

    /**
     * Constructor for BlueprintsServices.
     * @param bpp the blueprint persistence
     * @param bpf the blueprint filter
     * @param redundancyFilter the redundancy filter
     * @param subsamplingFilter the subsampling filter
     */
    public BlueprintsServices(BlueprintsPersistence bpp, BlueprintFilter bpf, @Qualifier("redundancyFilter") BlueprintFilter redundancyFilter, @Qualifier("subsamplingFilter") BlueprintFilter subsamplingFilter) {
        this.bpp = bpp;
        this.redundancyFilter = redundancyFilter;
        this.subsamplingFilter = subsamplingFilter;
        this.bpf = this.redundancyFilter;
    }
    /**
     * This method adds a new blueprint to the system
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists
     * @return the added blueprint
     */
    public Blueprint addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        bpp.saveBlueprint(bp); 
        return bp;
    }

    /**
     * Get all blueprints.
     * @return all the blueprints in the system
     * @throws BlueprintPersistenceException if there are no blueprints
     */
    public Set<Blueprint> getAllBlueprints() throws BlueprintPersistenceException {
        if (bpp != null) {
            return bpp.getAllBlueprints();
        }
        throw new BlueprintPersistenceException("There are no Blueprints.");
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        
        if(author!=null && name!=null) { return bpp.getBlueprint(author, name); }
        
        throw new BlueprintNotFoundException("The Blueprint made by " + author + " with name " + name + " does not exist.");
    }

    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintPersistenceException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintPersistenceException{

        Optional<Set<Blueprint>> bpsByAuthor = bpp.getAllBlueprints().stream().filter
        (bp -> bp.getAuthor()
        .equals(author))
        .collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toSet(), Optional::of));

        if (bpsByAuthor.isPresent() && !bpsByAuthor.get().isEmpty()) {
            return bpsByAuthor.get();
        }

        throw new BlueprintPersistenceException("The Blueprints made by " + author + " do not exist.");
    }

    /**
     * 
     * @param name blueprint's name
     * @return all the blueprints with the given name
     * @throws BlueprintPersistenceException if the given name doesn't exist
     *   
     */
    public Set<Blueprint> getBlueprintsByName(String name) throws BlueprintPersistenceException{
        
        Optional<Set<Blueprint>> bpsByName = bpp.getAllBlueprints().stream().filter
        (bp -> bp.getName()
        .equals(name))
        .collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toSet(), Optional::of));

        if (bpsByName.isPresent() && !bpsByName.get().isEmpty()) {
            return bpsByName.get();
        }

        throw new BlueprintPersistenceException("The Blueprint " + name + " does not exist.");
    }

    /**
     * Adds a filter to the service.
     * @param filter the filter to add
     */
    public void addFilter (BlueprintFilter filter) {
        this.bpf = filter;
    }

    /**
     * Activates a blueprint filter by name.
     * Recognized values (case-insensitive):
     * - "redundancy" or "redundancyFilter"
     * - "subsample", "subsampling" or "subsamplingFilter"
     * Pass null to deactivate filters (will return unfiltered blueprints).
     * @param filterName the name of the filter to activate
     */
    public void addFilter(String filterName) {
        if (filterName == null) {
            this.bpf = null;
            return;
        }
        String key = filterName.trim().toLowerCase();
        switch (key) {
            case "redundancy":
            case "redundancyfilter":
                this.bpf = redundancyFilter;
                break;
            case "subsample":
            case "subsampling":
            case "subsamplingfilter":
                this.bpf = subsamplingFilter;
                break;
            default:
                throw new IllegalArgumentException("Filtro desconocido: " + filterName + ". Use 'redundancy' o 'subsampling'.");
        }
    }

    /**
     * Updates an existing blueprint.
     * @param name the name of the blueprint to update
     * @param filter the filter to apply (if any)
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     * @throws BlueprintPersistenceException if there's an error during persistence
     */
    public void updateBlueprint(String name, String filter) throws BlueprintNotFoundException, BlueprintPersistenceException {
        Set<Blueprint> existingBpSet = getBlueprintsByName(name);
        if (existingBpSet == null || existingBpSet.isEmpty()) {
            throw new BlueprintNotFoundException("No Blueprint with name " + name + " exists.");
        }

        Blueprint bp = existingBpSet.iterator().next();
        if (!bp.getName().equals(name)) {
            throw new BlueprintPersistenceException("The name of the updated blueprint must match the existing one.");
        }
        if (filter != null) {
            addFilter(filter);
            if (bpf != null) {
                bp = bpf.filter(bp);
            }
        }
        bpp.updateBlueprint(bp);
    }

    /**
     * Deletes an existing blueprint.
     * @param name the name of the blueprint to delete
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     * @throws BlueprintPersistenceException if there's an error during persistence
     */
    public void deleteBlueprint(String name) throws BlueprintPersistenceException {
        Set<Blueprint> existingBpSet = getBlueprintsByName(name);
        if (existingBpSet == null || existingBpSet.isEmpty()) {
            throw new BlueprintPersistenceException("No Blueprint with name " + name + " exists.");
        }
        Blueprint bp = existingBpSet.iterator().next();
        bpp.deleteBlueprint(bp);
    }

    /**
     * 
     * @param author blueprint's author
     * @param bpname blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintPersistenceException if there is no such blueprint
     */
    public Object getBlueprintByNameAndAuthor(String author, String bpname) throws BlueprintPersistenceException {
        Optional<Blueprint> bp = bpp.getAllBlueprints().stream().filter
        (bprint -> bprint.getAuthor().equals(author) && bprint.getName().equals(bpname))
        .findFirst();
        if (bp.isPresent()) {
            return bp.get();
        }
        throw new BlueprintPersistenceException("The Blueprint " + bpname + " by " + author + " does not exist.");
    }
    public void updateBlueprint(String author, String bpname, Blueprint blueprint) throws BlueprintNotFoundException, BlueprintPersistenceException {
        Blueprint existingBlueprint = getBlueprint(author, bpname);
        if (existingBlueprint == null) {
            throw new BlueprintNotFoundException("No Blueprint with name " + bpname + " by author " + author + " exists.");
        }
        if (!existingBlueprint.getAuthor().equals(blueprint.getAuthor()) || !existingBlueprint.getName().equals(blueprint.getName())) {
            throw new BlueprintPersistenceException("The author and name of the updated blueprint must match the existing one.");
        }
        bpp.updateBlueprint(author, bpname, blueprint);

    }

}
