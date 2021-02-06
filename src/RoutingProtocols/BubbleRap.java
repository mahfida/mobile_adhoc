//******************************************************************************
//PACKAGE

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;
import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//BUBBLERAP ROUTING PROTOCOL

public class BubbleRap extends RoutingProtocol
{
public double localRanking[];//local centrality of nodes
public double  globalRanking[];//global centrality of nodes
public int communityLabel[];//ID of the community to which this node belongs
//******************************************************************************
//CONSTRUCTOR
public BubbleRap()
{

}

//******************************************************************************
//INITIALIZE RANKINGS AND COMMUNITY MEMBERSHIP
public void setPerimeters(int size)
{
    localRanking=new double[size];
    globalRanking=new double[size];
    communityLabel=new int[size]; 


}

//******************************************************************************
//DELIVER MESSAGE

public void DeliverData(Node nx, Node ny)
{

   for (Iterator<Map.Entry<Packet,Node>> i = nx.DestNPacket.entrySet().iterator(); i.hasNext(); )
    {
        Map.Entry<Packet,Node> entry = i.next();
        Packet packetObj = entry.getKey();
        Node   destNode = entry.getValue();
        transfer=false;
        isDestination=false;
        //If destination has not enough size to receive packet
        //OR if its TTL is expired, , it packet cannot be sent

         if(expiredTTL_LargeSize(nx,ny,packetObj)==true) ;

        //If destination has enough size to receive packet
        //and if its TTL is not expired, , it packet can be sent
        // if contact duration is enough to transfer the message
        
        else{      
        			transfer=false;
        			// if this is the destination node
                   if(destNode==ny) deliver_Destination(nx,ny,packetObj);
                   
                   // If nx has same community as destination then use local ranking of ny
                   else if(communityLabel[nx.ID-1]==communityLabel[destNode.ID-1])
                	    if((communityLabel[ny.ID-1]==communityLabel[destNode.ID-1]) &&
                            (localRanking[ny.ID-1]>localRanking[nx.ID-1]))
                            transfer=true;
     
                   //if ny has same community as destination node then us global ranking
                    else if((communityLabel[ny.ID-1]==communityLabel[destNode.ID-1])
                    		||(globalRanking[ny.ID-1]>globalRanking[nx.ID-1]))
                    		transfer=true;
                    
                   
                   //Transfer to carry on
                   if(transfer) 
                   deliver_Relay(nx,ny,destNode, packetObj, true);
                   
                   
                }

        }

}//end of method name

//******************************************************************************

}// end of class