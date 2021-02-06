/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Support;

public class Modularity {


    double A[][];
    int s[],i,j;
    double k[];
    double m=0;
    int sz;
    int a=0, b=0;
    double Q=0;
    double B[][];
    public Modularity(double[][] A,int sz)
    {
            this.A=A;
            this.sz=sz;
            


    }

public double vlaueOfModularity(int[] cluster){

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
                  if(cluster[i]==cluster[j])
                       Q+=A[i][j]-(k[i]*k[j])/(2*m);
                }
        Q=Q/(2*m);
        return Q;

}

}
