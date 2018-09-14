package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ObjectManager<T> implements Serializable {

    // Instance variables
    private int count = 0;
    private HashMap<Integer, T> objects;

    /**
     * Constructs a new object manager instance.
     */
    public ObjectManager() {
        objects = new HashMap<>();
    }

    /**
     * Gets the id for the next object to be added.
     *
     * @return Returns the current count of objects + 1.
     */
    public int getNextId() {
        return count + 1;
    }

    /**
     * Add a new object to the manager.
     *
     * @param obj The object to be added.
     */
    public void addObject(T obj) {
        objects.put(++count, obj);
    }

    /**
     * Remove an object from the manager.
     *
     * @param id The id of the object to be removed.
     */
    public void removeObject(int id) {
        if (objects.containsKey(id)) objects.remove(id);
    }

    /**
     * Get a list of all the objects stored in the manager.
     *
     * @return Returns a list of all the objects in the manager.
     */
    public Collection<T> getObjects() {
        return objects.values();
    }

    /**
     * Get all the objects that satisfy a particular predicate.
     *
     * @param predicate The predicate to check the objects against.
     * @return Returns a list of objects satisfying the predicate.
     */
    public List<T> getObjects(Predicate<T> predicate) {
        List<T> objs = new ArrayList<>();
        for (T obj : objects.values()) {
            if (obj != null && predicate.test(obj)) objs.add(obj);
        }
        return objs;
    }

    /**
     * Get all the objects of a particular class that satisfy the predicate.
     *
     * @param predicate The predicate you want to check against.
     * @param type      The class of the type of objects you want to return.
     * @param <K>       The type parameter for the returned objects.
     * @return Returns all the objects in the manager that satisfy the predicate and are of type K.
     */
    public <K extends T> List<K> getObjects(Predicate<T> predicate, Class<K> type) {
        List<K> objs = new ArrayList<>();
        for (T obj : objects.values()) {
            if (predicate.test(obj) && type.isInstance(obj)) objs.add((K) obj);
        }
        return objs;
    }

    /**
     * Gets a single object that satisfies the provided predicate.
     *
     * @param predicate The predicate you want to check against.
     * @return Returns a single object that satisfies the predicate.
     */
    public T getObject(Predicate<T> predicate) {
        List<T> objs = getObjects(predicate);
        return objs.size() > 0 ? objs.get(0) : null;
    }

    /**
     * Gets a single object that satisfies the provided predicate and type constraint.
     *
     * @param predicate The predicate you want to check against.
     * @param type      The class of the type of object you want to return.
     * @param <K>       The type parameter for the returned object.
     * @return Returns a single object that satisfies the predicate and type constraint.
     */
    public <K extends T> K getObject(Predicate<T> predicate, Class<K> type) {
        T obj = getObject(predicate);
        if (type.isInstance(obj)) return (K) obj;
        else return null;
    }
}