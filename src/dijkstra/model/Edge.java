/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra.model;

/**
 *
 * @author leoscalco
 */
public class Edge  {
        private final String id;
        private final Vertex source;
        private final Vertex destination;
        private int weight;

        public Edge(String id, Vertex source, Vertex destination, int weight) {
                this.id = id;
                this.source = source;
                this.destination = destination;
                this.weight = weight;
        }

        public String getId() {
                return id;
        }
        public Vertex getDestination() {
                return destination;
        }

        public Vertex getSource() {
                return source;
        }
        public int getWeight() {
                return weight;
        }
        public void setWeight(int w) {
                this.weight = w;
        }

        @Override
        public String toString() {
                return source + " " + destination;
        }


}
