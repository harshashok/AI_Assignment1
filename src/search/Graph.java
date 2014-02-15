package search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class Graph{
	
	Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
	
	public void addEdge(String vertex1, String vertex2, int distance){
		
		Map<String, Integer> adjacent = map.get(vertex1);
		Map<String, Integer> adjacent2 = map.get(vertex2);
		
		if(adjacent == null){
			adjacent = new HashMap<String, Integer>();
			map.put(vertex1, adjacent);		
		}
		adjacent.put(vertex2, distance);
		
		if(adjacent2 == null){
			adjacent2 = new HashMap<String, Integer>();
			map.put(vertex2, adjacent2);		
		}
		adjacent2.put(vertex1, distance);
	}
	
	
	public boolean isConnected(String vertex1, String vertex2){
		
		Map<String, Integer> adj = map.get(vertex1);
		if(adj == null){
			return false;
		}
		
		return adj.containsKey(vertex2);		
	}
	
	public Integer getDistance(String vertex1, String vertex2){
		
		Map<String, Integer> adjacent = map.get(vertex1);
		
		if(adjacent == null){
			return -1;
		}
		return adjacent.get(vertex2);
		
	}
}