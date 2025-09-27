/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {
    
    /** 
     * @throws BlueprintPersistenceException
     * @throws BlueprintNotFoundException
     */
    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);
        
        ibpp.saveBlueprint(bp0);
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        ibpp.saveBlueprint(bp);
        
        assertNotNull(ibpp.getBlueprint(bp.getAuthor(), bp.getName()),"Loading a previously stored blueprint returned null.");
        
        assertEquals(ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp, "Loading a previously stored blueprint returned a different blueprint.");
        
    }


    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }
        
        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaint",pts2);

        try{
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        }
        catch (BlueprintPersistenceException ex){
            
        }
                
        
    }

    /** 
     * @throws BlueprintPersistenceException
     * @throws BlueprintNotFoundException
     */
    @Test
    public void getAllBlueprints() throws BlueprintPersistenceException, BlueprintNotFoundException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts1 = new Point[]{new Point(1, 1), new Point(2, 2)};
        Blueprint bp1 = new Blueprint("alice", "paint1", pts1);

        Point[] pts2 = new Point[]{new Point(3, 3), new Point(4, 4)};
        Blueprint bp2 = new Blueprint("alice", "paint2", pts2);

        try {
            ibpp.saveBlueprint(bp1);
            ibpp.saveBlueprint(bp2);
        } catch (BlueprintPersistenceException e) {
            e.getMessage();
        }
       

        assertEquals(2, ibpp.getAllBlueprints().size(), "There should be two blueprints.");
        assertTrue(ibpp.getAllBlueprints().contains(bp1), "Blueprints should contain bp1.");
        assertTrue(ibpp.getAllBlueprints().contains(bp2), "Blueprints should contain bp2.");
    }

    /** 
     * @throws BlueprintPersistenceException
     */
    @Test
    public void getBlueprintNotFound() throws BlueprintPersistenceException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts = new Point[]{new Point(0, 0), new Point(10, 10)};
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        ibpp.saveBlueprint(bp);

        assertThrows(BlueprintNotFoundException.class, () -> {
            ibpp.getBlueprint("unknown", "thepaint");
        });
    }

    /** 
     * @throws BlueprintPersistenceException
     */
    @Test
    public void getEmptyBlueprints() throws BlueprintPersistenceException{
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintPersistenceException.class, () -> {
            ibpp.getAllBlueprints();
        });
    }

}
