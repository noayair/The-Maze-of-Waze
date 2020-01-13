package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.edge_data;
import dataStructure.graph;
import elements.Fruits_I;
import elements.Robot_I;
import elements.fruits;
import elements.robot;
import gui.Graph_GUI;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyGameGUI implements Runnable {
    private game_service game;
    private DGraph graph = new DGraph();
    private List<fruits> fruitsList = new ArrayList<>();
    private List<robot> robotsList = new ArrayList<>();
    private fruits f = new fruits();
    private robot r = new robot();

    //constructor
    public MyGameGUI() {

    }

    //getters ans setter
    public List<fruits> getFruitsList() {
        return this.fruitsList;
    }

    //functions
    public void startGame(int level) {
        this.game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games
        this.graph.init(game.getGraph()); //build a graph for the game
        Graph_GUI g1 = new Graph_GUI(graph);
        List<String> fruitList = this.game.getFruits(); // list of all the fruits in this level
        for (String s : fruitList) { // for that init all the fruits from json and adds it to the list
            f = new fruits();
            System.out.println(s);
            f = f.init(s);
            this.fruitsList.add(f);
        }
        List<String> robotList = this.game.getRobots();
        addRobots();
//        System.out.println(this.robotsList.size());
        g1.DrawGraph();
        this.f.drawFruits(this.fruitsList);
        this.r.drawRobots(this.robotsList);
    }

    /**
     * adds the robot next to the fruits before the start
     */
    public void addRobots(){
        int robotsSize = 0;
        robot rob = new robot();
        try {
            JSONObject ro = new JSONObject(this.game.toString());
            JSONObject r = ro.getJSONObject("GameServer");
            robotsSize = r.getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        edge_data e = new EdgeData();
        for (int i = 0; i < robotsSize; i++){
            rob = new robot();
            e = this.fruitsList.get(i).onEdge(this.graph);
            rob.setSrc(e.getSrc());
            rob.setDest(e.getDest());
            this.game.addRobot(e.getSrc());
            rob = rob.init(this.game.getRobots().get(i));
//            this.game.chooseNextEdge(e.getSrc() , e.getDest());
            this.robotsList.add(rob);
        }
    }

    public static void main(String[] args) {
        DGraph g = new DGraph();
        Graph_GUI g1 = new Graph_GUI(g);
        game_service game = Game_Server.getServer(5);
        System.out.println(game.toString());
        MyGameGUI gui = new MyGameGUI();
        gui.startGame(6);
//        g1.DrawGraph();
    }

    @Override
    public void run() {

    }
}
