package boxes;

import java.awt.*;

/**
 * Created by jggrime on 10/13/16.
 */
public class Line {

    public Line copy() {
        return new Line(
                new Point(start.x, start.y),
                new Point(end.x, end.y),
                orientation,
                magnitue
        );
    }

    public enum Orientation {
        VERTICAL,
        HORIZANTAL
    }

    public final Point start;
    public final Point end;
    public final Orientation orientation;
    public final int magnitue;

    public Line(final Point start, final Point end, final Orientation orientation, final int magnitue) {
        this.start = start;
        this.end = end;
        this.orientation = orientation;
        this.magnitue = magnitue;
    }
}
