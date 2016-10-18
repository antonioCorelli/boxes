package boxes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

/**
 * Created by jggrime on 10/14/16.
 *
 * tests the Converter class
 */
public class ConverterTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBoxToLines() throws Exception {
    }

    @Test
    public void testBoxesToLines1() throws Exception {
    }

    @Test
    public void testCanCut() throws Exception {
        // Vertical Orientation, Positive Magnitude
        Line cut = new Line(
                new Point(5, 5),
                new Point(5, 10),
                Line.Orientation.VERTICAL,
                2
        );

        Point tStartPt, tEndPt;

        // test Parallel Lines
        int correctValueIndex = 0;
        Boolean[] correctValues = new Boolean[] {
                false, false, false, false, false, false, false,
                false, true, true, true, true, true, false,
                false, true, true, true, true, true, false,
                false, false, false, false, false, false, false,
                false, false, false, false, false, false, false
        };
        for (int x = 4; x <= 8; x++) {
            for (int y = 0; y <= 12; y += 2) {
                tStartPt = new Point(x, y);
                tEndPt = new Point(x, y + 3);
                Line line = new Line(
                        tStartPt,
                        tEndPt,
                        Line.Orientation.VERTICAL,
                        10
                );
                Assert.assertEquals(correctValues[correctValueIndex++], Converter.canCut(cut, line));
            }
        }

        // test perpendicular lines
        cut = new Line(
                new Point(5, 5),
                new Point(5, 7),
                Line.Orientation.VERTICAL,
                5
        );
        correctValueIndex = 0;
        correctValues = new Boolean[] {
                false, false, false, false, false, false, false,
                false, true, true, true, true, false, false,
                false, true, true, true, true, false, false,
                false, true, true, true, true, false, false,
                false, false, false, false, false, false, false
        };
        for (int y = 4; y <= 8; y++) {
            for (int x = 0; x <= 12; x += 2) {
                tStartPt = new Point(x, y);
                tEndPt = new Point(x + 3, y);
                Line line = new Line(
                        tStartPt,
                        tEndPt,
                        Line.Orientation.HORIZANTAL,
                        10
                );
                Assert.assertEquals(correctValues[correctValueIndex++], Converter.canCut(cut, line));
            }
        }

        // test horiz cut parallel lines
        cut = new Line(
                new Point(5, 5),
                new Point(10, 5),
                Line.Orientation.HORIZANTAL,
                2
        );
        correctValueIndex = 0;
        correctValues = new Boolean[] {
                false, false, false, false, false, false, false,
                false, true, true, true, true, true, false,
                false, true, true, true, true, true, false,
                false, false, false, false, false, false, false,
                false, false, false, false, false, false, false
        };
        for (int y = 4; y <= 8; y++) {
            for (int x = 0; x <= 12; x += 2) {
                tStartPt = new Point(x, y);
                tEndPt = new Point(x + 3, y);
                Line line = new Line(
                        tStartPt,
                        tEndPt,
                        Line.Orientation.HORIZANTAL,
                        10
                );
                Assert.assertEquals(correctValues[correctValueIndex++], Converter.canCut(cut, line));
            }
        }
        // test horiz cut parallel lines
        cut = new Line(
                new Point(5, 5),
                new Point(10, 5),
                Line.Orientation.HORIZANTAL,
                2
        );
        correctValueIndex = 0;
        correctValues = new Boolean[] {
                false, false, false, false, false, false, false,
                false, true, true, true, true, true, false,
                false, true, true, true, true, true, false,
                false, false, false, false, false, false, false,
                false, false, false, false, false, false, false
        };
        for (int y = 4; y <= 8; y++) {
            for (int x = 0; x <= 12; x += 2) {
                tStartPt = new Point(x, y);
                tEndPt = new Point(x + 3, y);
                Line line = new Line(
                        tStartPt,
                        tEndPt,
                        Line.Orientation.HORIZANTAL,
                        10
                );
                Assert.assertEquals(correctValues[correctValueIndex++], Converter.canCut(cut, line));
            }
        }

        // test horiz cut perpendicular lines
        cut = new Line(
                new Point(5, 5),
                new Point(7, 5),
                Line.Orientation.HORIZANTAL,
                5
        );
        correctValueIndex = 0;
        correctValues = new Boolean[] {
                false, false, false, false, false, false, false,
                false, true, true, true, true, false, false,
                false, true, true, true, true, false, false,
                false, true, true, true, true, false, false,
                false, false, false, false, false, false, false
        };
        for (int x = 4; x <= 8; x++) {
            for (int y = 0; y <= 12; y += 2) {
                tStartPt = new Point(x, y);
                tEndPt = new Point(x, y + 3);
                Line line = new Line(
                        tStartPt,
                        tEndPt,
                        Line.Orientation.VERTICAL,
                        10
                );
                Assert.assertEquals(correctValues[correctValueIndex++], Converter.canCut(cut, line));
            }
        }

        // test negative magnitude
        cut = new Line(
                new Point(0, 10),
                new Point(10, 10),
                Line.Orientation.HORIZANTAL,
                -10
        );
        Line line = new Line(
                new Point(0, 0),
                new Point(10, 0),
                Line.Orientation.HORIZANTAL,
                10
        );
        Assert.assertFalse(Converter.canCut(cut, line));
        line = new Line(
                new Point(0, 1),
                new Point(10, 1),
                Line.Orientation.HORIZANTAL,
                10
        );
        Assert.assertTrue(Converter.canCut(cut, line));

        // test with both outside for one direction
        cut = new Line(
                new Point(5, 0),
                new Point(7, 0),
                Line.Orientation.HORIZANTAL,
                10
        );
        line = new Line(
                new Point(0, 2),
                new Point(10, 2),
                Line.Orientation.HORIZANTAL,
                2
        );
        Assert.assertTrue(Converter.canCut(cut, line));
    }

    @Test
    public void testCutLine() throws Exception {

    }

    @Test
    public void testNormalizeMagnitude() throws Exception {
        Line positiveLine = new Line(
                new Point(5, 7),
                new Point(5, 10),
                Line.Orientation.VERTICAL,
                -3
        );

        Line testLine = Converter.normalizeMagnitude(positiveLine);
        Assert.assertEquals(testLine.magnitue, 3);
        Assert.assertEquals(testLine.start.x, 3);
        Assert.assertEquals(testLine.end.x, 3);
        Assert.assertEquals(testLine.start.y, 7);
        Assert.assertEquals(testLine.end.y, 10);
        Assert.assertEquals(testLine.orientation, Line.Orientation.VERTICAL);
    }
}