import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorImage {
    private List<Integer> pixels;
    private int width;
    private int height;
    private int depth;

    // Constructor to create an image from a .ppm file
  public ColorImage(String filename) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));

    reader.readLine(); 
    reader.readLine(); // Skips first 2 lines of the text file

    String dimensions = reader.readLine(); // Width Height
    String maxColorValue = reader.readLine(); // Maximum color value (depth)

    String[] dimensionValues = dimensions.split(" ");
    this.width = Integer.parseInt(dimensionValues[0]);
    this.height = Integer.parseInt(dimensionValues[1]);
    this.depth = Integer.parseInt(maxColorValue);

    this.pixels = new ArrayList<>();

    String line;
    while ((line = reader.readLine()) != null) {
        String[] pixelValues = line.split(" ");
        for (int j = 0; j < pixelValues.length; j++) {
            int pixelValue = Integer.parseInt(pixelValues[j]);
            pixels.add(pixelValue);
        }
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
        int index = (j * width + i) * 3; // Each pixel has three values (RGB)
        int R = pixels.get(index);
        int G = pixels.get(index + 1);
        int B = pixels.get(index + 2);
        List<Integer> pixelValues = new ArrayList<>();
        pixelValues.add(R);
        pixelValues.add(G);
        pixelValues.add(B);
        return pixelValues;
    }
    

    public void reduceColor(int d) {
        for (int i = 0; i < pixels.size(); i++) {
            int colorValue = pixels.get(i);
            int newValue = colorValue >> (8 - d);
            pixels.set(i, newValue);
        }

        this.depth = d;
    }
}
