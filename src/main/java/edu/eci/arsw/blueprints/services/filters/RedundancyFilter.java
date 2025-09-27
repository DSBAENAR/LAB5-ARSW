package edu.eci.arsw.blueprints.services.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component("redundancyFilter")

/**
 * A filter that removes redundant points from a blueprint.
 */
public class RedundancyFilter implements BlueprintFilter {

    /**
     * @param bp the blueprint to filter
     * @return Blueprint
     */
    @Override
    public Blueprint filter(Blueprint bp) {
        if (bp == null) {
            return null;
        }
        List<Point> pts = bp.getPoints();
        if (pts == null || pts.size() <= 1) {
            List<Point> copy = pts == null ? new ArrayList<>() : new ArrayList<>(pts);
            Blueprint bpCopy = new Blueprint(bp.getAuthor(), bp.getName());
            for (Point pt : copy) {
                bpCopy.addPoint(pt);
            }
            return bpCopy;
        }
        List<Point> filtered = new ArrayList<>();
        Point prev = null;
        for (Point p : pts) {
            if (prev == null || p.getX() != prev.getX() || p.getY() != prev.getY()) {
                filtered.add(new Point(p.getX(), p.getY()));
                prev = p;
            }
        }
        Blueprint filteredBp = new Blueprint(bp.getAuthor(), bp.getName());
        for (Point pt : filtered) {
            filteredBp.addPoint(pt);
        }
        return filteredBp;
    }
}