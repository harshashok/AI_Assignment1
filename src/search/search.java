package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class search {
	
	//CommandLine variables list.
	static int task;
	static String startNode;
	static String goalNode;
	static String inputFile;
	static String tieBreakFile;
	static String outputFile;
	static String outputLogFile;
	
	//tie Breaker List in an Array.
	static LinkedList<Node> tieList = new LinkedList();
	
	//Stack to receive generated outputh path.
	static Stack<Node> generatedOutputPath = new Stack();
	
	//Graph that holds the adjacency list mapping.
	static Graph2 g = new Graph2();

	
	public class Log{
		String nodeName;
		int depth;
		double cost;
		
		Log(String nodeName, int depth, double cost){
			this.nodeName = nodeName;
			this.depth = depth;
			this.cost = cost;
		}
		
		public Log() {
		}

		public Log addLog(String nodeName, int depth, double cost){
			
			Log log = new Log(nodeName, depth, cost);
			return log;
			
		}
		
		public boolean checkLog(String name){
			if(name.contentEquals(this.nodeName))
				return true;
		return false;
		}
	}
	
	static List<Log> logWrite = new LinkedList();
	
	
	
	//ALGORITHM FUNC: Breadth First Search.
		public void bfs(String startNode){
			
			Queue<Node> queue = new LinkedList();
			
			//Map<String, Boolean> visited = new HashMap<String, Boolean>();
			Node[] previous = new Node[tieList.size()+1];
			Node root = getNodeValue(startNode);
			root.depth = 0;
			queue.add(root);
			previous[root.priority] = null;
			
			tieList.get(root.priority).visited = true;   
			tieList.get(root.priority).cost = 0;
			//System.out.println(root.name+","+ root.depth +","+root.cost);
			logWrite.add(new Log(root.name, root.depth, root.cost));
			while(!queue.isEmpty()) {
				Node node = (Node)queue.remove();
				Node child=null;
				
				while((child=getUnvisitedChildrenNode(node))!=null) {
					tieList.get(child.priority).visited = true;   
					tieList.get(child.priority).cost = node.cost + getCost(node, child);
					child.depth = node.depth + 1;
					//System.out.println(child.name+","+ child.depth +","+child.cost);
					logWrite.add(new Log(child.name, child.depth, child.cost));
					previous[child.priority] = node;
					queue.add(child);
				
				}
			}
			printPath(previous, getNodeValue(goalNode).priority);
			generateOutputFile(generatedOutputPath);
			
			// Reset visited = false.
			//clearNodes();
		}
		
		//ALGORITHM FUNC: Depth-First Search.
		public void dfs(String startNode, String goalNode){
			
			Stack<Node> stack = new Stack();
			Node[] previous = new Node[tieList.size()+1];
			
			Node root = getNodeValue(startNode);
			
			tieList.get(root.priority).visited = true;
			tieList.get(root.priority).cost = 0; 
			root.depth = 0;
			logWrite.add(new Log(root.name, root.depth, root.cost));
			stack.push(root);
			previous[root.priority] = null;
			while(!stack.isEmpty()) {
				Node node = (Node)stack.peek();
				Node child = getUnvisitedChildrenNode(node);
				if(child != null) {
					tieList.get(child.priority).visited = true;
					tieList.get(child.priority).cost = node.cost + getCost(node, child);
					child.depth = node.depth + 1;
					
					logWrite.add(new Log(child.name, child.depth, child.cost));
					//printNode(child);
					previous[child.priority] = node;
					stack.push(child);
				}
				else {
					stack.pop();
				}
			}

			printPath(previous, getNodeValue(goalNode).priority);
			generateOutputFile(generatedOutputPath);
			// Clear visited property of nodes
			clearNodes();
		}
		
		//ALGORITHM FUNC: Uniform Cost Search.
		public void ucs(String startNode, String goalNode){
			PriorityQueue<Node> queue = new PriorityQueue<Node>(tieList.size()+1,
					new Comparator<Node>(){

						public int compare(Node node1, Node node2) {
								
							if(node1.cost < node2.cost)
								return -1;
							else if(node1.cost > node2.cost){
								return 1;
							}
							if(node1.cost == node2.cost){
								if(node1.priority < node2.priority)
									return -1;
								else
									return 1;
								
							}
									else
							return 0;
						}

			});
			
			List<Node> children = new ArrayList<Node>();
			Node[] previous = new Node[tieList.size()+1];
			Node root = getNodeValue(startNode);
			int depth = 0;
			tieList.get(root.priority).visited = true;
			previous[root.priority] = null;
			queue.add(root);
			
			while (!queue.isEmpty()){
		        Node par = (Node)queue.remove();
		        
		        boolean someFlag = true;
		        
		        for(Log l : logWrite){
		        if(l.checkLog(par.name)){
		        	someFlag = false;
		        	break;
		        	}
		        }		    
		        if(someFlag){
		        	logWrite.add(new Log(par.name, par.depth, par.cost));
		        	 children = getUCSChildrenNodes(par);
		        }else 
		        	continue;
		        someFlag = true;
		        tieList.get(par.priority).visited = true;
		        //System.out.println("Node "+ par.name+" Visited with Cost"+ par.cost);
		        /*if(par.name.contentEquals(goalNode))
		        	return;
		        */
		          //children = getUCSChildrenNodes(par);
		        
		        
		        
		        for (Node i : children){
		        	
		        	if(tieList.get(i.priority).visited == false){
		        		queue.add(i);
		        		//System.out.println("P: "+ queue.peek().name +" ti: "+ tieList.get(i.priority).name+ "  "+tieList.get(i.priority).visited);
		        	}
		        	else System.out.println("LOL");
		           previous[i.priority] = par;
		            
		        }
		    }
			//clearNodes();
			printPath(previous, getNodeValue(goalNode).priority);
			generateOutputFile(generatedOutputPath);
		}
		
		//ALGORITHM FUNC: Component Search using BFS.
		public void componentSearch(){
			
			int group = 1;
			BufferedWriter bw = null;
			
				try {
					bw = new BufferedWriter(new FileWriter(outputLogFile));
					bw.write("name,depth,group");
					bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			
			
			for(Node i : tieList){
				//System.out.println("DEB: "+i.name);
				if(i.visited == false){
					//System.out.println("INPUT: "+i.name);
					
					Queue<Node> queue = new LinkedList();
					
					Node[] previous = new Node[tieList.size()+1];
					Node root = getNodeValue(i.name);
					root.depth = 0;
					queue.add(root);
					previous[root.priority] = null;
					
					tieList.get(root.priority).visited = true;   
					tieList.get(root.priority).cost = group;								//Cost variable is now used to piggyback group no.
					//System.out.println(root.name+","+ root.depth +","+group);
					try {
						bw.write(root.name+","+root.depth+","+group);
						bw.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logWrite.add(new Log(root.name, root.depth, group));
					while(!queue.isEmpty()) {
						Node node = (Node)queue.remove();
						Node child=null;
						
						while((child=getUnvisitedChildrenNode(node))!=null) {
							tieList.get(child.priority).visited = true;   
							tieList.get(child.priority).cost = group;
							child.depth = node.depth + 1;
							//System.out.println(child.name+","+ child.depth +","+group);
							try {
								bw.write(child.name+","+child.depth+","+group);
								bw.newLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							logWrite.add(new Log(child.name, child.depth, group));
							previous[child.priority] = node;
							queue.add(child);
						
						}
					}
					group++;
					//System.out.println("-------------------------");
					try {
						bw.write("---------------------");
						bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					//printPath(previous, getNodeValue(goalNode).priority);
					//generateOutputFile(generatedOutputPath);
					

				}
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			generateGroupOutput(group);
		}
		
		
		public void generateGroupOutput(int group){
			
			BufferedWriter out = null;
			boolean stupidFlag = true; 
			try {
				out = new BufferedWriter(new FileWriter(outputFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//System.out.println("DEBUG: "+ group);
			for(double i = 1; i< group; i++){
				
				for(Node node : tieList){
					//System.out.println("DEB:"+(int)node.cost+"  "+i);
					if((int)node.cost == i){
						try {
							if(stupidFlag){
								out.write(node.name);
								stupidFlag = false;
							}
							else
							out.write(","+node.name);
							
						} catch (IOException e) {					
							e.printStackTrace();
						}
					}
				}
				try {
					out.newLine();
					stupidFlag = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		//OUTPUT FUNC: Output to File - Generated Path.
		public void generateOutputFile(Stack stack){
		 try {
			       BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
			           while(!stack.isEmpty()) {
			            	Node node = (Node) stack.pop();
			                out.write(node.name);
			                out.newLine();
			            }
			            out.close();
			 } catch (IOException e) {
			     e.printStackTrace();
			   }
		}
		
		//OUTPUT FUNC: Output to Log File - Generated Logs.
				public void generateOutputLogFile(List<Log> list){
				 try {
					       BufferedWriter out = new BufferedWriter(new FileWriter(outputLogFile));
					       out.write("name,depth,cost");
					       out.newLine();
					           for(Log l : list){
					        	   
					        	   //System.out.println("LOG: "+l.nodeName+","+ l.depth +","+l.cost);
					        	   out.write(l.nodeName+","+ l.depth +","+l.cost);
					        	   out.newLine();
					        	   out.flush();
					        	   if(l.nodeName.contentEquals(goalNode))
					        		   return;
					           }
					         out.close();
					 } catch (IOException e) {
					     e.printStackTrace();
					   }
		}
				
		
		

	//DEBUG FUNC: Func to print node name.
	public void printNode(Node node){
		
		System.out.println("Node: "+node.name);
	}
	
	//UTILITY FUNC: Clearing visited flags from nodes.
	public void clearNodes(){
		
		for(Node i : tieList){
			i.visited = false;
			i.depth = 0;
			i.cost = 0;
		}
	}
	
	//UTILITY FUNC: Generate a stack with generated output path.
	public void printPath(Node[] prev, int n){
		//System.out.println("DEB: "+n );
		if(prev[n] == null){
			//System.out.println(tieList.get(n).name);
			generatedOutputPath.push(tieList.get(n));
			return;
		}
		//System.out.println(tieList.get(n).name);
		generatedOutputPath.push(tieList.get(n));
		printPath(prev, prev[n].priority);
	}
	
	
	//UTILITY FUNC: Return node from name.
	public static Node getNodeValue(String value){
		for(Node i : tieList){			
			if (value.equals(i.getName())){
				return i;
			}
		}
		return null;
	}
	
	//UTILITY FUNC: Return all adjacent nodes of a node.
	public static List<Node> getChildrenNodes(Node node){
		
		Iterator i = g.map.get(node.name).entrySet().iterator();
		List<Node> nodeList = new ArrayList<Node>();
		
		while(i.hasNext()){
			Map.Entry pairs = (Map.Entry)i.next();
			String child = pairs.getKey().toString();
			Node retNode = getNodeValue(child);
			nodeList.add(retNode);
		}
		
		return nodeList;
	}
	
	//UTILITY FUNC: Return all adjacent nodes of a node with weights.
	public static List<Node> getUCSChildrenNodes(Node node){
		
		Iterator i = g.map.get(node.name).entrySet().iterator();
		List<Node> nodeList = new ArrayList<Node>();
		
		while(i.hasNext()){
			Map.Entry pairs = (Map.Entry)i.next();
			String child = pairs.getKey().toString();
			Node retNode = getNodeValue(child);
			
			    			
			if(retNode.visited == true){
				continue;
			}
			else{
				double cost = g.map.get(node.name).get(retNode.name).doubleValue();
				Node newNode = new Node(retNode, node.cost+ cost);
				newNode.depth = node.depth + 1;
				nodeList.add(newNode);
			}
		}
		
		/*//DEBUG!!
		for(Node n : nodeList){
			System.out.println("DEBUGUCS: "+n.name+ " "+n.cost);
		}*/
		return nodeList;
		
	}
	
	//UTILITY FUNC
	public static double getCost(Node u, Node v){
		double cost = g.map.get(u.name).get(v.name).doubleValue();
		return cost;
	}
	
	//UTILITY FUNC: Return only an unvisited node after tiebreaking.
	public static Node getUnvisitedChildrenNode(Node child){
		
		List<Node> list = getChildrenNodes(child);
		Node minNode = new Node("INF", 999);
		Node node = null;
		for(Node i : list){
			node = getNodeValue(i.name.toString());
			if(node.priority < minNode.priority && node.visited == false){
				minNode = node;
			}
		}
		if(minNode.priority == 999)
			return null;
		return minNode;
	}
	
	//UTILITY FUNC: Return only unvisited nodes after priority sorting of edge cost.
	public static Node getUnvisitedPriorityNode(Node child){
		
		List<Node> nodeList = getChildrenNodes(child);
		
		return null;
	}
	
	//UTILITY FUNC: get HashMap values of edge.
	public static float getMap(Node node1, Node node2 ){
		try{
		float value = g.map.get(node1.name).get(node2.name).floatValue();
		//System.out.println("OUT: "+ g.map.get(node1.name).get(node2.name).floatValue());
		return value;
		}catch(NullPointerException e){
			return -1;
		}
	}

	//DEBUG FUNC: print HashMap of edge values.
	public static void printMap(Node node) {
		Iterator<Entry<String, Double>> i = g.map.get(node.name).entrySet().iterator();
	    //System.out.println("DEBUG: Iterator prolly fine.");
		while (i.hasNext()) {
	        Map.Entry<String, Double> pairs = (Map.Entry<String, Double>)i.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //i.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	//INPUT-READ FUNC: Read input text file into HashMap.
	void readInputFile(){
		
		BufferedReader reader = null;
		String[] inputs;
		
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
			    inputs = line.split(",");
			    g.addEdge(inputs[0].toString(), inputs[1].toString(), Double.parseDouble(inputs[2])-0.0f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//INPUT-READ FUNC: Dynamic list captures tieList and its priority.
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
			e.printStackTrace();
		}
		
	}
	
	//INPUT FUNC: Parse Command Line.
	public void parseCommandLine(String[] args){
		
		
		int i=0;
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
	
	//SELECT FUNC: Select algorithm to compute output.
	void selectTask(int task){
		
		switch(task){
		
		case 1: 
			//System.out.println("Task#1: BFS");
			bfs(startNode);
			//generateOutputFile(generatedOutputPath);
			generateOutputLogFile(logWrite);
			
			break;
			
		case 2:
			dfs(startNode, goalNode);
			generateOutputLogFile(logWrite);
			//System.out.println("Task#2: DFS");
			break;
			
		case 3:
			//System.out.println("Task#3: UCS");
			ucs(startNode, goalNode);
			generateOutputLogFile(logWrite);
			break;
		
		case 4:
			//System.out.println("Task#4: BFS/DFS");
			componentSearch();
			break;
		
		default:
			System.out.println("ERROR! Wrong task or task not inputted!");
			break;
		}
	}
	
public static void main(String[] args){
		
		
		
		search srch = new search();
		
		//Parse commandLine arguments.
		srch.parseCommandLine(args);
		
		//System.out.println(args.length);
		//Nodes for input file. Setup names for output file.
		srch.readTieBreakFile();
		
		//Insert into Adjacency List. 
		srch.readInputFile();
		
		//Switch-Case for Task Nos.
		srch.selectTask(task);
		
		
	
	}
	
	
}
