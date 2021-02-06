/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

/**
 *
 * @author sarmad
 */
//DRAWBACK OF WARSHALLCD IS THAT IN CONNECTED GRAPH IT CANNOT FIND CLUSTERS
public class WarshallCD {
int clusters[][];
int notRows[];
int x=1,h=0,numClusters=0;
double A[][];
int P[][];
int PT[][];
int Q[][];
int s,clusterMade;
int cMem[];
////////////////////////////////////////////////////////////////////////////////
public WarshallCD(double [][]A, int s)//Constructur
{
//System.out.print("\n\n\n");
this.s=s;
cMem=new int[s];
clusters=new int[s][s];
notRows=new int[s];
this.A=A;
P=new int [s][s];
PT=new int [s][s];
Q=new int[s][s];
}//End of constructor
////////////////////////////////////////////////////////////////////////////////
public int runWarshallCD()
{
int i,j,k;

for(i=0;i<s;i++)
{
    notRows[i] = 0;
	for(j=0;j<s;j++)
	{
		clusters[i][j]=0;
		if(A[i][j]==0)
                P[i][j]=0; //Path metrix
                else
                    P[i][j]=1;

	}
 }

//Path matrix
for(k=0;k<s;k++)
    for(i=0;i<s;i++)
        for(j=0;j<s;j++)
        {
                P[i][j]=P[i][j]|(P[i][k]&P[k][j]); //Path Matrix
                PT[j][i]=P[i][j];
        }
 

//Cluster matrix creation
for(i=0;i<s;i++)
{

    x++;
    	
	for(j=0;j<s;j++)
	{
		Q[i][j]=P[i][j]&PT[i][j]; //Cluster Matrix
                if(i==j)Q[i][j]=1;
	}
	
}
x=1;
int m=0,n=0;
//Clusters Identificaiton
for(i=0;i<s;i++)
{
	for(j=0;j<s;j++)
	{
		if(Q[i][j]==1)
		{
			clusters[m][n]=j+1;
			if((i<j)&& notRows[j]==0 )
				notRows[j]=j;
		}
			n=n+1;
		if(n==s) n=0;
	}
	m=m+1;
    }
//Number of clusters
        numClusters=1;
    for(i=0;i<s;i++)
    {
	if(notRows[i]==0)
            numClusters++;
    }
        return numClusters-1;
 } //End of Method
//******************************************************************************
public int[]displayClusters()
{
int x=1,i,j;
String y="",t="";
int hasMember=0;
numClusters=1;//Number of clusters

int l=0;
for(i=0;i<s;i++)
{
	if(notRows[i]==0)
	{
         hasMember=0;
         x++;
         t+=x-1;
         CDSInterface.rowNo[CDSInterface.r][CDSInterface.c++]=t;
         for(j=0;j<s;j++)
	 if(clusters[i][j]!=0)
            {

                hasMember=1;
                cMem[l++]=(x-1);
                y+="  "+clusters[i][j];
                CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]=y;
            }
         if(hasMember==1)
         {
              CDSInterface.r++;
              CDSInterface.c=0;
              y="";t="";
         }
        else
         {
             x=x-1;
             CDSInterface.c-=1;
             t="";
             
         }
	}
}
GirvanNewman.clustermembers=clusters;
numClusters=x-1;
x=1;

System.out.print("\n\nBroker Nodes\n");
    for(i=0;i<s;i++)
    {

	for(j=0;j<s;j++)
	{
	       if((A[i][j]==1) && A[i][j]!=Q[i][j])
		System.out.print("Path: "+(i+1)+"-->"+(j+1)+", ");
	}

    }
return cMem;
} //End of displayClusters()
}
