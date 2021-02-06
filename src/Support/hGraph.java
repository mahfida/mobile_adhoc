/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Support;
import Algorithms.ModularityCD;
import java.util.Random;

/**
 *
 * @author sarmad
 */
public class hGraph {
        Random rand=new Random();
        
        int r=rand.nextInt(32);
        int sz=7;
        int i,j;
        double k=0,l=0;
        double AdjM[][]=new double[sz][sz];
      //  double A[][]=new double[sz][sz];
        double A[][]= {{1,1,1,1,0,0,0},{1,1,1,1,0,0,0},{1,1,1,1,0,0,0},{1,1,1,1,0,0,0},
        {0,0,0,0,1,1,1},{0,0,0,0,1,1,1},{0,0,0,0,1,1,1}};
        public static int a,b;
        int vCluster[][]=new int[sz][2];
        int Vertex[]=new int[sz];
        int s[], Community[][];
        ModularityCD mcd;
        public hGraph()
        {
         for(int i=0;i<this.sz;i++)
        {
            this.Vertex[i] = i + 1;
            this.vCluster[i][0]=i+1;
        }
    /*      for(int i=0;i<hg.sz;i++)
        {
            for (int j = i + 1; j < hg.sz; j++)
            {

                    hg.A[i][j]=0;
                    hg.A[j][i]=0;

            }
        }

    /*    hg.A[0][3]=hg.A[0][10]=hg.A[0][11]=hg.A[0][18]=1.0;
        hg.A[1][2]=hg.A[1][16]=1.0;
        hg.A[2][1]=hg.A[2][16]=hg.A[20][19]=1.0;
        hg.A[3][0]=hg.A[3][17]=hg.A[3][19]=1.0;
        hg.A[4][14]=hg.A[4][12]=hg.A[4][15]=1.0;
        hg.A[5][14]=hg.A[5][6]=hg.A[5][13]=1.0;
        hg.A[6][5]=hg.A[6][12]=1.0;
        hg.A[7][16]=hg.A[7][8]=hg.A[7][9]=1.0;
        hg.A[8][7]=hg.A[8][9]=1.0;
        hg.A[9][7]=hg.A[9][8]=1.0;
        hg.A[10][0]=hg.A[10][19]=1.0;
        hg.A[11][0]=hg.A[11][17]=1.0;
        hg.A[12][4]=hg.A[12][6]=1.0;
        hg.A[13][5]=hg.A[13][15]=1.0;
        hg.A[14][4]=hg.A[14][5]=hg.A[14][19]=1.0;
        hg.A[15][4]=hg.A[15][13]=1.0;
        hg.A[16][1]=hg.A[16][2]=hg.A[16][7]=1.0;
        hg.A[17][3]=hg.A[17][11]=1.0;
        hg.A[18][20]=hg.A[18][0]=1.0;
        hg.A[19][0]=hg.A[19][3]=hg.A[19][2]=hg.A[19][14]=1.0;
        hg.A[20][18]=hg.A[20][17]=1.0;*/

//********************************************************************
      /*   hg.A[0][8]=hg.A[0][6]=hg.A[0][15]=1;
        hg.A[8][0]=hg.A[8][6]=hg.A[8][15]=hg.A[8][9]=1;
        hg.A[6][0]=hg.A[6][8]=hg.A[6][15]=1;
        hg.A[15][0]=hg.A[15][8]=hg.A[15][6]=hg.A[15][2]=1;
        hg.A[2][15]=hg.A[2][5]=hg.A[2][12]=hg.A[2][14]=1;
        hg.A[5][2]=hg.A[5][12]=hg.A[5][14]=1;
        hg.A[12][2]=hg.A[12][5]=hg.A[12][14]=1;
        hg.A[14][2]=hg.A[14][5]=hg.A[14][12]=1;
        hg.A[1][9]=hg.A[1][4]=hg.A[1][13]=1;
        hg.A[9][1]=hg.A[9][8]=hg.A[9][4]=hg.A[9][13]=1;
        hg.A[4][1]=hg.A[4][9]=hg.A[4][13]=1;
        hg.A[13][1]=hg.A[13][9]=hg.A[13][4]=hg.A[13][11]=1;
        hg.A[11][3]=hg.A[11][10]=hg.A[11][7]=hg.A[11][13]=1;
        hg.A[3][11]=hg.A[3][10]=hg.A[3][7]=1;
        hg.A[10][11]=hg.A[10][3]=hg.A[10][7]=1;
        hg.A[7][11]=hg.A[7][3]=hg.A[7][10]=1;*/
        ///////////////////////////////////////////////
/*
        hg.A[0][1]=hg.A[0][3]=1.0;
        hg.A[1][0]=hg.A[1][3]=hg.A[1][4]=hg.A[1][2]=1.0;

        hg.A[2][1]=hg.A[2][3]=hg.A[2][4]=hg.A[2][5]=hg.A[2][6]=hg.A[2][25]=1.0;
        hg.A[2][18]=hg.A[2][7]=hg.A[2][8]=hg.A[2][9]=hg.A[2][10]=hg.A[2][11]=1.0;
        hg.A[2][12]=hg.A[2][13]=hg.A[2][14]=hg.A[2][15]=1.0;

        hg.A[3][0]=hg.A[3][1]=hg.A[3][2]=hg.A[3][5]=1.0;

        hg.A[4][1]=hg.A[4][5]=hg.A[4][2]=1.0;
        hg.A[5][3]=hg.A[5][4]=hg.A[5][2]=1.0;
        hg.A[6][2]=1.0;
        hg.A[7][2]=hg.A[7][9]=hg.A[7][21]=1.0;

        hg.A[8][2]=hg.A[8][12]=hg.A[8][9]=1.0;
        hg.A[8][11]=hg.A[8][10]=hg.A[8][16]=1.0;
        hg.A[8][26]=hg.A[8][24]=hg.A[8][29]=1.0;
        hg.A[8][18]=1.0;

        hg.A[9][7]=hg.A[9][8]=hg.A[9][10]=hg.A[9][11]=1.0;
        hg.A[9][13]=hg.A[9][12]=hg.A[9][2]=1.0;

        hg.A[10][2]=hg.A[10][8]=hg.A[10][21]=hg.A[10][11]=hg.A[10][9]=1.0;
        hg.A[11][15]=hg.A[11][12]=hg.A[11][2]=1.0;
        hg.A[11][9]=hg.A[11][8]=hg.A[11][10]=1.0;

        hg.A[12][11]=hg.A[12][9]=hg.A[12][8]=hg.A[12][2]=1.0;
        hg.A[13][9]=hg.A[13][2]=1.0;
        hg.A[14][11]=hg.A[14][2]=1.0;
        hg.A[15][9]=hg.A[15][2]=1.0;

        hg.A[16][8]=hg.A[16][21]=1.0;
        hg.A[17][9]=hg.A[17][18]=hg.A[17][26]=hg.A[18][21]=1.0;
        hg.A[18][2]=hg.A[18][8]=hg.A[18][17]=hg.A[18][26]=1.0;
        hg.A[19][21]=hg.A[19][26]=1.0;
        hg.A[20][21]=hg.A[20][26]=1.0;

        hg.A[21][19]=hg.A[21][20]=hg.A[21][22]=hg.A[21][23]=1.0;
        hg.A[21][27]=hg.A[21][32]=hg.A[21][31]=hg.A[21][26]=1.0;
        hg.A[21][29]=hg.A[21][24]=hg.A[21][18]=hg.A[21][7]=1.0;
        hg.A[21][17]=hg.A[21][10]=hg.A[21][16]=1.0;

        hg.A[22][21]=hg.A[22][27]=1.0;
        hg.A[23][21]=hg.A[23][26]=1.0;
        hg.A[24][21]=hg.A[24][25]=1.0;

        hg.A[25][2]=hg.A[25][21]=hg.A[25][24]=1.0;
        hg.A[25][26]=hg.A[25][33]=hg.A[25][28]=1.0;

        hg.A[26][18]=hg.A[26][8]=hg.A[26][17]=hg.A[26][21]=1.0;
        hg.A[26][20]=hg.A[26][19]=hg.A[26][23]=hg.A[26][27]=1.0;
        hg.A[26][32]=hg.A[26][31]=hg.A[26][30]=hg.A[26][25]=1.0;

        hg.A[27][21]=hg.A[27][26]=hg.A[27][30]=1.0;
        hg.A[28][25]=hg.A[28][29]=hg.A[28][33]=1.0;

        hg.A[29][28]=hg.A[29][8]=hg.A[29][21]=hg.A[29][30]=1.0;
        hg.A[30][29]=hg.A[30][21]=hg.A[30][26]=hg.A[30][27]=hg.A[30][33]=1.0;
        hg.A[31][21]=hg.A[31][26]=1.0;
        hg.A[32][21]=hg.A[32][26]=1.0;

        hg.A[33][28]=hg.A[33][25]=hg.A[33][30]=1.0;
        */
        ///////////////////////////////////////////////
        this.AdjM=this.A;
        this.splitGraph(this.A,this.Vertex,this.sz);
        this.vertexCluster(this.vCluster);

    }
    public  void splitGraph(double[][] A, int[] V, int sz){
      
    s=new int[sz];
    System.out.print("\n\nADJACENCY MATRIX\n\n");
    for(i=0;i<sz;i++)
	{

	for(j=0;j<sz;j++)
		{
                if(A[i][j]>0)
		System.out.print(1+" ");
                else
                System.out.print(0+" ");

		}
	System.out.print("\n");
	}
    
    mcd=new ModularityCD(A,s,sz);
    boolean modularity=mcd.CheckModularity();
    System.out.print("\n\nModularity?"+modularity);
    if(modularity==true)
    {
        Community=mcd.doDivision(V);
        int c1=0,c2=0;
        for(i=0;i<sz;i++)
        if(Community[i][1]==a) c1+=1;
        c2=sz-c1;
        System.out.println("c1:"+c1);
        System.out.println("c2:"+c2);
        double[][] A1=new double[c1][c1];
        double[][] A2=new double[c2][c2];
        int Vertex1[]=new int[c1];
        int Vertex2[]=new int[c2];
       

        //SubDivision
        int i1=0,j1=0;
        int i2=0,j2=0;
        if(c1>0 || c2> 0){
        for(i=0;i<sz;i++)
            for(j=0;j<sz;j++)
            {
                
                if( Community[i][1]==a&&Community[j][1]==a && c1>0)
                {
                   A1[i1][j1]=AdjM[Community[i][0]-1][Community[j][0]-1] ;
                   j1=j1+1;
                  
                   if(j1==c1)
                   {
                       Vertex1[i1]=Community[i][0];
                       vCluster[Vertex1[i1]-1][1]=a;
                       i1 = i1 + 1;
                       j1=0;
                    }
                }
               else if(Community[i][1] ==b && Community[j][1] ==b && c2>0)
                {
                   A2[i2][j2]=AdjM[Community[i][0]-1][Community[j][0]-1];
                   j2=j2+1;
                   if(j2==c2)
                   {
                       Vertex2[i2]=Community[i][0];
                        vCluster[Vertex2[i2]-1][1]=b;
                       i2 = i2 + 1;
                       j2=0;
                    }
                }
            }
       
        //1st sub Division
        if(c1>1){
        Community = new int[c1][2];
        splitGraph(A1, Vertex1, c1);
        }
        //2nd SubDivision
          if(c2>1){
        Community=new int[c2][2];
        splitGraph(A2, Vertex2, c2);
        }}
        
    }
    }
    public void vertexCluster(int vCluster[][])
    {
        this.vCluster=vCluster;
        
         boolean isChanged[]=new boolean[sz];
        for(i=0;i<sz;i++)
        {
             isChanged[i]=false;
                 
        }
       int c=1,k=0;
        
       outer: while(true)
        {
                for(j=0;j<sz;j++)
                {
                    if(isChanged[j]==false)
                    {
                        k = this.vCluster[j][1];
                        break;
                    }
                    if(j==(sz-1)) break outer;
                }

                for(i=0;i<sz;i++)
                {
                    if(this.vCluster[i][1]==k && isChanged[i]==false)
                    {
                        this.vCluster[i][1]=c;
                        isChanged[i]=true;
                    }

                }
                c=c+1;
        }
       for(j=0;j<(c-1);j++)
       {
            System.out.println("\n\nMembers of Cluster:"+(j+1)+"\n");
                for(i=0;i<sz;i++){
                                    if(this.vCluster[i][1]==(j+1))
                                    System.out.print(this.vCluster[i][0]+",");
                                 }
        }
       System.out.println("\n");
    }
}
