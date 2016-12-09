/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulador2000;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;
import org.jgroups.Address;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import Simulador2000.Neighborhood;
import dijkstra.engine.DijkstraAlgorithm;
import dijkstra.model.Graph;
import dijkstra.model.Edge;
import dijkstra.model.Vertex;

import dijkstra.test.TestDijkstraAlgorithm;

public class SimpleChat extends ReceiverAdapter {
    
    Neighborhood neig;
    Graph graph;
    DijkstraAlgorithm dijkstra;
    
    JChannel channel;
    List<Address> dstList = new ArrayList<>();   
    String user_name=System.getProperty("user.name", "n/a");
    final List<String> state=new LinkedList<String>();

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line= (String) msg.getObject();  
        if(line.startsWith("#setInterdicted:")){
            String subLinha= line.substring(16,line.length());
            String[] indexes = subLinha.split("/");                       
            this.setInterdicted(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]));   
        }
        
        if(line.startsWith("#setCongested1:")){
            String subLinha= line.substring(14,line.length());
            String[] indexes = subLinha.split("/");
            this.setCongested(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]), 1);
        }
        if(line.startsWith("#setCongested2:")){
            String subLinha= line.substring(14,line.length());
            String[] indexes = subLinha.split("/");
            this.setCongested(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]), 2);
        }
        if(line.startsWith("#setCongested3:")){
            String subLinha= line.substring(14,line.length());
            String[] indexes = subLinha.split("/");
            this.setCongested(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]), 3);
        }
        
        if(line.startsWith("#setNormal:")){
            String subLinha= line.substring(11,line.length());
            String[] indexes = subLinha.split("/");
            this.setNormal(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]));                      
        }

        synchronized(state) {
            state.add(line);
            
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> list=(List<String>)Util.objectFromStream(new DataInputStream(input));
        synchronized(state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("received state (" + list.size() + " messages in chat history):");
        Message msg;
     
        for(String str: list) {
            msg = new Message(null, null, str);
            receive(msg);
        }
    }


    private void start() throws Exception {
        neig = new Neighborhood();
        graph = neig.makeNeighborhood();
        dijkstra = new DijkstraAlgorithm(graph);
        channel=new JChannel();       
        
        channel.setReceiver(this);
        
        channel.connect("iWantTOGO2000");
        channel.getState(null, 10000);           
        eventLoop();
        channel.close();
       
    }
    
    private LinkedList<Vertex> getPath(int or, int dest) {
        dijkstra.execute(graph.getVertexes().get(or));
        LinkedList<Vertex> path = dijkstra.getPath(graph.getVertexes().get(dest));
   
        return path;
        
    }
    
    private void setInterdicted(int or, int dest) {
        dijkstra.setDistance(graph.getVertexes().get(or), graph.getVertexes().get(dest), 100000);
    }
    
    private void setCongested(int or, int dest, int type) {
        dijkstra.setDistance(graph.getVertexes().get(or), graph.getVertexes().get(dest), 20*type); // pouco, medio, muito
    }

    private void setNormal(int or, int dest) {
        dijkstra.setDistance(graph.getVertexes().get(or), graph.getVertexes().get(dest), 10);
    }

    private void eventLoop() {
        dstList.add(null);
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                
                System.out.print("> "); System.out.flush();
                String line=in.readLine();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                else{ 
                    if(line.startsWith("#path:")){
                        String subLinha= line.substring(6,line.length());
                        String[] indexes = subLinha.split("/");            
                        LinkedList<Vertex> vt = this.getPath(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]));
                        for (Vertex v: vt){
                            System.out.println(v.getName());
                        }                      
                    }  
                    
                    Message msg;
                    for(Address dst: dstList){
                        msg=new Message(null, null, line);
                        channel.send(msg);
                    }
                }      
           }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        new SimpleChat().start();      
    }
}
