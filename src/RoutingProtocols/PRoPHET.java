//******************************************************************************
//PACKAGE

package RoutingProtocols;

//******************************************************************************
//IMPORT FILES

import DTNRouting.*;

import java.util.Iterator;
import java.util.Map;

//******************************************************************************
//A MULTI-COPY FREQUENCY OF ENCOUNTER BASED ROUTING PROTOCOL

public class PRoPHET extends RoutingProtocol
{
    //Instance Variables
    boolean warmFlag=false;
    public static int size;    //i and j are source and destination declared in main program
    double beta,gamma,p_encounter;
    static double [][] AgeCounter;//stores the time passed since last encounter

//******************************************************************************
//CONSTUCTOR

public  PRoPHET() {}

//******************************************************************************

@Override
public void setPerimeters()
{
    size=dtnrouting.allNodes.size();
    AgeCounter=new double[size][size];
    dtnrouting.p=new double[size][size];
    beta=0.75;
    gamma=0.75;
    p_encounter=0.5;
    for(int m=0;m<size;m++)
        for(int n=0;n<size;n++)
        {
            AgeCounter[m][n]=0;
            if(m==n)
            dtnrouting.p[m][n]=1.0; //same node has 1 as predictability value with itself
            else
            dtnrouting.p[m][n]=0.0; //different nodes has 0 as initial predictability value
        }
   dtnrouting.sdpTA.append("\nWARMUP PERIOD");
}

//******************************************************************************
//UPON ENCOUNTER RAISE THE DELIVERY PREDICTABILITY

@Override
public void Encounter(int x,int y)  //x and y are sender and receiver nodes
{
	//Setting probability of encountering between the two nodes
    dtnrouting.p[x][y]=dtnrouting.p[x][y]+((1-dtnrouting.p[x][y])*p_encounter); 
    //initially when two nodes are encountered its age counter is 0
    //and when they go out of range its age increases gradually
    AgeCounter[x][y]=0;     
}

//******************************************************************************
//RAISE DP WHEN INDIRECT ENCOUNTER VIA A MUTUAL FRIEND HAPPENS

@Override
public void Transitivity(int x, int y)
{
  if(dtnrouting.p[x][y]>0.75)
  {
      for(int z=0;z<size;z++)
          if(z!=x && z!=y)  //suppose z is 3rd node check that it is not x or y
          {
              if(dtnrouting.p[y][z]>0.75)   //if two nodes x and y has high probability
           //to be within range of each other and y & z has same relation wiht each other then so is the case of x &z
              dtnrouting.p[z][x]=dtnrouting.p[x][z]=dtnrouting.p[x][z]+(1-dtnrouting.p[x][z])*dtnrouting.p[x][y]*dtnrouting.p[y][z]*beta;
                           

          }
   }
}

//******************************************************************************
//DECREASE DP AS TIME OF LAST ENCOUNTER INCREASES

@Override
public void Aging(int x,int y)
{
    for(int z=0;z<size;z++)
        if(z!=x && z!=y)
        {
            //update aging of x and z
            dtnrouting.p[z][x]=dtnrouting.p[x][z]=dtnrouting.p[x][z]*Math.pow(gamma,AgeCounter[x][z]);
            AgeCounter[z][x]=AgeCounter[x][z]=AgeCounter[x][z]+0.001;  //when two nodes go away from each other its age counter is increase gradually
            //update aging of y and z
            dtnrouting.p[z][y]=dtnrouting.p[y][z]=dtnrouting.p[y][z]*Math.pow(gamma,AgeCounter[y][z]);
            AgeCounter[z][y]=AgeCounter[y][z]=AgeCounter[y][z]+0.001;  //when two nodes go away from each other its aging is increase gradually
         }
}

//******************************************************************************

public void DeliverData(Node nx,Node ny)
{  
  Encounter(nx.ID-1,ny.ID-1);
  Transitivity(nx.ID-1,ny.ID-1);
  Aging(nx.ID-1,ny.ID-1);

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
    		deliver_Destination(nx,ny, packetObj);
        

        else  if(dtnrouting.p[ny.ID-1][destNode.ID-1] > dtnrouting.p[nx.ID-1][destNode.ID-1])
        {
        	deliver_Relay(nx,ny,destNode, packetObj, true);
         packetObj.packetLoad+=1;
        }
     }

   }//End of for loop

}

}   //end of Deliver()

//******************************************************************************

}//End of class





