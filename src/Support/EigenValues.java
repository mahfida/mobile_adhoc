/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Support;

import Jama.Matrix;
import Jama.EigenvalueDecomposition;

public class EigenValues {
    Matrix V;
    Matrix D;
     int N;
   public double[] eigenValues()
   {
       double C[][];
       double U[]=new double[N];
       C=D.getArray();
       for(int i=0;i<N;i++)
       {
           U[i]=C[i][i];
       }
      return(U);
   }
   public double[][] eigenVectors()
   {
       double[][] EV=V.getArray();
       return EV;
   }
   public void calculateEigen(double[][] B, int sz) {
     N= sz;

      // create a symmetric positive definite matrix
      //Matrix A = Matrix.random(N, N);
      Matrix A= new Matrix(B);
      A = A.transpose().times(A);

      // compute the spectral decomposition
      EigenvalueDecomposition e = A.eig();
      V = e.getV();
      D = e.getD();

     
     // System.out.print("Eigen Vectors\n");
     // V.print(5, 3);

   
   }

}


