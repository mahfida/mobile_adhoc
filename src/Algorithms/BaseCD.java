/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import Jama.Matrix;

/**
 *
 * @author Abida
 */
public interface BaseCD {
    public void runClustering();
 //Matrix M;
 //double A[][];
 /*={{0,1,1,0,0,0,0,0,0,0,0,0,0,0},{1,0,1,0,0,0,0,0,0,0,0,0,0,0},{1,1,0,0,0,0,1,0,0,0,0,0,0,0},
 {0,0,0,0,1,1,0,0,0,0,0,0,0,0},{0,0,0,1,0,1,0,0,0,0,0,0,0,0},{0,0,0,1,1,0,1,0,0,0,0,0,0,0},{0,0,1,0,0,1,0,1,0,0,0,0,0,0},
 {0,0,0,0,0,0,1,0,1,0,0,1,0,0},{0,0,0,0,0,0,0,1,0,1,1,0,0,0},{0,0,0,0,0,0,0,0,1,0,1,0,0,0},{0,0,0,0,0,0,0,0,1,1,0,0,0,0},
 {0,0,0,0,0,0,0,1,0,0,0,0,1,1},{0,1,1,0,0,0,0,0,0,0,0,1,0,1},{0,1,1,0,0,0,0,0,0,0,0,1,1,0}};*/

  /*  int size,k; //Rows and colums of matrix and number of clusters
    public BaseCD(double [][]A, int size, int k)
    {
        //Display resulting similarity matrix
        this.A=A;
        this.k=k;
        this.size=size;
        M=new Matrix(A);
        System.out.print("\nAdjacency Matrix\n");
        M.print(4, 0);
    }
}

/*

    // Normalized Random Spectral clustering
        NRSpecClustering nrs=new NRSpecClustering(A,size,k);
        nrs.runNRSpectralClustering();

       // Normalized Symmetric Spectral clustering
        NSSpecClustering nss=new NSSpecClustering(A,size,k);
        nss.runNSSpecClustering();

        // Un Normalized Spectral clustering
        USpecClustering us=new USpecClustering(A,size,k);
        us.runUSpecClustering();

         //  For Girvan and Newman
        GirvanNewman gn=new GirvanNewman(A,size,k);
        gn.runGirvanNewman();

        //Agglomerative Clustering
        HAC ac= new HAC(k, size);
        ac.runHAC('T');

        //Similarity Neighborhood CD
        simNeighborhoodCD sn=new simNeighborhoodCD(A,size);
        sn.runSimNeighborhood();
    }*/




}


