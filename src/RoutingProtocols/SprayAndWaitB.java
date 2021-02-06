//******************************************************************************
//PACKAGE NAME

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

//******************************************************************************
//SPRAY AND WAIT BINARY ROUTING SCHEME

public class SprayAndWaitB extends RoutingProtocol
{
    //Instance Variables
    Random rand;
    String ferryNode=" ";
    boolean removepacketCopy=false;

//******************************************************************************
//CONSTRUCTOR
public SprayAndWaitB() {}

//******************************************************************************

public void DeliverData(Node nx, Node ny)
{
    //Transfer the packets
    for (Iterator<Map.Entry<Packet,Node>> i = nx.DestNPacket.entrySet().iterator(); i.hasNext(); )
    {
        Map.Entry<Packet,Node> entry = i.next();
        Packet packetObj = entry.getKey();
        Node   destNode = entry.getValue();
        //If destination has not enough size to receive packet
        //OR if its TTL is expired, , it packet cannot be sent
         if(expiredTTL_LargeSize(nx,ny,packetObj)==true) ;
        
        //If destination has enough size to receive packet
        //and if its TTL is not expired, , it packet can be sent
        // if contact duration is enough to transfer the message
        else 
        {
        	if(ny==destNode)
            {
                int bcopy=nx.packetCopies.get(packetObj.packetName);
                packetObj.packetReliability = (Math.min(packetObj.packetReliability, nx.reliability));
                packetObj.packetHops+=1;
                ny.DestNPacket.put(packetObj,null);
                ny.packetIDHash.add(packetObj.packetName);
                ny.packetCopies.put(packetObj.packetName,1);
                ny.queueSizeLeft-=packetObj.packetSize;
                
                packetObj.ispacketDelivered=true;
                nx.capacity -= packetObj.packetSize;
                
                //update nx
                nx.packetCopies.put(packetObj.packetName, bcopy-1);
                if((bcopy-1)<1)
                nx.queueSizeLeft+=packetObj.packetSize; // the whole space
                i.remove();
                dtnrouting.sdpTA.append("\n"+nx.ID+" ---> "+ny.ID+":"+packetObj.packetName);     }
        
            else if(nx.packetCopies.get(packetObj.packetName)>1)
                {
                       int bcopy=nx.packetCopies.get(packetObj.packetName);
                       packetObj.packetReliability = (Math.min(packetObj.packetReliability, nx.reliability));
                       packetObj.packetHops+=1;
                       ny.DestNPacket.put(packetObj,destNode);
                       ny.packetIDHash.add(packetObj.packetName);
                       
                       ny.queueSizeLeft-=packetObj.packetSize;
                       ny.packetCopies.put(packetObj.packetName,bcopy/2);
                       nx.packetCopies.put(packetObj.packetName, bcopy/2);
                       nx.capacity -= packetObj.packetSize;
                       dtnrouting.sdpTA.append("\n"+nx.name+" ---> "+ny.name+":("+ny.packetCopies.get(packetObj.packetName)+") "+packetObj.packetName);
               }

            }
    } //End of for loop

}//end of deliver method

//******************************************************************************

}// end of class
