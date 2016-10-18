package boxes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jggrime on 10/13/16.
 */
public class RandomBoxGenerator {

    private static int MAX_NUM_BOXES = 10;
    private static int MIN_BOX_SIZE = 3;
    private static int MAX_BOX_SIZE = 20;
    private static int MAX_GRID_SIZE = 50;

    public static List<Box> generateRandomBoxes(final long seed) {

        final Random random = new Random(seed);

        int numBoxes = 1 + random.nextInt(MAX_NUM_BOXES);

        final List<Box> boxes = new ArrayList<>(numBoxes);

        for (int i = 0; i < numBoxes; i++) {
            final Box box = new Box(
                    random.nextInt(MAX_GRID_SIZE - MAX_BOX_SIZE),
                    random.nextInt(MAX_GRID_SIZE - MAX_BOX_SIZE),
                    MIN_BOX_SIZE + random.nextInt(MAX_BOX_SIZE - MIN_BOX_SIZE + 1),
                    MIN_BOX_SIZE + random.nextInt(MAX_BOX_SIZE - MIN_BOX_SIZE + 1));
            boxes.add(box);
        }

        return boxes;
    }

}
