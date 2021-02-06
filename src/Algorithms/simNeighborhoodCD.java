/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import Support.Modularity;

/**
 *
 * @author sarmad
 */
public class simNeighborhoodCD{
 double avgRatio[];
 int cMem[], Nodes[], numN[];
 int N[][];
 double [][]A;
 int s;

 ///////////////////////////////////////////////////////////////////////////////

 public simNeighborhoodCD(double A[][], int sz)
{
this.A=A;   //Assign argument matrix A to instance variable A.
s=sz;       //Number of data point/nodes
Nodes=new int[s];
N=new int[s][s];
numN=new int[s];
int i,j,k;

 for(i=0;i<s;i++)
     {
        A[i][i]=1;  //reachability s 1 to ones self
         Nodes[i] = (i + 1);
     }


////////////////////////////////////////////////////////

//Counting one hop neighbors of a node AND storing the id of neighbor
for(i=0;i<s;i++)
{
	for(j=0;j<s;j++)
            {
                    if (A[i][j] == 1)
                    {
                            numN[i] += 1;   //Counting one hop neighbors of a node
                            N[i][j]=(j+1);  //storing the id of neighbor
                    }

                    else N[i][j]=0;
            }

 }

 }//End of constructor


//*************************************************************************
public void runSimNeighborhood()
{

 System.out.println("\n\nSIMILARITY OF NEIGHBORS CLUSTERING\n");
   //Finding out similarity ratio between the neighbors of two nodes
double simRatio[][]=new double[s][s];
int culsterMembers[][]=new int[s][];

////////////////////////////////////////////////////////////////////
simRatio=nodesSimilarity(N,s);

//Finding out neighbors of a node
culsterMembers=neighborList(simRatio,s);
deriveClusters(culsterMembers, Nodes,numN, s);

//Converting A in an Adjacency Matrix
for(int i=0;i<s;i++)
    A[i][i]=0;
//Finding out the quality of partitions
findModularity(A,s,cMem);
}

//*************************************************************************
public double[][] nodesSimilarity(int [][]N,int s)
{
        double simRatio[][]=new double[s][s];
        int i,j,k;
        double[] numN=new double[s];
        avgRatio=new double[s];
        //Number of neighbors of a node
            for(k=0;k<s;k++)
                  for(i=0;i<s;i++)
                        if(N[k][i]!=0) numN[k]+=1;

            for(k=0;k<s;k++)
                 for(i=0;i<s;i++)
                     {   for(j=0;j<s;j++)
                        {
                            if (k!=i && N[k][j]!=0 && N[k][j]==N[i][j])
                            {
                                simRatio[k][i] += 1;
                            }
                        }
                     }

            for(k=0;k<s;k++)
                    for(i=0;i<s;i++)
                    {
                        if(k!=i && simRatio[k][i]>0)
                        {
                            simRatio[k][i]/=numN[k];
                        //    System.out.println("\n"+(k+1)+","+(i+1)+":"+simRatio[k][i]);
                            avgRatio[k]+=simRatio[k][i];
                         }


                    }

        return simRatio;
    }
    //****************************************************************************

    public int [][] neighborList(double[][] simRatio,int s)
    {
        int clusMembers[][]=new int[s][];
        int i,j,k;
        int numN[]=new int[s];
        for(k=0;k<s;k++)
        {
                int n=0;
                for(j=0;j<s;j++)
                if(simRatio[k][j]>0)
                 {
                     n=n+1;
                 }
               // System.out.println("\n"+(k+1)+", "+ "avg"+avgRatio[k] +":");
                avgRatio[k]/=n;
                //System.out.println((k+1)+", "+ "avg"+avgRatio[k] +", n:"+(n-1));
         }
        //find number of neighbour nodes
        for(k=0;k<s;k++)
        {
            for (j = 0; j < s; j++)
            {
                if(k==j)
                    numN[k]+=1;
            if(simRatio[k][j]>=avgRatio[k])
                numN[k]+=1;

            }
            clusMembers[k]=new int[numN[k]];

        }
        //Display Neighbors of every node
        for(k=0;k<s;k++)
        {
            //System.out.print("\n Cluster of "+(k+1)+" : ");
            i=-1;
            for (j = 0; j < s; j++)
            {
            if(k==j|| simRatio[k][j]>=avgRatio[k])
            {
                clusMembers[k][++i] = (j+1);
              //  System.out.print( clusMembers[k][i]+" ");
            }

            }

        }
        return clusMembers;
  }

//******************************************************************************

public void deriveClusters(int [][]clusterMembers, int[] Nodes,int []numN, int s)
    {
        int i,j,k,max,cluster=0,cMem[]=new int[s], loc=0;
        float sim, nbor;

        int N[][]=new int[s][s],NodesIndex[]=new int[s];
        for(i=0;i<s;i++)
        {
            //find maxmum node
            max=numN[0]; loc=0;
            for(k=0;k<s;k++)
            {
                if(numN[k]>max)
                {
                    max = numN[k];
                    loc = k;
                }
            }
            NodesIndex[i]=Nodes[i]=(loc+1);
            for(j=0;j<s;j++)
            {
                if(j<clusterMembers[loc].length)
                    N[i][j]=clusterMembers[loc][j];
                else
                    N[i][j]=0;
            }
            numN[loc]=-1;
        }

      //Find out Cluter Elements

        for(k=0;k<s;k++)
        {
            if(Nodes[k]>0)
                {
                    cMem[NodesIndex[k]-1]=++cluster;
                    for(i=k+1;i<s;i++)
                    {
                        sim=0; nbor=0;
                        if(Nodes[i]>0)
                        {
                                for(j=0;j<s;j++)
                                {
                                        if(N[i][j]!=0)
                                        {
                                                nbor+=1;
                                        if(N[k][j]==N[i][j])
                                                sim+=1;
                                        }
                                }
                                if((sim/nbor)>0.1)
                                {
                                        Nodes[i]=0;
                                        cMem[NodesIndex[i]-1]=cluster;
                                }
                        }//End of IF
                    }//End of FOR
                }//END of IF
            }//End of FOR
        // Display node name and its cluster
        System.out.print("Custers");
            for(j=1;j<=cluster;j++)
            {
                System.out.print("\n"+j+":");
                for(i=0;i<s;i++)
                {
                    if(cMem[i]==j)
                    System.out.print((i+1)+" ");
                }
            }
this.cMem=cMem;

    }//END OF METHOD
//******************************************************************************

public void findModularity(double[][]A,int s,int []cMem)
    {
    Modularity md=new Modularity(A,s);
    System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
}

}//END OF CLASS


