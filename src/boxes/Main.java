package boxes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jggrime on 10/13/16.
 */
public class Main {

    public static void main(String [] args) throws IOException {

        while (true) {
            final List<Box> boxes = RandomBoxGenerator.generateRandomBoxes(System.currentTimeMillis());

        /*
        List<Line> currentLines = new ArrayList<>();
        for (final Box box : boxes) {
            System.out.println("adding box: (" + box.botLeft.x + ", " + box.botLeft.y + ") w:" + box.width + " h:" + box.height);
            List<Line> lines = Converter.addBoxToLines(currentLines, box);
            currentLines.clear();
            currentLines.addAll(lines);
            Render.drawLines(currentLines);
        }
        */

            System.in.read();
            final List<Line> lines = Converter.boxesToLines(boxes);
            Render.drawLines(lines);
        }
    }

}
