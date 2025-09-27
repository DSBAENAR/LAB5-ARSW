/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.model;

/**
 * Represents a point in 2D space.
 * @author hcadavid
 */
public class Point {
   
    private int x;
    private int y;

    /**
     * Constructor for Point.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Default constructor for Point.
     */
    public Point() {
    }    
    /**
     *  Gets the x coordinate of the point.
     * @return int the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the point.
     * @param x the new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the point.
     * @return int the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the point.
     * @param y the new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
    
    
    
}
