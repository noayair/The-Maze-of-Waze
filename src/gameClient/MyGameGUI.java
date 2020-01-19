package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import elements.fruits;
import elements.robot;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class MyGameGUI extends Thread {
    private game_service game;
    private DGraph graph = new DGraph();
    private Graph_Algo algo = new Graph_Algo(graph);
    private MyGameAlgo gameAlgo = new MyGameAlgo(this);
    private List<fruits> fruitsList = new ArrayList<>();
    private List<robot> robotsList = new ArrayList<>();
    private fruits f = new fruits();
    private robot r = new robot();
    public Range Range_x;
    public Range Range_y;
    private KML_Logger kml = new KML_Logger();
    public int level;


    //constructor
    public MyGameGUI() throws IOException {
        StdDraw.gameGUI = this;
        openWindow();
    }

    //constructor for the automatic game
    public MyGameGUI (int x) throws IOException {
        StdDraw.gameGUI=this;
        this.gameAlgo = new MyGameAlgo(this);
    }

    public void openWindow(){
        StdDraw.setCanvasSize(1024,512);
        StdDraw.clear(Color.white);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
    }

    /**
     * prints a clock and the score on the screen.
     */
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StdDraw.gameGUI = this;
    }


    //getters ans setter

    public List<fruits> getFruitsList() {
        return this.fruitsList;
    }

    public game_service getGame() {
        return game;
    }

    public DGraph getGraph() {
        return graph;
    }

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

    //functions

    /**
     * gets level and draw the graph, the fruits and the robots for the game
     * The user has to enter the nodes he wants to put the robots into.
     * If there are several robots on the user enter the numbers of the nodes in this way- **number, number..".
     * To move the robot to the next node, the user must click one of the nodes adjacent to it. Only so the robot can move.
     */
    public void startGameManual(int level) {
        this.level = level;
        game_service game = Game_Server.getServer(level); // you have [0,23] games
        this.game = game;
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
        String StringRob = JOptionPane.showInputDialog(null, "Please enter " + numberRobot + "node keys for Robots.", "Shape of x,y,z,w", 1);
        int[] robotArr = new int[numberRobot];
        String[] arr = new String[numberRobot];
        if (numberRobot == 1) {
            robotArr[0] = Integer.parseInt(StringRob);
        } else {
            arr = StringRob.split(",");
            for (int i = 0; i < numberRobot; i++) {
                robotArr[i] = Integer.parseInt(arr[i]);
                if(robotArr[i] == robotArr[i+1]){ // If the user entered 2 numbers the same, we will get an error
                    throw new NullPointerException("Err");
                }
            }
        }
        game.startGame();
        MikumRobot(robotArr); // we placed robots in the server
        List<String> robotList = game.getRobots();
        this.robotsList = r.fillRobotList(robotList);
        for (robot r : robotsList) {
            r.drawRobots(this.robotsList);
            StdDraw.show();
        }
        this.game.startGame();
        this.start();
    }

    /**
     * gets the fruits from the server.
     */
    public void initFruits(){
        this.fruitsList.clear();
        List<String> fruitList = this.game.getFruits(); // list of all the fruits in this level
        for (String s : fruitList) { // for that init all the fruits from json and adds it to the list
            f = new fruits();
            f = f.init(s);
            this.fruitsList.add(f);
        }
        this.fruitsList.sort((o1, o2) -> (int)(o2.getValue())-(int)(o1.getValue()));
    }

    /**
     * get the number of robots from server.
     * @return number of robots
     */
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

    public int numRobots(){
        String robNum = game.toString();
        int ans = numRobots(robNum);
        return ans;
    }

    /**
     * add robots to the game
     * @param arr
     */
    public void MikumRobot(int[] arr) {
        for (int i = 0; i <arr.length ; i++) {
            this.game.addRobot(arr[i]);
        }
    }

    /**
     * adds the robot next to the fruits before the start
     */

    /**
     * this function move robots manually.
     * in help STD and function GoToRobot.
     * Searches for the point the user clicks from the neighboring nodes and the robot goes to it
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
                    int src = obj.getInt("src");
                    int dest = obj.getInt("dest");
                    if (StdDraw.isMousePressed()) {
                        NodeData n = new NodeData(GoToRobot(StdDraw.mouseX(), StdDraw.mouseY()));
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

    /**
     *Takes the point of clicking a mouse and find the node closest to it.
     * @return
     */
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


    /**
     * update the fruits from the server while the game is running.
     */
    public void updateFruits(){
        List<String> updateFruits = this.game.getFruits();
        if(updateFruits != null){
            for (int i = 0; i < this.fruitsList.size(); i++) {
                this.fruitsList.get(i).update(updateFruits.get(i));
                if(this.kml != null){
                    if(this.fruitsList.get(i).getType() == 1){
                        this.kml.addPlaceMark("apple" , this.fruitsList.get(i).getPos().toString());
                    }else{
                        this.kml.addPlaceMark("http://cafe.themarker.com/media/t/204/675/8/file_0.jpg" , this.fruitsList.get(i).getPos().toString());
                    }
                }
            }
        }
    }

    /**
     * update the robots from the server while the game is running.
     */
    public void updateRobots(){
        List<String> updateRobots = this.game.getRobots();
        for (int i = 0; i < this.robotsList.size(); i++) {
            this.robotsList.get(i).update(updateRobots.get(i));
            if (this.kml != null){
                this.kml.addPlaceMark("https://banner2.cleanpng.com/20180219/hvw/kisspng-robot-cartoon-clip-art-robot-5a8b8347f17e33.1624179315190925519892.jpg" , this.robotsList.get(i).getPos().toString());
            }
        }
    }

    @Override
    public void run() {
        int i = 0;
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
                sleep(70);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            finishGame();
            System.out.println("game is over" + this.game.toString());
        try {
            KML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finishGame(){
        StdDraw.setCanvasSize(1024,512);
        StdDraw.clear(Color.BLACK);
        StdDraw.setYscale(-51,50);
        StdDraw.setXscale(-51,50);
        StdDraw.picture(0,0,"game over.jpg" , 50 , 50);
        StdDraw.show();
    }

    public void KML() throws IOException {
        int s = JOptionPane.showConfirmDialog(null,"Do you want to save the game to KML?","Please choose Yes/No",JOptionPane.YES_NO_OPTION);
        if(s == 1) StdDraw.saveToKML = false;
        else StdDraw.saveToKML = true;
        System.out.println(StdDraw.saveToKML + " at KML");
        if(StdDraw.saveToKML){
            kml.endKML();
        }
    }

    /**
     * draw the graph that the server give.
     */
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

    public void setKml(KML_Logger kml) {
        this.kml = kml;
    }

    public static void main(String[] args) throws IOException {
        MyGameGUI p = new MyGameGUI();
    }
}