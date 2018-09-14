package controllers.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This is a helper class for easily serializing and deserializing an object from a file.
 */
public class Serializer {

    // Instance variable
    private String path;

    /**
     * Constructs the serializer class.
     *
     * @param path The path at which to serialize/deserialize objects.
     */
    public Serializer(String path) {
        this.path = path;
    }

    /**
     * Deserialize an object from the file.
     *
     * @param <T> The type of the object to be deserialized.
     * @return Returns the deserialized object.
     */
    public <T> T deserialize() {
        try {
            FileInputStream fInput = new FileInputStream(path);
            ObjectInputStream oInput = new ObjectInputStream(fInput);
            return (T) oInput.readObject();
        } catch (java.io.IOException | ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Serialize an object to the file.
     *
     * @param obj The object to be serialized.
     * @return Returns whether the object was serialized successfully.
     */
    public boolean serialize(Object obj) {
        try {
            FileOutputStream fOutput = new FileOutputStream(path);
            ObjectOutputStream oOutput = new ObjectOutputStream(fOutput);
            oOutput.writeObject(obj);
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }
}
