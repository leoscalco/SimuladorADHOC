/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulador2000;

/**
 *
 * @author leoscalco
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dijkstra.engine.DijkstraAlgorithm;
import dijkstra.model.Graph;
import dijkstra.model.Edge;
import dijkstra.model.Vertex;

public class Neighborhood {
    
    private List<Vertex> nodes;
    private List<Edge> edges;

    public Graph makeNeighborhood() {
            nodes = new ArrayList<>();
            edges = new ArrayList<>();
            
            String[] points = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
             
            for (int i = 0; i < 9; i++) {
                    Vertex location = new Vertex(points[i], points[i]);
                    nodes.add(location);
            }

            addLane("AB", 0, 1, 10); // a -> b
            addLane("AH", 0, 7, 10); // a -> h
            addLane("AF", 0, 5, 10); // a -> f
            addLane("BC", 1, 2, 10); // b -> c
            addLane("CD", 2, 3, 10); // c -> d
            addLane("CF", 2, 5, 10); // c -> f
            addLane("CI", 2, 8, 10); // c -> i
            addLane("DE", 3, 4, 10); // d -> e
            addLane("DG", 3, 6, 10); // d -> g
            addLane("EF", 4, 5, 10); // e -> f
            addLane("FI", 5, 8, 10); // f -> i 
            addLane("GI", 6, 8, 10); // g -> i
            addLane("DI", 3, 8, 10); // d -> i 

            // Lets check from location Loc_1 to Loc_10
            Graph graph = new Graph(nodes, edges);
            
        return graph;
    }
    
    

    private void addLane(String laneId, int sourceLocNo, int destLocNo,
                    int duration) {
            Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
            Edge lane2 = new Edge("" + laneId.charAt(1) + laneId.charAt(0), nodes.get(destLocNo), nodes.get(sourceLocNo), duration );
            edges.add(lane);
            edges.add(lane2);
    }
    
}
