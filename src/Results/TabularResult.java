//******************************************************************************
//PACKAGES

package Results;

//******************************************************************************
//IMPORT FILES

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

//******************************************************************************
//DISPLAY RESULT IN TABULAR FORM
public class TabularResult extends Panel implements ActionListener
{
    //Instance Bariables
    Object[][] tempData;
    int countCols;
    JTable jt;
    RP_Performance rpp;
    boolean flatency=false,fcontact=false,fDP=false,fload=false, average=true;

//******************************************************************************
//CONSTRUCTORS

public TabularResult()
{ }

//******************************************************************************
//DISPALY TABLE

public void showTable(RP_Performance rpp)
{
    this.rpp=rpp;
    rpp.show.addActionListener(this);
    rpp.clear.addActionListener(this);
}

//******************************************************************************

public void actionPerformed(ActionEvent ae)
{
String buttonName=ae.getActionCommand();
if(buttonName.equals("Clear"))
{
    countCols=0;
    rpp.latency.setState(false);
    rpp.load.setState(false);
    rpp.contacts.setState(false);
    rpp.deliveryPredict.setState(false);
    jt=null;
    this.remove(rpp.jsp);
    tempData=null;
    repaint();
}

if(buttonName.equals("Show"))
{
if(rpp.avg.getState()==true) average=true;
else average=false;

if(flatency=rpp.latency.getState())   ++countCols;
if(fload=rpp.load.getState())         ++countCols;
if(fcontact=rpp.contacts.getState())  ++countCols;
if(fDP=rpp.deliveryPredict.getState())++countCols;

//initialize colHead
String[] colHeads=new String[countCols+1];
int l=0;
colHeads[l]="Routing Protocol";

if(flatency==true)			colHeads[++l]="Latency";
if(fload==true)                         colHeads[++l]="Number of Copies";
if(fcontact==true)	    		colHeads[++l]="Links Utilized";
if(fDP==true)		   		colHeads[++l]="Delivery Rate";

//Giving size to tempData
tempData=new Object[RP_Performance.AcountRows+1][countCols+1];

//Asigning values to tempData
//Displaying average information
if(average==true)
{
  rpp.lmetrics.setText("Performance Comparision of Routing Protocols: Avergae Results");
    for(int i=0;i<(RP_Performance.AcountRows+1);i++)
    {
    int j=0;
    tempData[i][j]=RP_Performance.avgData[i][0];
    if(flatency==true)			tempData[i][++j]=RP_Performance.avgData[i][1];
    if(fload==true)			tempData[i][++j]=RP_Performance.avgData[i][2];
    if(fcontact==true)	    		tempData[i][++j]=RP_Performance.avgData[i][3];
    if(fDP==true)			tempData[i][++j]=RP_Performance.avgData[i][4];
    }
}

else
{

rpp.lmetrics.setText("Performance Comparision of Routing Protocols: Standard Deviation");

    for(int i=0;i<(RP_Performance.ScountRows+1);i++)
    {
    int j=0;
    tempData[i][j]=RP_Performance.stdData[i][0];
    if(flatency==true)			tempData[i][++j]=RP_Performance.stdData[i][1];
    if(fload==true)			tempData[i][++j]=RP_Performance.stdData[i][2];
    if(fcontact==true)	    		tempData[i][++j]=RP_Performance.stdData[i][3];
    if(fDP==true)			tempData[i][++j]=RP_Performance.stdData[i][4];
    }
 }
jt=new JTable(tempData,colHeads);
int v=ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
int h=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
rpp.jsp=new JScrollPane(jt,v,h);
this.setLayout(new GridLayout(1,1));
this.add(rpp.jsp);
this.validate();
}
}

//******************************************************************************

}//End of class
