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
public class MeshCD {
 static int NodesIndex[];

//*************************************************************************
int s; //number of nodes
double [][]A; //Adjacency Matrix
int Nodes[];
int N[][],numN[],cMem[];

///////////////////////////////////////////////////////////////////////////////

public MeshCD(double [][]A, int sz)
{
this.A=A;
s=sz;
Nodes=new int[s];
NodesIndex=new int[s];
N=new int[s][s];
numN=new int[s];
cMem=new int[s];
int i,j,k;

for(i=0;i<s;i++)
        {
            NodesIndex[i]=Nodes[i]=(i+1);
            A[i][i]=1;
        }

////////////////////////////////////////////////////////
//Adjacency Matrix
for(i=0;i<s;i++)
    {
    for(j=0;j<s;j++)
        {
            if (A[i][j] == 1) 
            {
                numN[i] += 1;//counting neighbors of node i
                N[i][j]=(j+1);
             }
        }
    }
} //End of constructor
/////////////////////////////////////////////////////////////////////////////
public void runMeshCD()
{
  System.out.println("\n\n\nMESH CLUTSERING\n");
 int i,j,k;
//sort matrix in ascending order w.r.t number of neighbors
A=sortMatrix(A,numN,s);

//Calculate total number of neigburs matrix
for(i=0;i<s;i++)
	{
	     for(j=0;j<s;j++)
		    if(A[i][j]==1)
                         N[i][j]=(j+1);
                    
                    else N[i][j]=0;
	}


//Finding out similarities in matrix rows
float sim, nbor;
int cluster=0;
for(k=0;k<s;k++)
{
        if(Nodes[k]>0)
	{
            cMem[MeshCD.NodesIndex[k]-1]=++cluster;
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
			if((sim/nbor)>0.4)
                        {
                		Nodes[i]=0;
				cMem[MeshCD.NodesIndex[i]-1]=cluster;
			}
		}
	}
	}
}
// Display node name and its cluster
System.out.print("Clusters");
    for(j=1;j<=cluster;j++)
    {
        System.out.print("\n"+j+":");
        for(i=0;i<s;i++)
        {
            if(cMem[i]==j)
            System.out.print((i+1)+" ");
        }
    }
}
//End of Method
////////////////////////////////////////////////////////////////////////////////

public void findModularity()
{
    int i;
//Converting A in an Adjacency Matrix
for(i=0;i<s;i++)
    A[i][i]=0;
//Find Quality of Partitions
Modularity md=new Modularity(A,s);
System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
}

//*************************************************************************

public double[][] sortMatrix(double[][] A,int[] numN, int s)
{
    int max,loc,templ,temp;
    double tempA[]=new double[s];

        for(int i=0;i<(s-1);i++)
        {
            max = numN[i];
            loc = i;
            for(int j=i+1;j<s;j++)
                if(numN[j]>max)
                {
                    max=numN[j]; loc=j;
                }
            //chaning mtrix row postion according to number of neighbours
            tempA=A[i];
            A[i]=A[loc];
            A[loc]=tempA;
            temp=numN[i];
            numN[i]=numN[loc];
            numN[loc]=temp;
            templ=NodesIndex[i];
            //keeping information of changed position in 1-D array
            NodesIndex[i]=NodesIndex[loc];
            NodesIndex[loc]=templ;
          
        }
  
 
return A;
}
}
