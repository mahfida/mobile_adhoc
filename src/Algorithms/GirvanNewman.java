/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import Support.Modularity;


public class GirvanNewman implements BaseCD {

        static double S[][];
        double B[][];
        double A[][];
        int size;
        static double sum;
        static double Q[][];
        int Node[];
        static int  clusterNo;
        static int clustermembers[][];
        int k;
        double Sigma[][];//Storing number of Shortest Paths
        double Distance[][];//Storing shortest Distance
        double Parent[][];//Storing parent nodes information
        double max=-1;
        WarshallCD wCD;
        int cMem[]; //Storing the cluster number of a node
        int lastCluster=1;
        double result,expresult=0;
        
        double expCluster[][];
        double condCluster[][];
        double Conductance[][];
        public GirvanNewman(double [][] A, int s, int k)
        {
            //super(A,s,k);
            B=new double[s][s];
            this.A=new double[s][s];
            Conductance=new double[s][2];
            for(int i=0; i<s; i++){
                for(int j=0; j<s; j++){
                     B[i][j]=A[i][j];
                }
            }
            for(int i=0; i<s; i++){
                for(int j=0; j<s; j++){
            this.A[i][j]=A[i][j];
                }
            }
            this.size=s;
            this.k=k;// Required number of clusters
            condCluster=new double[k][2];
            expCluster=new double[k][2];
         }
        public void runClustering()
        {
            System.out.println("\n\n\nGIRVAN AND NEWMAN EDGE BETWEENNESS\n");
            Dijkstra();
        }
        public void Dijkstra()
        {
            int n=0;
            double min,solved=size+1, m=0,parent=0;
            Q=new double[3][size];// Priority Queue
            Sigma=new double[size][size];
            Node=new int[size];
            Distance=new double[size][size];
            Parent=new double[size][size];
        
         /////////////////////////////////////////////////

        ////////// Queue of vertices  //////////////
        for(int b=0; b<size; b++)//finding shortest distance from every node to every node
        {
             for (int i = 0; i < size; i++) {
             Q[0][i]=i+1;
        }
        ////////////////  Queue of costs of vertices  //////////////

        for(int i=0; i<size; i++)
           Q[1][i]=1000;//initially cost to other nodes is not known which is shown by 1000
         Q[1][b]=0;//cost to self is zero
         /////////////////  Queue of parent of vertices  ////////
        for(int i=0; i<size; i++)
           Q[2][i]=0;
          /////////////////////////////////////////////

         //Finding the node in queue havind minimum cost

          for(int j=0;j<size;j++)
           {
               min=1000;
                for(int i=0;i<size;i++)
                {
               //Extracting node from priority queue having lowest cost
                   if((min>Q[1][i])&&(Q[0][i]!=solved))
                   {
                       min=Q[1][i];

                       m=Q[0][i];
                       n=i;
                    }
                }


                parent=Q[0][n];
                Q[0][n]=solved; //Extract the node

                 sum=0;

                 for(int k=0;k<size;k++) //Adjacency List of current node
                  {

                     if ((A[(int) (m) - 1][k] > 0) && (Q[0][k] != solved))
                     {
                        sum=Q[1][n]+A[(int)(m)-1][k];
                        if(Q[1][k]>sum)
                         {
                            Q[1][k] = sum; //Update with smaller cost
                            Q[2][k]=parent;//Parent node
                         }
                      }


                  }

          }
 
           ////////////////////////////////////////////////

                    //Fill b number row of matrices Sigma, Parent and Distance
                    for(int h=0; h<size; h++)
                    {
                        if(Q[1][h]<1000)
                        {
                           Sigma[b][h]=1;
                           Parent[b][h]=Q[2][h];
                           Distance[b][h]=Q[1][h];
                        }
                        else
                        {
                            Sigma[b][h] = 0;
                            Parent[b][h]=0;
                            Distance[b][h]=1000;
                        }

                  }

     }

           /////////////////////////////////////////////
           //Finding the number of shortest paths between any two nodes
             for(int i=0; i<size; i++)
                  for(int j=0; j<size; j++)
              {
                  if(sum!=1000)
                      Sigma[i][j]=1;
                  else
                      Sigma[i][j]=0;
              }
      
        /////////////////////////////////////////////////////
        // Finding the shortest distance matrix
              for(int j=0; j<size;j++)
                    Distance[j][j]=0;

        calculateBetweenness(Sigma,Parent,Distance);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////
              //Finding Betweenness
 public void calculateBetweenness(double [][]S, double [][]P, double [][]D)
 {
        double [][]sumEdgeBC=new double[size][size];
        double [][]sumEdgeBCT=new double[size][size];
        double [][][]Anew=new double[size][size][size];
        double [][][]edgeBC=new double[size][size][size];
      
        for(int i=0; i<size; i++)//storing data in Anew
        {
            for(int j=0; j<size; j++){
                if(P[i][j]!=0)
                Anew[i][j][(int)(P[i][j])-1]=1;

            }

           }
     
        max=-1; int col=0,ind=0;
        for(int i=0; i<size; i++)// storing data in edgeBC
        {
            for(int j=0; j<size; j++)
            {
                max=-1;
                for(int k=0; k<size; k++){//picking ind
                    if(D[i][k]>max){
                        max=D[i][k];
                        ind=k;
                    }
                    }
               //     System.out.print("max: "+max+","+ind);

                 D[i][ind]=-1;//removing the extracted value
                 for(int l=0; l<size; l++)//picking col
                 {
                     if(Anew[i][ind][l]==1){
                         col=l;
                         break;
                     }
                 }
                 boolean leaf=true;
                 for(int l=0; l<size; l++){//finding weather ind node is leaf or not
                      if(Anew[i][l][ind]!=0){
                          leaf=false;
                          break;
                      }
                 }
             //////////////////////////////////////
                 //if ind is leaf node then betweenness is as below
                 if(ind==col)
                     edgeBC[i][ind][col]=0;//Head node
                else
                     if(leaf == true){
              edgeBC[i][ind][col]=S[i][col]/S[i][ind];

                    }
             /////////////////////////////////////
              //if ind is not a leaf node, use edge betweenness of adjacent nodes

              else if(leaf==false){
                     double sum=0;
                     for(int a=0; a<size; a++)//find adjacent nodes in adjacency matrix
                     {
                        if(A[ind][a]!=0){
                        sum+=edgeBC[i][ind][a]+edgeBC[i][a][ind];
                                        }
                     }
                    edgeBC[i][ind][col]=(1+sum)*S[i][col]/S[i][ind];
                    sum=0;

                                   }


            }

           }


             for(int i=0; i<size; i++)// number of  Anew and edgeBC matrices
             {

            for(int j=0; j<size; j++)
            {
                for (int k = 0; k < size; k++){

                        sumEdgeBC[j][k]+=edgeBC[i][j][k];
                 }
           }

             }

         //Sum of Edge Betweenness
          for(int j=0; j<size; j++)
            {
                for (int k = 0; k < size; k++){
                    sumEdgeBCT[j][k]=sumEdgeBC[k][j];
      
                 }
              }
  ////////////////////////////////////////////////////////////////////////////
             //deleting edge of highest betweeness
     
       //Edge with highest betweeness
           max=-1;
           for(int j=0; j<size; j++) //Finding out maximum value
             for (int k = 0; k < size; k++)
                    if(sumEdgeBCT[j][k]>max){
                                            max=sumEdgeBCT[j][k];
                                          }
           
        //Find out the edges whose value is equal to maximum
             for(int j=0;j<(size);j++)
                 for(int k=0;k<size;k++)
                 {
         
                     if (sumEdgeBCT[j][k] == max)
                     {
                       A[j][k]=A[k][j]=0;
                   
                     }
                 }
               

//Since the edge with highest betweenness has been removed the graph splits and converts to a
// disconnected graph. A disconnected graph can be better clustered via WarshallCD
wCD=new WarshallCD(A,size); //Showing the clusters after removing the edge with highest edge betweenness
int numClusters=wCD.runWarshallCD();
//If present number of cluster are less than the required find repeat the process again
    if(numClusters<k)
    {
        Dijkstra();
    }
 else
    {
     cMem=wCD.displayClusters();
     findModularity(cMem);
    }

}
//*****************************************************************************
 public void findModularity(int []cMem)
{
    int i;
//Converting A in an Adjacency Matrix

//Find Quality of Partitions
Modularity md=new Modularity(B,size);
//System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
CDSInterface.modresult=""+md.vlaueOfModularity(cMem);
CDSInterface.algoresult="GirvanNewman";
CDSInterface.comresult="O(n power 3)";
findConductance();
findExpansion();
}
public void findConductance()
{
 double boundaryEdges=0;;

 int clusterno=0;
 double allEdges=0;
 double insideEdges=0;

 for(int i=0; i<size; i++){
     for(int j=0; j<size; j++)
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
 
     for(int i=0; i<size; i++){
        
             condCluster[cMem[i]-1][0]+=Conductance[i][0];
             condCluster[cMem[i]-1][1]+=Conductance[i][1];
             
               
     }
  
  for(int l=0; l<k; l++)
           result+=((condCluster[l][0])/(condCluster[l][1]));
  
 result=result/k;
 System.out.println();
 CDSInterface.condresult=""+result;
}
public void findExpansion()
{
 double boundaryEdges=0;
 int clusterno=0;

 double insideEdges=0;

 for(int i=0; i<size; i++){
     for(int j=0; j<size; j++)
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
     for(int i=0; i<size; i++){
         
             expCluster[cMem[i]-1][0]+=Conductance[i][0];
             expCluster[cMem[i]-1][1]+=1;
            
     }

  for(int l=0; l<k; l++)
      expresult+=((expCluster[l][0])/(expCluster[l][1]));

 expresult=expresult/4;
 CDSInterface.expresult=""+expresult;
}




}//end of class



