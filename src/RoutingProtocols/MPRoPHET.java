//******************************************************************************
//PACKAGE

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;

import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//MULTI-COPY PROPHET

public class MPRoPHET extends PRoPHET{
static double[][] AvgICDuration; //Average inter-contact duration between any two nodes
int cases;
public MPRoPHET()
    {
    }
    @Override
public void setPerimeters()
        
    {
    super.setPerimeters();
    AvgICDuration=new double[size][size];  // The average inter-contact duration between any two nodes
    for(int m=0;m<size;m++)
            for(int n=0;n<size;n++)
                      AvgICDuration[m][n]=0;
    }

@Override
public void Encounter(int x,int y)  //x and y are sender and receiver nodes
{
if(AgeCounter[x][y]>0) // The current inter-contact duration is stored in k
    {
    if(AvgICDuration[x][y]>0)
        //Update the value of t that stores InterContact Durations
        AvgICDuration[x][y]=(AvgICDuration[x][y]+AgeCounter[x][y])/2;  //if node x encoutners y multiple times in history
    else
        AvgICDuration[x][y]=AgeCounter[x][y]; //Maximum value of time lapse after which x and y encounters

    }
super.Encounter(x, y);//to update predictability value and set AgeCounter to zero
}

@Override
public void DeliverData(Node nx,Node ny)
{
    Encounter(nx.ID-1,ny.ID-1);
    Transitivity(nx.ID-1,ny.ID-1);
    Aging(nx.ID-1,ny.ID-1);
   
  if(NodeMovement.warmupPeriod==size) //Warming Period finished
    {
      if(!warmFlag)
      {
        for(int h=0;h<dtnrouting.arePacketsDelivered.size();h++)
        {
            Packet packetObj=dtnrouting.arePacketsDelivered.get(h);
            packetObj.packetTTL=packetObj.maxTTL;
            packetObj.packetLatency=0;
        }
          dtnrouting.sdpTA.append(" FINISHED\n");
          dtnrouting.timer=0;
      }
      warmFlag=true;
   
   //Transfer the packets
   for (Iterator<Map.Entry<	Packet,Node>> i = nx.DestNPacket.entrySet().iterator(); i.hasNext(); )
    {
        Map.Entry<Packet,Node> entry = i.next();
        Packet packetObj = entry.getKey();
        Node   destNode = entry.getValue();
        
        if(expiredTTL_LargeSize(nx,ny,packetObj)==true) ;
        
        // if link capacity is enough to transfer the message
        else
        {
            //If destination encounters, hands over packet to it
            if(ny==destNode)
                   cases=1;//handle packet to destination and delete its copy

            else
               if((dtnrouting.p[ny.ID-1][destNode.ID-1])>(dtnrouting.p[nx.ID-1][destNode.ID-1]))
               {
                   if((AvgICDuration[nx.ID-1][destNode.ID-1]>0.0) && ((AgeCounter[nx.ID-1][destNode.ID-1])>(0.67*AvgICDuration[nx.ID-1][destNode.ID-1]))&&(dtnrouting.p[nx.ID-1][destNode.ID-1]>0.0))
                    cases=2;//give packet to ny and keep its copy with itself too
                   else
                    cases=3; //give packet to ny and delete its local copy
               }
             else   //if ny may meet destination soon
                 if((AvgICDuration[ny.ID-1][destNode.ID-1]>0.0) && ((AgeCounter[ny.ID-1][destNode.ID-1])>(0.67*AvgICDuration[ny.ID-1][destNode.ID-1]))&&(dtnrouting.p[ny.ID-1][destNode.ID-1]>0.0))
                     cases=2; // there is chances that ny may meet sooner than nx with ny, so hand over a copy to ny and keep one with nx
           
            //Hand Over the packet to encountered Node
            packetObj.packetTTL-=1;

            //Perform case specific functions
            switch(cases)
           {
                case 1:
                   //deliver packet to destination
                   deliver_Destination(nx, ny, packetObj);
                   packetObj.packetLatency=(int)dtnrouting.timer;
                   
                   break;
               
                case 2:
                   //deliver packet to ny and keep copy with nx too
                   deliver_Relay(nx, ny,destNode, packetObj, false);
                   break;

                case 3:
                  deliver_Relay(nx, ny,destNode, packetObj, true);
                  nx.packetIDHash.remove(packetObj.packetName);                
                  break;
               }
            }

           
         }
   //Check whether forwarding of packets have ended
    }

} //End of Deliver()
} //End of MCPRoPHET


