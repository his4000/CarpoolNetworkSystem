package ShortestPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Seoul {
	private List<Vertex> nodes = new ArrayList<Vertex>();
	private List<Edge> edges = new ArrayList<Edge>();
	private Graph graph = null;
	
	public Seoul(){
		/* make Vertexes */
		nodes.add(new Vertex("신도림","신도림"));  //0
		nodes.add(new Vertex("천호","천호"));  //1
		nodes.add(new Vertex("강남","강남"));  //2
		nodes.add(new Vertex("수원","수원"));  //3
		nodes.add(new Vertex("신촌","신촌"));  //4
		nodes.add(new Vertex("광화문","광화문"));  //5
		nodes.add(new Vertex("분당","분당"));  //6
		nodes.add(new Vertex("일산","일산"));  //7
		nodes.add(new Vertex("목동","목동"));  //8
		nodes.add(new Vertex("안산","안산"));  //9
		nodes.add(new Vertex("안양","안양"));  //10
		nodes.add(new Vertex("종로","종로"));  //11
		nodes.add(new Vertex("건대","건대"));  //12
		nodes.add(new Vertex("의정부","의정부"));  //13
		nodes.add(new Vertex("하남","하남"));  //14
		
		/* make Edges */
		addLane("수원-분당",3,6,45);addLane("분당-수원",6,3,45);
		addLane("분당-건대",6,12,297);addLane("건대-분당",12,6,297);
		addLane("분당-종로",6,11,205);addLane("종로-분당",11,6,205);
		addLane("분당-강남",6,2,196);addLane("강남-분당",2,6,196);
		addLane("분당-안양",6,10,67);addLane("안양-분당",10,6,67);
		addLane("건대-천호",12,1,237);addLane("천호-건대",1,12,237);
		addLane("천호-하남",1,14,69);addLane("하남-천호",14,1,69);
		addLane("건대-종로",12,11,140);addLane("종로-건대",11,12,140);
		addLane("종로-광화문",11,5,80);addLane("광화문-종로",5,11,80);
		addLane("종로-신촌",11,4,112);addLane("신촌-종로",4,11,112);
		addLane("종로-강남",11,2,34);addLane("강남-종로",2,11,34);
		addLane("강남-신촌",2,4,140);addLane("신촌-강남",4,2,140);
		addLane("강남-신도림",2,0,141);addLane("신도림-강남",0,2,141);
		addLane("강남-안양",2,10,171);addLane("안양-강남",10,2,171);
		addLane("안양-안산",10,9,276);addLane("안산-안양",9,10,276);
		addLane("목동-신도림",8,0,79);addLane("신도림-목동",0,8,79);
		addLane("목동-신촌",8,4,195);addLane("신촌-목동",4,8,195);
		addLane("신촌-일산",4,7,27);addLane("일산-신촌",7,4,27);
		addLane("신촌-의정부",4,13,195);addLane("의정부-신촌",13,4,195);
		this.graph = new Graph(nodes, edges);
	}
	
	public Vertex searchVertex(String name){
		Vertex result = null;
		for(Vertex temp : this.nodes){
			if(temp.equals(name)){
				result = temp;
				break;
			}
		}
		return result;
	}
	
	public LinkedList<Vertex> getPath(String source, String destination){
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(searchVertex(source));
		LinkedList<Vertex> path = dijkstra.getPath(searchVertex(destination));
		
		return path;
	}
	
	public void printPath(String source, String destination){
		LinkedList<Vertex> path = getPath(source, destination);
		
		for(Vertex vertex : path)
			System.out.print(vertex+" ");
	}
	
	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration){
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);
	}
}
