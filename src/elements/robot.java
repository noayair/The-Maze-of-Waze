package elements;

import Server.game_service;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.util.List;

public class robot implements Robot_I {
    private game_service game;
    private int src , dest , id , value , speed;
    private Point3D pos;

    public robot(){
        this.game = null;
        this.src = 0;
        this.dest = 0;
        this.id = 0;
        this.value = 0;
        this.speed = 0;
        this.pos = null;
    }

    public robot(String Jstr){
        this.init(Jstr);
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
}