/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import Support.EigenVector;
import Support.hGraph;

/**
 *
 * @author sarmad
 */
public class ModularityCD{
    double EValues[],EVectors[][];
    EigenVector ev;
    double A[][], B[][];
    int s[],i,j,Community[][];
    double k[];
    double m=0;
    int sz;
    int a=0, b=0;
    static int  cN=0;
    double Q;
  public ModularityCD(double[][] A,int []s, int sz)
    {
        this.A=A;
        this.sz=sz;
        this.s=s;
        B=new double[sz][sz];

    }

public boolean CheckModularity()
{

k=new double[sz];
// find degree of vertices
for (i=1;i<sz;i++)
    for(j=0;j<sz;j++)
    {
        if(A[i][j]>0){
            k[i]=k[i]+A[i][j];
            m=m+A[i][j];}

    }




    // find modularity matrix
    m=m/2;
    for (i=0;i<sz;i++)
    for(j=0;j<sz;j++)
    {
        B[i][j]=A[i][j]-(k[i]*k[j])/(2*m);
    }

     ev=new EigenVector(B,sz);
     EVectors=new double[sz][sz];
     EValues=new double[sz];
     ev.EVMatrix();
     EVectors=ev.getEVectors();
     EValues=ev.getEValues();
     double vmax=EValues[0];
     int loc=0;
     for(j=0;j<2;j++)
     {
            if(EValues[j]>vmax) { 
                                    loc=j; vmax=EValues[j];
                                }
            
     }

    int sumn=0,sump=0;
    for (i=0;i<sz;i++)
     {
     
        if(EVectors[i][loc]>0)
            {
                s[i] = 1;   sump+=1;
            }
        else
            {
                s[i] = -1; sumn+=1;
            }
      }

//////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////

//find Modularity Value

double stB[]=new double[sz];
for (i=0;i<sz;i++)
    for(j=0;j<sz;j++)
    {
    stB[i]+=s[j]*B[j][i];
    }
for (i=0;i<sz;i++)
    {
    Q+=stB[i]*s[i];
    }
Q/=(4*m);
if(Q<=0 || sumn==sz ||sump==sz) return false;
        else return true;

}

public int[][] doDivision(int[] Vertex)
{
    //find communities in contact graph
     
    
     

Community=new int[sz][2];
cN=cN+1;    a=cN;
cN=cN+1;    b=cN;
hGraph.a=a;
hGraph.b=b;
    // find Division
    for (i=0;i<sz;i++){
            Community[i][0]=Vertex[i];
        if(EVectors[i][0]>0)
            Community[i][1]=a;
        else
            Community[i][1]=b;
        }


return(Community);
    }

}
