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
			((NodeData)n).remove_node();
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

}
