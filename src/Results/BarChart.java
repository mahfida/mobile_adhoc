//******************************************************************************
//PACKAGE
package Results;

//******************************************************************************
//IMPORT FILES

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;



//******************************************************************************
//CREATING BAR CHART OUT OF RESULT

public class BarChart extends Panel implements ActionListener{
    //Instance Variables
    private Random rand=new Random();
    private int bounds[][];
    private Color[] barColor;
    private String[] colHeads;
    int loc;
    float highestValue;
    boolean flag=false, average=true;
    Object[][] tempData;
    RP_Performance rpp;
    String[] chartTitles={"Data Delivery Delay","Number of Bundle Copies Generated","Number of Links Utilized","Delivery Probability"};
    String[] y_axis={"Latency","Bundle Copies","Number of Links","Delivery Rate"};
    String[] AroutingProtocols,SroutingProtocols;;
    int Height[],OriginalValues[];
    int x,y,pwidth,pheight;
    private int countCols=0;
    boolean flatency=false,fcontact=false,fDP=false,fload=false;

//******************************************************************************
//CONSTRUCTOR
public BarChart(){}

//******************************************************************************

public void init(){}

//******************************************************************************

@Override
public void paint(Graphics g)
{
        int xstart=0;
        int ystart=10;
        g.setColor(new Color(0xb0c4de));
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
        g.drawRect(0, 0, this.getWidth(), 40);

        //Printing small squares
        if(flag==true)
        for(int i=0;i<=RP_Performance.AcountRows;i++)
        {
        g.setColor(barColor[i]);
        g.fillRect(xstart,ystart,10,10);
        g.setColor(Color.BLACK);
        xstart+=20;
        g.drawString((String)tempData[i][0], xstart, ystart+10);
        xstart+=100;
        if(xstart>=this.getWidth())
        {
            xstart=0;
            ystart=30;
        }
        }
        if(countCols!=0)
        {
       
        dividePlayfield(countCols);
        for(int i=1;i<=countCols;i++)
        {
            for(int j=0;j<=RP_Performance.AcountRows;j++)
            {
               OriginalValues[j]=Height[j] = (Integer) tempData[j][i];
            }
            printBars(Height,RP_Performance.AcountRows+1,g,i-1);
     
        }
   
        }
 
}

//******************************************************************************

public void dividePlayfield(int sections)
{
    int j=0,k=0;
    int w=0,h=0;
        if(sections<=3)
        {
                 w=pwidth/sections;          h=(pheight-40)/1;
                 j=sections;                 k=1;
        }
        else if(sections==4)
         {
                w=pwidth/2;                  h=(pheight-40)/2;
                j=2;                         k=2;
             
         }
     int g=0;

     for(int a=0;a<k;a++)
          for(int b=0;b<j;b++)
          {

           bounds[g][0]=b*w;
           bounds[g][1]=40+a*h;
           bounds[g][2]=w;
           bounds[g][3]=h;

           g=g+1;

          }            
}

//******************************************************************************

public void showChart(RP_Performance rpp)
{
    AroutingProtocols=new String[RP_Performance.AcountRows+1];
    for(int i=0;i<=RP_Performance.AcountRows;i++)
    {
         AroutingProtocols[i]=(String)RP_Performance.avgData[i][0];
    }
    SroutingProtocols=new String[RP_Performance.ScountRows+1];
    for(int i=0;i<=RP_Performance.ScountRows;i++)
    {
         SroutingProtocols[i]=(String)RP_Performance.stdData[i][0];
    }
    pwidth=this.getWidth();
    pheight=this.getHeight();
    this.rpp=rpp;
    rpp.show.addActionListener((ActionListener) this);
    rpp.clear.addActionListener((ActionListener) this);
}

//******************************************************************************

public void actionPerformed(ActionEvent ae)
{
    String buttonName=ae.getActionCommand();
    if(buttonName.equals("Clear"))
    {
        flag=false;
        countCols=0;
        rpp.latency.setState(false);
        rpp.load.setState(false);
        rpp.contacts.setState(false);
        rpp.deliveryPredict.setState(false);
        repaint();
    }

    if(buttonName.equals("Show"))
    {

    if(flatency=rpp.latency.getState())   ++countCols;
    if(fload=rpp.load.getState())         ++countCols;
    if(fcontact=rpp.contacts.getState())  ++countCols;
    if(fDP=rpp.deliveryPredict.getState())++countCols;

    //initialize colHead
    if(rpp.avg.getState()==true)
        average=true;
    else
        average=false;

    colHeads=new String[countCols+1];
    bounds=new int[countCols][4];
    barColor=new Color[RP_Performance.AcountRows+1];
    flag=true;
    int l=0;
    colHeads[l]="Routing Protocol";
    if(flatency==true)			colHeads[++l]="Latency";
    if(fload==true)                         colHeads[++l]="Number of Copies";
    if(fcontact==true)	    		colHeads[++l]="Links Utilized";
    if(fDP==true)		   		colHeads[++l]="Delivery Rate";

    //Giving size to tempData
    tempData=new Object[RP_Performance.AcountRows+1][countCols+1];
    Height=new int[RP_Performance.AcountRows+1];
    OriginalValues=new int[RP_Performance.AcountRows+1];
    //Asigning values to tempData

    //For Displaying average result
    if(average==true)
    {
        rpp.lmetrics.setText("Performance Comparision of Routing Protocols: Average Results");
        for(int i=0;i<(RP_Performance.AcountRows+1);i++)
        {
        barColor[i]=new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
        int j=0;
        tempData[i][j]=RP_Performance.avgData[i][0];
        if(flatency==true)			tempData[i][++j]=RP_Performance.avgData[i][1];
        if(fload==true)			tempData[i][++j]=RP_Performance.avgData[i][2];
        if(fcontact==true)	    		tempData[i][++j]=RP_Performance.avgData[i][3];
        if(fDP==true)			tempData[i][++j]=RP_Performance.avgData[i][4];
        }
    }
    //For Displaying standard deviation result
    else
    {
        rpp.lmetrics.setText("Performance Comparision of Routing Protocols: Standard Deviation");
        for(int i=0;i<(RP_Performance.ScountRows+1);i++)
        {
        barColor[i]=new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
        int j=0;
        tempData[i][j]=RP_Performance.stdData[i][0];
        if(flatency==true)			tempData[i][++j]=RP_Performance.stdData[i][1];
        if(fload==true)			tempData[i][++j]=RP_Performance.stdData[i][2];
        if(fcontact==true)	    		tempData[i][++j]=RP_Performance.stdData[i][3];
        if(fDP==true)			tempData[i][++j]=RP_Performance.stdData[i][4];
        }
    }
        repaint();
        this.validate();

    }
}

//******************************************************************************

int findMaximum(int array[], int n)
{
     int max= array[0];
     for(int i=1;i<n;i++)
         if( array[i]>max)
         {
             max=array[i];
             loc=i;
         }
     return max;
}

//******************************************************************************

public void setHeightofBars(int[] height,int max)
{

     float divisor=(float)(max/highestValue);
   
     for(int i=0;i<height.length;i++)
     {
         Height[i]=(int)(height[i]/divisor);
         if(loc==i)
             loc=Height[i];
     }
}

//******************************************************************************

public void printBars(int height[],int rows, Graphics g,int colNumber)
{
    int max=findMaximum(height,rows);
    highestValue=bounds[colNumber][3]-bounds[colNumber][3]/4;
    setHeightofBars(height,max);
    x= (bounds[colNumber][2]-40)/rows;
    y=bounds[colNumber][1]+bounds[colNumber][3]-bounds[colNumber][3]/10;
    int w=(bounds[colNumber][2]-40)/(rows*2);
    g.setColor(Color.BLACK);
    g.drawLine(bounds[colNumber][0]+50, y-loc, bounds[colNumber][0]+50, y);
    g.drawLine(bounds[colNumber][0]+50,y,bounds[colNumber][2]+bounds[colNumber][0],y);
    g.setFont(new Font("Dialog",Font.BOLD,12));
    g.drawString(colHeads[colNumber+1],bounds[colNumber][0]+bounds[colNumber][2]/3, y+15);
    g.setFont(new Font("Dialog",Font.PLAIN,12));
    g.setColor(new Color(0xb0c4de));
    g.drawRect(bounds[colNumber][0], bounds[colNumber][1],bounds[colNumber][2],bounds[colNumber][3]);
   
   for(int i=0;i<rows;i++)
   {
        int x1=i*x+x/4;
        g.setColor(barColor[i]);
        g.fillRect(x1+bounds[colNumber][0]+40,y-Height[i],w,Height[i]);
        g.setColor(Color.BLACK);
        g.drawString(""+OriginalValues[i],x1+bounds[colNumber][0]+40, y-Height[i]-10);
              
   }
}

//******************************************************************************
 
}//End of class

