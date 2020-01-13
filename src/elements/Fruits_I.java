package elements;

import java.util.List;

/**
 * This interface represents a fruit in the game.
 */
public interface Fruits_I {
    /**
     * Init fruit from a json file.
     * @param Jstr
     */
    public fruits init(String Jstr);
    /**
     * Get a list of fruits and prints them on the graph.
     * @param fruitList
     */
    public void drawFruits(List<fruits> fruitList);
}