
package Algorithms;

public class MinCutAlgorithm {
     static double A[][]={{1,2,0,0,3,0,0,0},{2,1,3,0,2,2,0,0},{0,3,1,4,0,0,2,0},{0,0,4,1,0,0,2,2},{3,2,0,0,1,3,0,0},
     {0,2,0,0,3,1,1,0},{0,0,2,2,0,1,1,3},{0,0,0,2,0,0,3,1}};
     static int size;
     static int a;
     static double max;
     public static void main(String args[]){
    // public MinCutAlgorithm(){
    /* for(int i=0; i<size; i++){
         for(int j=0; j<size; j++){

         }
      }*/
     a=1; max=-100;
     //while(size!=2){

       for(int j=0; j<size; j++){
          if(A[a][j]>max){
              max=A[a][j];
          }
       }
       System.out.println("Max Cost="+max);
      }

   // }
}
