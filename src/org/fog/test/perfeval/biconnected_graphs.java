package org.fog.test.perfeval;

import java.util.*;

public class biconnected_graphs {
	
	public static ArrayList<Integer> points= new ArrayList<Integer>();
	static int time;
	public static boolean[][] bool=new boolean[100][100];

	static void addEdge(ArrayList<ArrayList<Integer> > adj, int u, int v)
	{
		adj.get(u).add(v);
		adj.get(v).add(u);
		bool[u][v]=true;
		bool[v][u]=true;
	}

	static void APUtil(ArrayList<ArrayList<Integer> > adj, int u,
					boolean visited[], int disc[], int low[],
					int parent, boolean isAP[])
	{
		int children = 0;

		visited[u] = true;

		disc[u] = low[u] = ++time;

		for (Integer v : adj.get(u)) {

			if (!visited[v]) {
				children++;
				APUtil(adj, v, visited, disc, low, u, isAP);

				low[u] = Math.min(low[u], low[v]);

				if (parent != -1 && low[v] >= disc[u])
					isAP[u] = true;
			}

			else if (v != parent)
				low[u] = Math.min(low[u], disc[v]);
		}

		if (parent == -1 && children > 1)
			isAP[u] = true;
	}

	static void AP(ArrayList<ArrayList<Integer> > adj, int V)
	{
		boolean[] visited = new boolean[V];
		int[] disc = new int[V];
		int[] low = new int[V];
		boolean[] isAP = new boolean[V];
		int time = 0, par = -1;

		for (int u = 0; u < V; u++)
			if (visited[u] == false)
				APUtil(adj, u, visited, disc, low, par, isAP);

		for (int u = 0; u < V; u++)
			if (isAP[u] == true) {
				System.out.print(u + " ");
				points.add(u);
			}
		System.out.println();
	}

	public static void main(String[] args)
	{
		int noofnodes=9;
		ArrayList< ArrayList<Integer> > adj1 = new ArrayList< ArrayList<Integer> >();
		for (int i = 0; i < noofnodes; i++) {
			adj1.add(new ArrayList<Integer>());
		}
		//connect all nodes
		for(int i=0;i< noofnodes-1 ;i++) {
			addEdge(adj1, i, i+1);
		}
		Random ran=new Random();
		int j=ran.nextInt(noofnodes*(noofnodes-1)/2-(noofnodes-1));
		System.out.println(noofnodes*(noofnodes-1)/2-(noofnodes-1)+"    "+j);
		j=4;
		ArrayList<Integer> sum=new ArrayList<>();
		ArrayList<Integer> mult=new ArrayList<>();
		for(int i =0;i<j;i++) {
			int j1=ran.nextInt(noofnodes);
			int j2=ran.nextInt(noofnodes);
			/*while(j1==j2||j1==j2+1||j2==j1+1) {
				j2=ran.nextInt(noofnodes);
				System.out.println(j1+",,,,,"+j2);
			}*/
			while(sum.contains(j1+j2) && mult.contains(j1*j2) || (j1==j2||j1==j2+1||j2==j1+1)) {
				System.out.println(j1+"   wrong    "+j2);
				j2=ran.nextInt(noofnodes);
				j1=ran.nextInt(noofnodes);
			}
			sum.add(j1+j2);
			mult.add(j1*j2);
			addEdge(adj1, j1, j2);
			System.out.println(j1+"   "+j2);
		}
		//addEdge(adj1, 3, 0);
		System.out.println("Articulation points in first graph");
		AP(adj1, noofnodes);
		for(int i:points) {
			ArrayList<Integer> a=adj1.get(i);
			int[] points1=new int[a.size()];
			int i2=0;
			for(int i1:a) {
				points1[i2]=i1;
				i2++;
			}
			if(i==0||i==1||i==2) {
				continue;
			}
			for(int i1=0;i1<points1.length;i1++) {
				for(int j1=i1+1;j1<points1.length && bool[points1[i1]][points1[j1]]==false && bool[points1[j1]][points1[i1]]==false;j1++) {
					addEdge(adj1,points1[i1],points1[j1]);
					System.out.println(points1[i1]+"  new   "+points1[j1]);
				}
			}
		}
		points.clear();
		int failednodes=4;
		for(int i=0;i<failednodes;i++) {
			int j1=ran.nextInt(noofnodes);
			while(j1==0||j1==1||j1==2) {
				j1=ran.nextInt(noofnodes);
			}
			System.out.println(j1+"  failed node");
			ArrayList<Integer> a=adj1.get(j1);
			int l=0;
			while(l!=a.size() && a.size()!=0) {
				int q=adj1.get(j1).get(0);
				bool[j1][q]=false;
				bool[q][j1]=false;
				adj1.get(j1).remove(0);
				l++;
			}
			for(int i1=0;i1<noofnodes;i1++) {
				ArrayList<Integer> a1=adj1.get(i1);
				int l1=0;
				while(l1!=a1.size() && a1.size()!=0) {
					int a2=adj1.get(i1).get(l1);
					if(a2==j1) {
						System.out.println(j1+"    jururjdu  "+ l1+"      "+a2+"      "+a1);
						bool[i1][j1]=false;
						bool[j1][i1]=false;
						adj1.get(i1).remove(l1);
						break;
					}
					l1++;
				}
			}
			//noofnodes--;
			System.out.println("Articulation points in first graph");
			AP(adj1, noofnodes);
			for(int i3:points) {
				ArrayList<Integer> a3=adj1.get(i3);
				int[] points1=new int[a3.size()];
				int i2=0;
				for(int i1:a3) {
					points1[i2]=i1;
					i2++;
				}
				if(i3==0||i3==1||i3==2) {
					continue;
				}
				System.out.println(a3+"        last");
				for(int i1=0;i1<points1.length;i1++) {
					for(int j3=i1+1;j3<points1.length && points1[i1]!=points1[j3] && bool[points1[i1]][points1[j3]]==false && bool[points1[j3]][points1[i1]]==false ;j3++) {
						addEdge(adj1,points1[i1],points1[j3]);
						System.out.println(points1[i1]+"  new   "+points1[j3]);
					}
				}
			}
			points.clear();
		}
	}
}