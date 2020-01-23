package elements;

import Server.game_service;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class robot {
    private game_service game;
    private int src, dest, id, value, speed;
    private Point3D pos;
    private List<node_data> way = new LinkedList<>();
    private Queue<Integer> check = new LinkedList<>();

    // constructors

    public robot() {
        this.game = null;
        this.src = 0;
        this.dest = -1;
        this.id = 0;
        this.value = 0;
        this.speed = 0;
        this.pos = null;
        for (int j = 0; j < 3; j++) {
            this.check.add(-2);
        }
    }

    public robot(int id, int s, int d, int v, int speed, Point3D p) {
        this.id = id;
        this.pos = p;
        this.value = v;
        this.speed = speed;
        this.dest = d;
        this.src = s;
    }

    public robot(String Jstr) {
        this.init(Jstr);
    }

    //Getters and setters

    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public int getId() {
        return id;
    }

    public int getDest() {
        return dest;
    }

    public Point3D getPos() {
        return pos;
    }

    public int getSrc() {
        return src;
    }

    //functions


    /**
     * Init robots from a json file
     *
     * @param Jstr
     * @return
     */
    public robot init(String Jstr) {
        robot robots = new robot();
        try {
            JSONObject rob = new JSONObject(Jstr);
            JSONObject r = rob.getJSONObject("Robot");
            robots.src = r.getInt("src");
            String location_str = r.getString("pos");
            robots.pos = new Point3D(location_str);
            robots.id = r.getInt("id");
            robots.dest = r.getInt("dest");
            robots.value = r.getInt("value");
            robots.speed = r.getInt("speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return robots;
    }

    /**
     * Get a list of robots and prints them on the graph.
     *
     * @param robotsList
     */
    public void drawRobots(List<robot> robotsList) {
        for (robot r : robotsList) {
            StdDraw.picture(r.pos.x(), r.pos.y(), "robot.png", 0.001, 0.001);
        }
    }

    /**
     * Gets from the server the update information about the robots and updates the robot accordingly.
     *
     * @param Jstr
     */
    public void update(String Jstr) {
        try {
            JSONObject rob = new JSONObject(Jstr);
            JSONObject r = rob.getJSONObject("Robot");
            this.src = r.getInt("src");
            String location_str = r.getString("pos");
            this.pos = new Point3D(location_str);
            this.id = r.getInt("id");
            this.dest = r.getInt("dest");
            this.value = r.getInt("value");
            this.speed = r.getInt("speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * if the list size is <= 3 , add the node to the queue.
     * else, remove the head and then add the node.
     * this function help us to check if the robot is stuck on 1 edge.
     *
     * @param i
     */
    public void checkNode(int i) {
        this.check.remove();
        this.check.add(i);
    }

    public List<robot> fillRobotList(List<String> arr) {
        List<robot> temp = new LinkedList<>();
        for (String rob : arr) {
            robot r = init(rob);
            temp.add(r);
        }
        return temp;
    }
}