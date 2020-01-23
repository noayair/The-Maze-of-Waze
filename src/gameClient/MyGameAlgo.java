package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.edge_data;
import dataStructure.node_data;
import elements.fruits;
import elements.robot;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.StdDraw;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class MyGameAlgo extends Thread {
    private MyGameGUI gameGUI;
    private KML_Logger kml = new KML_Logger();
    private int levelNum;

    //constructor
    public MyGameAlgo(MyGameGUI gameGUI) throws IOException {
        this.gameGUI = gameGUI;
    }

    //functions

    /**
     * Starts the game according to the selected level,
     * and updates the fruits, robots and graph accordingly.
     *
     * @param level
     */
    public void startGameAutomatic(int level) {
        levelNum = level;
        game_service game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games)
        gameGUI.setGame(game); // set this game to the MyGameGUI
        gameGUI.getGraph().init(game.getGraph()); //build a graph for the game
        gameGUI.initFruits();
        addRobots(); // adds robots to the game and to the list
        gameGUI.DrawGraph(); // draw the graph
        gameGUI.getF().drawFruits(gameGUI.getFruitsList()); // draw the fruits on the graph
        gameGUI.getR().drawRobots(gameGUI.getRobotsList()); // draw the robots on the graph
        gameGUI.getGame().startGame();
        this.start();
    }

    /**
     * get from the server how many robots it has in this level,
     * and add the robots to the game on a strategic node start.
     */
    public void addRobots() {
        int robotsSize = 0;
        List<fruits> fruitsList = gameGUI.getFruitsList(); // the fruits list from the gui that updated during the game
        List<robot> robotList = gameGUI.getRobotsList(); // the robots list from the gui that updated during the game
        robot rob;
        edge_data edge; // empty edge
        try {
            JSONObject ro = new JSONObject(gameGUI.getGame().toString()); // checks how many robots we need
            JSONObject r = ro.getJSONObject("GameServer");
            robotsSize = r.getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fruits fruit;
        for (int i = 0; i < robotsSize; i++) {
            if (levelNum == 16 && i == 1) {
                rob = new robot(); // create empty robot
                gameGUI.getGame().addRobot(25);
            }
            fruit = fruitsList.get(i);
            rob = new robot(); // create empty robot
            edge = fruit.fruitEdge(gameGUI.getGraph()); // checking on which edges the fruit i is (the list is sort by value)
            if (fruit.getType() == -1) { // if the fruit is banana
                gameGUI.getGame().addRobot(Math.max(edge.getSrc(), edge.getDest())); // add the robot to the max node between the src and the dest
            } else if (fruit.getType() == 1) { // if the fruit is apple
                gameGUI.getGame().addRobot(Math.min(edge.getSrc(), edge.getDest())); // add the robot to the min node between the src and the dest
            }
            rob = rob.init(gameGUI.getGame().getRobots().get(i)); // init from the server all the information about the robot to rob
            if (fruit.getType() == -1) {
                rob.setDest(edge.getSrc());
            } else if (fruit.getType() == 1) {
                rob.setDest(edge.getDest());
            }
            robotList.add(rob); // add the current robot to the robots list
            System.out.println(rob.getDest());
            System.out.println(gameGUI.getGame().getRobots());
        }
    }

    /**
     * choose the next dest for all the robots.
     *
     * @param gs
     * @param dg
     */
    public void moveRobots(game_service gs, DGraph dg) {
        int robDest = -1;
        for (robot rob : gameGUI.getRobotsList()) {
            robDest = nextNode(rob, dg);
            gs.chooseNextEdge(rob.getId(), robDest);
            System.out.println("Turn to node: " + robDest);
        }
    }


    public int nextNode(robot rob, DGraph dg) {
        double temp;
        double shortestDist = Double.MAX_VALUE;
        List<robot> robotList = gameGUI.getRobotsList();
        List<node_data> shortest = new ArrayList<>();
        edge_data fruitEdge = new EdgeData();
        fruits f = new fruits();
        int robDest = rob.getDest();
        if (rob.getDest() == -1) {
            for (fruits fruit : gameGUI.getFruitsList()) {
                if (fruit.getTag() == 0) {
                    fruitEdge = fruit.fruitEdge(dg);
                    temp = gameGUI.getAlgo().shortestPathDist(rob.getSrc(), fruitEdge.getSrc());
                    if (rob.getSrc() == fruitEdge.getSrc()) {
                        robDest = fruitEdge.getDest();
                        fruit.setTag(1);
                        rob.setDest(robDest);
                        rob.checkNode(robDest);
                        return robDest;
                    }
                    if (temp < shortestDist) {
                        shortestDist = temp;
                        shortest = gameGUI.getAlgo().shortestPath(rob.getSrc(), fruitEdge.getSrc());
                        f = fruit;
                    }
                }
            }
            robDest = shortest.get(1).getKey();
        }
        f.setTag(1);
        rob.setDest(robDest);
        rob.checkNode(robDest);
        return robDest;
    }


    /**
     * decides how much time to write in the 'sleep' method.
     *
     * @return
     */
    public int timeToSleep() {
        int ans = 110;
        if (levelNum == 23) {
            ans = 50;
        } else if (levelNum == 9) {
            ans = 95;
        }
        if (gameGUI.getRobotsList().size() < gameGUI.getFruitsList().size() / 3) {
            ans = 130;
        } else if ((gameGUI.getRobotsList().size() == gameGUI.getFruitsList().size()) && (gameGUI.getRobotsList().size() > 1)) {
            ans = 100;
        }
        edge_data edge = new EdgeData();
        for (robot rob : gameGUI.getRobotsList()) {
            for (fruits fruit : gameGUI.getFruitsList()) {
                edge = fruit.fruitEdge(gameGUI.getGraph());
                if (edge.getSrc() == rob.getSrc() && edge.getDest() == rob.getDest()) {
                    if (levelNum == 23 || levelNum == 21) {
                        ans = 20;
                        return ans;
                    }
                    ans = 52;
                    return ans;
                }
            }
        }
        return ans;
    }

    @Override
    public void run() {
        int i = 0;
        while (gameGUI.getGame().isRunning()) {
            gameGUI.getGame().move();
            gameGUI.updateFruits();
            gameGUI.updateRobots();
            moveRobots(gameGUI.getGame(), gameGUI.getGraph());
            gameGUI.DrawGraph();
            gameGUI.getF().drawFruits(gameGUI.getFruitsList()); // draw the fruits on the graph
            gameGUI.getR().drawRobots(gameGUI.getRobotsList()); // draw the robots on the graph
            StdDraw.show();
            try {
                sleep(timeToSleep());
                System.out.println(timeToSleep());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameGUI.finishGame();
        System.out.println("game is over" + gameGUI.getGame().toString());
        try {
            gameGUI.KML();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KML_Logger kml = new KML_Logger();
        String res = gameGUI.getGame().toString();
        String remark = kml.getString();
        gameGUI.getGame().sendKML(remark);
        System.out.println(res);
    }
}