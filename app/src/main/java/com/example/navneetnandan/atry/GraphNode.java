package com.example.navneetnandan.atry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Navneet Nandan on 11-10-2016.
 */

public class GraphNode {
    int noOfVertices;
    LinkedList<Integer> adj[];
    int[] previous;
    GraphNode(int v){
        noOfVertices=v;
        previous=new int[v];
        adj=new LinkedList[v];
        for (int i=0;i<v;++i){
            previous[i]=0;
            adj[i]=new LinkedList<>();
        }
    }
    void addEdge(int v,int w){
        adj[v].add(w);
    }
    ArrayList<Integer> BFS(int s, int d){
        ArrayList<Integer> path=new ArrayList<>();
        // Mark all the vertices as not visited(By default
        // set as false)
        boolean visited[] = new boolean[noOfVertices];

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[s]=true;
        previous[s]=100;
        queue.add(s);
        int source=s;
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            s = queue.poll();
            //push into stack

            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext())
            {
                int n = i.next();
                if (!visited[n])
                {
                    previous[n]=s;
                    if(n==d){
                        int t=d;
                        while (previous[t]!=100){
                            path.add(t);
                            t=previous[t];
                        }
                        path.add(source);
                        return path;

                    }
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return null;
    }
}
