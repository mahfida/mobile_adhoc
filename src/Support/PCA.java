/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Support;
import java.util.Random;

/**
 *
 * @author sarmad
 */
public class PCA {
public static void main(String args[])
{
Random rand=new Random();
final int n=4;
double EValues[]=new double[2], EVectors[][]=new double[2][2],wo=0,w[][]=new double[n][n];
int i,j;
double fij[][]=new double[n][n];
double fmean=0,fd[][]=new double[n][n],fds[][]=new double[n][n],fstd=0,zij[][]=new double[(n)*(n-1)][2],cov[][]={{0,0},{0,0}};
double tij[][]=new double[n][n],tmean=0,td[][]=new double[n][n],tds[][]=new double[n][n],tstd=0,xbar=0,ybar=0,xitem[]=new double[n*(n-1)],yitem[]=new double[n*(n-1)];
System.out.print("\n\tEncounter Frequencies between node pairs\n\n");
for(i=0;i<n;i++)
for(j=0;j<n;j++)
{
	if(i!=j)
	{
                fij[i][j]=rand.nextDouble()*100;
		System.out.println("f("+(i+1)+","+(j+1)+"):"+fij[i][j]);
		fmean+=fij[i][j];
	}
}
fmean/=n*(n-1);
System.out.print("\n\tSum of Contact Duration between node pairs\n\n");
for(i=0;i<n;i++)
for(j=0;j<n;j++)
{
	if(i!=j)
	{
                tij[i][j]=rand.nextDouble()*200;
		System.out.println("t("+(i+1)+","+(j+1)+"):"+tij[i][j]);
                tmean+=tij[i][j];
	}
}
tmean/=n*(n-1);
int k=0;
System.out.print("\n\t(fij-fmean)\t(tij-tmean)\n\n");
for(i=0;i<n;i++)
for(j=0;j<n;j++)
{
	if(i!=j)
	{
		System.out.print(",fd("+(i+1)+","+(j+1)+"):");
		fd[i][j]=fij[i][j]-fmean;
		System.out.print(fd[i][j]);
		fds[i][j]=fd[i][j]*fd[i][j];
		fstd+=fds[i][j];


		System.out.print("\t,td("+(i+1)+","+(j+1)+"):");
		td[i][j]=tij[i][j]-tmean;
		System.out.print(td[i][j]);
		tds[i][j]=td[i][j]*td[i][j];
		tstd+=tds[i][j];
		System.out.print("\n");
	}
}



fstd=fstd/(n*(n-1)-1);	tstd=tstd/(n*(n-1)-1);
fstd=   (double) Math.sqrt(fstd);   	tstd=(double) Math.sqrt(tstd);
for(i=0;i<n;i++)
for(j=0;j<n;j++)
{
	if(i!=j)
	{
		zij[k][0]=fd[i][j]/fstd;   xbar+=zij[k][0];
		zij[k][1]=td[i][j]/tstd;   ybar+=zij[k][1];
		System.out.print("\nz("+i+","+j+"):"+zij[k][0]+","+zij[k][1]);
		k=k+1;
	}
}
xbar/=(n)*(n-1);	ybar/=(n)*(n-1);
for(j=0;j<(n)*(n-1);j++)
{
     xitem[j]=zij[j][0]-xbar;
     yitem[j]=zij[j][1]-ybar;
     cov[0][0]+=xitem[j]*xitem[j];	cov[0][1]+=xitem[j]*yitem[j];
     cov[1][0]+=yitem[j]*xitem[j];	cov[1][1]+=yitem[j]*yitem[j];
}
System.out.print("\n\nCovariance\n");
for(i=0;i<2;i++)
for(j=0;j<2;j++)
{
cov[i][j]/=n*(n-1)-1;
System.out.print("\ncov("+i+","+j+"):"+cov[i][j]);
}
EigenVector ev=new EigenVector(cov,2);
ev.EVMatrix();
EVectors=ev.getEVectors();
EValues=ev.getEValues();
System.out.print("\n\nEigen Values\n");
for(j=0;j<2;j++)
{
System.out.println("Eigen Value["+j+"]: "+EValues[j]);
}
double vmax=EValues[0];
int loc=0;
for(j=0;j<2;j++)
{
    if(EValues[j]>vmax) {loc=j; vmax=EValues[j];}
}
System.out.println("vmax: "+vmax+ ", loc:"+loc);
//w0
double w0[]=new double[2];
w0[0]=-fmean/fstd;  w0[1]=-tmean/tstd;
System.out.println("w0[0]: "+w0[0]+ ", w0[1]:"+w0[1]);
for(i=0;i<2;i++)
     {
       wo+=w0[i]*EVectors[i][loc];
    }
System.out.println("wo:"+wo);
//Obtaning weight that combines frequency component and contact duration
//of two nodes i and j into a single value
System.out.println("\n\nValue of Node pairs intraction w.r.t frequency and contact duration\n");
j=0;
for(i=0;i<n;i++)
    for(k=0;k<n;k++)
    {
        if(i!=k)
        {
        w[i][k]=EVectors[0][loc]*zij[j][0]+EVectors[1][loc]*zij[j][1]+wo;
        j=j+1;
        }

        else
        {
            w[i][k]=0;
        }
       System.out.println("w("+(i+1)+","+(k+1)+"):"+w[i][k]);
    }
    }
}
