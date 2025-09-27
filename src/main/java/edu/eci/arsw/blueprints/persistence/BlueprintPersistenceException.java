/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

/**
 * Exception thrown when a persistence error occurs.
 * @author hcadavid
 */
public class BlueprintPersistenceException extends Exception{

    /**
     * @param message the detail message
     */
    public BlueprintPersistenceException(String message) {
        super(message);
    }

    /**
     * @param message the detail message
     * @param cause the cause
     */
    public BlueprintPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
