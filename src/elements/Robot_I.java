package elements;

import java.util.List;

/**
 * This interface represents a robot in the game.
 */
public interface Robot_I {
    public robot init(String Jstr);

    public void drawRobots(List<robot> robotsList);
}