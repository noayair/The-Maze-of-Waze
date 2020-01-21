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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyGameAlgo extends Thread {
    private MyGameGUI gameGUI;
    private KML_Logger kml = new KML_Logger();

    //constructor
    public MyGameAlgo(MyGameGUI gameGUI) throws IOException {
        this.gameGUI = gameGUI;
    }

    //functions
    /**
     * Starts the game according to the selected level,
     * and updates the fruits, robots and graph accordingly.
     * @param level
     */
    public void startGameAutomatic(int level) {
//        int id = 313431702;
//        Game_Server.login(id);
        game_service game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games)
        gameGUI.setGame(game); // set this game to the MyGameGUI
        gameGUI.getGraph().init(game.getGraph()); //build a graph for the game
        gameGUI.initFruits();
        addRobots(); // adds robots to the game and to the list
        gameGUI.DrawGraph(); // draw the graph
        gameGUI.getF().drawFruits(gameGUI.getFruitsList()); // draw the fruits on the graph
        gameGUI.getR().drawRobots(gameGUI.getRobotsList()); // draw the robots on the graph
//        game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
        gameGUI.getGame().startGame();
        this.start();
    }

    /**
     * get from the server how many robots it has in this level,
     * and add the robots to the game on a strategic node start.
     */
    public void addRobots(){
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
        for (int i = 0; i < robotsSize; i++){
            fruit = fruitsList.get(i);
            rob = new robot(); // create empty robot
            edge = fruit.fruitEdge(gameGUI.getGraph()); // checking on which edges the fruit i is (the list is sort by value)
            if(fruit.getType() == -1){ // if the fruit is banana
                gameGUI.getGame().addRobot(Math.max(edge.getSrc() , edge.getDest())); // add the robot to the max node between the src and the dest
            }else if(fruit.getType() == 1){ // if the fruit is apple
                gameGUI.getGame().addRobot(Math.min(edge.getSrc() , edge.getDest())); // add the robot to the min node between the src and the dest
            }
            rob = rob.init(gameGUI.getGame().getRobots().get(i)); // init from the server all the information about the robot to rob
            if(fruit.getType() == -1){
                rob.setDest(edge.getSrc());
            }else if(fruit.getType() == 1){
                rob.setDest(edge.getDest());
            }
            robotList.add(rob); // add the current robot to the robots list
            fruit.setTag(1);
            System.out.println(rob.getDest());
            System.out.println(gameGUI.getGame().getRobots());
        }
    }

    /**
     * get from the server the id, the src and the dest of the robot and set the next dest.
     * @param gs
     * @param dg
     */
//    public void moveRobots(game_service gs, DGraph dg) {
//        List<String> move = gs.move();
//        if (move != null) {
//            long time = gs.timeToEnd();
//            for (int i = 0; i < move.size(); i++) {
//                String robotJson = move.get(i);
//                try {
//                    JSONObject obj = new JSONObject(robotJson);
//                    JSONObject rob = obj.getJSONObject("Robot");
//                    int id = rob.getInt("id");
//                    int src = rob.getInt("src");
//                    int dest = rob.getInt("dest");
//                    if(dest == -1) { // if the robot has no dest
//                        List<node_data> shortest;
//                        if (nextNode(dg, src) == -1) { // if there is no fruit on any edge that exits from the node on which the robot is on
//                            if(!gameGUI.getRobotsList().get(id).getWay().isEmpty()){ // if the robot's target list is not over yet, it has not yet reached its final destination
//                                shortest = gameGUI.getRobotsList().get(id).getWay(); // shortest list = to the robot's target list
//                                shortest.get(0).setTag(1);
//                                dest = shortest.get(0).getKey(); // update the dest of the robot to be the j object in shortest
//                                gs.chooseNextEdge(id, dest); // update the dest in the server
//                                gameGUI.getRobotsList().get(id).checkNode(dest); // add the dest to the check list
//                                gameGUI.getRobotsList().get(id).getWay().remove(shortest.get(0)); // remove the dest from the list
//                            }else { // if the robot has reached its final destination and needs a new target
//                                int closestDest = closestFruit(gameGUI.getGraph().getNode(src).getKey()); // search the closest fruit to the robot and update 'closestDest' to be thr src of the edge that the fruit is on
//                                shortest = shortestWay(src, closestDest); // update the shortest list to be the way to the closest fruit
//                                dest = shortest.get(0).getKey(); // update the dest to be the first object in the shortest list
//                                gs.chooseNextEdge(id, dest); // update the dest in the server
//                                gameGUI.getRobotsList().get(id).checkNode(dest); // add the dest to the check list
//                                shortest.remove(shortest.get(0)); // remove the dest from the list
//                                gameGUI.getRobotsList().get(id).setWay(shortest);
//                            }
//                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
//                            System.out.println(rob);
//                        }else { // if there is a fruit on one of the edges that exits from the node which the robot is on
//                            dest = nextNode(dg, src); // update the dest to be the dest of the edge that the fruit is on
//                            gs.chooseNextEdge(id, dest); // update the dest in the server
//                            gameGUI.getRobotsList().get(id).checkNode(dest); // add the dest to the check list
//                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
//                            System.out.println(rob);
//                        }
//                    }
//                    if(dest == gameGUI.getRobotsList().get(id).getCheck().peek()){ // if the robot return to the same node more then 2 times
//                        System.out.println("random");
//                        dest = nextNodeRandom(dg , src); // update the dest of the robot to be another
//                        gs.chooseNextEdge(id, dest);
//                        gameGUI.getRobotsList().get(id).checkNode(dest);
//                    }
//                    if(gameGUI.getRobotsList().size() > 1){
//                        for (int j = 0; j < gameGUI.getRobotsList().size()-1; j++) { // check if tow or more robots go to the same fruit
//                            if(gameGUI.getRobotsList().get(j).getDest() == gameGUI.getRobotsList().get(j+1).getDest()){ // if they are, its send one of them to another random fruit
//                                gameGUI.getRobotsList().get(j+1).setDest(nextNodeRandom(dg , gameGUI.getRobotsList().get(j+1).getSrc()));
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//
//                }
//            }
//        }
//    }

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
                    if(dest == -1) { // if the robot has no dest
                        List<node_data> shortest;
                        if (nextNode(dg, src) == -1) { // if there is no fruit on any edge that exits from the node on which the robot is on
                            int closestDest = closestFruit(gameGUI.getGraph().getNode(src).getKey()); // search the closest fruit to the robot and update 'closestDest' to be thr src of the edge that the fruit is on
                            shortest = shortestWay(src, closestDest); // update the shortest list to be the way to the closest fruit
                            dest = shortest.get(1).getKey(); // update the dest to be the first object in the shortest list
                            gameGUI.getRobotsList().get(id).setDest(dest);
                            gameGUI.getRobotsList().get(id).checkNode(dest);
//                            gs.chooseNextEdge(id, dest); // update the dest in the server
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        }else { // if there is a fruit on one of the edges that exits from the node which the robot is on
                            dest = nextNode(dg, src); // update the dest to be the dest of the edge that the fruit is on
//                            gs.chooseNextEdge(id, dest); // update the dest in the server
                            gameGUI.getRobotsList().get(id).checkNode(dest); // add the dest to the check list
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        }
                    }
                    if(gameGUI.getRobotsList().size() > 1){
                        for (int j = 0; j < gameGUI.getRobotsList().size()-1; j++) { // check if tow or more robots go to the same fruit
                            if(gameGUI.getRobotsList().get(j).getDest() == gameGUI.getRobotsList().get(j+1).getDest()){ // if they are, its send one of them to another random fruit
                                dest = nextNodeRandom(dg , gameGUI.getRobotsList().get(j+1).getSrc());
                                gameGUI.getRobotsList().get(j+1).setDest(dest);
                                gs.chooseNextEdge(gameGUI.getRobotsList().get(j+1).getId() , dest);
                            }
                        }
                    }
                    gs.chooseNextEdge(id, dest); // update the dest in the server
                    gameGUI.getEdgesWithFruits().clear();
                } catch (JSONException e) {

                }
            }
        }
    }

//    public void sendRobot(int dest , int id , game_service gs){
//        gs.chooseNextEdge(id, dest); // update the dest in the server
//        gameGUI.getRobotsList().get(id).checkNode(dest); // add the dest to the check list
//        gameGUI.getRobotsList().get(id).getWay().remove(shortest.get(0)); // remove the dest from the list
//    }

    /**
     * a random walk implementation.
     * @param g
     * @param src
     * @return
     */
    public int nextNodeRandom(DGraph g, int src) {
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

    /**
     * Goes over all the edges that come out of the given node(by key) and checks to see if one has fruit.
     * If so, returns the dest of that edge.
     * If not, returns -1.
     * @param dg
     * @param src
     * @return the dest of the edge, if it has on it a fruit, -1 if not.
     */
    public int nextNode(DGraph dg, int src) {
        Collection<edge_data> edges = dg.getE(src); // list of all the edges that exits from the node that the robot is on
        int closest1 = -1;
        int closest2 = -1;
        edge_data close1 = new EdgeData();
        for (edge_data e : edges) {
            for (int i = 0; i < gameGUI.getFruitsList().size(); i++) { // go through the fruitsList
//                if (gameGUI.getFruitsList().get(i).getTag() == 0) { // if no robot is designed to go for this fruit
                    edge_data fruitEdge = gameGUI.getFruitsList().get(i).fruitEdge(dg); // find the edge that the fruit is on
                    if (e.getDest() == fruitEdge.getDest() && e.getSrc() == fruitEdge.getSrc()) { // if it is the same edge that the robot is on
                        if(closest1 == -1){ // there is no edge yet
                            close1 = e;
                            closest1 = 0;
                        }else if(closest1 == 0 && closest2 == -1){ // there is 1 edge but not 2
                            gameGUI.getEdgesWithFruits().add(e);
                            closest2 = 0;
                        }else if(closest1 == 0 && closest2 == 0){
                            break;
                        }
                    }
//                }
            }
        }if(closest1 == 0){
//            gameGUI.getFruitsList().get(i).setTag(1); // change fruit tag to 1
            gameGUI.getEdgesWithFruits().add(close1);
            return close1.getDest(); // return the dest of the edge
        }else{
            return -1; // if we didn't find edge with fruit return -1
        }
    }

    /**
     * finds the closest fruit to the robot.
     * @param src
     * @return the src of the edge that the closest fruit is on.
     */
    public int closestFruit (int src){
        double dest = Double.POSITIVE_INFINITY;
        int ans = -1;
        edge_data edgeFruit = new EdgeData();
        int fruitSrc;
        double shortest;
        for (int i = 0; i < gameGUI.getFruitsList().size(); i++) {
            fruitSrc = gameGUI.getFruitsList().get(i).fruitEdge(gameGUI.getGraph()).getSrc(); // find the src of the edge that the fruit is on it
            shortest = gameGUI.getAlgo().shortestPathDist(src , fruitSrc); // find the distance between the robot location to the src fruit
            if(shortest < dest){
                dest = shortest;
                ans = fruitSrc;
                edgeFruit = gameGUI.getFruitsList().get(i).fruitEdge(gameGUI.getGraph());
            }
        }
        gameGUI.getEdgesWithFruits().add(edgeFruit);
        return ans;
    }

    /**
     * @param src
     * @param dest
     * @return list of the way to the closest fruit.
     */
    public List<node_data> shortestWay (int src, int dest){
        return gameGUI.getAlgo().shortestPath(src, dest);
    }

    public int timeToSleep(DGraph dg){
        int ans = 170;
        if(gameGUI.getRobotsList().size() < gameGUI.getFruitsList().size()/3){
            ans = 130;
        }else if((gameGUI.getRobotsList().size() == gameGUI.getFruitsList().size()) && (gameGUI.getRobotsList().size() >1)){
            ans = 83;
        }
        edge_data edge = new EdgeData();
        for (robot rob : gameGUI.getRobotsList()) {
            for (fruits fruit : gameGUI.getFruitsList()){
                edge = fruit.fruitEdge(gameGUI.getGraph());
//                if(rob.getDest() == rob.getCheck().peek()){ // if the robot return to the same node more then 2 times
//                        System.out.println("random");
//                        ans = 10;
//                        return ans;
//                    }
                if(edge.getSrc() == rob.getSrc() && edge.getDest() == rob.getDest()){
                    ans = 50;
//                    System.out.println(ans);
                    return ans;
                }
            }
        }
        return ans;
//                    Point3D srcLocation = dg.getNode(rob.getSrc()).getLocation();
//                    Point3D destLocation = dg.getNode(rob.getDest()).getLocation();
//                    double edgeWeight = dg.getEdge(rob.getSrc() , rob.getDest()).getWeight();
//                    int robSpeed = rob.getSpeed();
//                    double dest_fruit_dist = destLocation.distance2D(fruit.getPos());
//                    double src_dest_dist = srcLocation.distance2D(destLocation);
//                    double n = dest_fruit_dist / src_dest_dist;
//                    ans = ((edgeWeight * n) / robSpeed);



//        return ans;
    }

    public void checkSleep(){
        for(robot rob : gameGUI.getRobotsList()){

        }
    }

    @Override
    public void run() {
        int i = 0;
        while(gameGUI.getGame().isRunning()){
            gameGUI.updateFruits();
            gameGUI.updateRobots();
            moveRobots(gameGUI.getGame() , gameGUI.getGraph());
            gameGUI.DrawGraph();
            gameGUI.getF().drawFruits(gameGUI.getFruitsList()); // draw the fruits on the graph
            gameGUI.getR().drawRobots(gameGUI.getRobotsList()); // draw the robots on the graph
            StdDraw.show();
            try {
                sleep(timeToSleep(gameGUI.getGraph()));
                System.out.println(timeToSleep(gameGUI.getGraph()));
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
        gameGUI.getGame().sendKML(remark); // Should be your KML (will not work on case -1).
        System.out.println(res);
    }
}