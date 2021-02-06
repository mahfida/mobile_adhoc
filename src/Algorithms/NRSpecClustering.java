/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;
import Support.Kmeans;
import Support.Inverse;
import Support.EigenValues;
import Support.Modularity;
import Jama.Matrix;

/**
 *
 * @author sarmad
 */
public class NRSpecClustering implements BaseCD {
    //Instance variables
    static double A[][];
    static double D[][];
    static double DI[][];
    static double DIA[][];//inverse of degree matrix*Adjacency
    static double L[][];
    static double I[][];// Identity matrix of order szxsz
    static double U[][];
    static int K,sz;
    static double B[][];
    static double condCluster[][];
    static double expCluster[][];
    int CMem[];
    static double Conductance[][];
    static double EVectors[][];
    static Matrix M;
    //*************************************************************************
    public NRSpecClustering(double A[][],int sz, int k)
    {
        this.sz=sz;
        this.A=new double[sz][sz];
        for(int i=0; i<sz; i++){
                for(int j=0; j<sz; j++){
                this.A[i][j]=A[i][j];
                 }}

        K=k;
        Conductance=new double[sz][2];
        condCluster=new double[k][2];
        expCluster=new double[k][2];
        CMem=new int[sz];
        B=new double[sz][sz];
        for(int i=0; i<sz; i++){
            for(int j=0; j<sz; j++){
                B[i][j]=A[i][j];
            }
        }
       
    }
    //**************************************************************************
    public void runClustering()
    {
        
        System.out.println("\n\n\nNORMALIZED RANDOM SPECTRAL CLUSTERING\n");
        createLaplacian(A);
        findModularity(CMem);
        findConductance();
        findExpansion();
    }
    //**************************************************************************
    public void createLaplacian(double A[][])
    {
        int i,j;
        D=new double[sz][sz];
        DI=new double[sz][sz];
        DIA=new double[sz][sz];
        I=new double[sz][sz];

        //Degree Matrix
        for(i=0;i<sz;i++)
           for(j=0;j<sz;j++)
           {
               if(i!=j) D[i][j]=0;
               D[i][i]+=A[i][j];
           }
        M=new Matrix(D);

        //Inverse Degree Matrix
        Inverse inD=new Inverse(sz);
        DI=inD.invert(D);


        // Identity Matrix
          for(i=0; i<sz; i++)
            {
            for(j=0;j<sz;j++)
                         I[i][j] = (i == j ? 1 : 0);
             }
        // Finding multiplication Matrix of DI and A
         DIA=matrixMultiplication(DI,A,sz);

        //Laplacian Matrix
         L=new double[sz][sz];
        for(i=0;i<sz;i++)
           for(j=0;j<sz;j++)
           {
                      L[i][j]=I[i][j]-DIA[i][j];
           }
      

          eigenVector(L);
    }

    public void eigenVector(double L[][])
    {
        int i,j;
        //Eigen Vector of Lalacian Matrix
        EigenValues ev=new EigenValues();
        ev.calculateEigen(L, sz);
        EVectors=ev.eigenVectors();

        //Pick up first K eigen vectors
        U=new double[sz][K];
        for(i=0;i<sz;i++)
            for(j=0;j<K;j++)
                U[i][j]=1000*EVectors[i][j];
        kMeans(U);
    }

    public void kMeans(double U[][])
    {
        Kmeans km=new Kmeans();
        CMem=km.findClusters(U,K,sz);


    }
     public static double[][] matrixMultiplication(double[][] A,double[][] B,int s)
 {
     double[][] C=new double[s][s];
     for(int i=0; i<s; i++)
     {
         for(int j=0; j<s; j++)
         {
             C[i][j]=0;
             for(int k=0; k<s; k++)
             {
                 C[i][j]+=A[i][k]*B[k][j];
             }
         }
     }
     return C;
 }
//*****************************************************************************************
public void findModularity(int []cMem)
{
    int i;
//Converting A in an Adjacency Matrix

//Find Quality of Partitions
Modularity md=new Modularity(B,sz);
//System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
CDSInterface.modresult=""+md.vlaueOfModularity(cMem);
CDSInterface.algoresult="Normalized Spectral Clustering";
CDSInterface.comresult="O(n power 3)";
}

//*****************************************************************************************
public void findConductance()
{
 double boundaryEdges=0;
 double result=0;
 double allEdges=0;
 double insideEdges=0;

 for(int i=0; i<sz; i++){
     for(int j=0; j<sz; j++)
     {
           if(B[i][j]==1){
               if(CMem[i]==CMem[j])
                   {
                       insideEdges += 1;
                       allEdges+=1;
                   }


             else{
                    boundaryEdges+=1;
                    allEdges+=1;//If edge crosses boundary
                 }


          }

       }
               Conductance[i][0]=boundaryEdges;
               Conductance[i][1]=allEdges;


      insideEdges=0; boundaryEdges=0; allEdges=0;
 }

     for(int i=0; i<sz; i++){
         
             condCluster[CMem[i]-1][0]+=Conductance[i][0];
             condCluster[CMem[i]-1][1]+=Conductance[i][1];
        

     }

  for(int l=0; l<K; l++){
      System.out.print("boundary edges "+condCluster[l][0]+"  "+"all edges "+condCluster[l][1]);
      result+=((condCluster[l][0])/(condCluster[l][1]));
  }
 result=result/K;
 System.out.println();
 System.out.println("result="+result);
 CDSInterface.condresult=""+result;
}

//*****************************************************************************************
public void findExpansion()
{
 double boundaryEdges=0;
 int clusterno=0;

 double insideEdges=0;

 for(int i=0; i<sz; i++){
     for(int j=0; j<sz; j++)
     {
           if(B[i][j]==1){
               if(CMem[i]==CMem[j])
                   {
                       insideEdges += 1;

                   }


             else{
                    boundaryEdges+=1;

                 }


          }

       }
               Conductance[i][0]=boundaryEdges;



      insideEdges=0; boundaryEdges=0;
 }
clusterno=0;
double expresult=0;
     for(int i=0; i<sz; i++){
        
             expCluster[CMem[i]-1][0]+=Conductance[i][0];
             expCluster[CMem[i]-1][1]+=1;
            
     }

  for(int l=0; l<K; l++){
      System.out.print("  no. of vertices in cluster "+expCluster[l][1]);
      expresult+=((expCluster[l][0])/(expCluster[l][1]));
  }
 System.out.println();
 expresult=expresult/K;
 System.out.println("result of expansion="+expresult);
 CDSInterface.expresult=""+expresult;
}




}
