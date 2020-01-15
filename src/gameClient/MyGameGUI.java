package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.*;
import elements.Fruits_I;
import elements.Robot_I;
import elements.fruits;
import elements.robot;
import gui.Graph_GUI;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class MyGameGUI extends Thread {
    private game_service game;
    private DGraph graph = new DGraph();
    private Graph_GUI gui = new Graph_GUI(graph);
    private Graph_Algo algo = new Graph_Algo(graph);
    private List<fruits> fruitsList = new ArrayList<>();
    private List<robot> robotsList = new ArrayList<>();
    private fruits f = new fruits();
    private robot r = new robot();
    public Range Range_x;
    public Range Range_y;


    //constructor
    public MyGameGUI() {
        StdDraw.gui = this;
        setWin();
    }

    //getters ans setter
    public List<fruits> getFruitsList() {
        return this.fruitsList;
    }

    //functions

    /**
     * gets level and draw the graph, the fruits and the robots for the game
     * @param level
     */
    public void startGame(int level) {
        this.game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games
        this.graph.init(game.getGraph()); //build a graph for the game
        List<String> fruitList = this.game.getFruits(); // list of all the fruits in this level
        for (String s : fruitList) { // for that init all the fruits from json and adds it to the list
            f = new fruits();
            System.out.println(s);
            f = f.init(s);
            this.fruitsList.add(f);
        }
        addRobots(); // adds robots to the game and to the list
        DrawGraph(); // draw the graph
        this.f.drawFruits(this.fruitsList); // draw the fruits on the graph
        this.r.drawRobots(this.robotsList); // draw the robots on the graph
        this.game.startGame();
        this.start();
    }

    /**
     * adds the robot next to the fruits before the start
     */
    public void addRobots(){
        int robotsSize = 0;
        robot rob = new robot();
        try {
            JSONObject ro = new JSONObject(this.game.toString()); // checks how many robots we need
            JSONObject r = ro.getJSONObject("GameServer");
            robotsSize = r.getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        edge_data e = new EdgeData();
        for (int i = 0; i < robotsSize; i++){
            rob = new robot(); // create empty robot
            e = this.fruitsList.get(i).fruitEdge(this.graph); // checking on which edges the fruit is
            if(this.fruitsList.get(i).getType() == -1){
                this.game.addRobot(Math.max(e.getSrc() , e.getDest()));
            }else if(this.fruitsList.get(i).getType() == 1){
                this.game.addRobot(Math.min(e.getSrc() , e.getDest())); // ask from the server to add the robot on the src edge
            }
            rob = rob.init(this.game.getRobots().get(i));
            rob.setDest(e.getDest());
            this.robotsList.add(rob); // add the current robot to the robots list
            System.out.println(rob.getDest());
            System.out.println(this.game.getRobots());
        }
    }

    public void updateFruits(){
        List<String> updateFruits = this.game.getFruits();
        if(updateFruits != null){
            for (int i = 0; i < this.fruitsList.size(); i++) {
                this.fruitsList.get(i).update(updateFruits.get(i));
            }
        }
    }

    public void updateRobots(){
        List<String> updateRobots = this.game.getRobots();
        for (int i = 0; i < this.robotsList.size(); i++) {
            this.robotsList.get(i).update(updateRobots.get(i));
        }
    }


    public void moveRobots(game_service gs, DGraph dg) {
        List<String> move = gs.move();
        if (move != null) {
            long time = gs.timeToEnd();
            for (int i = 0; i < move.size(); i++) {
                String robotJson = move.get(i);
                try {
                    JSONObject obj = new JSONObject(robotJson);
                    JSONObject rob = obj.getJSONObject("Robot");
                    int id = rob.getInt("id");
                    int src = rob.getInt("src");
                    int dest = rob.getInt("dest");
                    if(dest == -1) {
                        List<node_data> shortest = new LinkedList<>();
                        if (nextNode(dg, src) == -1) {
                            if(!this.robotsList.get(id).getWay().isEmpty()){
                                shortest = this.robotsList.get(id).getWay();
                                dest = shortest.get(0).getKey();
                                gs.chooseNextEdge(id , dest);
                                this.robotsList.get(id).getWay().remove(shortest.get(0));
                            }else {
                                int closestDest = closestFruit(this.graph.getNode(src).getKey());
                                shortest = shotestWay(src, closestDest);
                                if (shortest==null)
                                    System.out.println(closestDest + " " + src);
                                dest = shortest.get(0).getKey();
                                gs.chooseNextEdge(id, dest);
                                shortest.remove(shortest.get(0));
                                //     gs.chooseNextEdge(id,shortest.get(0).getKey());
                                this.robotsList.get(id).setWay(shortest);
                            }
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        } else {
                            dest = nextNode(dg, src);
                            gs.chooseNextEdge(id, dest);
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        }
                    }

                } catch (JSONException e) {

                }
            }
        }
    }
    /**
     * a very simple random walk implementation!
     * @param
     * @param src
     * @return
     */
    public int nextNode(DGraph dg, int src) {
        Collection<edge_data> edges = dg.getE(src);
        for (edge_data e : edges) {
            for (int i = 0; i < this.fruitsList.size(); i++) {
                if (e.getDest() == this.fruitsList.get(i).fruitEdge(dg).getDest()) {
                    return e.getDest();
                }
            }
        }
        return -1;
    }

    public int closestFruit (int src){
        double dest = Double.POSITIVE_INFINITY;
        int ans = -1;
        int fruitSrc;
        double shortest;
//        Point3D fruitLocation;
        for (int i = 0; i < this.fruitsList.size(); i++) {
            fruitSrc = this.fruitsList.get(i).fruitEdge(this.graph).getSrc(); // find the src of the edge that the fruit is on it
//            fruitLocation = this.graph.getNode(fruitsrc).getLocation();
            shortest = this.algo.shortestPathDist(src , fruitSrc); // find the distance between the robot location to the src fruit
            if(shortest < dest){
                dest = shortest;
                ans = fruitSrc;
            }
        }
        return ans;
    }

    public List<node_data> shotestWay (int src, int dest){
        return this.algo.shortestPath(src, dest);
    }

    @Override
    public void run() {
        while(this.game.isRunning()){
            updateFruits();
            updateRobots();
            moveRobots(this.game , this.graph);
            this.DrawGraph();
            this.f.drawFruits(this.fruitsList); // draw the fruits on the graph
            this.r.drawRobots(this.robotsList); // draw the robots on the graph
            StdDraw.show();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Game over " + this.game.toString());
    }


    public void setWin(){
        StdDraw.setCanvasSize(1000, 500);
//        StdDraw.clear(Color.blue);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
////        StdDraw.picture(0,0,"Maze.png");
//        int senario=0;
//        String senarioString = JOptionPane.showInputDialog(null,"Please choose a Game Senario");
//        try{
//            senario=Integer.parseInt(senarioString);
//        }catch(Exception e1){e1.printStackTrace();}
//        String[] chooseGame = {"Manually Game","Auto Game"};
//        Object selctedGame = JOptionPane.showInputDialog(null,"Choose a Game mode","Message",JOptionPane.INFORMATION_MESSAGE,null,chooseGame,chooseGame[0]);
//        if(selctedGame=="Manually Game") {
//            StdDraw.clear();
//            StdDraw.enableDoubleBuffering();
//            startGame(senario);
//        }
    }

    public void DrawGraph() {
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        DGraph g = this.graph;
        Range x = Rangex();
        Range y = Rangey();
        StdDraw.setXscale(x.get_min()-0.004, x.get_max()+0.004);
        StdDraw.setYscale(y.get_min()-0.004, y.get_max()+0.004);
        StdDraw.setPenColor(Color.blue);
        StdDraw.setPenRadius(0.15);
        String s = "";
        double ScaleX = ((Range_x.get_max()-Range_x.get_min())*0.04);
        for (node_data n : g.getV()) {
            Point3D p = n.getLocation();
            StdDraw.filledCircle(p.x(), p.y(),ScaleX*0.1);
            s += Integer.toString(n.getKey());
            StdDraw.text(p.x() , p.y()+ScaleX*0.3 , s);
            s = "";
        }
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())){
                double src_x = n.getLocation().x();
                double src_y = n.getLocation().y();
                double dest_x = g.getNode(e.getDest()).getLocation().x();
                double dest_y = g.getNode(e.getDest()).getLocation().y();
                StdDraw.setPenColor(Color.lightGray);
                StdDraw.setPenRadius(0.003);
                StdDraw.line(src_x , src_y , dest_x , dest_y);
                double w = Math.round(e.getWeight()*100.0)/100.0;
                String weight = Double.toString(w);
                StdDraw.text(src_x * 0.3 + dest_x * 0.7 , src_y * 0.3 + dest_y * 0.7 , weight);
                StdDraw.setPenColor(Color.yellow);
                StdDraw.setPenRadius(0.15);
                StdDraw.filledCircle(src_x * 0.2 + dest_x * 0.8, src_y * 0.2 + dest_y * 0.8, ScaleX*0.1);
            }
        }
    }

    public Range Rangex(){
        Range r;
        if(graph.nodeSize() != 0){
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for(node_data n : graph.getV()){
                if(n.getLocation().x() < min){
                    min = n.getLocation().x();
                }
                if(n.getLocation().x() > max){
                    max = n.getLocation().x();
                }
            }
            r = new Range(min , max);
            this.Range_x = r;
            return r;
        }else{
            r = new Range(-100 , 100);
            this.Range_x = r;
            return r;
        }
    }

    public Range Rangey(){
        Range r;
        if(graph.nodeSize() != 0){
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for(node_data n : graph.getV()){
                if(n.getLocation().y() < min){
                    min = n.getLocation().y();
                }
                if(n.getLocation().y() > max){
                    max = n.getLocation().y();
                }
            }
            r = new Range(min , max);
            this.Range_y = r;
            return r;
        }else{
            r = new Range(-100 , 100);
            this.Range_y = r;
            return r;
        }
    }

    public static void main(String[] args) {
        MyGameGUI g = new MyGameGUI();
        g.startGame(15);
    }
}
