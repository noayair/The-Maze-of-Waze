import dataStructure.DGraph;
import dataStructure.NodeData;
import dataStructure.graph;
import dataStructure.node_data;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DGraphTest {
    public static DGraph g = new DGraph();

    @Before
    public void setUp() throws Exception {
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        node_data n4 = new NodeData(4);
        node_data n5 = new NodeData(5);
        node_data n6 = new NodeData(6);
        node_data n7 = new NodeData(7);
        node_data n8 = new NodeData(8);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);
        g.addNode(n7);
        g.addNode(n8);
        g.connect(2, 1, 4);
        g.connect(1, 3, 3);
        g.connect(1, 5, 20);
        g.connect(3, 2, 6);
        g.connect(2, 4, 5);
        g.connect(4, 6, 2);
        g.connect(6, 7, 3);
        g.connect(7, 5, 5);
        g.connect(3, 4, 11);
        g.connect(4, 5, 10);
        g.connect(5, 3, 8);
        g.connect(2 , 8 , 9);
        g.connect(8 , 4 , 9);
        g.connect(4 ,1 , 7);
    }

    @Test
    public void addNode() { //test also getV
        System.out.println(g.getMC());
        assertEquals("8" , g.getNode(8).toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]" , g.getV().toString());
    }

    @Test
    public void connect() { //test also getE
        System.out.println(g.getMC());
        assertEquals("[3, 5]" , g.getE(1).toString());
        assertEquals("[1, 4, 8]" , g.getE(2).toString());
        assertEquals("[2, 4]" , g.getE(3).toString());
        assertEquals("[1, 5, 6]" , g.getE(4).toString());
        assertEquals("[3]" , g.getE(5).toString());
        assertEquals("[7]" , g.getE(6).toString());
        assertEquals("[5]" , g.getE(7).toString());
        assertEquals("[4]" , g.getE(8).toString());
    }

    @Test
    public void removeNode() {
        System.out.println(g.getMC());
        g.removeNode(8);
        assertEquals("[1, 2, 3, 4, 5, 6, 7]" , g.getV().toString());
        assertEquals("[1, 4]" , g.getE(2).toString());
    }

    @Test
    public void removeEdge() {
        System.out.println(g.getMC());
        g.removeEdge(4,1);
        assertEquals("[5, 6]" , g.getE(4).toString());
    }

    @Test
    public void nodeSize() {
        System.out.println(g.getMC());
        assertEquals(7 , g.nodeSize());
    }

    @Test
    public void edgeSize() {
        System.out.println(g.getMC());
        assertEquals(11 , g.edgeSize());
    }

    @Test
    public void getMC() {
//        assertEquals(73 , g.getMC());
    }
}