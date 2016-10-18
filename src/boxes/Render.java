package boxes;

import java.util.List;
import java.util.Objects;

/**
 * Created by jggrime on 10/13/16.
 */
public class Render {

    public static void drawCuttingLines(final Line lineToCut, final Line lineToBeCut) {
        System.out.println("Cut: (" + lineToCut.start.x + ", " + lineToCut.start.y + ")->(" + lineToCut.end.x + ", " + lineToCut.end.y + ") mag: " + lineToCut.magnitue + "\n" +
                "Line: (" + lineToBeCut.start.x + ", " + lineToBeCut.start.y + ")->(" + lineToBeCut.end.x + ", " + lineToBeCut.end.y + ") mag: " + lineToBeCut.magnitue);
        drawGrid((row, col) -> {
            char value1 = drawLineWithMagnitude(lineToCut, row, col);
            char value2 = drawLineWithMagnitude(lineToBeCut, row, col);
            if (value1 == 'H' || value2 == 'H') {
                return 'H';
            } else if (value1 == 'X' || value2 == 'X') {
                return 'X';
            } else {
                return ' ';
            }
        });
    }

    private static char drawLineWithMagnitude(final Line line, final int row, final int col) {
        final Line l = Converter.normalizeMagnitude(line);
        if (Line.Orientation.VERTICAL.equals(l.orientation)) {
            if (Converter.between(row, l.start.y - 1, l.end.y + 1)) {
                if (line.start.x == col) {
                    return 'H';
                } else if (Converter.between(col, l.start.x - 1, l.start.x + l.magnitue)) {
                    return 'X';
                }
            }
        } else {
            if (Converter.between(col, l.start.x - 1, l.end.x + 1)) {
                if (line.start.y == row) {
                    return 'H';
                } else if (Converter.between(row, l.start.y - 1, l.start.y + l.magnitue)) {
                    return 'X';
                }
            }
        }
        return ' ';
    }

    public interface Painter {
        char paint(final int row, final int col);
    }

    public static void drawBox(final Box box) {
        drawGrid((row, col) -> {
            // horizontal lines
            if ((Objects.equals(row, (int) box.botLeft.getY())
                    || Objects.equals(row, (int) (box.botLeft.getY() + box.height - 1)))
                    && col >= box.botLeft.getX()
                    && col < box.botLeft.getX() + box.width) {
                return 'O';
            }

            // vertical lines
            else if ((Objects.equals(col, box.botLeft.x)
                    || Objects.equals(col, box.botLeft.x + box.width - 1))
                    && row >= box.botLeft.y
                    && row < box.botLeft.y + box.height) {
                return 'O';
            }

            // nothing to draw
            else {
                return ' ';
            }
        });
    }

    public static void drawLines(final List<Line> lines) {
        drawGrid((row, col) -> {
            for (final Line line : lines) {
                if (doesLineHit(line, row, col)) {
                    return (char) (col % 10 + '0');
                }
            }
            return ' ';
        });
    }

    public static void drawLine(final Line line) {
        drawGrid((row, col) -> doesLineHit(line, row, col) ? 'H' : ' ');
    }

    private static boolean doesLineHit(final Line line, int row, int col) {
        if (Line.Orientation.HORIZANTAL.equals(line.orientation)) {
            if (Objects.equals(row, line.start.y)
                    && col >= line.start.x
                    && col <= line.end.x) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (Objects.equals(col, line.start.x)
                    && row >= line.start.y
                    && row <= line.end.y) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static void drawGrid(Painter painter){
        for (int row = 50; row >= 0; row--) {
            for(int col=0; col<50; col++) {
                System.out.print(painter.paint(row, col) + " ");
            }
            System.out.println();
        }
    }
}
