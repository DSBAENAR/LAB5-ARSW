/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

/**
 * A blueprint persistence implementation that stores blueprints in memory.
 * @author dsbaenar
 */

@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final ConcurrentMap<Tuple<String,String>,Blueprint> blueprints = new ConcurrentHashMap<>();

    /**
     * Constructor for InMemoryBlueprintPersistence.
     */
    public InMemoryBlueprintPersistence() {
        Blueprint bp=new Blueprint("dsbaenar", "ECI Blueprint", new Point[]{new Point(140, 140),new Point(115, 115)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("dsbaenar", "Highway Blueprint", new Point[]{new Point(10, 10),new Point(15, 15)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("Maria", "House Blueprint", new Point[]{new Point(0, 0),new Point(10, 10)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
    }    
    
    /**
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Tuple<String,String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        if (blueprints.putIfAbsent(key, bp) != null) {
            throw new BlueprintPersistenceException("The given blueprint already exists.");
        }      
    }
    /**
     * @param author blueprint's author
     * @param bprintname blueprint's name
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Blueprint bp = blueprints.get(new Tuple<>(author, bprintname));
        if (bp != null) {
            return bp;
        }
        throw new BlueprintNotFoundException("Blueprint not found: " + author + ", " + bprintname);
    }

    /**
     * @return all the blueprints in the system
     * @throws BlueprintPersistenceException if there are no blueprints
     */
    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintPersistenceException {
        if (blueprints.isEmpty()){
            throw new BlueprintPersistenceException("There are no Blueprints.");
        }
        // ConcurrentHashMap.values() is thread-safe for iteration
        return Set.copyOf(blueprints.values());
    }

    /** 
     * @param bp the blueprint to update
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     */
    @Override
    public void updateBlueprint(Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException {
        Tuple<String,String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        if (blueprints.replace(key, bp) == null) {
            throw new BlueprintNotFoundException("Blueprint not found: " + bp);
        }
    }

    /** 
     * @param bp the blueprint to delete
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     * @throws BlueprintPersistenceException if there's an error during persistence
     */
    @Override
    public void deleteBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Tuple<String,String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        if (blueprints.remove(key) == null) {
            throw new BlueprintPersistenceException("Blueprint not found: " + bp);
        }
    }

    @Override
    public void updateBlueprint(String author, String bpname, Blueprint blueprint) {
    Tuple<String, String> key = new Tuple<>(author, bpname);
    blueprints.replace(key, blueprint);
    }
}
