//*****************************************************************************
//PACKAGE NAME

package DTNRouting;

//*****************************************************************************
//IMPORT FILES

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import DTNRouting.*;



//*****************************************************************************
//ALLOCATING MOVEMENT PATH TO NODES IN A DATA SET

public class DSPath
{
    //Instance Variables
    public static double pathDetail[][][];
    int rows, srows,sdays,nodes;
    double pow2,r;
    String filename;
	private BufferedReader br;

//*****************************************************************************
//CONSTRUCTOR

public DSPath(String filename, int nodes)
{
         while(pow2<nodes)
        {
            pow2=Math.pow(2, r);
            r=r+1;
        }
        this.filename = filename;
        srows=(int)pow2*7*4;
        sdays=(int)pow2*4;
        this.rows =nodes*srows;
        this.nodes=nodes;
        //source, dest, day, hour, presence/chances, x-coord, y-coord
        //nodes,total number of rows for a node
        pathDetail=new double[nodes][srows][6];

}



//*****************************************************************************
//RETRIEVE DATA FROM DATASET AND GENERATE PATH

public void Paths_allNodes() throws IOException
{
    int token=-1,i=0,j=0,k=0;
    
    // First "R" node

    i=i+dtnrouting.first_regular_node_index;
    j=j+dtnrouting.first_regular_node_index;
    	
    
    //Maximum number of x/y-coordinates for the "first" regular nodes
    dtnrouting.allNodes.get(dtnrouting.first_regular_node_index).x_max=(srows);
    dtnrouting.allNodes.get(dtnrouting.first_regular_node_index).y_max=(srows);
    
    try
    {
        //File to be read
        FileInputStream fstream = new FileInputStream(filename);
        DataInputStream in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        
        // Divide the row elements of the files into token
        while ((strLine = br.readLine()) != null)
        {
         StringTokenizer st = new StringTokenizer( strLine,"\t");
         String s="";
         while(st.hasMoreTokens())
         {
             token=token+1;
             s=st.nextToken();
             if(token<5)         pathDetail[j][k][token]=Double.parseDouble(s);//source
             else if(token==5)   dtnrouting.allNodes.get(j).x_coord.add(Double.parseDouble(s));//x-coord
             else if(token==6)   dtnrouting.allNodes.get(j).y_coord.add(Double.parseDouble(s));//y-coord
         }
         dtnrouting.allNodes.get(j).prob_coord.add(pathDetail[j][k][4]);
         i=i+1;
         
         
         //If all srows traces, do same as above for the next node
         if(k==(srows-1))
        	 {k=0; j=j+1;}
         else k = k + 1;//next row
         token=-1;
    }

    //Close the input stream
    in.close();

    }
    catch (Exception e)
    {//Catch exception if any
			System.err.println("Error: " + e.getMessage());
    }
 }//End of method

//*****************************************************************************
}//End of class
