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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyGameAlgo extends Thread {
    private MyGameGUI gameGUI;

    public MyGameAlgo(MyGameGUI gameGUI) {
        this.gameGUI = gameGUI;
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
        for (int i = 0; i < robotsSize; i++){
            rob = new robot(); // create empty robot
            edge = fruitsList.get(i).fruitEdge(gameGUI.getGraph()); // checking on which edges the fruit is
            if(fruitsList.get(i).getType() == -1){
                gameGUI.getGame().addRobot(Math.max(edge.getSrc() , edge.getDest()));
            }else if(fruitsList.get(i).getType() == 1){
                gameGUI.getGame().addRobot(Math.min(edge.getSrc() , edge.getDest())); // ask from the server to add the robot on the src edge
            }
            rob = rob.init(gameGUI.getGame().getRobots().get(i));
//            if(this.fruitsList.get(i).getType() == -1){
//                rob.setDest(e.getSrc());
//            }else if(this.fruitsList.get(i).getType() == 1){
//                rob.setDest(e.getDest());
//            }
            rob.setDest(edge.getDest());
            robotList.add(rob); // add the current robot to the robots list
            System.out.println(rob.getDest());
            System.out.println(gameGUI.getGame().getRobots());
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
                            if(!gameGUI.getRobotsList().get(id).getWay().isEmpty()){
                                shortest = gameGUI.getRobotsList().get(id).getWay();
                                dest = shortest.get(0).getKey();
                                gs.chooseNextEdge(id , dest);
                                gameGUI.getRobotsList().get(id).checkNode(dest);
                                gameGUI.getRobotsList().get(id).getWay().remove(shortest.get(0));
                            }else {
                                int closestDest = closestFruit(gameGUI.getGraph().getNode(src).getKey());
                                shortest = shotestWay(src, closestDest);
                                if (shortest==null)
                                    System.out.println(closestDest + " " + src);
                                dest = shortest.get(0).getKey();
                                gs.chooseNextEdge(id, dest);
                                gameGUI.getRobotsList().get(id).checkNode(dest);
                                shortest.remove(shortest.get(0));
                                //     gs.chooseNextEdge(id,shortest.get(0).getKey());
                                gameGUI.getRobotsList().get(id).setWay(shortest);
                            }
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        } else {
                            dest = nextNode(dg, src);
                            gs.chooseNextEdge(id, dest);
                            gameGUI.getRobotsList().get(id).checkNode(dest);
                            System.out.println("Turn to node: " + dest + "  time to end:" + (time / 1000));
                            System.out.println(rob);
                        }
                    }
//                    if(this.robotsList.get(id).getCheck().size() == 3){

//                    System.out.println(dest);
//                    this.robotsList.get(id).getCheck().peek();
                    if(dest == gameGUI.getRobotsList().get(id).getCheck().peek()){
                        System.out.println("random");
                        dest = nextNodeRandom(dg , src);
                        gs.chooseNextEdge(id, dest);
                        gameGUI.getRobotsList().get(id).checkNode(dest);
                    }
//                    }

                    if(gameGUI.getRobotsList().size() > 1){
                        for (int j = 0; j < gameGUI.getRobotsList().size()-1; j++) { // check if tow or more robots go to the same fruit
                            if(gameGUI.getRobotsList().get(j).getDest() == gameGUI.getRobotsList().get(j+1).getDest()){ // if they are, its send one of them to another random fruit
                                gameGUI.getRobotsList().get(j+1).setDest(nextNodeRandom(dg , gameGUI.getRobotsList().get(j+1).getSrc()));
                            }
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


    public int nextNode(DGraph dg, int src) {
        Collection<edge_data> edges = dg.getE(src);
        for (edge_data e : edges) {
            for (int i = 0; i < gameGUI.getFruitsList().size(); i++) {
                edge_data fruitEdge = gameGUI.getFruitsList().get(i).fruitEdge(dg);
                if (e.getDest() == fruitEdge.getDest() && e.getSrc() == fruitEdge.getSrc()) {
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
        for (int i = 0; i < gameGUI.getFruitsList().size(); i++) {
            fruitSrc = gameGUI.getFruitsList().get(i).fruitEdge(gameGUI.getGraph()).getSrc(); // find the src of the edge that the fruit is on it
//            fruitLocation = this.graph.getNode(fruitsrc).getLocation();
            shortest = gameGUI.getAlgo().shortestPathDist(src , fruitSrc); // find the distance between the robot location to the src fruit
            if(shortest < dest){
                dest = shortest;
                ans = fruitSrc;
            }
        }
        return ans;
    }

    public List<node_data> shotestWay (int src, int dest){
        return gameGUI.getAlgo().shortestPath(src, dest);
    }
}
