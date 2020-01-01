package algorithms;

import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.graph;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class Graph_AlgoTest {
    public static Graph_Algo g = new Graph_Algo();

    @Before
    public void setUp() throws Exception {
        graph graphs = new DGraph();
        g.init(graphs);
        Graph_Algo g = new Graph_Algo(graphs);
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);
        NodeData n3 = new NodeData(3);
        NodeData n4 = new NodeData(4);
        NodeData n5 = new NodeData(5);
        NodeData n6 = new NodeData(6);
        NodeData n7 = new NodeData(7);
        graphs.addNode(n1);
        graphs.addNode(n2);
        graphs.addNode(n3);
        graphs.addNode(n4);
        graphs.addNode(n5);
        graphs.addNode(n6);
        graphs.addNode(n7);
        graphs.connect(2, 1, 4);
        graphs.connect(1, 3, 3);
        graphs.connect(1, 5, 20);
        graphs.connect(3, 2, 6);
        graphs.connect(2, 4, 5);
        graphs.connect(4, 6, 2);
        graphs.connect(6, 7, 3);
        graphs.connect(7, 5, 5);
        graphs.connect(3, 4, 11);
        graphs.connect(4, 5, 10);
        graphs.connect(5, 3, 8);
    }

    @Test
    public void save_init() throws IOException {
        g.save("file.txt");
        graph g2 = new DGraph();
        graph_algorithms gr = new Graph_Algo(g2);
        gr.init("file.txt");
        assertEquals(g.shortestPath(1,7) , gr.shortestPath(1,7));
    }

    @Test
    public void isConnected() {
        assertEquals(true , g.isConnected());
    }

    @Test
    public void shortestPathDist() {
        assertEquals(19.0 , g.shortestPathDist(1,7) , 0.0000001);
    }

    @Test
    public void shortestPath() {
        assertEquals("[1, 3, 4, 6, 7]" , g.shortestPath(1,7).toString());
    }

    @Test
    public void TSP() {
        List<Integer> target = new LinkedList<>();
        target.add(1);
        target.add(2);
        target.add(3);
        target.add(4);
        target.add(5);
        assertEquals("[1, 3, 2, 4, 5]" , g.TSP(target).toString());
    }

    @Test
    public void copy() {
        graph g1 = g.copy();
        assertEquals("[1, 2, 3, 4, 5, 6, 7]" , g1.getV().toString());
        assertEquals("[3, 5]" , g1.getE(1).toString());
        assertEquals("[1, 4]" , g1.getE(2).toString());
        assertEquals("[2, 4]" , g1.getE(3).toString());
        assertEquals("[5, 6]" , g1.getE(4).toString());
        assertEquals("[3]" , g1.getE(5).toString());
        assertEquals("[7]" , g1.getE(6).toString());
        assertEquals("[5]" , g1.getE(7).toString());
    }
}