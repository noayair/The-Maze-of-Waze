package dataStructure;

import java.io.IOException;
import java.io.Serializable;

/**
 *  This interface represents the set of operations applicable on a
 *  directional edge(src,dest) in a (directional) weighted graph.
 *  @author Noa Yair and Oriya Kronfeld
 */

public class EdgeData implements edge_data , Serializable {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    //default constructor
    public EdgeData() {
        this.src = 0;
        this.dest = 0;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
    }

    //constructors

    public EdgeData(int src , int dest , double weight){
        if(src == dest){
            throw new RuntimeException("Error - the same source and destination cannot be entered");
        }
        this.src = src;
        this.dest = dest;
        if(weight <= 0){
            throw new RuntimeException("Error - the weight can't be negative");
        }
        this.weight = weight;
        this.tag = 0;
        this.info = null;
    }

    public EdgeData(int src , int dest , double weight , String info , int tag){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    //Getters and Setters

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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

    //functions

    public String toString(){
        String s = "";
        s += "src: ";
        s += String.valueOf(this.src);
        s += " dest: ";
        s += String.valueOf(this.dest);
        return s;
    }
}