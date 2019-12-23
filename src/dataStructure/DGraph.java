package dataStructure;

import java.util.Collection;
import java.util.HashMap;

public class DGraph implements graph{
    private HashMap<Integer,node_data> node_hash;
    private int counter;

    public DGraph(){
        this.node_hash = new HashMap<Integer, node_data>();
        counter = 0;
    }

    @Override
    public node_data getNode(int key) {
        return this.node_hash.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return ((NodeData)node_hash.get(src)).getEdge(dest);
    }
    @Override
    public void addNode(node_data n) {
        this.node_hash.put(n.getKey() , n);
        counter++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        EdgeData e = new EdgeData(src , dest , w);
        ((NodeData)node_hash.get(src)).add_edge(dest , e);
        counter++;
    }

    @Override
    public Collection<node_data> getV() {
        return this.node_hash.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return ((NodeData)node_hash.get(node_id)).getE();
    }

    @Override
    public node_data removeNode(int key) {
        for (node_data n : node_hash.values()){
            ((NodeData)n).remove_node(key);
        }
        counter++;
        return this.node_hash.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        counter++;
        return ((NodeData)this.node_hash.get(src)).remove_edge(dest);
    }

    @Override
    public int nodeSize() {
        return this.node_hash.size();
    }

    @Override
    public int edgeSize() {
        int size = 0;
        for (node_data n : node_hash.values()){
            size += ((NodeData)n).edgeSize();
        }
        return size;
    }

    @Override
    public int getMC() {
        return counter;
    }

    public String toString(){
        String s = "";
        for (node_data n : this.node_hash.values()){
            s += ((NodeData)n).toString() + " , ";
        }
        return s;
    }

    public static void main(String[] args) {
        DGraph graph = new DGraph();
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);
        NodeData n3 = new NodeData(3);
        NodeData n4 = new NodeData(4);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        System.out.println("node size: " + graph.nodeSize());
        graph.connect(1 ,2 , 10.4);
        graph.connect(4 , 3 , 22);
        System.out.println("edge from 1: " + graph.getE(1));
        System.out.println("edge size: " + graph.edgeSize());
        graph.connect(3 , 1 , 78.3);
        graph.connect(1 , 4 , 20.2);
        System.out.println("edge from 1: " + graph.getE(1));
        System.out.println("edge size: " + graph.edgeSize());
        graph.removeNode(1);
        System.out.println("node size: " + graph.nodeSize());
        System.out.println("edge size: " + graph.edgeSize());
        graph.removeEdge(4 , 3);
        System.out.println(graph.toString());
        System.out.println(graph.getMC());
    }

}
