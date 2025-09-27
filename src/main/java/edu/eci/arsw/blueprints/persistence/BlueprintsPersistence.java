/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

import java.util.Set;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 * Interface for blueprint persistence.
 * @author hcadavid
 * @author dsbaenar
 */
public interface BlueprintsPersistence {
    
    /**
     * Saves a new blueprint.
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    
    /**
     * Gets a blueprint by its author and name.
     * @param author blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String bprintname) throws BlueprintNotFoundException;

    
    /**
     * Gets all the blueprints in the system.
     * @return all the blueprints in the system
     * @throws BlueprintPersistenceException if there are no blueprints
     */
    public Set<Blueprint> getAllBlueprints() throws BlueprintPersistenceException;
    

    /**
     * Updates an existing blueprint.
     * @param bp the blueprint with updated information
     * @throws BlueprintPersistenceException if a persistence error occurs
     * @throws BlueprintNotFoundException if the blueprint does not exist
     */
    public void updateBlueprint(Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException;


    /**
     * Deletes an existing blueprint.
     * @param bp the blueprint to delete
     * @throws BlueprintNotFoundException if the blueprint does not exist
     * @throws BlueprintPersistenceException if a persistence error occurs
     */
    public void deleteBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    public void updateBlueprint(String author, String bpname, Blueprint blueprint);
}
