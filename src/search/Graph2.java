package search;

import java.util.HashMap;
import java.util.Map;

public class Graph2 {

Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
	
	public void addEdge(String vertex1, String vertex2, Double distance){
		
		Map<String, Double> adjacent = map.get(vertex1);
		Map<String, Double> adjacent2 = map.get(vertex2);
		
		if(adjacent == null){
			adjacent = new HashMap<String, Double>();
			map.put(vertex1, adjacent);		
		}
		adjacent.put(vertex2, distance);
		
		if(adjacent2 == null){
			adjacent2 = new HashMap<String, Double>();
			map.put(vertex2, adjacent2);		
		}
		adjacent2.put(vertex1, distance);
	}
	
	
	public boolean isConnected(String vertex1, String vertex2){
		
		Map<String, Double> adj = map.get(vertex1);
		if(adj == null){
			return false;
		}
		
		return adj.containsKey(vertex2);		
	}
	
	public Double getDistance(String vertex1, String vertex2){
		
		Map<String, Double> adjacent = map.get(vertex1);
		
		if(adjacent == null){
			return (double) -1;
		}
		return adjacent.get(vertex2);
		
	}
}
