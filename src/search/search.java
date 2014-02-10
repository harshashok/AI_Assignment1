package search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class search {
	
	//CommandLine variables list.
	static int task;
	static String startNode;
	static String goalNode;
	static String inputFile;
	static String tieBreakFile;
	static String outputFile;
	static String outputLogFile;
	
	//Processed CommandLine Input.								//tie Breaker List in an Array.
	
	static LinkedList<Node> tieList = new LinkedList();
	
	static Graph g = new Graph();

	public Node vertices[];
	public static void main(String[] args){
		
		
		
		search srch = new search();
		//System.out.println(args.length);
		
		//Parse commandLine arguments.
		srch.parseCommandLine(args);
		
		//Nodes for input file. Setup names for output file.
		srch.readTieBreakFile();
				
		//Insert into Adjacency List. 
		srch.readInputFile();

		//Switch-Case for Task Nos.
		srch.selectTask(task);
		
		/*List<String> li = srch.getChildNodes(new Node("Alice", 0));
		
		for(String str : li){
			System.out.println("Main: "+str);
		}
		
		Node no = srch.getUnvisitedChildNode(new Node("Alice", 0));
		
		
		if(no == null){
			System.out.println("NULL RESULT!");
		}else
			System.out.println("DEB-unvisitedFirst: "+ no.name);
*/		
		for(Node i : tieList)
			System.out.println("tie: "+i.name +" "+i.visited+ " "+i.priority );
			
	}
	

	public void printNode(Node node){
		
		System.out.println("Node: "+node.name);
	}
	
	public void clearNodes(){
		
		for(Node i : tieList){
			i.visited = false;
		}
	}
	public void bfs(LinkedList<Node> tieList, String startNode)
	{
		// BFS uses Queue data structure
		Queue queue = new LinkedList();
		//Map<String, Boolean> visited = new HashMap<String, Boolean>();
		Node root = getNodeValue(startNode);
		queue.add(root);
		printNode(root);
		tieList.get(root.priority).visited = true;                       //root.visited = true;
		while(!queue.isEmpty()) {
			Node node = (Node)queue.remove();
			Node child=null;
			while((child=getUnvisitedChildNode(node))!=null) {
				tieList.get(child.priority).visited = true;//child.visited=true;
				printNode(child);
				queue.add(child);
			}
		}
		// Clear visited property of nodes
		//clearNodes();
}
	
	public static Node getNodeValue(String value){
		
		for(Node i : tieList){			
			if (value.equals(i.getName())){
				return i;
			}
		}
		return null;			
	}
	
	public static List<String> getChildNodes(Node node){
		Iterator i = g.map.get(node.name).entrySet().iterator();
		List<String> childList = new ArrayList<String>();
		List<Node> nodeList = new ArrayList<Node>();
		while (i.hasNext()) {
	        Map.Entry pairs = (Map.Entry)i.next();
	        String child = pairs.getKey().toString();
	        childList.add(child);	       
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        i.remove(); // avoids a ConcurrentModificationException
	    }
		List<String> newList = childList;
		return newList;
			
	}
	
	public static Node getUnvisitedChildNode(Node child){
		List<String> list = getChildNodes(child);
		Node minNode = new Node("INF", 999);
		
		for(String str : list){
			//System.out.println("Inside Unvis: "+str);
		}
		//The children's visited and prio must be from a global list of 
		for(String str : list){
			Node node = getNodeValue(str);
			
			if(node == null){
				return null;
			}
			
			if(node.visited == false && node.priority < minNode.priority){
				minNode = node;
				//System.out.println("MIN: "+ minNode.name);
			}
		}
		if(minNode.priority == 999){
			return null;
		}else
			return minNode;
		
	}

	public static void printMap(Node node) {
		Iterator i = g.map.get(node.name).entrySet().iterator();
	    
		while (i.hasNext()) {
	        Map.Entry pairs = (Map.Entry)i.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        i.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	void readInputFile(){
		BufferedReader reader = null;
		String[] inputs;
		
		
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
			    inputs = line.split(",");
			    //System.out.println(inputs[0].toString()+ " "+inputs[1].toString()+" "+inputs[2].toString());
			    g.addEdge(inputs[0].toString(), inputs[1].toString(), Integer.parseInt(inputs[2].toString()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Dynamic list captures tieList and its priority.
	public void readTieBreakFile(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tieBreakFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = null;
		
		int i = 0;
		try {
			while ((line = reader.readLine()) != null) {
				Node newNode = new Node(line, i);
				tieList.add(newNode);
				i++;
			    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void parseCommandLine(String[] args){
		
		
		int i=1;
		boolean flag = false;
		String option;
		while(i < args.length){
			
		option = args[i++].toString();
		//System.out.println("option: "+ option);
		
		if(option.equals("-t") && !flag){
			task = Integer.parseInt(args[i]);
			//System.out.println("Task: "+task);			
		}
		
		if(option.equals("-s")){
			startNode = args[i];
			//System.out.println("startNode: "+ startNode);
		}
		
		if(option.equals("-g")){
			goalNode = args[i];
			//System.out.println("goalNode: "+ goalNode);
		}
		
		if(option.equals("-i")){
			inputFile = args[i];
			//System.out.println("inputFile: "+ inputFile);
		}
			
		if(option.equals("-t") && flag){
			tieBreakFile = args[i];
			//System.out.println("tieFile: "+ tieBreakFile);
		}
		
		if(option.equals("-op")){
			outputFile = args[i];
			//System.out.println("outputFile: "+ outputFile);
		}
		if(option.equals("-ol")){
			outputLogFile = args[i];
			//System.out.println("outLogFile: "+ outputLogFile);
		}
		i++;
		flag = true;
		if(i >= args.length){
			//System.out.println("SUCCESS!");
			break;
		}
		}
		
	}
	
	void selectTask(int task){
		
		switch(task){
		
		case 1: 
			//System.out.println("Task#1: BFS");
			bfs(tieList, startNode);
			break;
			
		case 2:
			//System.out.println("Task#2: DFS");
			break;
			
		case 3:
			//System.out.println("Task#3: UCS");
			break;
		
		case 4:
			//System.out.println("Task#4: BFS/DFS");
			break;
		
		default:
			//System.out.println("ERROR! Wrong task or task not inputted!");
			break;
		}
	}
	
	
}
