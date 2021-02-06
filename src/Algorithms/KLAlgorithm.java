
package Algorithms;

import Support.Modularity;
import java.util.Random;

public class KLAlgorithm implements BaseCD {
 
       double A[][], D[],Anodes[]; //D value of all nodes(D=E-I)
       double Original[][];
       int EB[][];//External Cost
       int IB[][];//Internal cost
       double condCluster[][]; //conductance
       double expCluster[][];//Expansion
       int CMem[];//Node's Cluster
       double Conductance[][];
       int k,K;//where capital K shows the actual number of clusters that can be designed
       boolean isClusterEmpty[];
       int clusters[][];
       int clusterCount=-1;
       double superGain=0,Gain[],GainPair[][];
       boolean F[];//for unique random number generation
       boolean P[];//for one time pass
       int R[];//R is for Random numbers, array of Gain and array of GainPair
       int randomInt=0;
       int findg=0;
       double Diff[][];
       double swappedPair[][], stop=0;
       int oldsize,size;
      public KLAlgorithm (double [][] A, int s, int k)
      {
           this.size=s;
           this.A=new double[size][size];
           Original=new double[size][size];
           for(int i=0;i<size;i++)
               for(int j=0;j<size;j++)
               {
                   this.A[i][j] = A[i][j];
                   Original[i][j]=A[i][j];
               }
           oldsize=s;
          
           this.k=k;// Required number of clusters
           CMem=new int[size];
           condCluster=new double[k][2];
           expCluster=new double[k][2];
           Conductance=new double[s][2];
      }
      public void runClustering()
    {
           //Add fake nodes
           int n=1;
           int pow=2;
           while(pow<size)
           {
               pow=(int) Math.pow(2, n);
               n=n+1;
           }
           size=pow;
           
           //Update number of nodes to power of 2
           double TA[][]=new double[size][size];
           for(int i=0;i<size;i++)
               for(int j=0;j<size;j++)
                   if(i< oldsize && j< oldsize)
                   TA[i][j]=A[i][j];
                   else
                       TA[i][j]=0;
           //update adjacency
           A=TA;
           Diff=new double[size][2];
           swappedPair=new double [size][size];
           for(int i=0;i<size;i++)
              for(int j=0;j<size;j++) swappedPair[i][j]=0;
           EB=new int[size][2];
           IB=new int[size][2];
           int c=differentClusters(k);
           clusters=new int[c][size];
           isClusterEmpty=new boolean[c];
           Anodes=new double[size];
           for(int i=0;i<size;i++)
           Anodes[i]=i;

            for(int i=0;i<c;i++)
            {
                isClusterEmpty[i]=true;
                for (int j = 0; j < size; j++)
                 {
                  clusters[i][j]=-1;
                 }
           }
           P=new boolean[size];
           double Aadj[][]=new double[size][size];
           for(int i=0;i<size;i++)
           {
               {
                   for (int j = 0; j < size; j++)
                   Aadj[i][j]=A[i][j];
               }
               IB[i][0]=i;EB[i][0]=i; Diff[i][0]=i;
           }
           function(Aadj,Anodes,size);
           displayClusters(size);
           findModularity(CMem,oldsize);
           findConductance(oldsize,K);
           findExpansion(oldsize,K);

    }
   //**************************************************************************
       public  int differentClusters(int k)
       {
             double count=0;
             double c=(double)k;
             if(c%2!=0)
                     c=c+1;
             count=c;
             do
             {
             c=Math.ceil(c/2);
             if(c==1) break;
             else
                 count+=c;
             }
             while(c>1);
              return (int)count;
       }
//**************************************************************************
       public  void function(double [][]Aadj, double []Anodes, int size){
            
            double []Cnodes=new double[(int)(size/2)];
            double []OC=new double[(int)(size/2)];
            double []Bnodes=new double[(int)(size/2)];
            double []OB=new double[(int)(size/2)];

       //*********************************************************************
             F=new boolean[size];
           for(int j=0; j<size; j++){
              F[j]=false;   }

      //************************************************************************
        R=new int[size];
        R=randomNo(size);

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
            if(clusterCount<(k-1))
               {
               double Badj[][]=new double[size/2][size/2];
               double Cadj[][]=new double[size/2][size/2];
                  //**************************************************************

               for(int i=0;i<size/2;i++){
                  for(int j=0;j<size/2;j++)
                 {
                     Badj[i][j] = A[(int)Bnodes[i]][(int)Bnodes[j]];
                     Cadj[i][j] = A[(int)Cnodes[i]][(int)Cnodes[j]];

                 }}
                for(int i=0;i<size;i++)
                for(int j=0;j<size;j++) swappedPair[i][j]=0;
                        findg=0; function(Cadj,Cnodes,size/2);
                        findg=0; function(Badj,Bnodes,size/2);

                 }


    }
 //*************************************************************************
     public  int[] randomNo(int size)
     {
       int count=0;
       Random rand = new Random();
       int ans[]=new int[size];

       while(count<size)
        {
            randomInt = rand.nextInt(size);
            while(F[randomInt]!=true){
            F[randomInt]=true;
            ans[count]=randomInt;
            count++;}
        }
   
      return ans;
    }
//***************************************************************************

 public void internalCost(double[] B, double[] C, int size)
 {
          int internal=0; int a=size/2;
          for(int x=0; x<size/2; x++)
          {
               for(int y=0; y<size/2; y++)
               {
                 if((x!=y)&&(A[(int)B[x]][(int)B[y]]!=0))
                         internal+= A[(int)B[x]][(int)B[y]];
                }
                 IB[(int)B[x]][1]=internal;
                 internal=0;
          }
            for(int x=0; x<size/2; x++)
            {
               for(int y=0; y<size/2; y++)
               {
                 if((x!=y)&&(A[(int)C[x]][(int)C[y]]!=0))
                         internal += A[(int)C[x]][(int)C[y]];
               }
               IB[(int)C[x]][1]=internal;
               a=a+1;
               internal=0;
             }
}

//**************************************************************************

      public void externalCost(double[] B, double[] C, int size)
      {
        int external=0; int b=size/2;
        for(int x=0; x<size/2; x++)
        {
               for(int y=0; y<size/2; y++)
               {
                     if(A[(int)B[x]][(int)C[y]]!=0)
                         external += A[(int)B[x]][(int)C[y]];
                 }
                EB[(int)B[x]][1]=external;
                external=0;
        }

        for(int x=0; x<size/2; x++)
        {
              for(int y=0; y<size/2; y++)
                {
                 if(A[(int)C[x]][(int)B[y]]!=0)
                         external+= A[(int)C[x]][(int)B[y]];
                 }
               EB[(int)C[x]][1]=external;
               b=b+1;
               external=0;
          }

      }
//**************************************************************************

  public void DiffValue(double[] B, double[] C, int size)
  {

        for(int i=0; i<size/2; i++)
        {
                  Diff[(int)B[i]][1] = EB[(int)B[i]][1] - IB[(int)B[i]][1];
                  Diff[(int)C[i]][1] = EB[(int)C[i]][1] - IB[(int)C[i]][1];
        }
 }

//**********************************************************************

 public  void findGain(double[][] D,double[] B, double[] C, double[] OB, double[] OC, int size)
 {
          double maxgain=-100,temp=0,k=0,l=0;
          int x=0,y=0;
          double G[]=new double[size/2*size/2];

          int j=0;
          for(int i=0; i<size/2; i++)
          {
            for(int o=0; o<size/2; o++)
            {
             if(P[(int)B[i]]==false && P[(int)C[o]]==false)
                {
                     G[j]=D[(int)B[i]][1] + D[(int)C[o]][1] - 2*A[(int)B[i]][(int)C[o]];
                      if(G[j]>maxgain)
                      {
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
                 P[(int)B[x]]=true;
                 P[(int)C[y]]=true;
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

               for(int b=0;b<size/2;b++)
               {
                   if( P[(int)B[b]] != true)
                    {
                    D[(int)B[b]][1]=D[(int)B[b]][1]+2*A[(int)B[b]][(int)l]-2*A[(int)B[b]][(int)k];
                   
                    }

                   if( P[(int)C[b]] != true)
                    {
                   D[(int)C[b]][1]=D[(int)C[b]][1]+2*A[(int)C[b]][(int)k]-2*A[(int)C[b]][(int)l];
                 
                    }

               }

        int loc=0; double sum=0,max=-100;
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
         double a=-1,b=-1;

                 //Do Exchange if the swapping never happened before
                  if(swappedPair[(int)B[(int)GainPair[loc][0]]][(int)C[(int)GainPair[loc][1]]]==0)
                  {
                      a=OB[(int)GainPair[loc][0]];
                      b=OC[(int)GainPair[loc][1]];
                         temp = OB[(int)GainPair[loc][0]];
                         OB[(int)GainPair[loc][0]]=OC[(int)GainPair[loc][1]];
                         OC[(int)GainPair[loc][1]]=temp;
                         for(int i=0;i<size/2;i++)
                         {
                             B[i]=OB[i];
                             C[i]=OC[i];
                         }
                        swappedPair[(int)a][(int)b]=1; swappedPair[(int)b][(int)a]=1;
                        findg=0;
                        resetVariables(B,C,size);

                        internalCost(B,C,size);
                        externalCost(B,C,size);
                        DiffValue(B,C,size);
                        findGain(D,B,C,OB,OC,size);
                 }


          }



    }//method
//**************************************************************************
      public  void resetVariables(double B[], double C[], int size)
    {

            Gain=new double[size/2];
            GainPair=new double[size/2][2];

            for(int i=0;i<size/2;i++)
            {
                P[(int)B[i]] = false;
                P[(int)C[i]]=false;
            }
      }
//*******************************************************************
     public  void displayClusters(int size)
     {
            int s=clusterCount-k;
            int c=0;
            K=0;
            int nodes=0;
            for(int i=(s+1);i<=(clusterCount);i++)
            {
                if( isClusterEmpty[i]==false)//if cluster has no member, it must not be displayed
                {
                   K=K+1;//Valid number of clusters
                   ++c;
                 //  System.out.println("\n Cluster "+(c)+" :");
                   CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]=""+(c);
                   CDSInterface.c+=1;
                    for (int j = 0; j < size; j++)
                      if(clusters[i][j]!=-1)
                      {
                       //   System.out.print(" " + clusters[i][j]);
                          CMem[clusters[i][j]]=c;
                          nodes+=1;
                          CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]+=" "+clusters[i][j];
                      }
                   CDSInterface.r+=1;
                   CDSInterface.c=0;

                }
            }
            int i=s+1;
            //System.out.println("nodes :"+nodes);
          while(nodes<oldsize)
          {
             i-=1;
             if( isClusterEmpty[i]==false)//if cluster has no member, it must not be displayed
                {
                   K=K+1;//Valid number of clusters
                   ++c;
                 //  System.out.println("\n Cluster "+(c)+" :");
                   CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]=""+(c);
                   CDSInterface.c+=1;
                    for (int j = 0; j < size; j++)
                      if(clusters[i][j]!=-1)
                      {
                        //  System.out.print(" " + clusters[i][j]);
                          CMem[clusters[i][j]]=c;
                          nodes+=1;
                          CDSInterface.rowNo[CDSInterface.r][CDSInterface.c]+=" "+clusters[i][j];
                      }
                   CDSInterface.r+=1;
                   CDSInterface.c=0;

                }
          }
          ////////////////////////////////////////////////////////////////////////




    }


//*******************************************************************
      public void presentClusters(double[] OB, double []OC, int size)
        {
         clusterCount+=1;
        
         int m=0,n=0;
        // System.out.print("\n CC "+clusterCount+": ");
         for(int i=0;i<size/2;i++)
             if(OB[i]<oldsize)
             {
                   isClusterEmpty[clusterCount]=false;
                   clusters[clusterCount][n++] = (int) OB[i];
                    System.out.print("  "+(int)OB[i]);
             }
            
          clusterCount+=1;
     // System.out.print("\n CC "+clusterCount+": ");
            for(int i=0;i<size/2;i++)
            {
            if(OC[i]<oldsize)
                {
                     isClusterEmpty[clusterCount]=false;
                     clusters[clusterCount][m++] = (int) OC[i];
                     //System.out.print("  "+ (int)OC[i]);
                }

            }
      }

//******************************************************************************
    public void runClustering(int size, int k)
    {

        System.out.println("\n\n\nKL Algorithm\n");
        findModularity(CMem,size);
        findConductance(size,k);
        findExpansion(size,k);
    }
//******************************************************************************
public void findModularity(int []cMem,int size)
{
    int i;
//Converting A in an Adjacency Matrix

//Find Quality of Partitions
Modularity md=new Modularity(Original,size);
CDSInterface.modresult=""+md.vlaueOfModularity(cMem);
CDSInterface.algoresult="KL Algorithm";
CDSInterface.comresult="O(no. of iterations*n power 3)";
}
//******************************************************************************
public void findConductance(int size, int K)
{
 double boundaryEdges=0;
 double result=0;
 double allEdges=0;
 double insideEdges=0;
 for(int i=0; i<size; i++)
 {
     for(int j=0; j<size; j++)
     {
           if(Original[i][j]==1){
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
//******************************************************************************
     for(int i=0; i<size; i++)
     {
      //   System.out.println("Mem: "+CMem[i]+"i:"+i);
             condCluster[CMem[i]-1][0]+=Conductance[i][0];
             condCluster[CMem[i]-1][1]+=Conductance[i][1];
      }
//******************************************************************************
  for(int l=0; l<K; l++)
  {
     // System.out.print("boundary edges "+condCluster[l][0]+"  "+"all edges "+condCluster[l][1]);
      result+=((condCluster[l][0])/(condCluster[l][1]));
  }
     result=result/K;
     //System.out.println();
    // System.out.println("result="+result);
     CDSInterface.condresult=""+result;
}

//******************************************************************************
public void findExpansion(int size, int k)
{
 double boundaryEdges=0;
 int clusterno=0;

 double insideEdges=0;

 for(int i=0; i<size; i++){
     for(int j=0; j<size; j++)
     {
           if(Original[i][j]==1){
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
    //  System.out.print("  no. of vertices in cluster "+expCluster[l][1]);
      expresult+=((expCluster[l][0])/(expCluster[l][1]));
  }
 //System.out.println();
 expresult=expresult/k;
 //System.out.println("result of expansion="+expresult);
 CDSInterface.expresult=""+expresult;
}




}
//class







