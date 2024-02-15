import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimilaritySearch {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimilaritySearch <query_image_filename> <image_dataset_directory>");
            return;
        }

        String queryImgFileInput = args[0];
        String datasetDirectory = args[1];

        try {
            // Construct the absolute path to the query image file
            File queryImageFile = new File("queryImages", queryImgFileInput);

            // Check if the file exists
            if (!queryImageFile.exists() || queryImageFile.isDirectory()) {
                System.out.println("The specified query image file does not exist.");
                return;
            }

            // Load query image and compute its histogram
            ColorImage queryImage = new ColorImage(queryImageFile.getAbsolutePath());
            ColorHistogram queryHistogram = new ColorHistogram(3); // Assume 3-bit color reduction
            queryHistogram.setImage(queryImage);

            // Load image dataset and pre-computed histograms
            File dataset = new File(datasetDirectory);
            File[] imageFiles = dataset.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg.txt"));
            List<ColorHistogram> histograms = new ArrayList<>();
            for (File file : imageFiles) {
                ColorHistogram histogram = new ColorHistogram(file.getAbsolutePath());
                histograms.add(histogram);
            }

            // Compute similarity scores for each image in the dataset
            Map<String, Double> similarityScores = new HashMap<>();
            for (int i = 0; i < histograms.size(); i++) {
                double similarity = queryHistogram.compare(histograms.get(i));
                similarityScores.put(imageFiles[i].getName(), similarity);
            }

            // Sort the images by similarity scores
            List<Map.Entry<String, Double>> sortedList = new ArrayList<>(similarityScores.entrySet());
            sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Print the names of the 5 most similar images
            System.out.println("5 most similar images to the query image:");
            for (int i = 0; i < 5 && i < sortedList.size(); i++) {
                System.out.println(sortedList.get(i).getKey());
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
