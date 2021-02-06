//******************************************************************************
//PACKAGE

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;
import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//DIRECT DELIVERY ROUTING PROTOCOL

public class DirectDelivery extends RoutingProtocol
{

//******************************************************************************
//CONSTRUCTOR
public DirectDelivery() {}

//******************************************************************************
//DELIVER MESSAGE

public void DeliverData(Node nx, Node ny)
{
   for (Iterator<Map.Entry<Packet,Node>> i = nx.DestNPacket.entrySet().iterator(); i.hasNext(); )
    {
       Map.Entry<Packet,Node> entry = i.next();
       Packet packetObj = entry.getKey();
       Node   destNode = entry.getValue();

        //If destination has not enough size to receive packet
        //OR if its TTL is expired, , it packet cannot be sent

         if(expiredTTL_LargeSize(nx,ny,packetObj)==true); 
  
         //If destination has enough size to receive packet
         //and if its TTL is not expired, , it packet can be sent
         // if contact duration is enough to transfer the message
         else if(destNode==ny) deliver_Destination(nx, ny, packetObj);
     
    }// traversed via all the packets
}//end of method name
//******************************************************************************

}// end of class
