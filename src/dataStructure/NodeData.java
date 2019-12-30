package dataStructure;

import org.w3c.dom.Node;
import utils.Point3D;

import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {
    private int key;
    private double weight;
    private Point3D location;
    private String info;
    private int tag;
    private HashMap<Integer,edge_data> edge_hash;

    //default constructor
    public NodeData(){
        this.key = 0;
        this.weight = Double.POSITIVE_INFINITY;
        this.location = new Point3D(0,0,0);
        this.info = "";
        this.tag = 0;
        this.edge_hash = new HashMap<Integer, edge_data>();
    }
    //constructor
    public NodeData (int key , double weight , Point3D location , String info , int tag){
        this.key = key;
        this.weight = weight;
        this.location = location;
        this.info = info;
        this.tag = tag;
        this.edge_hash = new HashMap<Integer, edge_data>();
    }

    public NodeData(int key){
        this.key = key;
        this.tag = 0;
        this.info = null;
        this.weight = Double.POSITIVE_INFINITY;
        this.edge_hash = new HashMap<Integer, edge_data>();
        this.location = null;
    }

    //Getters and Setters

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Point3D getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Point3D p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

//    public void setKey(int key){
//        this.key = key;
//    }

    //functions
    public void add_edge(int dest , edge_data e){
        this.edge_hash.put(dest , e);
    }

    public edge_data getEdge(int dest){
        return this.edge_hash.get(dest);
    }

    public Collection<edge_data> getE() {
        return this.edge_hash.values();
    }

    public edge_data remove_node(int key){
        for (edge_data edge : edge_hash.values()){
            if(edge.getDest() == key){
                return this.edge_hash.remove(edge.getDest());
            }
        }
        return null;
    }

    public edge_data remove_edge(int dest){
        return this.edge_hash.remove(dest);
    }

    public int edgeSize() {
        return this.edge_hash.size();
    }

    public node_data copy(){
        node_data n = new NodeData(this.getKey());
        n.setInfo(this.info);
        n.setWeight(this.weight);
        n.setTag(this.tag);
        n.setLocation(this.location);

        return null;
    }

    public String toString(){
        if(this.edge_hash.isEmpty()){
            return "key: " + this.key;
        }
        String s = this.key + ": ";
        for (edge_data e : edge_hash.values()){
            s += ((EdgeData)e).toString();
        }
        return s;
    }
}