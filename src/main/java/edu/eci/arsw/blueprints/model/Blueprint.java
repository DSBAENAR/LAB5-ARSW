
package edu.eci.arsw.blueprints.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Represents a blueprint consisting of an author, name, and a list of points.
 * @author hcadavid
 * @author dsbaenar
 */
public class Blueprint {

    private String author=null;
    
    private List<Point> points=null;
    
    private String name=null;

    /**
     * Constructor for Blueprint.
     * @param author blueprint's author
     * @param name blueprint's name
     * @param pnts array of points
     */
    public Blueprint(String author,String name,Point[] pnts){
        this.author=author;
        this.name=name;
        points=Arrays.asList(pnts);
    }

    /**
     * Constructor for Blueprint.
     * @param author blueprint's author
     * @param name blueprint's name
     */
    public Blueprint(String author, String name){
        this.name=name;
        this.author=author;
        points=new ArrayList<>();
    }

    /**
     * Default constructor for Blueprint.
     */
    public Blueprint() {
    }    
    
    /** 
     * Get the name of the blueprint.
     * @return String
     */
    public String getName() {
        return name;
    }

    /** 
     * Get the author of the blueprint.
     * @return String
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Get points from the blueprint.
     * @return {@link java.util.List} of {@link edu.eci.arsw.blueprints.model.Point}
     */
    public List<Point> getPoints() {
        return points;
    }
    
    /** 
     * Add a point to the blueprint.
     * @param p the point to add
     */
    public void addPoint(Point p){
        this.points.add(p);
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "Blueprint{" + "author=" + author + ", name=" + name + '}';
    }

    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    /** 
     * Checks if two blueprints are equal.
     * @param obj the object to compare
     * @return boolean true if the blueprints are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Blueprint other = (Blueprint) obj;
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (Objects.equals(points, other.points)) {
            return true;
        }
        
        for (int i=0;i<this.points.size();i++){
            if (this.points.get(i)!=other.points.get(i)){
                return false;
            }
        }
        
        return true;
    }
    
    
    
}
