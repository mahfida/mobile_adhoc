package Support;



import Jama.Matrix;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class DatasetFile {
    static String dataset;
     
static Matrix m;
public static double A[][];
  public DatasetFile(String ds)
  {
      dataset=ds+".txt";
  }
////////////////////////////////////////////////////////////////////////////////
  public double[][] getContactGrapd() throws FileNotFoundException, IOException
    {
       int count = 0;
       int source,dest;
       String fileName=dataset;
       String strLine;
       FileInputStream fstream = new FileInputStream(fileName); // Get the object of DataInputStream
       DataInputStream in = new DataInputStream(fstream);
       BufferedReader br = new BufferedReader(new InputStreamReader(in));
       strLine = br.readLine();
       count=(int)(Double.parseDouble(strLine));
       A=new double[count][count];
        while ((strLine = br.readLine())!= null)
        {
            source=0;dest=0;
    
               StringTokenizer st = new StringTokenizer( strLine,"\t");
               while(st.hasMoreTokens())
               {
                  String str = st.nextToken();
                 
                  if(source==0)
                      source=(int)(Double.parseDouble(str));
                  else
                      dest=(int)(Double.parseDouble(str));
                  //Once a LINESTRING is entered
                  
              }
          //     if(source==0 && dest!=0) A[source][dest-1]=1.0;
         //   else if(source != 0 && dest == 0) A[source-1][dest] = 1.0;
          //  else if(source == 0 && dest == 0) A[source - 1][dest - 1] = 1.0;
          //
                    if(dataset.equals("D:\\CS Department.txt"))
                         A[source-1][dest-1] = A[dest-1][source-1]=1.0;
                    else
                         A[source][dest] = A[dest][source]=1.0;
          

        }
 
return A;

    }//////////////////////////////////////////////////////////////////////////
  //End of Method
   }//End of Class










