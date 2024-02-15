import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorImage {
    private List<List<Integer>> pixels;
    private int width;
    private int height;
    private int depth;

    // Constructor to create an image from a .ppm file
    public ColorImage(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String dimensions = reader.readLine(); // Width Height
        String maxColorValue = reader.readLine(); // Maximum color value (depth)

        String[] dimensionValues = dimensions.split(" ");
        this.width = Integer.parseInt(dimensionValues[0]);
        this.height = Integer.parseInt(dimensionValues[1]);
        this.depth = Integer.parseInt(maxColorValue);

        this.pixels = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            List<Integer> row = new ArrayList<>();
            String[] pixelValues = reader.readLine().split(" ");
            for (int j = 0; j < pixelValues.length; j += 3) {
                int R = Integer.parseInt(pixelValues[j]);
                int G = Integer.parseInt(pixelValues[j + 1]);
                int B = Integer.parseInt(pixelValues[j + 2]);
                row.add(R);
                row.add(G);
                row.add(B);
            }
            pixels.add(row);
        }

        reader.close();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public List<Integer> getPixel(int i, int j) {
        return pixels.get(i * width + j);
    }

    public void reduceColor(int d) {

        for (List<Integer> row : pixels) {
            for (int i = 0; i < row.size(); i++) {
                int colorValue = row.get(i);
                int newValue = colorValue >> (8 - d);
                row.set(i, newValue);
            }
        }

        this.depth = d;
    }
}
