
package Algorithms;
import Support.Modularity;
import Jama.Matrix;

public class HACGA implements BaseCD {
int [][]C;// Possible number of Clusters and their members
int I[], stopIndex[];
double A[][];//The changing similarity matrix
double O[][];//Original Similarity Matrix
static Matrix M;
int k;
int sz;
int clusters[][];
int tA[];
int tB[];
int clustermembers[][];
int lastCluster=1;
double result,expresult=0;
char scheme;
double B[][];
int cMem[]; //Storing the cluster number of a node
double expCluster[][];
double condCluster[][];
double Conductance[][];
public HACGA(double [][]A, int k, int sz,char scheme )
    {
   this.scheme=scheme;
   this.k=k;
   this.sz=sz;
   clusters=new int[sz][3];
   tA=new int[sz];
   tB=new int[sz];
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
          I[i]=i;// Each node is a separate cluster with its name
          stopIndex[i]=0;
          clusters[i][0]=i+1;//entering nodes IDs
          clusters[i][1]=0;//every cluster has initially only one node
          clusters[i][2]=1;//every node is in a cluster having id similar to it
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
    for(int i=0;i<sz-1;i++){
        for(int j=i+1; j<sz; j++){
          if((clusters[i][2]==1)&&(clusters[j][2]==1)){
           //finding out nodes in clusters[i]
             int a=clusters[i][0],m=0;
            do{
                  tA[m++]=a;
                  a=clusters[a-1][1];
             }
              while(a>0);
           //finding out nodes in clusters[j]
             int b=clusters[j][0],n=0;
            do{
                  tB[n++]=b;
                  b=clusters[b-1][1];
             }
              while(b>0);
           //finding group average between cluster[i] and clusters[j]
           double GA=0;
           for(int p=0; p<m; p++){
               for(int u=0; u<n; u++){
                  GA+=A[tA[p]-1][tB[u]-1];
               }
           }

              GA=GA/(m*n);
              if(min>GA){
                  min=GA;
                  index1=i;
                  index2=j;
              }
            
          }
        }
    }
     //updating members of clusters
    int w=index1+1;
         while(clusters[w-1][1]!=0){
            w=clusters[w-1][1];//finding out the address of node to be added
           
             }
    clusters[w-1][1]=index2+1;
    clusters[index2][2]=0;//removing index2 node from its clusters



    //Count number of clusters
    int countClusters=0;
     for(int j=0; j<sz; j++)
            if(clusters[j][2]==1)
            {
                countClusters+=1;
            }
    //Display Clusters or run HAC again
       if(countClusters>k)
        runHAC();
       else
       {
           displayClusters();
      findModularity(cMem);}
    }

////////////////////////////////////////////////////////////////////////////////
  //Display clusters
    public void displayClusters(){
   String y="",t="";
int c=1; int l=0;
        System.out.println("Clusters");
        for(int i=0; i<sz; i++){

            if(clusters[i][2]==1){
                        System.out.println("Cluster: "+(c++));
                         t+=c-1;
                        CDSInterface.rowNo[CDSInterface.r][CDSInterface.c++]=t;
                        int a=clusters[i][0];
                       do
                       {
                             System.out.println(clusters[a-1][0]);
                             //Displaying node in table
                          //  if(clusters[a-1][1]!=0)
                            //{
                                y += "  " + clusters[a - 1][0];
                              CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]=y;//}

                            //Updating a
                            a=clusters[a-1][1];
                             cMem[l++]=c-1;

                       }
                        while (a!=0);
            CDSInterface.r++;
            CDSInterface.c=0;
            y="";t="";
            }

            }


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
M=new Matrix(A);
System.out.print("\nAdjacency Matrix\n");
M.print(4, 1);
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
CDSInterface.algoresult="HAC/GROUP-AVERAGE LINKAGE";
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
