package backend.helpers;

import java.io.*;

/**
 * RequestsManager manages all necessary changes to the requests.txt file.
 */
public class RequestsManager {

    // Constants
    private static final File requestsFile = new File("requests.txt");

    // Singleton instance
    private static RequestsManager instance = new RequestsManager();

    /**
     * Returns the RequestManager instance
     *
     * @return The RequestManager
     */
    public static RequestsManager getInstance() {
        return instance;
    }

    /**
     * Creates the requests.txt file.
     */
    private RequestsManager() {
        try {
            requestsFile.createNewFile();
        } catch (IOException ignored) {
        }
    }

    /**
     * Returns the requested quantity for a particular ingredient (-1 if ingredient doesn't exist in requests.txt).
     *
     * @param ingredientName The name of the ingredient to search for.
     * @return The requested quantity of ingredient in requests.txt.
     */
    private double getRequestedQuantity(String ingredientName) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(requestsFile));
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts[0].trim().toLowerCase().equals(ingredientName.toLowerCase())) {
                    fileReader.close();
                    return Double.parseDouble(parts[1]);
                }
            }
            fileReader.close();

        } catch (IOException ignored) {
        }
        return -1;
    }

    /**
     * Removes a given ingredient from requests.txt (if present).
     *
     * @param ingredientName String name of ingredient to be removed
     */
    public boolean removeIngredient(String ingredientName) {
        return changeIngredientQuantity(ingredientName, -getRequestedQuantity(ingredientName));
    }

    /**
     * Deduct the requested quantity of an ingredient by a specific amount.
     *
     * @param ingredientName Name of the ingredient to modify.
     * @param quantity       Quantity to be deducted.
     */
    public boolean removeIngredient(String ingredientName, double quantity) {
        return changeIngredientQuantity(ingredientName, -quantity);
    }

    /**
     * Increase requested quantity of an ingredient by a specific amount.
     *
     * @param ingredientName Name of the ingredient to modify.
     * @param quantity       Quantity to be incremented.
     */
    public boolean addIngredient(String ingredientName, double quantity) {
        return changeIngredientQuantity(ingredientName, quantity);
    }

    /**
     * Helper method to modify an ingredient's requested quantity by adding supplied quantity.
     *
     * @param ingredientName Name of the ingredient whose quantity we want to modify
     * @param quantity       Amount of ingredient to be requested.
     */
    private boolean changeIngredientQuantity(String ingredientName, double quantity) {
        File alternativeFile = new File("requests.temp.txt"); // Create temp file
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(requestsFile));
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(alternativeFile));

            String line;
            double ingredientQuantity = 0;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);

                if (parts[0].trim().toLowerCase().equals(ingredientName.toLowerCase()))
                    ingredientQuantity = Double.parseDouble(parts[1]);
                else fileWriter.write(line + System.lineSeparator());
            }

            // Calculate and write new quantity
            double newQuantity = ingredientQuantity + quantity;
            if (newQuantity > 0) fileWriter.write(ingredientName + " | " + newQuantity + System.lineSeparator());

            // Close files and rename temp file to requests.txt
            fileReader.close();
            fileWriter.close();
            return requestsFile.delete() && alternativeFile.renameTo(requestsFile);
        } catch (IOException e) {
            return false;
        }
    }
}

