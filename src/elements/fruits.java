package elements;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * This class represents a fruit in the game.
 */
public class fruits{
    public static final double EPS1 = 0.000001 , EPS2 = EPS1+EPS1 , EPS = EPS2;
    private double value;
    private int type;
    private Point3D pos;
    private String image;

    // constructors
    public fruits(){
        this.value = 0;
        this.type = 0;
        this.pos = null;
        this.image = null;
    }

    public fruits (double v, int t, Point3D p){
        this.value = v;
        this.type= t;
        this.pos = p;
    }

    public fruits(String Jstr){
        this.init(Jstr);
    }

    //functions

    /**
     * Init fruit from a json file.
     * @param Jstr
     */
    public fruits init(String Jstr){
        fruits ans = new fruits();
        try {
            JSONObject fruits = new JSONObject(Jstr);
            JSONObject f = fruits.getJSONObject("Fruit");
            ans.value = f.getDouble("value");
            ans.type = f.getInt("type");
            String location_str = f.getString("pos");
            ans.pos = new Point3D(location_str);
            if(ans.type == 1){
                ans.image = "apple.png";
            }else{
                ans.image = "banana.png";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public Point3D getPos() {
        return pos;
    }

    public String getImage() {
        return image;
    }

    /**
     * Get a list of fruits and prints them on the graph.
     * @param fruitList
     */
    public void drawFruits(List<fruits> fruitList){
        for(fruits f : fruitList){
            StdDraw.picture(f.pos.x() , f.pos.y() , f.image , 0.0009 , 0.0009);
        }
    }

    public void update (String Jstr){
        try {
            JSONObject fruits = new JSONObject(Jstr);
            JSONObject f = fruits.getJSONObject("Fruit");
            this.value = f.getDouble("value");
            this.type = f.getInt("type");
            String location_str = f.getString("pos");
            this.pos = new Point3D(location_str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking on which edges are the fruits
     * @param graph
     * @return
     */
    public edge_data fruitEdge(DGraph graph){
        Collection<edge_data> edgeList = new ArrayList<>();
        double edgeDist , srcDist , destDist;
        for(node_data n : graph.getV()){
            for(edge_data e : graph.getE(n.getKey())){
                edgeDist = graph.getNode(e.getSrc()).getLocation().distance2D(graph.getNode(e.getDest()).getLocation());
                srcDist = graph.getNode(e.getSrc()).getLocation().distance2D(this.pos);
                destDist = graph.getNode(e.getDest()).getLocation().distance2D(this.pos);
                if (edgeDist > (srcDist + destDist) - EPS2) {
                    boolean a = e.getSrc() < e.getDest();
                    boolean b = this.type > 0;
                    if(a == b)
                        return e;
                }
            }
        }
        return null;
    }

}