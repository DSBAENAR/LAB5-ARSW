/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

/**
 * Exception thrown when a blueprint is not found.
 * @author hcadavid
 */
public class BlueprintNotFoundException extends Exception{

    /**
     * @param message the detail message
     */
    public BlueprintNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message the detail message
     * @param cause the cause
     */
    public BlueprintNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
