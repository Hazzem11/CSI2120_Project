import java.util.List;
import java.io.*;

public class ColorHistogram {
    private int d;
    private double[] histogram;
    private String filename;

    // Constructor to create a ColorHistogram instance for a d-bit image
    public ColorHistogram(int d) {
        this.d = d;
        int bins = (int) Math.pow(2, 3 * d);
        this.histogram = new double[bins];
    }

    // Constructor to create a ColorHistogram from a text file
    public ColorHistogram(String filename) throws IOException {
        this.filename = filename;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        // Skip the first line
        reader.readLine();

        String[] values;
        line = reader.readLine();
        
        reader.close();
        
        values = line.split(" ");
        this.histogram = new double[values.length]; // Correctly initialize the histogram size
        int index = 0;
        int totalPixels = 0; // Variable to store the total sum of pixel values
        
        for (String value : values) {
            int pixelValue = Integer.parseInt(value);
            histogram[index] = pixelValue;
            totalPixels += pixelValue; // Accumulate the pixel values
            index++;
        }
        
        // Normalize the histogram
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= totalPixels; // Normalize by the total sum of pixel values
        }
        
    }

    // Associate an image with a histogram instance
    public void setImage(ColorImage image) {
        int totalPixels = image.getWidth() * image.getHeight(); // Correctly calculate the total number of pixels

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                List<Integer> pixel = image.getPixel(x, y);
                int R = pixel.get(0);
                int G = pixel.get(1);
                int B = pixel.get(2);

                int R_prime = R >> (8 - d);
                int G_prime = G >> (8 - d);
                int B_prime = B >> (8 - d);

                int index = (R_prime << (2 * d)) + (G_prime << d) + B_prime;
                histogram[index]++;
            }
        }

        // Normalize the histogram
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= totalPixels;
        }
    }

    // Get the normalized histogram of the image
    public double[] getHistogram() {
        return histogram;
    }

    // Compare two histograms using histogram intersection
    public double compare(ColorHistogram hist) {
        double[] hist1 = this.histogram;
        double[] hist2 = hist.getHistogram();

        double intersection = 0.0;
        for (int i = 0; i < hist1.length; i++) {
            intersection += Math.min(hist1[i], hist2[i]);
        }
        return intersection;
    }

    // Save the histogram into a text file
    public void save(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < histogram.length; i++) {
            writer.write(String.valueOf(histogram[i]));
            if (i != histogram.length - 1) {
                writer.write(",");
            }
        }
        writer.close();
    }

    public String getImageFilename() {

        return this.filename;

    }
}
