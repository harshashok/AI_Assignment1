package search;

public class Node {
	
	String name;
	static boolean visited;
	final int priority;
	Node(String name, int priority){
		this.visited = false;
		this.name = name;
		this.priority = priority;
	
	}
	public Node(Node newNode) {
		this.name = newNode.name;
		this.priority = newNode.priority;
		this.visited = newNode.visited;
		
	
	}
	public String getName(){
		return this.name;
	}
	
	public Node getNode(String value){
		
		if(value == this.name){
			return  this;
		}
		
		return null;
		
		
	}
}
