package search;

import java.util.HashMap;
import java.util.Map;

public class Graph1 {

Map<String, Map<String, Float>> map = new HashMap<String, Map<String, Float>>();
	
	public void addEdge(String vertex1, String vertex2, Float distance){
		
		Map<String, Float> adjacent = map.get(vertex1);
		Map<String, Float> adjacent2 = map.get(vertex2);
		
		if(adjacent == null){
			adjacent = new HashMap<String, Float>();
			map.put(vertex1, adjacent);		
		}
		adjacent.put(vertex2, distance);
		
		if(adjacent2 == null){
			adjacent2 = new HashMap<String, Float>();
			map.put(vertex2, adjacent2);		
		}
		adjacent2.put(vertex1, distance);
	}
	
	
	public boolean isConnected(String vertex1, String vertex2){
		
		Map<String, Float> adj = map.get(vertex1);
		if(adj == null){
			return false;
		}
		
		return adj.containsKey(vertex2);		
	}
	
	public Float getDistance(String vertex1, String vertex2){
		
		Map<String, Float> adjacent = map.get(vertex1);
		
		if(adjacent == null){
			return (float) -1;
		}
		return adjacent.get(vertex2);
		
	}
}
