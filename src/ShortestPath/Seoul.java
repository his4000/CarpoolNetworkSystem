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
		nodes.add(new Vertex("�ŵ���","�ŵ���"));  //0
		nodes.add(new Vertex("õȣ","õȣ"));  //1
		nodes.add(new Vertex("����","����"));  //2
		nodes.add(new Vertex("����","����"));  //3
		nodes.add(new Vertex("����","����"));  //4
		nodes.add(new Vertex("��ȭ��","��ȭ��"));  //5
		nodes.add(new Vertex("�д�","�д�"));  //6
		nodes.add(new Vertex("�ϻ�","�ϻ�"));  //7
		nodes.add(new Vertex("��","��"));  //8
		nodes.add(new Vertex("�Ȼ�","�Ȼ�"));  //9
		nodes.add(new Vertex("�Ⱦ�","�Ⱦ�"));  //10
		nodes.add(new Vertex("����","����"));  //11
		nodes.add(new Vertex("�Ǵ�","�Ǵ�"));  //12
		nodes.add(new Vertex("������","������"));  //13
		nodes.add(new Vertex("�ϳ�","�ϳ�"));  //14
		
		/* make Edges */
		addLane("����-�д�",3,6,45);addLane("�д�-����",6,3,45);
		addLane("�д�-�Ǵ�",6,12,297);addLane("�Ǵ�-�д�",12,6,297);
		addLane("�д�-����",6,11,205);addLane("����-�д�",11,6,205);
		addLane("�д�-����",6,2,196);addLane("����-�д�",2,6,196);
		addLane("�д�-�Ⱦ�",6,10,67);addLane("�Ⱦ�-�д�",10,6,67);
		addLane("�Ǵ�-õȣ",12,1,237);addLane("õȣ-�Ǵ�",1,12,237);
		addLane("õȣ-�ϳ�",1,14,69);addLane("�ϳ�-õȣ",14,1,69);
		addLane("�Ǵ�-����",12,11,140);addLane("����-�Ǵ�",11,12,140);
		addLane("����-��ȭ��",11,5,80);addLane("��ȭ��-����",5,11,80);
		addLane("����-����",11,4,112);addLane("����-����",4,11,112);
		addLane("����-����",11,2,34);addLane("����-����",2,11,34);
		addLane("����-����",2,4,140);addLane("����-����",4,2,140);
		addLane("����-�ŵ���",2,0,141);addLane("�ŵ���-����",0,2,141);
		addLane("����-�Ⱦ�",2,10,171);addLane("�Ⱦ�-����",10,2,171);
		addLane("�Ⱦ�-�Ȼ�",10,9,276);addLane("�Ȼ�-�Ⱦ�",9,10,276);
		addLane("��-�ŵ���",8,0,79);addLane("�ŵ���-��",0,8,79);
		addLane("��-����",8,4,195);addLane("����-��",4,8,195);
		addLane("����-�ϻ�",4,7,27);addLane("�ϻ�-����",7,4,27);
		addLane("����-������",4,13,195);addLane("������-����",13,4,195);
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
