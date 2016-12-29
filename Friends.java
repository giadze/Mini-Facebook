
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;


class Vertex implements Comparable<Vertex>{
	
    public boolean connector;
    public int dfsnum;
    public int back;
    public  String name;
    public String school;
    public boolean visited;
    public List<Edge> adjacencies = new ArrayList<Edge>();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    
    public Vertex(String name){ 
    	this.name = name; 
    	this.school = "" ;
    	this.visited = false;
    	this.connector = false;
    	
    }
    
    public Vertex(String name , String school){ 
    	this.name = name; 
    	this.school = school;
    	this.visited = false;
    	this.connector = false;    	
    }
    
    public String toString(){ 
    	return name; 
    }
    
    public int compareTo(Vertex other){
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight){ 
    	target = argTarget; 
    	weight = argWeight; 
    }
}

public class Friends
{
    public static int dfsnumber = 1;
    public static void computePaths(Vertex source){
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
      	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();

            for (Edge e : u.adjacencies){
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
		if (distanceThroughU < v.minDistance) {
		    vertexQueue.remove(v);
		    v.minDistance = distanceThroughU ;
		    v.previous = u;
		    vertexQueue.add(v);
				}
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target){
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
    
    public static int getNextUnvisited(Vertex []vertex , String school){
        for(int i =0 ; i < vertex.length ; i++)
            if(vertex[i].visited==false && vertex[i].school.equals(school))
                return i;
        return -1;
    }
    
    public static int getIndex(Vertex[] vertex , Vertex v){
        for(int i = 0 ; i < vertex.length ; i++)
            if(vertex[i].name.equals(v.name))
                return i;
        System.err.println("Exception Case ");
        return -1;
    }
    
    public static void computeClingue(Vertex []vertex, String school){
        int i = 1;
        int index = getNextUnvisited(vertex , school);
        while(index >= 0){
            System.out.println("\nClingue "+i);
            vertex[index].visited=true;
            Queue<Integer> queue = new LinkedList<Integer>();
            queue.add(index);
            while(queue.size()>0){
                int head = queue.remove().intValue();
                System.out.print(vertex[head].name+" --- ");
                for(Edge e:vertex[head].adjacencies){
                    int x = getIndex(vertex, e.target);
                    if(vertex[x].visited==false && vertex[x].school.equals(school)){
                        vertex[x].visited=true;
                        queue.add(x);
                    }
                }
                
            }
            index = getNextUnvisited(vertex , school);
            i++;
            
        }
    }
    
    public static void computeConnectors( Vertex src ){
        
        src.visited=true;
        src.dfsnum = dfsnumber;
        src.back = dfsnumber;
        dfsnumber++;
        for(int i = 0 ; i < src.adjacencies.size();i++){
            if(!src.adjacencies.get(i).target.visited){
                computeConnectors(src.adjacencies.get(i).target);
                if(src.dfsnum > src.adjacencies.get(i).target.back)
                    src.back = Math.min(src.back, src.adjacencies.get(i).target.back);
                if(src.dfsnum <= src.adjacencies.get(i).target.back)
                    src.connector = true;
            }
        }
        src.back = Math.min(src.back, minDfsnum(src.adjacencies));

    }
    public static void clearAll(Vertex []vertex){
        for(int i = 0 ; i < vertex.length ; i++){
            vertex[i].dfsnum=0;
            vertex[i].back=0;
            vertex[i].visited=false;
            vertex[i].connector=false;
        }
    }
    
    public static int minDfsnum(List<Edge> edge){
        int min = 1000000000;
        for(int i = 0 ; i < edge.size() ; i++){
            if(edge.get(i).target.visited)
                min = Math.min(min, edge.get(i).target.dfsnum);
        }
        return min;
    }
    public static void main(String[] args) {
	System.out.println("Enter the name of the Input file ( eg : input.txt ) : ");
        String file = new Scanner(System.in).nextLine();
        Scanner s=null;
        try {
            s = new Scanner(new File(file));
        } catch (FileNotFoundException ex) {
            System.out.println("The File Does not Exist");
            System.exit(404);
        }
        int n = Integer.parseInt(s.nextLine());
        Vertex vertex[] = new Vertex[n];
        for(int i = 0 ; i < n ; i++){
            String r[] = s.nextLine().split(",");
            if(r[1].trim().equals("y"))
                vertex[i] = new Vertex(r[0].trim() , r[2].trim());
            else
                vertex[i] = new Vertex(r[0].trim());
        }
        while(s.hasNext()){
            String []r = s.nextLine().split(",");
            int i = 0 , j = 0;
            while(!vertex[i].name.equals(r[0].trim())) i++;
            while(!vertex[j].name.equals(r[1].trim())) j++;
            vertex[i].adjacencies.add(new Edge(vertex[j], 1));
            vertex[j].adjacencies.add(new Edge(vertex[i], 1));
            
        }
        s.close();
        s  = new Scanner(System.in);
        int choice ;
        do{
            System.out.println("\n1. Shortest chain intro \n2. Cliques at school \n3. Connectors \n4. quit");
            choice = Integer.parseInt(s.nextLine());
             if(choice==1){
                System.out.println("\nEnter first person name : ");
                String name1 = s.nextLine();
                System.out.println("\nEnter second person name : ");
                String name2 = s.nextLine();
                Vertex v1=null,v2=null;
                for(Vertex v:vertex){
                    if(v.name.equals(name1))
                        v1=v;
                    if(v.name.equals(name2))
                        v2=v;
                }
                computePaths(v1);
                List<Vertex> path = getShortestPathTo(v2);
                System.out.println("PATH : ");
                for(int i = 0 ; i < path.size() ; i++)
                    System.out.print(path.get(i)+" ---- ");
                    
                }
            else if(choice==2){
                clearAll(vertex);
                System.out.println("\nEnter School name");
                String school = s.nextLine();
                computeClingue(vertex, school);
            }
            else if(choice==3){
                clearAll(vertex);
                
                for(Vertex v : vertex){
                    if(!v.visited && v.adjacencies.size()>1)
                        computeConnectors(v);
                }
                System.out.println("\nConnectors in the graph are : ");
                for(Vertex v : vertex){
                    if(v.connector)
                        System.out.println(v.name);
                }
            }
        }while(choice>0 && choice <4);
    }
}


