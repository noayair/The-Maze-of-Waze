package elements;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.util.List;

public class fruits implements Fruits_I {
    private double value;
    private int type;
    private Point3D pos;
    private String image;

    // constructor
    public fruits() {
        this.value = 0;
        this.type = 0;
        this.pos = null;
        this.image = null;
    }

    /**
     * init fruit from json file
     *
     * @param Jstr
     */
    public void init(String Jstr) {
        try {
            JSONObject fruits = new JSONObject(Jstr);
            JSONObject f = fruits.getJSONObject("Fruit");
            this.value = f.getDouble("value");
            this.type = f.getInt("type");
            String location_str = f.getString("pos");
            this.pos = new Point3D(location_str);
            if (this.type == 1) {
                this.image = "apple.png";
            } else {
                this.image = "banana.png";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * draw all the fruits on the graph
     *
     * @param fruitList
     */
    public void drawFruits(List<fruits> fruitList) {
        for (fruits f : fruitList) {
            StdDraw.picture(f.pos.x(), f.pos.y(), f.image, 0.0007, 0.0007);
        }
    }
}
