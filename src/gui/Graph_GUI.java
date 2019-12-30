package gui;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

import java.awt.*;

public class Graph_GUI {
    public static void DrawGraph(DGraph g) {
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
                StdDraw.filledCircle(dest_x , dest_y+2,1);
            }
        }
    }

    public static void main(String[] args) {
        DGraph graph = new DGraph();
        Graph_Algo g = new Graph_Algo(graph);
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
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);
        graph.addNode(n7);
        graph.connect(1, 2, 4);
        graph.connect(1, 3, 3);
        graph.connect(1, 5, 20);
        graph.connect(3, 2, 6);
        graph.connect(2, 4, 5);
        graph.connect(4, 6, 2);
        graph.connect(6, 7, 3);
        graph.connect(7, 5, 5);
        graph.connect(3, 4, 11);
        graph.connect(4, 5, 10);
        graph.connect(5, 3, 8);
        DrawGraph(graph);
    }
}