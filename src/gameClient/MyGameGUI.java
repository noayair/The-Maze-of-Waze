package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Fruits_I;
import elements.fruits;
import gui.Graph_GUI;

import java.util.ArrayList;
import java.util.List;

public class MyGameGUI {
    private game_service game;
    private DGraph graph = new DGraph();
    private List<fruits> fruitsList = new ArrayList<>();

    //constructor
    public MyGameGUI(){

    }

    public void startGame(int level){
        this.game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games
        this.graph.init(game.getGraph());
        Graph_GUI g1 = new Graph_GUI(graph);
        g1.DrawGraph();
        Fruits_I fruit = new fruits();
        List<String> list = this.game.getFruits();
        for(String s : list){
            fruits f = new fruits();
            f.init(s);
            this.fruitsList.add(f);
        }
        fruit.drawFruits(this.fruitsList);
    }

    public static void main(String[] args) {
        MyGameGUI m = new MyGameGUI();
        m.startGame(2);
//        game_service game = Game_Server.getServer(10);
//        DGraph g = new DGraph();
//        System.out.println(game.getGraph());
//        g.init(game.getGraph());
//        System.out.println(g.toString());
//        System.out.println(game.toString());
//        System.out.println(game.getFruits());
//        Graph_GUI g1 = new Graph_GUI(g);
//        g1.DrawGraph();
    }

}
