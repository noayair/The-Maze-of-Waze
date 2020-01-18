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
    //private Graph_GUI gui = new Graph_GUI(graph);
    private Graph_Algo algo = new Graph_Algo(graph);
    private MyGameAlgo gameAlgo = new MyGameAlgo(this);
    private List<fruits> fruitsList = new ArrayList<>();
    private List<robot> robotsList = new ArrayList<>();
    private fruits f = new fruits();
    private robot r = new robot();
    public Range Range_x;
    public Range Range_y;


    //constructor
    public MyGameGUI() {
        StdDraw.gameGUI = this;
        openWindow();
        //   setWin();
    }

    public MyGameGUI (int x){
        StdDraw.gameGUI=this;
        this.gameAlgo = new MyGameAlgo(this);

    }

    public void openWindow(){
        StdDraw.setCanvasSize(1024,512);
        StdDraw.clear(Color.white);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
        //   StdDraw.picture(0,0,"openingScreen.png");
//        int senario=0;
//        String senarioString = JOptionPane.showInputDialog(null,"Please choose a Game Senario");
//        try{
//            senario=Integer.parseInt(senarioString);
//        }catch(Exception e1){e1.printStackTrace();}
//        String[] chooseGame = {"Manually Game","Auto Game"};
//        Object selctedGame = JOptionPane.showInputDialog(null,"Choose a Game mode","Message",JOptionPane.INFORMATION_MESSAGE,null,chooseGame,chooseGame[0]);
//        if(selctedGame =="Auto Game") {
//            StdDraw.clear();
//            StdDraw.enableDoubleBuffering();
//      //      algoGame.startGame(senario);
//        }
//        else{
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();

//
//
//        }

    }

    private void clock() {
        try {
            String gameInfo = game.toString();
            JSONObject line = new JSONObject(gameInfo);
            JSONObject ttt = line.getJSONObject("GameServer");
            int score = ttt.getInt("grade");
            StdDraw.setPenColor(Color.blue);
            StdDraw.setPenRadius(0.4);
            Font font = new Font("Arial", Font.BOLD, 15);
            StdDraw.setFont(font);
            StdDraw.text(Range_x.get_max()-0.0004, Range_y.get_max() - 0.0005, "Score : " + score);
            StdDraw.setPenColor(Color.red);
            StdDraw.setPenRadius(0.4);
            StdDraw.text(Range_x.get_max()-0.0002, Range_y.get_max() ,"Time to end : " +game.timeToEnd() / 1000);
//            StdDraw.setPenRadius(0.015);
//            StdDraw.setPenColor(new Color(142,17,17));
//            StdDraw.rectangle(Range_x.get_max()-0.0009,Range_y.get_max()-0.00009,0.0014,0.0004);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StdDraw.gameGUI = this;
    }


    //getters ans setter
    public List<fruits> getFruitsList() {
        return this.fruitsList;
    }

    //functions

    /**
     * gets level and draw the graph, the fruits and the robots for the game
     //     * @param level
     */



    public void startGameManual(int level) {
        this.game = Game_Server.getServer(level); // ask from the server the requested stage (you have [0,23] games)
//        this.game = game;
        String g = game.getGraph();
        DGraph d = new DGraph();
        d.init(g);
        this.graph = d;
        String RobNum = game.toString();
        initFruits();
        int numberRobot = numRobots(RobNum);
        DrawGraph();
        this.f.drawFruits(this.fruitsList);
        StdDraw.show();
        String StringRob = JOptionPane.showInputDialog(null,"Please enter " + numberRobot + "node keys for Robots.","Shape of x,y,z,w",1);
        int[] robotArr = new int[numberRobot];
//        System.out.println("num of robs:" + numberRobot);
//        System.out.println(StringRob);
        String[] arr=new String[numberRobot];
        if(numberRobot==1){
            robotArr[0]=Integer.parseInt(StringRob);
        }
        else {
            arr = StringRob.split(",");
            for (int i = 0; i < numberRobot; i++) {
                robotArr[i] = Integer.parseInt(arr[i]);
            }
        }
        // we placed robots in the server
        MikumRobot(robotArr);

        List<String> robotList = game.getRobots();
        this.robotsList = r.fillRobotList(robotList);
        r.drawRobots(this.robotsList);
        StdDraw.show();
        this.game.startGame();
        run();
    }

    public void initFruits(){
        this.fruitsList.clear();
        List<String> fruitList = this.game.getFruits(); // list of all the fruits in this level
        for (String s : fruitList) { // for that init all the fruits from json and adds it to the list
            f = new fruits();
            f = f.init(s);
            //        System.out.println(s);
            this.fruitsList.add(f);
        }
        this.fruitsList.sort((o1, o2) -> (int)(o2.getValue())-(int)(o1.getValue()));
    }


    public int numRobots(String s){  //get the number of robots from server.
        int num=0;
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject r = obj.getJSONObject("GameServer");
            num = r.getInt("robots");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public void MikumRobot(int[] arr) {
        for (int i = 0; i <arr.length ; i++) {
            this.game.addRobot(arr[i]);
        }
    }

    /**
     * adds the robot next to the fruits before the start
     */

    /**
     * this function move robots manuaaly.
     */

    public void moveRobotsByClick(game_service game, DGraph g) {
        List<String> log = game.move();
        if (log != null) {
            long t = game.timeToEnd();
            for (int i = 0; i < log.size(); i++) {
                String robotjson = log.get(i);
                try {
                    JSONObject line = new JSONObject(robotjson);
                    JSONObject obj = line.getJSONObject("Robot");
                    int rid = obj.getInt("id");
                    //       System.out.println("rob id: " + rid);
                    int src = obj.getInt("src");
                    int dest = obj.getInt("dest");

                    // which robot to move
//                    if (StdDraw.isMousePressed()) {
//                        x = StdDraw.mouseX();
//                        y = StdDraw.mouseY();
//                       node_data n = (NodeData) findNode(x, y);
//                        while(n==null) {
//                            x=StdDraw.mouseX();
//                            y=StdDraw.mouseY();
//                            n=(NodeData)findNode(x,y);
//                        }
//                        for (robot r : robotsList) {
//                            if (r.getSrc() == n.getKey())
//                                RobotId = r.getId();
//                            System.out.println("robot id: " + RobotId);
//                        }
//                    }

                    //   System.out.println("robot id second: " + RobotId);
                    // move robot to where
                    if (StdDraw.isMousePressed()) {
                        NodeData n = new NodeData(GoToRobot(StdDraw.mouseX(), StdDraw.mouseY()));
                        //                    System.out.println(n.getKey()+" the key");
                        if (dest == -1) {
                            this.game.chooseNextEdge(rid, n.getKey());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public int GoToRobot (double x, double y){
        Point3D p = new Point3D(x,y);
        Collection<node_data> node = this.graph.getV();
        for (node_data n : node){
            if(Math.abs(p.distance3D(n.getLocation()))<0.0003){
                return n.getKey();
            }
        }
        return -1;
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

    @Override
    public void run() {
        while (this.game.isRunning()) {
            System.out.println(this.game.timeToEnd() / 1000);
            updateFruits();
            updateRobots();
            moveRobotsByClick(this.game, graph);
            DrawGraph();
            this.r.drawRobots(this.robotsList);
            this.f.drawFruits(this.fruitsList);
            StdDraw.show();
            try {
                sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //      System.out.println( this.game.timeToEnd()/1000);

//                if(game.timeToEnd() == 0)
//                System.out.println("game is over" + this.game.toString());

    }

    public void finishGame(){
        StdDraw.setCanvasSize(1024,512);
        StdDraw.clear(Color.BLACK);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
        StdDraw.picture(0,0,"gameOver.png");
        StdDraw.show();
    }



    public void setWin(){
        StdDraw.setCanvasSize(1000, 500);
//        StdDraw.clear(Color.blue);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
        int level=-1;
        String level1 = JOptionPane.showInputDialog(null,"Please choose a Game level (1-23)");
        try{
            level=Integer.parseInt(level1);

        }catch(Exception e1){e1.printStackTrace();}
        String[] chooseGame = {"Manually Game","Auto Game"};
        Object selctedGame = JOptionPane.showInputDialog(null,"Choose a Game mode","Message",JOptionPane.INFORMATION_MESSAGE,null,chooseGame,chooseGame[0]);
        if(selctedGame =="Auto Game") {
            StdDraw.clear();
            StdDraw.enableDoubleBuffering();
            this.gameAlgo.startGameAutomatic(level);
        }
        else{
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        startGameManual(level);

        }
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
//                StdDraw.text(src_x * 0.3 + dest_x * 0.7 , src_y * 0.3 + dest_y * 0.7 , weight);
                StdDraw.setPenColor(Color.yellow);
                StdDraw.setPenRadius(0.15);
                StdDraw.filledCircle(src_x * 0.2 + dest_x * 0.8, src_y * 0.2 + dest_y * 0.8, ScaleX*0.1);
            }
        }
        clock();
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

    public game_service getGame() {
        return game;
    }

    public DGraph getGraph() {
        return graph;
    }

//    public Graph_GUI getGui() {
//        return gui;
//    }

    public Graph_Algo getAlgo() {
        return algo;
    }

    public List<robot> getRobotsList() {
        return robotsList;
    }

    public fruits getF() {
        return f;
    }

    public robot getR() {
        return r;
    }

    public MyGameAlgo getGameAlgo() {
        return gameAlgo;
    }

    public void setGame(game_service game) {
        this.game = game;
    }

    public void setGraph(DGraph graph) {
        this.graph = graph;
    }

//    public void setGui(Graph_GUI gui) {
//        this.gui = gui;
//    }

    public void setAlgo(Graph_Algo algo) {
        this.algo = algo;
    }

    public void setFruitsList(List<fruits> fruitsList) {
        this.fruitsList = fruitsList;
    }

    public void setRobotsList(List<robot> robotsList) {
        this.robotsList = robotsList;
    }

    public void setF(fruits f) {
        this.f = f;
    }

    public void setR(robot r) {
        this.r = r;
    }

    public static void main(String[] args) {
        MyGameGUI p = new MyGameGUI();
//        p.startGame(8);
    }
}