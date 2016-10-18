package boxes;

import java.awt.Point;

/**
 * Created by jggrime on 10/13/16.
 * POJO Box
 */
public class Box {
    public final int height;
    public final int width;
    public final Point botLeft;

    public Box(final int x, final int y, final int width, final int height) {
        botLeft = new Point(x, y);
        this.width = width;
        this.height = height;
    }
}
