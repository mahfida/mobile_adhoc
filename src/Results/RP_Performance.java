//******************************************************************************
//PACKAGE

package Results;

//******************************************************************************
//IMPORT FILES
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

//******************************************************************************
//SHOWING PERFORMANCE WITH CHART AND TABLE

public class RP_Performance
{
    //Instance Variables
    JFrame jf;
    static Object[][] avgData=new Object[10][5];
    static Object[][] stdData=new Object[10][5];
    static int AcountCols=0,ScountCols=0;
    static int AcountRows=-1,ScountRows=-1;
    JScrollPane jsp;
    Panel p1,p2;
    BarChart barObj;
    TabularResult tableObj;
    Checkbox latency,contacts, deliveryPredict,load;
    Label lmetrics =new Label("Performance Comparision of Routing Protocols",Label.CENTER);

    CheckboxGroup cbg = new CheckboxGroup();
    Checkbox avg=new Checkbox("Average Result", cbg, true);
    Checkbox std=new Checkbox("Standard Deviation", cbg, false);

    Button show=new Button("Show");
    Button clear=new Button("Clear");

    
//******************************************************************************
//CONSTRUCTOR
public RP_Performance() {}

//******************************************************************************
//CRAETE GRAPHICAL INTERFACE FOR THE DISPLAYING RESULTS

public void CreateGUI()
{
     p1=new Panel();
     p2=new Panel();
     p1.setBackground(new Color(0xb0c4de));  //set the background color of p1
     p2.setBackground(new Color(0xb0c4de));  //set the background color of p2
     //panel for chart
     barObj=new BarChart();
     barObj.setBackground(Color.WHITE);  //set the background color of p3
     barObj.setPreferredSize(new Dimension(940,600));
    //panel for table
     tableObj=new TabularResult();
     tableObj.setBackground(Color.WHITE);  //set the background color of p3
     tableObj.setPreferredSize(new Dimension(940,600));
     jf=new JFrame("Perfomance T");
     jf.setLayout(new BorderLayout());
     latency=new Checkbox("Latency");
     latency.setFont(new Font("Dialog",Font.PLAIN,11));
     contacts=new Checkbox("Links Utilized");
     contacts.setFont(new Font("Dialog",Font.PLAIN,11));
     deliveryPredict=new Checkbox("Delivery Rate");
     deliveryPredict.setFont(new Font("Dialog",Font.PLAIN,11));
     load=new Checkbox("Bundle Copies");
     load.setFont(new Font("Dialog",Font.PLAIN,11));
     p1.setPreferredSize(new Dimension(940,30));
     p2.setPreferredSize(new Dimension(120,600));
     lmetrics.setFont(new Font("Dialog",Font.BOLD,14));
     lmetrics.setPreferredSize(new Dimension(940,30));

     show.setPreferredSize(new Dimension(5,10));
     clear.setPreferredSize(new Dimension(5,10));
     p1.add(lmetrics);         jf.add(p1,BorderLayout.NORTH);
     
     p2.setLayout(new GridLayout(8,1,5,5));
     p2.add(latency);    p2.add(load);  p2.add(contacts);
     p2.add(deliveryPredict);  p2.add(avg); p2.add(std); p2.add(show); p2.add(clear);
     jf.add(p2,BorderLayout.WEST);
}

//******************************************************************************
//DISPLAY TABLE

public void displayTable()
{
    jf.add(tableObj,BorderLayout.CENTER);
    jf.setSize(new Dimension(940,600));
    jf.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    jf.setResizable(false);
    jf.setVisible(true);
    tableObj.showTable(this);
}

//******************************************************************************
//DISPLAY CHART

public void displayChart()
{
    jf.add(barObj,BorderLayout.CENTER);
    jf.setSize(new Dimension(940,600));
    jf.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    jf.setResizable(false);
    jf.setVisible(true);
    barObj.showChart(this);
}

//******************************************************************************

public void setAvgData(String pName,int delay,int burden,int bw,int dp)
{
    avgData[++AcountRows][0]=pName;
    avgData[AcountRows][1]=delay;
    avgData[AcountRows][2]=burden;
    avgData[AcountRows][3]=bw;
    avgData[AcountRows][4]=dp;
}

//******************************************************************************

public static void setSTDData(String pName,int delay,int burden,int bw,int dp)
{
    stdData[++ScountRows][0]=pName;
    stdData[ScountRows][1]=delay;
    stdData[ScountRows][2]=burden;
    stdData[ScountRows][3]=bw;
    stdData[ScountRows][4]=dp;
}

//******************************************************************************

public void clearData()
{
    AcountRows=-1; ScountRows=-1;
    AcountCols=0; ScountCols=0;
}

//******************************************************************************

}//End of the class
