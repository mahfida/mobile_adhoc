/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import Support.Kmeans;
import Support.Inverse;
import Support.EigenValues;
import Jama.Matrix;

/**
 *
 * @author sarmad
 */
public class NeighborSpectralClustering {
 static double A[][]={{1,0.4,0.4,0.33,0,0,0,0,0.285,0.167,0.167,0,0,0},{0.4,1,0.5,0.4,0.143,0,0,0,0.143,0,0,0,0,0},
 {0.4,0.5,1,0.33,0.143,0,0,0,0.143,0,0,0,0,0},{0.33,0.4,0.33,1,0.33,0.143,0,0.167,0,0,0,0,0,0},
{0,0.143,0.143,0.33,1,0.33,0.4,0.4,0.125,0,0,0.143,0,0},{0,0,0,0.143,0.33,1,0.4,0.4,0,0,0,0.33,0.167,0.167},
{0,0,0,0,0.4,0.4,1,0.5,0.143,0,0,0.167,0,0},{0,0,0,0.167,0.4,0.4,0.5,1,0.143,0,0,0,0,0},
{0.285,0.143,0.143,0,0.125,0,0.143,0.143,1,0.6,0.6,0,0,0},{0.167,0,0,0,0,0,0,0,0.6,1,1,0,0,0},
{0.167,0,0,0,0,0,0,0,0.6,1,1,0,0,0},{0,0,0,0,0.143,0.33,0.167,0,0,0,0,1,0.75,0.75},
{0,0,0,0,0,0.167,0,0,0,0,0,0.75,1,1},{0,0,0,0,0,0.167,0,0,0,0,0,0.75,1,1}};

    static double D[][];
    static double DI[][];
    static double DIA[][];//inverse of degree matrix*Adjacency
    static double L[][];
    static double I[][];// Identity matrix of order szxsz
    static double U[][];
    static int K=5,sz=14;
    static double EVectors[][];
    static Matrix M;

    public static void main(String args[])
    {
    /*  A=new double[sz][sz];
        int i,j;
         for(i=0;i<sz;i++)
        {
            for (j = i; j < sz; j++)


                    A[i][j]=0;


        }

  //****************************************************


        A[0][3]=A[0][10]=A[0][11]=A[0][18]=A[0][19]=1;
        A[1][2]=A[1][16]=1;
        A[2][1]=A[2][16]=A[20][19]=1;
        A[3][0]=A[3][17]=A[3][19]=1;
        A[4][14]=A[4][12]=A[4][15]=1;
        A[5][14]=A[5][6]=A[5][13]=1;
        A[6][5]=A[6][12]=1;
        A[7][16]=A[7][8]=A[7][9]=1;
        A[8][7]=A[8][9]=1;
        A[9][7]=A[9][8]=1;
        A[10][0]=A[10][19]=1;
        A[11][0]=A[11][17]=1;
        A[12][4]=A[12][6]=1;
        A[13][5]=A[13][15]=1;
        A[14][4]=A[14][5]=A[14][19]=1;
        A[15][4]=A[15][13]=1;
        A[16][1]=A[16][2]=A[16][7]=1;
        A[17][3]=A[17][11]=1;
        A[18][20]=A[18][0]=1;
        A[19][0]=A[19][3]=A[19][2]=A[19][14]=1;
        A[20][18]=A[20][17]=1; */

        M=new Matrix(A);
        System.out.print("\nAdjacency Matrix\n");
        M.print(4, 0);
        createLaplacian(A);
    }

    public static void createLaplacian(double A[][])
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

        //Display
        System.out.print("\nDegree Matrix\n");
        M.print(4, 0);

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
         M=new Matrix(L);
         System.out.print("\nLaplaian Matrix\n");
         M.print(4, 0);

          eigenVector(L);
    }

    public static void eigenVector(double L[][])
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

    public static void kMeans(double U[][])
    {
        Kmeans km=new Kmeans();
        km.findClusters(U,K,sz);


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

}
