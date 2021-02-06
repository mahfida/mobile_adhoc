//******************************************************************************
//PACKAGE

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;

import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//FRESH ROUTING ALGORITHM - BASED ON RECENCY OF ENCOUNTERS

public class FRESH extends RoutingProtocol
{
    //Instance Variables
    boolean warmFlag=false;
    public static int size;    //i and j are source and destination declared in main program
    static double [][] LastEncounterTime;//stores the last encounter Time

//******************************************************************************
//CONSTRUCTOR
public   FRESH() {}

//******************************************************************************

@Override
public void setPerimeters()
{
    size=dtnrouting.allNodes.size();
    LastEncounterTime=new double[size][size];
    
    for(int m=0;m<size;m++)
        for(int n=0;n<size;n++)
           LastEncounterTime[m][n]=-1; //Initially last encounter time is unknown shown by -1
    dtnrouting.sdpTA.append("\nWARMUP PERIOD");
}

//******************************************************************************

public void updateLastEncounterTime(int x,int y)
{
    LastEncounterTime[y][x]=LastEncounterTime[y][x]=dtnrouting.timer;
}

//******************************************************************************
public void DeliverData(Node nx,Node ny)    //x and y are intermediate sender and receiver
{
  updateLastEncounterTime(nx.ID-1,ny.ID-1);
  if(NodeMovement.warmupPeriod==size) //Warming Period finished
  {
      if(!warmFlag)
      {
          dtnrouting.sdpTA.append(" FINISHED ");
          for(int h=0;h<dtnrouting.arePacketsDelivered.size();h++)
            {
                Packet packetObj=dtnrouting.arePacketsDelivered.get(h);
                packetObj.packetTTL=packetObj.maxTTL;
                packetObj.packetLatency=0;
            }
          dtnrouting.timer=0;
      }
      warmFlag=true;
      
      
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
        		deliver_Destination(nx ,ny, packetObj);

            else  if(LastEncounterTime[nx.ID-1][destNode.ID-1]<LastEncounterTime[ny.ID-1][destNode.ID-1])
            	deliver_Relay(nx ,ny,destNode, packetObj, true);
        }
        
     }//End of for loop
}
}   //end of Deliver()
//******************************************************************************
}//End of class






