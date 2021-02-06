/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;
import Support.Modularity;

public class SimCD {
   
//*************************************************************************
double [][]A;
int s;
int N[][], Nodes[];
int numN[],cMem[];
public SimCD(double [][]A, int sz)
{
s=sz;
Nodes=new int[s];
this.A=A;
N=new int[s][s];
numN=new int[s];
cMem=new int[s];
int i,j;

 for(i=0;i<s;i++)
      Nodes[i]=(i+1);


//Adjacency Matrix
    for(i=0;i<s;i++)
    {
	for(j=0;j<s;j++)
            {
                if (A[i][j] == 1)
                {
                    numN[i] += 1;
                    N[i][j]=1;
                }
                else
                    N[i][j]=0;

            }
	
    }


}
///////////////////////////////////////////////////////////////////////////////
public void runSimCD()
{

System.out.println("\n\n\nSIMILARITY CLUSTERING\n");
int i,j,k;
//Finding out similarities in matrix rows
float sim, nbor;
int cluster=0;
for(k=0;k<s;k++)
{
        if(Nodes[k]>0)
	{
            cMem[k]=++cluster;
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
				cMem[i]=cluster;
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
}//End of the method
////////////////////////////////////////////////////////////////////////////////

public void findModularity()
{
        //Converting A in an Adjacency Matrix
        for(int i=0;i<s;i++)
            A[i][i]=0;
        //Find Quality of Partitions
        Modularity md=new Modularity(A,s);
        System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));

}

//*************************************************************************
}
