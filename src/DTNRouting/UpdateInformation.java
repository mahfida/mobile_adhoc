// PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import Results.*;
import RoutingProtocols.RoutingProtocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;




//******************************************************************************
// CLASS MADE FOR UPDATING THE INFORMATION DURING SIMULATION

public class UpdateInformation {
    //Instance Variables
	RoutingProtocol rp;
    RP_Performance rpp=new RP_Performance();
	FireLocation fire_loc=new FireLocation();
    Random rand;
    public static int number_of_uavs = 0;
   

//******************************************************************************
//Constructor
public UpdateInformation(){
}


//******************************************************************************
//Update TTL and packet Latency
public void UpdateTTLandLatency()
{   
	  //1. Update simulation timer
	  dtnrouting.timer+=1;
	  
	  //2. Update delivered and expired packets information
	  for(int h=0;h < dtnrouting.arePacketsDelivered.size();h++)
      {   Packet packetObj=dtnrouting.arePacketsDelivered.get(h);
          packetObj.packetTTL-=1;
          
          if(packetObj.isTTLExpired == false && packetObj.ispacketDelivered==false) {
                  packetObj.packetLatency=(int) dtnrouting.timer;      
          if(packetObj.packetTTL==0){
            //if packet's TTL expires, it cannot be delivered else if
            packetObj.isTTLExpired=true;
            packetObj.packetLatency=packetObj.maxTTL;
            dtnrouting.total_packetsDeliveredExpired += 1;
            
            dtnrouting.performanceFile.append(dtnrouting.SIMULATION_N0+","+dtnrouting.SIMULATION_PART+","+packetObj.destNode_ofpacket.name+","+packetObj.sourceNode_ofpacket.name+","+packetObj.destNode_ofpacket.num_packets+","+packetObj.packetName+","+packetObj.packetHops+","+((int)dtnrouting.timer)+","+packetObj.packetReliability+","+0+"\n");
        	dtnrouting.performanceFile.flush();
          
          }}}
      
     //3. When packets are delivered or expired
	 //   stop simulation temporarily
     if(dtnrouting.total_packetsDeliveredExpired==dtnrouting.arePacketsDelivered.size() 
     & dtnrouting.arePacketsDelivered.size()!=0)
     {
         dtnrouting.deliveryTA.setText("");
    	 dtnrouting.deliveryTA.setText("PACKET DEDLIVER || EXPIRED");
	     dtnrouting.THIS_SIMULATION_ENDED=true;}

     //4. Call Fire Model		
     if(dtnrouting.SIMULATION_PART>1)
     	fire_loc.fireSpread();
     
     //5. If a center of a node is in the range of fire---consider it dead and remove from live list
      fire_loc.deadNodes();

}

//******************************************************************************

public void nextPositionForMovement() throws IOException
{
	
	    //NODE MOVEMENT
		if(dtnrouting.movementtype.equals("Random"))
	    for(int i=0; i< dtnrouting.allNodes.size();i++)
	    {      Node n = dtnrouting.allNodes.get(i);
	    	   if(!n.name.substring(0,1).equals("U"))
	    		   n.node_nm.RandomMovement(n);
	    }
	    else if(dtnrouting.movementtype.equals("Pseudorandom"))
	    	 for(int i=0; i< dtnrouting.allNodes.size();i++)
	    	 {
	    		  Node n = dtnrouting.allNodes.get(i);
		    	  if(!n.name.substring(0,1).equals("U"))
		    		  n.node_nm.Follow_PseudoRandomPath(dtnrouting.allNodes.get(i));
		    	   
	    	 }
	    		 
	    else if(dtnrouting.movementtype.equals("Dataset"))
	    	 for(int i=0; i< dtnrouting.allNodes.size();i++)
	    	 { 
	    		 Node n = dtnrouting.allNodes.get(i);
		    	 if(!n.name.substring(0,1).equals("U"))
		    		 n.node_nm.Follow_DatasetPath(dtnrouting.allNodes.get(i));
	    	 }
	
}

//******************************************************************************
//Clear all the settings when clear (eraser) button is clicked

public void clearSettings()
{
	 
        dtnrouting.allNodes.clear();
        Node.ID_INCREMENTER=0;
        dtnrouting.timer=0;
        dtnrouting.total_packetsDeliveredExpired=0;
        
        //Clearings the array lists of source, destination, their packets and their parameter
        dtnrouting.allSources.clear();     
        dtnrouting.allDestinations.clear();
        //Set movement model to null
        dtnrouting.movementtype="Random";
        dtnrouting.arePacketsDelivered.clear();
        dtnrouting.SIMULATION_N0 = dtnrouting.TOTAL_SIMULATION_RUNS;
        Packet.packetID=0; 
               
        //Empty Text areas
        dtnrouting.sdpTA.setText("Source    Dest.    packet");
        dtnrouting.contactsTA.setText("");
        dtnrouting.transferTA.setText("");
        dtnrouting.deliveryTA.setText("");
        rpp.clearData(); //clear data from table and charts
        dtnrouting.THIS_SIMULATION_ENDED=false;
        dtnrouting.SIMULATION_RUNNING=false;
        
}

//******************************************************************************
//When a simulation run completes
public void simulationSettings(dtnrouting dtn)
{
    if(dtnrouting.SIMULATION_PART==2) {
    	dtnrouting.SIMULATION_N0=dtnrouting.SIMULATION_N0-1;
    	dtnrouting.SIMULATION_PART=1;}
    else
    	dtnrouting.SIMULATION_PART=2;
//    UNCOMENT ABOVE AND REMMOVE BELOW
	//dtnrouting.SIMULATION_PART+=1;
 
    dtnrouting.sdpTA.setText("Source    Dest.    packet");
    dtnrouting.contactsTA.setText("");
    dtnrouting.transferTA.setText("");
    
    
    //...Display the result when all SIMULATIONS END
    if(dtnrouting.SIMULATION_N0==0)  
    	dtnrouting.SIMULATION_RUNNING=false;

   
    //...When a simulation run ends, update the average results
    else if(dtnrouting.SIMULATION_N0>0)  
    {
    	CreateNode cnObj = new CreateNode();
    	QoITPATH pathObj = new QoITPATH ();
    	
    	// WITHOUT UAV PART-----------------
    	if(dtnrouting.SIMULATION_PART==1) {
        	
    	    //Remove packets and destination information from nodes
        	//And change the number of packets and requirements of destinations
        	dtnrouting.arePacketsDelivered.clear();
        	Packet.packetID=0; 
        	for(int g=0;g<dtnrouting.allNodes.size();g++) {
        		dtnrouting.allNodes.get(g).clearNodeSettings();
        		if(dtnrouting.allNodes.get(g).name.substring(0,1).equals("D"))
        		{cnObj.RequirementsofDestination(dtnrouting.allNodes.get(g));
        	     cnObj.PacketsforDestination(dtnrouting.allNodes.get(g));}}
        	
             //--------------
    		 //Reset liveNodes to allNodes and deadNodes to empty
    		 dtnrouting.liveNodes = (ArrayList<Node>) dtnrouting.allNodes.clone();
    		 dtnrouting.liveSources = (ArrayList<Node>) dtnrouting.allSources.clone();
    		 dtnrouting.liveDestinations = (ArrayList<Node>) dtnrouting.allDestinations.clone();
    		 dtnrouting.deadNodes.clear();
        	
        	//Remove UAV Nodes
        		//dtnrouting.allNodes.subList(dtnrouting.uav_index[0], dtnrouting.uav_index[0] +
        		//dtnrouting.uav_index.length).clear();
        	
	       
           //Take a break of one second
	       try
	         { Thread.sleep(1000);
	         } catch (InterruptedException ex) {}
	
	        // Re-define path for nodes, if pseudo-random and mobile
	        if(dtnrouting.movementtype.equals("Pseudorandom"))
	            for(int i=0; i< dtnrouting.allNodes.size(); i++)
	            if(dtnrouting.allNodes.get(i).name.substring(0,1).equals("R"))
	           	dtnrouting.allNodes.get(i).node_nm.InitializePsuedoPath(dtnrouting.allNodes.get(i));
	        
    	}
    	
    	// WITH UAV PART OR REPEAT SIMULATION UNDER SAME SETTINGS-----------------------
        else {
  
        	 //remove this part
				
				  //if(dtnrouting.allNodes.get(dtnrouting.allNodes.size()-1).name.contains("U"))
				  //Remove UAV Nodes 
					  //  dtnrouting.allNodes.subList(dtnrouting.uav_index[0],
					  //  dtnrouting.uav_index[0] + dtnrouting.uav_index.length).clear();
				  //number_of_uavs = number_of_uavs+1;
				  //remove above part
        	 	 
        	 	 for(int n=0;n<dtnrouting.allNodes.size();n++)
            		dtnrouting.allNodes.get(n).refreshNodeSettings();
         		 for(int p=0;p<dtnrouting.arePacketsDelivered.size();p++)
         			dtnrouting.arePacketsDelivered.get(p).refreshPacketSettings();
         		
         		 //Reset liveNodes to allNodes and deadNodes to empty
         		 dtnrouting.liveNodes = (ArrayList<Node>) dtnrouting.allNodes.clone();
         		 dtnrouting.liveSources = (ArrayList<Node>) dtnrouting.allSources.clone();
         		 dtnrouting.liveDestinations = (ArrayList<Node>) dtnrouting.allDestinations.clone();
         		 dtnrouting.deadNodes.clear();
         		 //Add UAV nodes
         		 	//cnObj.CreateUAV(number_of_uavs);
 
         	}
    			   
		pathObj.ShortestPathsSD();
    	pathObj.setInitialSource();
	    runSimulation();
	    
	    //5. Set fire radius to initial radius
	    FireLocation.radius=2;
	    
	
	    
    }
}//end of the method

//******************************************************************************

public void runSimulation()
{  
	dtnrouting.total_packetsDeliveredExpired=0;
    dtnrouting.timer=0;
    dtnrouting.THIS_SIMULATION_ENDED=false;
    dtnrouting.SIMULATION_RUNNING=true;
}
//******************************************************************************

} // END OF CLASS
