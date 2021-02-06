/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Support;

/**
 *
 * @author sarmad
 */
public class EigenVector {
double[][] A;
double EValues[];
double t, c, s;
int p, q, icount, state, size = 4;
double tol = 1.e-5; // the tolerance level of convergence
int icmax = 10; // the maximum iterations number
double[][] V, diagD;
//******************************************************************************
public EigenVector(double A[][],int s)
{
  this.A=A;
  size=s;
  V= new double[size][size];
  diagD=new double[size][size];
}
//******************************************************************************
public void EVMatrix() {

int[] colRowOfElMax = new int[size], rowOfElMax = new int[1];
double[][] temp = new double[size][size], D = new double[size][size];
EValues=new double[size];
double[] maxElColRow = new double[size], maxElRow = new double[1];
double[][] dMinusDiagD = new double[size][size], absDminusDiagD = new double[size][size];
double[][] rot = new double[2][2], rotT = new double[2][2];    // makes V into a unit matrix

for (int i = 0; i < size; i++) {
    for (int j = 0; j < size; j++) {
        V[i][j] = 0;
        }
        V[i][i] = 1.0;
        }
        D = A; // copies A to D
        diagD = diag(D, size);// outputs DiagD=diagonal of D
        dMinusDiagD = minus(D, diagD, size); // does D-DiagD
        abs(dMinusDiagD, absDminusDiagD, size);// does abs(D-DiagD)
        maxMatrix(absDminusDiagD, size, colRowOfElMax, maxElColRow);
        maxVector(maxElColRow, size, rowOfElMax, maxElRow);
        q = rowOfElMax[0];
        p = colRowOfElMax[q];
        icount = 0;
        state = 1;    // Iterations
        while (state == 1 && icount < icmax) {
            icount = icount + 1;
                if (D[q][q] == D[p][p]) { // check to prevent t from diverging
                    D[q][q] = D[p][p] + 1.e-10;
                    }
                    t = D[p][q] / (D[q][q] - D[p][p]);
                    c = 1 / Math.sqrt(t * t + 1);
                    s = c * t;
                    rot[0][0] = c;
                    rot[0][1] = s;
                    rot[1][0] = -s;
                    rot[1][1] = c;
                    transpose(rot, rotT, 2);// rotT=transpose(Rot)
                    for (int i = 0; i < size; i++) {
                        temp[p][i] = rotT[0][0] * D[p][i] + rotT[0][1] * D[q][i];
                        temp[q][i] = rotT[1][0] * D[p][i] + rotT[1][1] * D[q][i];
                        D[p][i] = temp[p][i];
                        D[q][i] = temp[q][i];
                        }
                        for (int i = 0; i < size; i++) {
                            temp[i][p] = D[i][p] * rot[0][0] + D[i][q] * rot[1][0];
                            temp[i][q] = D[i][p] * rot[0][1] + D[i][q] * rot[1][1];
                            D[i][p] = temp[i][p];
                            D[i][q] = temp[i][q];
                            }
                            for (int i = 0; i < size; i++) {
                                temp[i][p] = V[i][p] * rot[0][0] + V[i][q] * rot[1][0];
                                temp[i][q] = V[i][p] * rot[0][1] + V[i][q] * rot[1][1];
                                V[i][p] = temp[i][p];
                                V[i][q] = temp[i][q];
                                }      // find the new q, p element array values that need to be changed
                                diagD = diag(D, size); // outputs diagD=diagonal of D
                                dMinusDiagD = minus(D, diagD, size); // does D-DiagD
                                abs(dMinusDiagD, absDminusDiagD, size); // does abs(D-DiagD)
                                maxMatrix(absDminusDiagD, size, colRowOfElMax, maxElColRow);
                                maxVector(maxElColRow, size, rowOfElMax, maxElRow);
                                q = rowOfElMax[0];
                                p = colRowOfElMax[q];
                                if (Math.abs(D[p][q]) < tol * Math.sqrt(sumDiagElSq(diagD, size)) / size) {
                                    state = 0;
                                    }
                                    }   
                                        // diagD are eigen values
                                 
                                    for ( int i = 0; i < size; i++) 
                                        for (int j = 0; j < size; j++) {
                                            if(i==j) EValues[i]=diagD[i][j];
                                          
                                            }
                                            
                                            
                    
   }
//******************************************************************************
public double[][] getEVectors()
    {
    return V;
    }
//******************************************************************************
public double[] getEValues()
        {
            return EValues;
        }
//******************************************************************************
// finds the diagonal elements of A and puts them into B   *    * @param A   * @param n   * @return
    public static double[][] diag(double A[][], int n) {
        double[][] B = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = 0;
                }
                B[i][i] = A[i][i];
                }
                return B;
                }  /**   * C = A - B   *    * @param A   * @param B   * @param n   * @return C   */
                public static double[][] minus(double A[][], double B[][], int n) {
                    double[][] C = new double[n][n];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            C[i][j] = A[i][j] - B[i][j];

                            }
                            }
                            return C;
                            }
/**   * Finds the absolute value of a matrix   *    * @param A   * @param B   * @param n   */
public static void abs(double A[][], double B[][], int n) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            B[i][j] = Math.abs(A[i][j]);

            }
            }
            }
/**   * finds the maximum elements of each column; returns the maximums in Max and   * their array positions in Row   *    * @param A   * @param n   * @param Row   * @param Max   */
public static void maxMatrix(double A[][], int n, int Row[], double Max[]) {
    for (int i = 0; i < n; i++) {
        int k = 0;
        Max[i] = A[k][i];
        Row[i] = k;
        for (int j = 0; j < n; j++) {
            if (A[j][i] > Max[i]) {
                Max[i] = A[j][i];
                Row[i] = j;
                }
                }
                k = k + 1;
                }
                }  /**   * finds the maximum elements of a column of A; returns the maximum of a   * column as Max and its array position as Row   *    * @param A   * @param n   * @param Row   * @param Max   */
                public static void maxVector(double A[], int n, int Row[], double Max[]) {
                    Max[0] = A[0];
                    Row[0] = 0;
                    for (int i = 0; i < n; i++) {
                        if (A[i] > Max[0]) {
                            Max[0] = A[i];
                            Row[0] = i;
                            }
                            }
                            }
        /**   * finds the transpose of A and puts it into B   *    * @param A   * @param B   * @param n   */
        public static void transpose(double A[][], double B[][], int n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    B[i][j] = A[j][i];
                    }
                    }
                    }
/**   * finds the sums of the squared of the diagonal elements of A   *    * @param A   * @param n   * @return   */
public static double sumDiagElSq(double A[][], int n) {
    double sum = 0;    for (int i = 0; i < n; i++) {
        sum = A[i][i] * A[i][i] + sum;
        //System.out.print(sum);
        }
        return sum;
        }
public void displayEVectorsNValues()
        {
      //  Displaying V is the eigen vectors
                       System.out.println("\n\nJacobi Eigen Vectors\n");
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++)
                            {
                                System.out.print(V[i][j] + "\t");
                            }
                        System.out.println("");
                        }
                        // diagD are eigen values
                    System.out.println("\n\nJacobi Eigenvalue");
                    for ( int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++)
                            {
                                if(i==j)
                                {
                                    EValues[i]=diagD[i][j];
                                    System.out.print(diagD[i][j] + "\t");
                                }
                            }
                            
                        }
}
}

