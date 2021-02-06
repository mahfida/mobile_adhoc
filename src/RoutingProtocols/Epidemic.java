//******************************************************************************
//PACKAGE
package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;
import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//FLOOD ROUTING PROTOCOL

public class Epidemic extends RoutingProtocol
{
    //Instance Variables
    String ferryNode=" ";
    boolean removepacketcopy=false,forward=false;

//******************************************************************************
//CONSTRUCTOR
public Epidemic()  {}

//******************************************************************************
//DELIVER MESSAGES

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
        //and if its TTL is not expired, it packet can be sent
        // if contact duration is enough to transfer the message
        else {  
        	
        	    packetObj.packetLoad+=1;    //packet copy increments
                if(destNode==ny)
                {
                    deliver_Destination(nx, ny, packetObj);
                    packetObj.packetLoad-=1;    //packetCopy decrements
                }
                else 	
                // if(ny.name.startsWith("R")||ny.name.startsWith("D"))  
                {
                	deliver_Relay(nx, ny,destNode, packetObj, false);
                	//increase the number of copies made by nx of this packetObj
                	nx.packetCopies.put(packetObj.packetName,nx.packetCopies.get(packetObj.packetName)+1);
           
                }
             }
        }//End of for loop

}//end of method
}//end of class
