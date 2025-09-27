package edu.eci.arsw.blueprints.services.filters;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 * Interface for filtering blueprints.
 */
public interface BlueprintFilter {
    /**
     * Filter a blueprint.
     * @param bp the blueprint to filter
     * @return the filtered blueprint
     */
    Blueprint filter(Blueprint bp);
}