package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.fruits;

import java.util.List;

public class MyGameAlgo extends Thread {
    private MyGameGUI gameGUI;// = new MyGameGUI();

    public MyGameAlgo(MyGameGUI gameGUI) {
        this.gameGUI = gameGUI;
    }

    public void startGame(int level) {
        game_service game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games
        this.gameGUI.setGame(game);
        DGraph dg = new DGraph();
        dg.init(game.getGraph()); //build a graph for the game
        this.gameGUI.setGraph(dg);
        this.gameGUI.initFruits();
        this.gameGUI.addRobots(); // adds robots to the game and to the list
        this.gameGUI.DrawGraph(); // draw the graph
        this.gameGUI.getF().drawFruits(this.gameGUI.getFruitsList()); // draw the fruits on the graph
        this.gameGUI.getR().drawRobots(this.gameGUI.getRobotsList()); // draw the robots on the graph
        this.gameGUI.getGame().startGame();
        this.gameGUI.start();
    }



}
