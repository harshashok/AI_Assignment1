package search;

public class Node {
	
	String name;
	boolean visited;
	int priority;
	double cost;
	int depth;
	Node(String name, int priority){
		this.visited = false;
		this.name = name;
		this.priority = priority;
	
	}
	public Node(Node newNode) {
		this.name = newNode.name;
		this.priority = newNode.priority;
		this.visited = newNode.visited;
		this.cost = 0;
	
	}
	
	public Node(Node newNode, double cost){
		this(newNode);
		this.cost = cost;
	}
	public String getName(){
		return this.name;
	}
	
	public Node getNode(String value){
		
		if(value == this.name){
			return  Node.this;
		}
		
		return null;
		
		
	}
}
