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
import utils.StdDraw;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.lang.Thread.sleep;

public class MyGameGUI implements Runnable {
    private game_service game;
    private DGraph graph = new DGraph();
    private Graph_Algo algo = new Graph_Algo(graph);
    private List<fruits> fruitsList = new ArrayList<>();
    private List<robot> robotsList = new ArrayList<>();
    private fruits f = new fruits();
    private robot r = new robot();
    private Graph_GUI gui = new Graph_GUI(graph);

    //constructor
    public MyGameGUI() {
        this.gui.setCanvas();
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
//        Graph_GUI g1 = new Graph_GUI(graph);
        List<String> fruitList = this.game.getFruits(); // list of all the fruits in this level
        for (String s : fruitList) { // for that init all the fruits from json and adds it to the list
            f = new fruits();
            System.out.println(s);
            f = f.init(s);
            this.fruitsList.add(f);
        }
//        List<String> robotList = this.game.getRobots();
        addRobots(); // adds robots to the game and to the list
//        System.out.println(this.robotsList.size());

        this.gui.DrawGraph(); // draw the graph
        this.f.drawFruits(this.fruitsList); // draw the fruits on the graph
        this.r.drawRobots(this.robotsList); // draw the robots on the graph

        this.game.startGame();
        run();
//        while(this.game.isRunning()){
//            updateFruits();
//            updateRobots();
//            moveRobots(this.game , this.graph);
//            this.gui.DrawGraph();
//            this.f.drawFruits(this.fruitsList); // draw the fruits on the graph
//            this.r.drawRobots(this.robotsList); // draw the robots on the graph
//            StdDraw.show();
//        }

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
//            rob.setSrc(e.getSrc());
//            rob.setDest(e.getDest());
            this.game.addRobot(e.getSrc()); // ask from the server to add the robot on the src edge
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

//

    private static void moveRobots(game_service game, DGraph gg) {
        List<String> log = game.move();
        if(log!=null) {
            long t = game.timeToEnd();
            for(int i=0;i<log.size();i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");

                    if(dest==-1) {
                        dest = nextNode(gg, src);
                        game.chooseNextEdge(rid, dest);
                        System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
                        System.out.println(ttt);
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
        }
    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(DGraph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

    @Override
    public void run() {
        while(this.game.isRunning()){
            updateFruits();
            updateRobots();
            moveRobots(this.game , this.graph);
            this.gui.DrawGraph();
            this.f.drawFruits(this.fruitsList); // draw the fruits on the graph
            this.r.drawRobots(this.robotsList); // draw the robots on the graph
            StdDraw.show();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public void setWin(){
//        StdDraw.setCanvasSize(1000, 500);
//        StdDraw.setYscale(-51,50);
//        StdDraw.setXscale(-51,50);
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
//    }

    public static void main(String[] args) {
        MyGameGUI g = new MyGameGUI();
        g.startGame(3);
//        DGraph g = new DGraph();
//        Graph_GUI g1 = new Graph_GUI(g);
//        game_service game = Game_Server.getServer(5);
//        game.addRobot(0);
//        System.out.println(game.move());
//        System.out.println(game.toString());
//        MyGameGUI gui = new MyGameGUI();
//        gui.startGame(6);
//        g1.DrawGraph();
    }
}
