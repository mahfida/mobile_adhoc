
package Algorithms;

import Support.Modularity;
import java.util.HashSet;
import java.util.Random;

public class KLAlgorithm_1 {
       //static int A[][]={{0,1,0,0,2,0},{1,0,2,2,3,0},{0,2,0,3,0,1},{0,2,3,0,2,2},{2,3,0,2,0,0},{0,0,1,2,0,0}};//Adjacency Matrix
   static int A[][]={{0,1,2,3,2,4},{1,0,1,4,2,1},{2,1,0,3,2,1},{3,4,3,0,4,3},{2,2,2,4,0,2},{4,1,1,3,2,0}};//Adjacency Matrix
   /* static int A[][]={{0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,0},{1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0},{1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
 {0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,0},{0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0},{0,0,0,1,1,0,1,0,0,0,0,0,0,0,1,0},{0,0,1,0,0,1,0,1,0,0,0,0,0,0,1,0},
 {0,0,0,0,0,0,1,0,1,0,0,1,0,0,1,0},{0,0,0,0,0,0,0,1,0,1,1,0,0,0,1,0},{0,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0},{0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0},
 {0,0,0,0,0,0,0,1,0,0,0,0,1,1,1,0},{0,1,1,0,0,0,0,0,0,0,0,1,0,1,0,0},{0,1,1,0,0,0,0,0,0,0,0,1,1,0,1,0},{0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0},
 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};*/
       //static int A[][];
       static int D[];//D value of all nodes(D=E-I)
       static double B[][];
       static int Inter[][];
       static int Exter[][];
       static int EB[][];
       static int IB[][];
       static double condCluster[][];
       static double expCluster[][];
       int CMem[];
       static double Conductance[][];
      // static int size=16,k;
       static int k=4;
       static int clusters[][];
       static int clusterCount=-1;
       static int superGain=0;
       static boolean F[];//for unique random number generation
       static boolean P[];//for one time pass
       static int R[],Gain[],GainPair[][];//R is for Random numbers, array of Gain and array of GainPair
       static int randomInt=0;
       static int findg=0;
       static int Diff[][];
       static int swappedPair[][], stop=0;
       static int Anodes[];
       static int oldsize=6;
      public KLAlgorithm_1(int [][] A, int s, int k){
           //super(A,s,k);
           // this.A=A;
           // this.size=s;
           // this.k=k;// Required number of clusters
          

       }
       public static void main(String args[]){
       //public void runClstering(){
         // A=new int[size];
           int size=6;
           int n=1;
           int pow=2;
           while(pow<size)
           {
               pow=(int) Math.pow(2, n);
               n=n+1;
           }
           size=pow;
           System.out.println(size);
           //Update number of nodes to power of 2
           int TA[][]=new int[size][size];
           for(int i=0;i<size;i++)
               for(int j=0;j<size;j++)
                   if(i< oldsize && j< oldsize)
                   TA[i][j]=A[i][j];
                   else
                       TA[i][j]=0;
           //update adjacency
           A=TA;
                 Diff=new int[size][2];
                 swappedPair=new int [size][size];
          for(int i=0;i<size;i++)
              for(int j=0;j<size;j++) swappedPair[i][j]=0;
           EB=new int[size][2];
           IB=new int[size][2];
           int c=differentClusters(k);
           System.out.println(c);
           clusters=new int[c][size];
           Anodes=new int[size];
           for(int i=0;i<size;i++)
            Anodes[i]=i;


            for(int i=0;i<c;i++)
            {   
                for (int j = 0; j < size; j++)
                 {
                  clusters[i][j]=-1;
                 }
           }
           P=new boolean[size];
           int Aadj[][]=new int[size][size];
           for(int i=0;i<size;i++)
           {
               {
                   for (int j = 0; j < size; j++)
                   Aadj[i][j]=A[i][j];}IB[i][0]=i;EB[i][0]=i; Diff[i][0]=i;}
           function(Aadj,Anodes,size);
           displayClusters(size);
           B=new double[size][size];
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    B[i][j]=A[i][j];
                }
        }
    }
       public static int differentClusters(int k)
       {
         int count=0;
         count=k;
         do
         {
         k=k/2;
         if(k==1) break;
         else
             count+=k;
         }
         while(k>1);
          return count;
       }
       public static void function(int [][]Aadj, int []Anodes, int size){
            int K=2;
            int []Cnodes=new int[(int)(size/2)];
            int []OC=new int[(int)(size/2)];
            int []Bnodes=new int[(int)(size/2)];
            int []OB=new int[(int)(size/2)];
        
       //*********************************************************************
             F=new boolean[size];
           for(int j=0; j<size; j++){
              F[j]=false;
             // System.out.print("\nFlag of "+j+" "+F[j]);
            }

    //************************************************************************
        R=new int[size];
        R=randomNo(size);
 
          for(int i=0; i<size; i++){
              
          }
          //***********************************************************
          //Assigning Random numbers to original and temporary subarrays
          //***********************************************************
             int a=(int)(size/2);
             for(int j=0; j<a; j++){
                Bnodes[j]=Anodes[R[j]];
                OB[j]=Anodes[R[j]];
             }
         //*********************************************************
             for(int k=0; k<(int)(size/2); k++){
                Cnodes[k]=Anodes[R[a]];
                OC[k]=Anodes[R[a]];
                a++;
             }
         //*********************************************************
          
  
              resetVariables(Bnodes, Cnodes, size);
       
//***************************************************************************
          
  //*************************************************************************
          internalCost(Bnodes,Cnodes,size);
          externalCost(Bnodes,Cnodes,size);
          DiffValue(Bnodes,Cnodes, size);
          findGain(Diff,Bnodes,Cnodes,OB,OC,size);
          presentClusters(OB,OC,size);
            if(clusterCount<3){
               
               int Badj[][]=new int[size/2][size/2];
               int Cadj[][]=new int[size/2][size/2];
                  //**************************************************************
                 // System.out.println("Adjacency of B and C");
                  for(int i=0;i<size/2;i++){
                         for(int j=0;j<size/2;j++)
                         {
                             Badj[i][j] = A[Bnodes[i]][Bnodes[j]];
                             Cadj[i][j] = A[Cnodes[i]][Cnodes[j]];

                         }

                        //System.out.println();
                                            }

        

                                    for(int i=0;i<size;i++)
                                    for(int j=0;j<size;j++) swappedPair[i][j]=0;
                                    findg=0; function(Badj,Bnodes,size/2);
                                    findg=0; function(Cadj,Cnodes,size/2);
                                  
                            }


    }
 //*************************************************************************
     public static int[] randomNo(int size){
         int count=0;
       Random rand = new Random();
       int ans[]=new int[size];
     // for (int idx = 0; idx < size; idx++){
       while(count<size){
      
         randomInt = rand.nextInt(size);
          while(F[randomInt]!=true){
           F[randomInt]=true;
           // System.out.println("\nGenerated : " + randomInt);
             // System.out.println(randomInt);
              ans[count]=randomInt;
//             R[count]=randomInt;
                 count++;
            

             
           }
       
          }
      return ans;



            
    }
//***************************************************************************

      public static void internalCost(int[] B, int[] C, int size){
        
          int internal=0; int a=size/2;
      
          //double IC[]=new double[size/2];
           for(int x=0; x<size/2; x++){
               for(int y=0; y<size/2; y++){
                 if((x!=y)&&(A[B[x]][B[y]]!=0))
                         internal+= A[B[x]][B[y]];
                  
                 
                  
                 }
                 IB[B[x]][1]=internal;
                 
                 internal=0;
             }
            for(int x=0; x<size/2; x++){
               for(int y=0; y<size/2; y++){
                 if((x!=y)&&(A[C[x]][C[y]]!=0))
                         internal += A[C[x]][C[y]];

                 

                 }
                IB[C[x]][1]=internal;
                
                 
                  a=a+1;
                 internal=0;
             }
          
       }
          
          
      
      
//**************************************************************************

      public static void externalCost(int[] B, int[] C, int size){
          
          int external=0; int b=size/2;
           // double EC[]=new double[size/2];
          for(int x=0; x<size/2; x++){
               for(int y=0; y<size/2; y++){
                 if(A[B[x]][C[y]]!=0)
                         external += A[B[x]][C[y]];



                 }
                 EB[B[x]][1]=external;
              
                 external=0;
          }
         
        for(int x=0; x<size/2; x++){
              for(int y=0; y<size/2; y++){
                 if(A[C[x]][B[y]]!=0)
                         external+= A[C[x]][B[y]];



                 }
               EB[C[x]][1]=external;
                
                b=b+1;
                 external=0;
          }
          
      }
  


//**************************************************************************

      public static void DiffValue(int[] B, int[] C, int size){
          
    
            for(int i=0; i<size/2; i++){

                      Diff[B[i]][1] = EB[B[i]][1] - IB[B[i]][1];
                      Diff[C[i]][1] = EB[C[i]][1] - IB[C[i]][1];
                
   
            }
      

    }

//**********************************************************************

      public static void findGain(int[][] D,int[] B, int[] C, int[] OB, int[] OC, int size){
          int maxgain=-100;
          int x=0,y=0,temp=0,k=0,l=0;
          int G[]=new int[size/2*size/2];

          int j=0;
        for(int i=0; i<size/2; i++){
            for(int o=0; o<size/2; o++){
             if(P[B[i]]==false && P[C[o]]==false){
             G[j]=D[B[i]][1] + D[C[o]][1] - 2*A[B[i]][C[o]];
              if(G[j]>maxgain){
                 maxgain=G[j];
                 x=i;
                 y=o;
             }
             j=j+1;
            }
            }
        }
          
                superGain+=maxgain;
                
                
              // Swapping the two vertices having greatest gain
                 P[B[x]]=true;
                 P[C[y]]=true;
                 temp=B[x];
                 B[x]=C[y];
                 C[y]=temp;
                 k=B[x];
                 l=C[y];
               //Storing to be swapped nodes
                
                Gain[findg]=maxgain;
                GainPair[findg][0]=x;
                GainPair[findg][1]=y;
                findg=findg+1;
           
          
        
          
        // System.out.println("Maximum gain is of "+x+" and "+y+"="+maxgain);
        //  System.out.println("swapped values: "+k+" and "+l);
            for(int b=0;b<size/2;b++)
               {
                   if( P[B[b]] != true)
                    {
                    D[B[b]][1]=D[B[b]][1]+2*A[B[b]][l]-2*A[B[b]][k];
                   //  System.out.println(B[b]+":"+D[B[b]]);
                    }

                   if( P[C[b]] != true)
                    {
                   D[C[b]][1]=D[C[b]][1]+2*A[C[b]][k]-2*A[C[b]][l];
                    //    System.out.println(C[b]+":"+D[C[b]]);
                    }

               }

        int loc=0; int sum=0,max=-100;
        if(findg<size/2){
                    findGain(D,B,C,OB,OC, size);
                  }
        else if (findg>=size/2)
                        {

                          max=Gain[0];
                          for(int i=0; i<size/2; i++){
                          sum+=Gain[i];
                          if(sum>max){
                          max=sum; loc=i;
                                    }
                        }
                   
                    }
      if(max>0 && loc!=(size/2-1))
      {
         int a=-1,b=-1;
   
                 //Do Exchange if the swapping never happened before
                  if(swappedPair[B[GainPair[loc][0]]][C[GainPair[loc][1]]]==0)
                  {
                      a=OB[GainPair[loc][0]];
                      b=OC[GainPair[loc][1]];
                         temp = OB[GainPair[loc][0]];
                         OB[GainPair[loc][0]]=OC[GainPair[loc][1]];
                         OC[GainPair[loc][1]]=temp;
                         for(int i=0;i<size/2;i++)
                         {
                             B[i]=OB[i];
                             C[i]=OC[i];
                         }
                        swappedPair[a][b]=1; swappedPair[b][a]=1;
                        findg=0;
                        resetVariables(B,C,size);
                    
                        internalCost(B,C,size);
                        externalCost(B,C,size);
                        DiffValue(B,C,size);
                        findGain(D,B,C,OB,OC,size);
                 }
         
           
          }
        
          
      
    }//method
      public static void resetVariables(int B[], int C[], int size)
    {
            
            Gain=new int[size/2];
            GainPair=new int[size/2][2];
        
            for(int i=0;i<size/2;i++)
            {
                P[B[i]] = false;
                P[C[i]]=false;
            }
      }
      //*******************************************************************
        public static void displayClusters(int size)
        {
            int s=clusterCount-k;
            int c=0;
            for(int i=(s+1);i<=(clusterCount);i++)
            {
               System.out.println("\n Cluster "+(++c)+" :") ;

                for (int j = 0; j < size; j++)
                  if(clusters[i][j]!=-1)
                    System.out.print(" "+clusters[i][j]);
            }
        }
        
      //*******************************************************************
      public static void presentClusters(int[] OB, int []OC, int size)
        {
         clusterCount+=1;
         int m=0,n=0;
        for(int i=0;i<size/2;i++)
            {
            if(OB[i]<oldsize)
            clusters[clusterCount][n++]=OB[i];
           
            }
         clusterCount+=1;
            for(int i=0;i<size/2;i++)
            {
            if(OC[i]<oldsize)
             clusters[clusterCount][m++] = OC[i];
          
            }
      }

//**************************************************************************
    public void runClustering(int size, int k)
    {

        System.out.println("\n\n\nKL Algorithm\n");
        findModularity(CMem,size);
        findConductance(size,k);
        findExpansion(size,k);
    }
    //**************************************************************************
//*****************************************************************************************
public void findModularity(int []cMem, int size)
{
    int i;
//Converting A in an Adjacency Matrix

//Find Quality of Partitions
Modularity md=new Modularity(B,size);
//System.out.println("\nModularity is: "+md.vlaueOfModularity(cMem));
CDSInterface.modresult=""+md.vlaueOfModularity(cMem);
CDSInterface.algoresult="KL Algorithm";
CDSInterface.comresult="O(no. of iterations*n power 3)";
}

//*****************************************************************************************
public void findConductance(int size, int K)
{
 double boundaryEdges=0;
 double result=0;
 double allEdges=0;
 double insideEdges=0;

 for(int i=0; i<size; i++){
     for(int j=0; j<size; j++)
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

     for(int i=0; i<size; i++){

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
public void findExpansion(int size, int k)
{
 double boundaryEdges=0;
 int clusterno=0;

 double insideEdges=0;

 for(int i=0; i<size; i++){
     for(int j=0; j<size; j++)
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
     for(int i=0; i<size; i++){

             expCluster[CMem[i]-1][0]+=Conductance[i][0];
             expCluster[CMem[i]-1][1]+=1;

     }

  for(int l=0; l<k; l++){
      System.out.print("  no. of vertices in cluster "+expCluster[l][1]);
      expresult+=((expCluster[l][0])/(expCluster[l][1]));
  }
 System.out.println();
 expresult=expresult/k;
 System.out.println("result of expansion="+expresult);
 CDSInterface.expresult=""+expresult;
}




}
//class

    
    
  
  


