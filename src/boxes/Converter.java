package boxes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jggrime on 10/13/16.
 *
 * Can be used to convert boxes to lines and remove any interior of the boxes.
 */
public class Converter {

    public static List<Line> boxesToLines(final Box box) {
        final List<Line> lines = new ArrayList<>(4);

        // bottom horizontal line
        lines.add(new Line(
                new Point(box.botLeft.x, box.botLeft.y),
                new Point(box.botLeft.x + box.width - 1, box.botLeft.y),
                Line.Orientation.HORIZANTAL,
                box.height
        ));

        // top horizontal line
        lines.add(new Line(
                new Point(box.botLeft.x, box.botLeft.y + box.height - 1),
                new Point(box.botLeft.x + box.width - 1, box.botLeft.y + box.height - 1),
                Line.Orientation.HORIZANTAL,
                -box.height
        ));

        // left vertical line
        lines.add(new Line(
                new Point(box.botLeft.x, box.botLeft.y),
                new Point(box.botLeft.x, box.botLeft.y + box.height - 1),
                Line.Orientation.VERTICAL,
                box.width
        ));

        // right vertical line
        lines.add(new Line(
                new Point(box.botLeft.x + box.width - 1, box.botLeft.y),
                new Point(box.botLeft.x + box.width - 1, box.botLeft.y + box.height - 1),
                Line.Orientation.VERTICAL,
                -box.width
        ));

        return lines;
    }

    public static List<Line> addBoxToLines(final List<Line> lines, final Box box) {
        if (lines.isEmpty()) {
            return boxesToLines(box);
        }

        final List<Line> fullList = new ArrayList<>(lines);

        final List<Line> origLines = boxesToLines(box);
        final List<Line> cutLines = new ArrayList<>();

        for (final Line line : origLines) {
            cutLines.addAll(cutLine(fullList, 0, line));
        }

        for (final Line line : fullList) {
            cutLines.addAll(cutLine(origLines, 0, line));
        }

        fullList.clear();
        fullList.addAll(cutLines);

        return fullList;
    }

    public static List<Line> boxesToLines(final List<Box> boxes) {
        final List<Line> fullList = new ArrayList<>();

        if (boxes == null || boxes.isEmpty()) {
            return fullList;
        }

        //System.out.println("Box:" + " (" + boxes.get(0).botLeft.x + ", " + boxes.get(0).botLeft.y + ") w:" + boxes.get(0).width + " h:" + boxes.get(0).height);
        fullList.addAll(boxesToLines(boxes.get(0)));

        if (boxes.size() == 1) {
            return fullList;
        }

        for (int i = 1; i < boxes.size(); i++) {
            final Box box = boxes.get(i);
            //System.out.println("Box:" + " (" + box.botLeft.x + ", " + box.botLeft.y + ") w:" + box.width + " h:" + box.height);
            final List<Line> origLines = boxesToLines(box);
            final List<Line> cutLines = new ArrayList<>();

            for (final Line line : origLines) {
                cutLines.addAll(cutLine(fullList, 0, line));
            }

            for (final Line line : fullList) {
                cutLines.addAll(cutLine(origLines, 0, line));
            }

            fullList.clear();
            fullList.addAll(cutLines);
        }

        return fullList;
    }

    private static List<Line> cutLine(final List<Line> origLines, final int start, final Line origLine) {
        final List<Line> lines = new ArrayList<>();

        Line line = origLine.copy();

        for (int i = start; i < origLines.size(); i++) {
            final List<Line> cutLines = cutLine(origLines.get(i), line);
            if (cutLines != null) {
                if (cutLines.isEmpty()) {
                    return Collections.emptyList();
                } else if (cutLines.size() == 1) {
                    line = cutLines.get(0);
                } else {
                    lines.addAll(cutLine(origLines, i, cutLines.get(0)));
                    lines.addAll(cutLine(origLines, i, cutLines.get(1)));
                    return lines;
                }
            }
        }
        return Collections.singletonList(line);
    }

    /**
     * Returns an empty list, a single Line list or a two Line list based on how it is cut
     * @param lineToCut line trying to cut
     * @param lineToBeCut line being cut into by lineToCut
     * @return null if the line cannot be cut or an array of the results to lineToBeCut if
     * the line can be cut.
     */
    private static List<Line> cutLine(final Line lineToCut, final Line lineToBeCut) {
        // determine if we can cut the line
        if (!canCut(lineToCut, lineToBeCut)) {
            return null;
        }

        Render.drawCuttingLines(lineToCut, lineToBeCut);
        final boolean lcVert = Line.Orientation.VERTICAL.equals(lineToCut.orientation);
        final List<Line> list = new ArrayList<>();

        if (lineToCut.orientation == lineToBeCut.orientation) {
            if (lcVert) {
                if (lineToCut.start.x == lineToBeCut.start.x && magnitudesAreSameDirection(lineToCut, lineToBeCut)) {
                    list.add(lineToBeCut.copy());
                } else if (lineToCut.start.x + lineToCut.magnitue + (lineToCut.magnitue >= 0 ? -1 : 1) == lineToBeCut.start.x && !magnitudesAreSameDirection(lineToCut, lineToBeCut)) {
                    list.add(lineToBeCut);
                }
            } else {
                if (lineToCut.start.y == lineToBeCut.start.y && magnitudesAreSameDirection(lineToCut, lineToBeCut)) {
                    list.add(lineToBeCut.copy());
                } else if (lineToCut.start.y + lineToCut.magnitue + (lineToCut.magnitue >= 0 ? -1 : 1) == lineToBeCut.start.y && !magnitudesAreSameDirection(lineToCut, lineToBeCut)) {
                    list.add(lineToBeCut);
                }
            }
        } else {
            if (lcVert
                    && (lineToCut.start.y == lineToBeCut.start.y || lineToCut.end.y == lineToBeCut.start.y)) {
                list.add(lineToBeCut.copy());
            } else if (!lcVert
                    && (lineToCut.start.x == lineToBeCut.start.x || lineToCut.end.x == lineToBeCut.start.x)) {
                list.add(lineToBeCut.copy());
            }
        }

        // we should not cut even though there is an intersection
        if (!list.isEmpty()) {
            System.out.println("we should not cut even though there is an intersection");
            return list;
        }
        Line newLine;

        final Line lc = normalizeMagnitude(lineToCut);
        final Line l = lineToBeCut.copy();

        if (lc.orientation == l.orientation) {
            if (lcVert) {
                if (l.start.y < lc.start.y || l.end.y > lc.end.y) {
                    // the top of line is cut
                    if (l.start.y < lc.start.y) {
                        newLine = new Line(
                                new Point(l.start.x, l.start.y),
                                new Point(l.end.x, lc.start.y - 1),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                    // the bottom of line is cut
                    if (l.end.y > lc.end.y) {
                        newLine = new Line(
                                new Point(l.start.x, lc.end.y + 1),
                                new Point(l.end.x, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                }
            } else {
                if (l.start.x < lc.start.x || l.end.x > lc.end.x) {
                    if (l.start.x < lc.start.x) {
                        newLine = new Line(
                                new Point(l.start.x, l.start.y),
                                new Point(lc.start.x - 1, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                    if (l.end.x > lc.end.x) {
                        newLine = new Line(
                                new Point(lc.end.x + 1, l.start.y),
                                new Point(l.end.x, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                }
            }
        } else {
            if (lcVert) {
                if (l.start.x < lc.start.x || l.end.x >= lc.start.x + lc.magnitue) {
                    if (l.start.x < lc.start.x) {
                        newLine = new Line(
                                new Point(l.start.x, l.start.y),
                                new Point(lc.start.x - 1, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                    if (l.end.x >= lc.start.x + lc.magnitue) {
                        newLine = new Line(
                                new Point(lc.start.x + lc.magnitue, l.start.y),
                                new Point(l.end.x, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                }
            } else {
                if (l.start.y < lc.start.y || l.end.y >= lc.start.y + lc.magnitue) {
                    if (l.start.y < lc.start.y) {
                        newLine = new Line(
                                new Point(l.start.x, l.start.y),
                                new Point(l.end.x, lc.start.y - 1),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                    if (l.end.y >= lc.start.y + lc.magnitue) {
                        newLine = new Line(
                                new Point(l.start.x, lc.start.y + lc.magnitue),
                                new Point(l.end.x, l.end.y),
                                l.orientation,
                                l.magnitue
                        );
                        list.add(newLine);
                    }
                }
            }
        }
        for (final Line line : list) {
            System.out.println("ResultLine: (" + line.start.x + ", " + line.start.y + ")->(" + line.end.x + ", " + line.end.y + ") mag: " + line.magnitue);
        }
        return list;
    }

    /**
     * @param cuttingLine the line trying to cut second param line
     * @param line the line being cut
     * @return true if cuttingLine can cut into line, false otherwise
     */
    static boolean canCut(final Line cuttingLine, final Line line) {
        final Line cut = normalizeMagnitude(cuttingLine);
        final boolean isCutVer = Line.Orientation.VERTICAL.equals(cut.orientation);
        if (cut.orientation == line.orientation) {
            if (isCutVer) {
                return
                        // verify that a point on the line is in the Y bounds of the cutting line
                        (between(line.start.y, cut.start.y - 1, cut.end.y + 1)
                                || between(cut.start.y, line.start.y - 1, line.end.y + 1))
                        // verify that a point on the line is within the X cut magnitude
                        && between(line.start.x, cut.start.x - 1, cut.start.x + cut.magnitue);
            } else {
                return
                        // verify that a point on the line is in the X bounds of the cutting line
                        (between(line.start.x, cut.start.x - 1, cut.end.x + 1)
                                || between(cut.start.x, line.start.x - 1, line.end.x + 1))
                        // verify that a point on the line is within the Y cut magnitude
                        && between(line.start.y, cut.start.y - 1, cut.start.y + cut.magnitue);
            }
        } else {
            if (isCutVer) {
                return
                        // verify the line is in the Y bounds of the cut
                        between(line.start.y, cut.start.y - 1, cut.end.y + 1)
                        // verify the line is in the X bounds of the cut
                        && (between(line.start.x, cut.start.x - 1, cut.start.x + cut.magnitue)
                                || between(cut.start.x, line.start.x - 1, line.end.x + 1));
            } else {
                return
                        // verify the line is in the X bounds of the cut
                        between(line.start.x, cut.start.x - 1, cut.end.x + 1)
                        // verify the line is in the Y bounds of the cut
                        && (between(line.start.y, cut.start.y - 1, cut.start.y + cut.magnitue)
                                || between(cut.end.y, line.start.y - 1, line.end.y + 1));
            }
        }
    }

    /**
     * This method returns a copied line where the magnitude is
     * always positive and the line is always to the left or bottom
     * @param line - line to normalize
     * @return normalized line
     */
    static Line normalizeMagnitude(final Line line) {
        if (line.magnitue < 0) {
            boolean isVert = Line.Orientation.VERTICAL.equals(line.orientation);
            int xMagAdd = isVert ? line.magnitue + 1 : 0;
            int yMagAdd = isVert ? 0 : line.magnitue + 1;
            return new Line(
                    new Point(line.start.x + xMagAdd, line.start.y + yMagAdd),
                    new Point(line.end.x + xMagAdd, line.end.y + yMagAdd),
                    line.orientation,
                    -line.magnitue);
        } else {
            return line.copy();
        }
    }

    static boolean between(final int val, final int start, final int end) {
        return val > start && val < end;
    }

    static boolean magnitudesAreSameDirection(final Line lineToBeCut, final Line lineToCut) {
        return (lineToBeCut.magnitue > 0 && lineToCut.magnitue > 0) || (lineToBeCut.magnitue <= 0 && lineToCut.magnitue <= 0);
    }
}
