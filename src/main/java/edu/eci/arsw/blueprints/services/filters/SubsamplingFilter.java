package edu.eci.arsw.blueprints.services.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("subsamplingFilter")

/**
 * A filter that applies subsampling to a blueprint.
 */
public class SubsamplingFilter implements BlueprintFilter {

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
        for (int i = 0; i < pts.size(); i += 2) {
            Point p = pts.get(i);
            filtered.add(new Point(p.getX(), p.getY()));
        }
        Blueprint bpCopy = new Blueprint(bp.getAuthor(), bp.getName());
        for (Point pt : filtered) {
            bpCopy.addPoint(pt);
        }
        return bpCopy;
    }
}