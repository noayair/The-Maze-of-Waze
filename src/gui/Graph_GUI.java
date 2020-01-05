package gui;

import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.*;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

public class Graph_GUI{
    public graph_algorithms algo = new Graph_Algo();
    public graph dgraph = new DGraph();

    //constructor

    public Graph_GUI(){
        algo = new Graph_Algo();
        dgraph = new DGraph();
        StdDraw.g = this;
    }

    public Graph_GUI (graph d){
        dgraph=d;
        algo.init(d);
        StdDraw.g=this;
    }

    public void addNode(int key, double x, double y){
        NodeData n = new NodeData(key);
        n.setLocation(new Point3D(x,y));
        dgraph.addNode(n);
        StdDraw.g=this;
    }

    public void add_edge(int src, int dest, double w){
        dgraph.connect(src, dest, w);
        StdDraw.g=this;
    }

    public void removeNode(int key){
        dgraph.removeNode(key);
        StdDraw.g=this;
    }

    public void removeEdge(int src, int dest) {
        dgraph.removeEdge(src,dest);
        StdDraw.g=this;
    }

    public void save(String file_name){
        algo.init(dgraph);
        algo.save(file_name );
    }

    public graph up(String file_name) throws FileNotFoundException {
        Graph_Algo g = new Graph_Algo();
        g.init(file_name);
        algo = g;
        dgraph = ((Graph_Algo) algo).getGraph();
        return dgraph;
    }

    public boolean isConnected(){
        algo.init(dgraph);
        return algo.isConnected();
    }

    public double shortestPathDist (int src, int dest){
        algo.init(dgraph);
        return algo.shortestPathDist(src,dest);
    }

    public List<node_data> shortestPath(int src, int dest) {
        algo.init(dgraph);
        return algo.shortestPath(src,dest);
    }

    public List<node_data> TSP(List<Integer> targets) {
        algo.init(dgraph);
        return algo.TSP(targets);
    }

    public void DrawGraph() {
        graph g = this.dgraph;

        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);
        StdDraw.setPenColor(Color.blue);
        StdDraw.setPenRadius(0.01);
        String s = "";
        for (node_data n : g.getV()) {
            Point3D p = n.getLocation();
            StdDraw.filledCircle(p.x(), p.y(),1);
            s += Integer.toString(n.getKey());
            StdDraw.text(p.x() +2,p.y() +2,s);
            s = "";
        }
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())){
                double src_x = n.getLocation().x();
                double src_y = n.getLocation().y();
                double dest_x = g.getNode(e.getDest()).getLocation().x();
                double dest_y = g.getNode(e.getDest()).getLocation().y();
                StdDraw.setPenColor(Color.RED);
                StdDraw.setPenRadius(0.002);
                StdDraw.line(src_x , src_y , dest_x , dest_y);
                String weight = Double.toString(e.getWeight());
                StdDraw.text((src_x + dest_x)/2 , (src_y + dest_y)/2 , weight);
                StdDraw.setPenColor(Color.yellow);
                StdDraw.setPenRadius(0.01);
                StdDraw.filledCircle(src_x * 0.2 + dest_x * 0.8, src_y * 0.2 + dest_y * 0.8, 1);

            }
        }
    }

    public static void main(String[] args) {
        //example of the graph
        NodeData n1 = new NodeData(1);
        n1.setLocation(new Point3D(80,90));
        NodeData n2 = new NodeData(2);
        n2.setLocation(new Point3D(80,60));
        NodeData n3 = new NodeData(3);
        n3.setLocation(new Point3D(70,70));
        NodeData n4 = new NodeData(4);
        n4.setLocation(new Point3D(50,60));
        NodeData n5 = new NodeData(5);
        n5.setLocation(new Point3D(50,90));
        NodeData n6 = new NodeData(6);
        n6.setLocation(new Point3D(20,30));
        NodeData n7 = new NodeData(7);
        n7.setLocation(new Point3D(20,50));
        node_data n8 = new NodeData(8);
        n8.setLocation(new Point3D(2,7));


        DGraph dgraph = new DGraph();
        dgraph.addNode(n1);
        dgraph.addNode(n2);
        dgraph.addNode(n3);
        dgraph.addNode(n4);
        dgraph.addNode(n5);
        dgraph.addNode(n6);
        dgraph.addNode(n7);

        dgraph.connect(2, 1, 4);
        dgraph.connect(1, 3, 3);
        dgraph.connect(1, 5, 20);
        dgraph.connect(3, 2, 6);
        dgraph.connect(2, 4, 5);
        dgraph.connect(4, 6, 2);
        dgraph.connect(6, 7, 3);
        dgraph.connect(7, 5, 5);
        dgraph.connect(3, 4, 11);
        dgraph.connect(4, 5, 10);
        dgraph.connect(5, 3, 8);

        Graph_GUI p = new Graph_GUI(dgraph);
        p.algo.init(dgraph);

        p.DrawGraph();
    }
}