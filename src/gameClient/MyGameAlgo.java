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
            if(levelNum == 16 && i == 1){
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
     * get from the server the id, the src and the dest of the robot and set the next dest.
     *
//     * @param gs
//     * @param dg
     */
    public void moveRobots(game_service gs , DGraph dg){
        int robDest = -1;
        for(robot rob : gameGUI.getRobotsList()){
            robDest = nextNode(rob , dg);
            gs.chooseNextEdge(rob.getId() , robDest);
            System.out.println("Turn to node: " + robDest);
        }
    }



    public int nextNode(robot rob , DGraph dg){
            double temp;
            double shortestDist = Double.MAX_VALUE;
            List<robot> robotList = gameGUI.getRobotsList();
            List<node_data> shortest = new ArrayList<>();
            edge_data fruitEdge = new EdgeData();
            fruits f = new fruits();
            int robDest = rob.getDest();
                if(rob.getDest() == -1) {
                    for (fruits fruit : gameGUI.getFruitsList()) {
                        if (fruit.getTag() == 0) {
                            fruitEdge = fruit.fruitEdge(dg);
                            temp = gameGUI.getAlgo().shortestPathDist(rob.getSrc(), fruitEdge.getSrc());
                            if(rob.getSrc() == fruitEdge.getSrc()){
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

//    /**
//     * a random walk implementation.
//     *
//     * @param g
//     * @param src
//     * @return
//     */
//    public int nextNodeRandom(DGraph g, int src) {
//        int ans = -1;
//        Collection<edge_data> ee = g.getE(src);
//        Iterator<edge_data> itr = ee.iterator();
//        int s = ee.size();
//        int r = (int) (Math.random() * s);
//        int i = 0;
//        while (i < r) {
//            itr.next();
//            i++;
//        }
//        ans = itr.next().getDest();
//        return ans;
//    }

//    /**
//     * Goes over all the edges that come out of the given node(by key) and checks to see if one has fruit.
//     * If so, returns the dest of that edge.
//     * If not, returns -1.
//     *
//     * @param dg
//     * @param src
//     * @return the dest of the edge, if it has on it a fruit, -1 if not.
//     */
//    public int nextNode(DGraph dg, int src) {
//        Collection<edge_data> edges = dg.getE(src); // list of all the edges that exits from the node that the robot is on
//        for (edge_data e : edges) {
//            for (int i = 0; i < gameGUI.getFruitsList().size(); i++) { // go through the fruitsList
//                if(gameGUI.getFruitsList().get(i).getTag() == 0) {
//                    edge_data fruitEdge = gameGUI.getFruitsList().get(i).fruitEdge(dg); // find the edge that the fruit is on
//                    if (e.getDest() == fruitEdge.getDest() && e.getSrc() == fruitEdge.getSrc()) { // if it is the same edge that the robot is on
//                        gameGUI.getFruitsList().get(i).setTag(1); // change fruit tag to 1
//                        return e.getDest();
//                    }
//                }
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * finds the closest fruit to the robot.
//     * @param src
//     * @return the src of the edge that the closest fruit is on.
//     */
//    public int closestFruit (int src){
//        double dest = Double.POSITIVE_INFINITY;
//        int ans = -1;
//        edge_data edgeFruit = new EdgeData();
//        int fruitSrc;
//        double shortest;
//        for (int i = 0; i < gameGUI.getFruitsList().size(); i++) {
//            fruitSrc = gameGUI.getFruitsList().get(i).fruitEdge(gameGUI.getGraph()).getSrc(); // find the src of the edge that the fruit is on it
//            shortest = gameGUI.getAlgo().shortestPathDist(src , fruitSrc); // find the distance between the robot location to the src fruit
//            if(shortest < dest){
//                dest = shortest;
//                ans = fruitSrc;
//                edgeFruit = gameGUI.getFruitsList().get(i).fruitEdge(gameGUI.getGraph());
//            }
//        }
//        gameGUI.getEdgesWithFruits().add(edgeFruit);
//        return ans;
//    }

//    /**
//     * @param src
//     * @param dest
//     * @return list of the way to the closest fruit.
//     */
//    public List<node_data> shortestWay (int src, int dest){
//        return gameGUI.getAlgo().shortestPath(src, dest);
//    }

    public int timeToSleep() {
        int ans = 100;
        if(levelNum == 23){
            ans = 80;
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
//                if(rob.getDest() == rob.getCheck().peek()){ // if the robot return to the same node more then 2 times
//                        ans = 10;
//                        return ans;
//                    }
                if (edge.getSrc() == rob.getSrc() && edge.getDest() == rob.getDest()) {
                    if(levelNum == 23) {
                        ans = 20;
                        return ans;
                    }
//                    }else if(levelNum == 16){
//                        ans = 80;
//                        return ans;
//                    }
                    ans = 50;
                    return ans;
                }
//                if(rob.getSpeed() > 2){
//                    ans = 80;
//                }
            }
        }
        return ans;
    }

    @Override
    public void run() {
        int i = 0;
        while(gameGUI.getGame().isRunning()){
            gameGUI.getGame().move();
            gameGUI.updateFruits();
            gameGUI.updateRobots();
            moveRobots(gameGUI.getGame() , gameGUI.getGraph());
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