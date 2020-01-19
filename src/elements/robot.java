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

public class robot{
    private game_service game;
    private int src , dest , id , value , speed;
    private Point3D pos;
    private List<node_data> way = new LinkedList<>();
    private Queue<Integer> check = new LinkedList<>();

    // constructors

    public robot(){
        this.game = null;
        this.src = 0;
        this.dest = -1;
        this.id = 0;
        this.value = 0;
        this.speed = 0;
        this.pos = null;
        for (int i = 0; i < 3 ; i++) {
            this.check.add(-2);
        }
    }

    public robot(int id, int s, int d, int v, int speed, Point3D p){
        this.id = id;
        this.pos= p;
        this.value=v;
        this.speed= speed;
        this.dest=d;
        this.src=s;
    }

    //functions

    public robot(String Jstr){
        this.init(Jstr);
    }

    public List<node_data> getWay() {
        return way;
    }

    public int getSrc() {
        return src;
    }

    public void setWay(List<node_data> way) {
        this.way = way;
    }

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

    public Queue<Integer> getCheck() {
        return check;
    }

    public void drawRobots(List<robot> robotsList){
        for(robot r : robotsList){
            StdDraw.picture(r.pos.x() , r.pos.y() , "robot.png" , 0.001 , 0.001);
        }
    }

    public void update(String Jstr){
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
     * @param i
     */
    public void checkNode(int i){
        if(this.check == null){
            this.check.add(i);
        }
        if(this.check.size() <= 3){
            this.check.add(i);
        }else{
            this.check.remove();
            this.check.add(i);
        }
    }

    public List<robot> fillRobotList(List<String> arr){
        List<robot> temp = new LinkedList<>();
        for (String rob:arr) {
            robot r = init(rob);
            temp.add(r);
        }
        return temp;
    }
}