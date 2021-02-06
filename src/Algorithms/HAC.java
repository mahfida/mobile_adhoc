
package Algorithms;
import Support.Modularity;
import Jama.Matrix;

public class HAC implements BaseCD {
int [][]C;// Possible number of Clusters and their members
int I[], stopIndex[];
double A[][];//The changing similarity matrix
double O[][];//Original Similarity Matrix
static Matrix M;
int k;
int sz;
int clustermembers[][];
int lastCluster=1;
double result,expresult=0;
char scheme;
double B[][];
int cMem[]; //Storing the cluster number of a node
double expCluster[][];
double condCluster[][];
double Conductance[][];
public HAC(double [][]A, int k, int sz,char scheme )
    {
   this.scheme=scheme;
   this.k=k;
   this.sz=sz;
   this.A=new double[sz][sz];
   B=new double[sz][sz];
   for(int i=0; i<sz; i++){
                for(int j=0; j<sz; j++){
                this.A[i][j]=A[i][j];
                B[i][j]=A[i][j];
                }
        }
   cMem=new int[sz];
   
   Conductance=new double[sz][2];
   I=new int[sz]; //Keep track of active clusters
   stopIndex=new int[sz]; //Number of nodes in a cluster
   C=new int[sz][sz];
    clustermembers=new int[sz][sz];
   O=new double[sz][sz];
   condCluster=new double[k][2];
   expCluster=new double[k][2];
    for(int i=0;i<sz;i++)
        { C[i][0]=i+1;
          I[i]=1;// Each node is a separate cluster
          stopIndex[i]=0;
        }
  
    
     System.out.println("\n\n\nHIERARCHICAL AGGLOMERATIVE CLUSTERING\n");
 }

////////////////////////////////////////////////////////////////////////////////
//Display Smilarity Matrix

public void displayMatrix()
{
    //Display resulting similarity matrix
    M=new Matrix(A);
    System.out.print("\nCurrent Matrix\n");
    M.print(4, 0);
}
////////////////////////////////////////////////////////////////////////////////
//Find CLusters
public void runClustering()
{
     //New matrix after merging

   A=neighborSimilarity(A,sz);
   for(int i=0; i<sz;i++){
     for(int j=0; j<sz;j++)  {
      O[i][j]=A[i][j]; }}//Assigning changing marix to the original one
     runHAC();
}
////////////////////////////////////////////////////////////////////////////////
public void runHAC(){
    double min=1000;
    int index1=0,index2=0;
    for(int i=0;i<sz-1;i++)
        {
            for (int j = i+1; j<sz; j++)
            {
                  if((A[i][j]<min)&& I[i]==1 && I[j]==1)
                        {
                      min=A[i][j];
                      index1=i;index2=j;
                      }
            }
        }
    
    //Merging the members the two clusters
    int i=0;
    while (i<=stopIndex[index2])
    {

        stopIndex[index1]+=1;
        C[index1][stopIndex[index1]]=C[index2][i];
        i=i+1;
    }
    //Deactivae cluster col
    I[index2]=0;

    //Updating the row and column vector of row.
    switch(scheme)
    {
        //Singe Linkage Scheme
        case 'S':
            for(i=0;i<sz;i++)
            {

                         if(A[index1][i]>A[index2][i])
                         A[index1][i]=A[i][index1]=A[index2][i];


            }
            break;
        //Complete Linkage Scheme
        case 'C':
                   for(i=0;i<sz;i++)
                    {

                         if(A[index1][i]<A[index2][i])
                         A[index1][i]=A[i][index1]=A[index2][i];


                    }
        break;
        //Group-Average Linkage Scheme
        case 'A':
                  for(i=0;i<sz;i++)
                   {

                         if(A[index1][i]>A[index2][i])
                         A[index1][i]=A[i][index1]=A[index2][i];


                    }
        //Centriod Linkage Scheme
        break;
            default:
                  for(i=0;i<sz;i++)
                    {
                        int stop=0;
                        double sum=0;
                        while(stop<=stopIndex[index1])
                        {
                            sum+=O[C[index1][stop]-1][i];
                         //   System.out.print("\n>"+(index1+1)+"-"+C[index1][stop]+": sum="+sum+", stop:"+stop);
                            stop++;
                        }
                        
                        //Storing Centriod
                        A[index1][i] = A[i][index1] = sum/((double)stop);
                   }

    }
       
    //Count number of clusters
    int countClusters=0;
     for(int j=0; j<sz; j++)
            if(I[j]==1)
            {
                countClusters+=1;
            }
    //Display Clusters or run HAC again
       if(countClusters>k)
        runHAC();
       else
       displayClusters(C,I);
      findModularity(cMem);
}

////////////////////////////////////////////////////////////////////////////////
  //Display clusters
    public int displayClusters(int [][]C,int[] I){
        int countClusters=0;int l=0;
        String y="",t="";

        System.out.println("Clusters");
        for(int i=0; i<sz; i++){
            if(I[i]==1)
            {
                countClusters+=1;
                t+=(countClusters);
                CDSInterface.rowNo[CDSInterface.r][CDSInterface.c++]=t;
                System.out.print("\n" + ( countClusters) + ":");
                for(int j=0; j<sz; j++){
                                     if(C[i][j]!=0)
                                     {
                                      System.out.print((int) (C[i][j]) + "  ");
                                      y+="  "+C[i][j];
                                      cMem[l++]=countClusters;
                                      CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]=y;
                    }
                                        }
                CDSInterface.r++;
          CDSInterface.c=0;
          y="";t="";
            }


         
        }
        return countClusters;

    }

////////////////////////////////////////////////////////////////////////////////
//Find neighbor Similarity Matrix
public double[][] neighborSimilarity(double[][] A, int sz)
{
    double NS[][]=new double[sz][sz];
/*A=new double[sz][sz];
for(int i=0;i<sz;i++)
        {
            for (int j = 0; j<sz; j++)
                    A[i][j]=0;
        }
A[0][0]=A[0][1]=A[0][2]=A[0][10]=1;
A[1][1]=A[1][0]=A[1][3]=1;
A[2][0]=A[2][3]=A[2][2]=1;
A[3][2]=A[3][3]=A[3][4]=1;
A[4][3]=A[4][4]=A[4][5]=A[4][6]=1;
A[5][5]=A[5][11]=A[5][4]=1;
A[6][4]=A[6][6]=A[6][11]=1;
A[7][7]=A[7][8]=A[7][9]=1;
A[8][8]=A[8][7]=A[8][10]=1;
A[9][9]=A[9][7]=A[9][10]=1;
A[10][10]=A[10][9]=A[10][8]=A[10][0]=1;
A[11][11]=A[11][5]=A[11][6]=1;*/

double sum=0,total=0; double avg=0;
for(int k=0; k<sz; k++){
for(int i=0;i<sz;i++)
        {
            for (int j = 0; j<sz; j++){
                if(k!=i){
                   if((A[k][j]==1)&&(A[i][j]==1))
                       sum+=1;
                      if((A[k][j]==1)||(A[i][j]==1))
                         total+=1;


                   }
                }
            
           if(i==k)
               NS[k][i]=0;
           else
               NS[k][i]=(1-(sum/total));
         
            System.out.print(NS[k][i]+" ");

            sum=0;
            total=0;
            avg=0;

        }
        System.out.println();
    }
    return NS;
    }//End of method
//*****************************************************************************
 public void findModularity(int []cMem)
{
    int i;
//Converting A in an Adjacency Matrix

//Find Quality of Partitions
Modularity md=new Modularity(B,sz);
//System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
CDSInterface.modresult=""+md.vlaueOfModularity(cMem);
if(scheme=='S')
CDSInterface.algoresult="HAC/SINGLE LINKAGE";
    else if(scheme == 'C')
CDSInterface.algoresult="HAC/COMPLETE LINKAGE";
    else
CDSInterface.algoresult="HAC/CENTRIOD LINKAGE";

CDSInterface.comresult="O(n power 2)";
findConductance();
findExpansion();
}
public void findConductance()
{
 double boundaryEdges=0;;

 int clusterno=0;
 double allEdges=0;
 double insideEdges=0;

 for(int i=0; i<sz; i++){
     for(int j=0; j<sz; j++)
     {
           if(B[i][j]==1){
               if(clustermembers[i][j]==(j+1))
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
             
             condCluster[cMem[i]-1][0]+=Conductance[i][0];
             condCluster[cMem[i]-1][1]+=Conductance[i][1];


     }

  for(int l=0; l<k; l++){
  //    System.out.print("boundary edges "+condCluster[l][0]+"  "+"all edges "+condCluster[l][1]);
      result+=((condCluster[l][0])/(condCluster[l][1]));
  }
 result=result/k;
 System.out.println();
// System.out.println("result="+result);
 CDSInterface.condresult=""+result;
}
public void findExpansion()
{
 double boundaryEdges=0;
 int clusterno=0;

 double insideEdges=0;

 for(int i=0; i<sz; i++){
     for(int j=0; j<sz; j++)
     {
           if(B[i][j]==1){
               if(clustermembers[i][j]==(j+1))
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
expCluster=new double[k][2]; clusterno=0; lastCluster=1;
     for(int i=0; i<sz; i++){

             expCluster[cMem[i]-1][0]+=Conductance[i][0];
             expCluster[cMem[i]-1][1]+=1;

     }

  for(int l=0; l<k; l++){
   //   System.out.print("  no. of vertices in cluster "+expCluster[l][1]);
      expresult+=((expCluster[l][0])/(expCluster[l][1]));
  }
 System.out.println();
 expresult=expresult/4;
 //System.out.println("result of expansion="+expresult);
 CDSInterface.expresult=""+expresult;
}



    }// End of class

