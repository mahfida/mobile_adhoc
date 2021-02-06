//PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;


//------------------------------------------------------------------------------
//START OF CLASS packet
public class QoITPATH extends dtnrouting implements ActionListener
{

	public int num_packets,ttl_packets,size_packets;
	public PlayField pf = new PlayField();
	public shortestPath  sp;
	Random rand=new Random();

	//******************************************************************************

	public  QoITPATH(){}
  

	//******************************************************************************
	public void ShortestPathsSD() {
		int total_nodes = dtnrouting.liveNodes.size();
		System.out.println("\nNodes size: "+total_nodes +", Senario: "+dtnrouting.SIMULATION_PART);
		
		// Give size to the destination source pair
		dtnrouting.destsourcePair= new int [dtnrouting.liveDestinations.size()];
		
		// Initialize the adjacency table
		dtnrouting.adjacencyMatrix =new double[total_nodes][total_nodes];
		
		// Initialize the Dynamic TSA arrays
		dtnrouting.RR=new int[total_nodes];
		dtnrouting.CR=new int[total_nodes];
		dtnrouting.RC=new int[total_nodes];
		dtnrouting.RA=new int[total_nodes]; 
		dtnrouting.PP=new int[total_nodes];
		dtnrouting.EP=new int[total_nodes];
		dtnrouting.Result=new int[total_nodes];
		
		// Calculate Adjacency matrix
		dtnrouting.source_index = new int[dtnrouting.liveSources.size()];
		dtnrouting.dest_index = new int[dtnrouting.liveDestinations.size()];
		int s_counter=0 , d_counter=0;

		for (int i = 0; i < (total_nodes-1); i++) {
			dtnrouting.adjacencyMatrix[i][i] = 0;
			if(dtnrouting.liveNodes.get(i).name.substring(0,1).equals("S"))
				dtnrouting.source_index[s_counter++] = i;
			else if(dtnrouting.liveNodes.get(i).name.substring(0,1).equals("D"))
				dtnrouting.dest_index[d_counter++] = i;
			   // System.out.println("DESTINATION ID IN ORDER:"+ dtnrouting.dest_index[d_counter-1]);
		}
      
		pf.FindNeighborhoods();

		// For last node
		dtnrouting.adjacencyMatrix[total_nodes-1][total_nodes-1]=0;
		if(dtnrouting.liveNodes.get(total_nodes-1).name.substring(0,1).equals("S"))
			dtnrouting.source_index[s_counter++] = total_nodes-1;
		else if(dtnrouting.liveNodes.get(total_nodes-1).name.substring(0,1).equals("D"))
			dtnrouting.dest_index[d_counter++] = total_nodes-1;

		// Find shortest path from each source to each destination
		//System.out.println("Path from source nodes");
		for(int s=0; s < dtnrouting.source_index.length; s++) {
			sp = new shortestPath();
			dtnrouting.liveSources.get(dtnrouting.source_index[s]).ptD = new pathToDestination(dtnrouting.dest_index.length);
			sp.runDijkstra(dtnrouting.adjacencyMatrix, dtnrouting.dest_index , dtnrouting.source_index[s], dtnrouting.liveSources.get(dtnrouting.source_index[s]));
			//System.out.println(dtnrouting.liveSources.get(dtnrouting.source_index[s]).ptD.paths);			
			sp=null;}
		
		
		
		BestPathsSD();
	}// End of Method
	
	//******************************************************************************
	
	
	
	 public void BestPathsSD() {

			//1. Destination-wise Source-Destination Paths i.e. s1:s2:s3..:sn-->d1, s1:s2:s3..:sn-->d2, ...s1:s2:s3..:sn-->dn
			ArrayList<ArrayList<ArrayList<Integer>>> srcDestPaths_All = new ArrayList<ArrayList<ArrayList<Integer>>>();
			ArrayList<ArrayList<Integer>> srcDestPaths = null;

			/******************************************************************************/
			networkMetricValues_AlongPath nmV = new networkMetricValues_AlongPath();
			if(dtnrouting.SIMULATION_PART==1) // only call one for a new simulation
				nmV.setInformationUtility();
			/******************************************************************************/  

			for(int dest_index = 0; dest_index < dtnrouting.dest_index.length; dest_index++) {
				srcDestPaths = new ArrayList<ArrayList<Integer>>();
				//CHAANGEES liveNodes to liveSources
				for(int s = 0; s < dtnrouting.source_index.length; s++) {
					if((dtnrouting.liveSources.get(dtnrouting.source_index[s]).ptD.paths.get(dest_index).size() - 1) != 0)
						srcDestPaths.add(dtnrouting.liveSources.get(dtnrouting.source_index[s]).ptD.paths.get(dest_index));
				}
				srcDestPaths_All.add(srcDestPaths);
			}

			//2. Exploring Network Metric Values possessed by particular Src-Dst path AND 
			// Evaluating goodness value of a metric i.e. percentage of user need met w.r.t a metric
			// Choose source for each destination node
			for(int dest_index = 0; dest_index < dtnrouting.dest_index.length; dest_index++) {
				int destID = dtnrouting.dest_index[dest_index];
				//System.out.print("For destination " + destID + ", number of available sources: " + srcDestPaths_All.get(dest_index).size() + "\n");
				if(srcDestPaths_All.get(dest_index).size() != 0) {
					
					// row: no. of available sources; [column: no. of network metrics; 0 = HC; 1 = BW; 2 = PI; 3 = IU] 
					double networkMetricValues[][] = new double[srcDestPaths_All.get(dest_index).size()][4];
					double goodnessValue_wrtNetworkMetrics[][] = new double[srcDestPaths_All.get(dest_index).size()][4];


					for(int source_index = 0; source_index < srcDestPaths_All.get(dest_index).size(); source_index++) {
						int hopCount = srcDestPaths_All.get(dest_index).get(source_index).size() - 1;
						//int sourceNode = dtnrouting.liveNodes.get(srcDestPaths_All.get(dest_index).get(source_index).get(0)).ID-1;

						
						// Exploring Network Metric Values possessed by particular Src-Dst route
						if(hopCount != 0) {	
							networkMetricValues_AlongPath nmv = new networkMetricValues_AlongPath();
							networkMetricValues[source_index][0] = hopCount;
							networkMetricValues[source_index][1] = dtnrouting.liveNodes.get(srcDestPaths_All.get(dest_index).get(source_index).get(0)).informationUtility;
							networkMetricValues[source_index][3] = nmv.getPathReliability(srcDestPaths_All.get(dest_index).get(source_index)); 
						} 
						
						// Evaluating goodness value of a metric i.e. percentage of user need met w.r.t a metric	
						// Hop-Count
						goodnessValue_wrtNetworkMetrics[source_index][0] = 
								(networkMetricValues[source_index][0] <= dtnrouting.liveNodes.get(destID).neworkMetricRequirements[0])? 
										1: dtnrouting.liveNodes.get(destID).neworkMetricRequirements[0] / networkMetricValues[source_index][0];
						
						// Information Utility
						goodnessValue_wrtNetworkMetrics[source_index][1] = 
								(networkMetricValues[source_index][1] >= dtnrouting.liveNodes.get(destID).neworkMetricRequirements[1])?
										1: networkMetricValues[source_index][1] / dtnrouting.liveNodes.get(destID).neworkMetricRequirements[1];

						// Bandwidth
						goodnessValue_wrtNetworkMetrics[source_index][2] =
								(networkMetricValues[source_index][2] >= dtnrouting.liveNodes.get(destID).neworkMetricRequirements[2])?
										1: networkMetricValues[source_index][2] / dtnrouting.liveNodes.get(destID).neworkMetricRequirements[2];

						// Path-Integrity or Reliability
						goodnessValue_wrtNetworkMetrics[source_index][3] = 
								(networkMetricValues[source_index][3] >= dtnrouting.liveNodes.get(destID).neworkMetricRequirements[3])?
										1: networkMetricValues[source_index][3] / dtnrouting.liveNodes.get(destID).neworkMetricRequirements[3];

						

						/*
						 * System.out.println("Network Metric Actual Values: \n" + "HC: " +
						 * networkMetricValues[source_index][0] + "; IU: " +
						 * networkMetricValues[source_index][1] + "; BW: " +
						 * networkMetricValues[source_index][2] + "; PI: " +
						 * networkMetricValues[source_index][3]);
						 * System.out.println("Network Metric Required Values: \n" + "HC: " +
						 * dtnrouting.liveNodes.get(destID).neworkMetricRequirements[0] + "; IU: " +
						 * dtnrouting.liveNodes.get(destID).neworkMetricRequirements[1] + "; BW: " +
						 * dtnrouting.liveNodes.get(destID).neworkMetricRequirements[2] + "; PI: " +
						 * dtnrouting.liveNodes.get(destID).neworkMetricRequirements[3]);
						 * System.out.println("Goodness Values: \n" + "HC: " +
						 * goodnessValue_wrtNetworkMetrics[source_index][0] + "; IU: " +
						 * goodnessValue_wrtNetworkMetrics[source_index][1] + "; BW: " +
						 * goodnessValue_wrtNetworkMetrics[source_index][2] + "; PI: " +
						 * goodnessValue_wrtNetworkMetrics[source_index][3]);
						 */			
						
					}

					// Choose best source for each destination AND update source-dest pair array
					
					
					//System.out.println(srcDestPaths_All.get(dest_index)+","+dest_index);
					SourceSelection ss = new SourceSelection();
					try {
						dtnrouting.destsourcePair[dest_index] = ss.sourceSelection_QoIT_withoutUAV(networkMetricValues, goodnessValue_wrtNetworkMetrics, srcDestPaths_All.get(dest_index), destID);
						//System.out.println("::"+dtnrouting.destsourcePair[dest_index]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
												
					//System.exit(0);
				}
				else { // If 
					try {
						NetworkMetricValues_SelectedSource.writeToFile(destID, -1, 0, 0, 0, 0);
						QualityMetricsScore_SelectedSource.writeToFile(destID, -1, 0, 0, 0, 0);
						PNP_SelectedSource.writeToFile(destID, -1, 0, 0, 0);
						NetworkMetricsScore_SelectedSource.writeToFile(destID, -1, 0, 0, 0, 0);
					} catch (IOException e) { e.printStackTrace(); }				
				}
			} 
		} //End Method
			
//******************************************************************************	

public void setInitialSource() {
       
     // For each dest and source pair
	 for(int d = 0; d < dtnrouting.dest_index.length; d++) {
	
		    //Destination chooses number of packets randomly
			Node destNode = dtnrouting.liveNodes.get(dtnrouting.dest_index[d]);
			Node sourceNode = dtnrouting.liveNodes.get(dtnrouting.destsourcePair[d]);
			
			System.out.println("Initial SOURCE: "+sourceNode.name+", of DEST: "+destNode.name);
		
			//stores the path a packet will take from the shortest path from its source to destination
			// Stores them in its selected source buffer
			for(int j=0; j< destNode.nodePackets.size(); j++) {//number of packets that each source will transmit..
				Packet packetObj = destNode.nodePackets.get(j);
				
				// Packet will follow the shortest path from source too destination
				for(int c=0; c < sourceNode.ptD.paths.get(d).size(); c ++) {
					if(sourceNode.ptD.paths.get(d).get(c)==(-1)) {
						break; // store no path for packet
					}
					else {
						packetObj.pathHops.add(dtnrouting.liveNodes.get(sourceNode.ptD.paths.get(d).get(c)));}}
					

				// Store packet to inside source buffer
				if(sourceNode.queueSizeLeft > packetObj.packetSize)
				  {    
					sourceNode.queueSizeLeft-=packetObj.packetSize; //update queue space after putting packet in it
					sourceNode.packetIDHash.add(packetObj.packetName); //Store ID of packet in the source as Hash value
					//sourceNode.packetTimeSlots.put(p.packetName,0);
					sourceNode.packetCopies.put(packetObj.packetName,1);
					packetObj.sourceNode_ofpacket=sourceNode;
					// these packets have no path
					if(packetObj.pathHops.size()==0) {
						sourceNode.DestNPacket.put(packetObj,null);
						sourceNode.number_packet_arrived+=1;}
					else
						sourceNode.DestNPacket.put(packetObj,destNode);
					
					dtnrouting.sdpTA.append("\n "+sourceNode.ID+"--"+destNode.ID+" ("+packetObj.packetName+")");}

			  else    //If queue of the packet has not enough space to store the new packet then
					dtnrouting.sdpTA.append("\nSource "+ sourceNode.ID+" has not enough space to occupy "+packetObj.packetName);  
			   }  //all packets of the destination assigned to the source of dest.     
		}  

	} // End of Method

	//******************************************************************************
	public void changeSource() {
	       
	     // For each dest and source pair
		 for(int d = 0; d < dtnrouting.dest_index.length; d++) {
		
				//Destination chooses number of packets randomly
				Node destNode = dtnrouting.liveNodes.get(dtnrouting.dest_index[d]);
				Node sourceNode = dtnrouting.liveNodes.get(dtnrouting.destsourcePair[d]);
				
				System.out.println("New SOURCE: "+sourceNode.name+", of DEST: "+destNode.name);
				
				
				for(int j=0; j< (destNode.nodePackets.size()); j++) {
					
					//Identify the packets not yet delivered to the destination nor expired
					if(!destNode.nodePackets.get(j).ispacketDelivered & !destNode.nodePackets.get(j).isTTLExpired ) {
					Packet packetObj = destNode.nodePackets.get(j);
					Node oldSource = packetObj.sourceNode_ofpacket;
					
					// Remove it from old source if not yet transmitted and move to new source
					if(oldSource.DestNPacket.containsKey(packetObj)) {
					
					oldSource.packetIDHash.remove(packetObj.packetName);
					oldSource.queueSizeLeft+=packetObj.packetSize; // the whole space 
					oldSource.DestNPacket.remove(packetObj);
					//System.out.println("\nPath of packet:"+packetObj.packetName);
					// Packet will follow the shortest path from source to destination
					for(int c=0; c < sourceNode.ptD.paths.get(d).size(); c ++) {
						if(sourceNode.ptD.paths.get(d).get(c)==(-1)) {
							break; // store no path for packet
						}
						else {
							packetObj.pathHops.add(dtnrouting.liveNodes.get(sourceNode.ptD.paths.get(d).get(c)));
							//System.out.print(dtnrouting.liveNodes.get(sourceNode.ptD.paths.get(d).get(c)).name
							//		+"->");
							}}
					
						

					// Store packet inside source buffer
					if(sourceNode.queueSizeLeft > packetObj.packetSize)
					  {    
						sourceNode.queueSizeLeft-=packetObj.packetSize; //update queue space after putting packet in it
						sourceNode.packetIDHash.add(packetObj.packetName); //Store ID of packet in the source as Hash value
						//sourceNode.packetTimeSlots.put(p.packetName,0);
						sourceNode.packetCopies.put(packetObj.packetName,1);
						packetObj.sourceNode_ofpacket=sourceNode;
						//System.out.println(packetObj.packetName+", old source:"+oldSource.name+", newsource:"+sourceNode.name);
						// these packets have no path
						if(packetObj.pathHops.size()==0) {
							sourceNode.DestNPacket.put(packetObj,null);
							sourceNode.number_packet_arrived+=1;}
						else
							sourceNode.DestNPacket.put(packetObj,destNode);
						//System.out.println("Old SOURCE: "+oldSource.name+", of DEST: "+destNode.name);
						
						dtnrouting.sdpTA.append("\n "+sourceNode.ID+"--"+destNode.ID+" ("+packetObj.packetName+")");}

				  else    //If queue of the packet has not enough space to store the new packet then
						dtnrouting.sdpTA.append("\nSource "+ sourceNode.ID+" has not enough space to occupy "+packetObj.packetName);  
				   }  //all packets of the destination assigned to the source of dest.     
			} } }

		} // End of Method

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
	    }

	//******************************************************************************
}//END OF packet CLASS
