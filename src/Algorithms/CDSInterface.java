/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithms;

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import Support.DatasetFile;
import javax.swing.table.DefaultTableColumnModel;

//////////////////////////
import javax.swing.JTable;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;

public class CDSInterface extends JFrame implements Runnable,ItemListener,ActionListener{
  /*   static double A[][]={{0,1,1,0,0,0,0,0,0,0,0,0,0,0},{1,0,1,0,0,0,0,0,0,0,0,0,0,0},{1,1,0,0,0,0,1,0,0,0,0,0,0,0},
 {0,0,0,0,1,1,0,0,0,0,0,0,0,0},{0,0,0,1,0,1,0,0,0,0,0,0,0,0},{0,0,0,1,1,0,1,0,0,0,0,0,0,0},{0,0,1,0,0,1,0,1,0,0,0,0,0,0},
 {0,0,0,0,0,0,1,0,1,0,0,1,0,0},{0,0,0,0,0,0,0,1,0,1,1,0,0,0},{0,0,0,0,0,0,0,0,1,0,1,0,0,0},{0,0,0,0,0,0,0,0,1,1,0,0,0,0},
 {0,0,0,0,0,0,0,1,0,0,0,0,1,1},{0,1,1,0,0,0,0,0,0,0,0,1,0,1},{0,1,1,0,0,0,0,0,0,0,0,1,1,0}};*/

    static int size, k;
   //Four Panels
    static double[][] A;
    String s="Karate Club";
    Panel titlePanel=new Panel();
    Panel settingsPanel=new Panel();
    Panel clusterPanel=new Panel();
    Panel resultPanel=new Panel();
    BorderLayout b1=new BorderLayout(10,10);
    Button run=new Button("Run");
    Button clear=new Button("Clear All");
    String imagePath="..\\Images\\abc.png";
    BaseCD basecd;
    //*************************************
    //String Metric
    static String algoresult;
    static String modresult;
    static String condresult;
    static String expresult;
    static String comresult;


    //Labels for Panels
    Label titleLabel=new Label("Comparison of Clustering Schemes w.r.t Social Networks");
    Label cd=new Label("Cluster Division");
    Label settingsLabel=new Label("Select Algorithm and Dataset");
    Label resultLabel=new Label("Results");
    ImageIcon sn=new ImageIcon("..\\Images\\a.jpg");
    JLabel snlabel=new JLabel(sn);
    ImageIcon dg=new ImageIcon("..\\Images\\b.jpg");
    JLabel dglabel=new JLabel(dg);
    ImageIcon mc=new ImageIcon("..\\Images\\c.png");
    JLabel mclabel=new JLabel(mc);
    Label algo=new Label("Algorithm");
    //Result Panel Labels
    Label ds=new Label("Dataset");
    Label dataset=new Label("Dataset");
    Label cNumber=new Label("Number of Clusters");
    Choice cluster=new Choice();
    Object columnNames[]={"Algorithm","Modularity","Complexity","Expansion","Conductance"};
    int row=0; int col=0;
    Object rowData[][]=new Object[9][5];
    JTable resultTable=new JTable(rowData,columnNames);
    JScrollPane jsp;
    String[] algorithms = { "GirvanNewman", "NSpectral", "UnNSpectral", "SymNSpectral",

    "KLAlgorithm","HAC/SL","HAC/CL","HAC/GAL","HAC/CTL"};
    String[] datasets = { "Karate Club", "Les Miserables", "CS/SBBWUP","Third Grade"};
    Choice algorithmsList = new Choice();
    Choice datasetList = new Choice();
    static public Object colNames[]={"Cluster#","Cluster Members"};
    static public Object rowNo[][]=new Object[15][2];
    static public JTable   clusterTable=new JTable(rowNo,colNames);
    static public int r=0,c=0;
    static JScrollPane jsp1;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //***************************************************************************
     public CDSInterface(){
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setBounds(0,0,screenSize.width, screenSize.height);
     setVisible(true);
     this.setLayout(b1);
     //Settings for titlePanel
     titleLabel.setFont(new Font("Dialog",Font.BOLD,30));
     titlePanel.add(titleLabel);
     titlePanel.add(snlabel);
     titlePanel.setBackground(Color.yellow);
     titlePanel.setPreferredSize(new Dimension(screenSize.width, 100));
     add(titlePanel,BorderLayout.NORTH);
     //Adding data sets to choices
     for(int i=0; i<4; i++){
         datasetList.add(datasets[i]);
     }
     for(int i=0; i<9; i++){
         algorithmsList.add(algorithms[i]);
     }
     datasetList.addItemListener(this);
     algorithmsList.addItemListener(this);

     //Settings for settingsPanel
     settingsPanel.setFont(new Font("Arial",Font.BOLD,12));
     settingsPanel.add(settingsLabel);
     settingsPanel.add(dglabel);
     settingsPanel.add(mclabel);
    ////////////Datasets
     settingsPanel.add(ds);
     datasetList.setFont(new Font("Arial",Font.PLAIN,12));
     settingsPanel.add(datasetList);
     ///////////Clustr Number
     settingsPanel.add(cNumber);
     for(int i=2;i<10;i++)
         cluster.add(""+i);
     settingsPanel.add(cluster);
     cluster.addItemListener(this);
     ///////////Algorithms
     settingsPanel.add(algo);
     algorithmsList.setFont(new Font("Arial",Font.PLAIN,12));
     settingsPanel.add(algorithmsList);
     ///////////Run Button
     settingsPanel.add(run);
     run.addActionListener((ActionListener)this);
      // Clear Button
     settingsPanel.add(clear);
     clear.addActionListener(this);
     settingsPanel.setBackground(Color.cyan);
     settingsPanel.setPreferredSize(new Dimension(220,screenSize.height-100));
     add(settingsPanel,BorderLayout.WEST);
    
      //Settings for resultPanel
      resultPanel.setBackground(Color.ORANGE);
      resultPanel.setPreferredSize(new Dimension(screenSize.width, 100));
      add(resultPanel,BorderLayout.SOUTH);
      resultPanel.setFont(new Font("Arial",Font.BOLD,12));
      BorderLayout rB=new BorderLayout();
      resultPanel.setLayout(rB);
      resultPanel.add(resultLabel,BorderLayout.WEST);
      jsp=new JScrollPane(resultTable);
      resultPanel.add(jsp, BorderLayout.CENTER);

      //Setting for Cluster Panel
     
      
      clusterPanel.setForeground(Color.BLACK);
      //Resizing table width
      BorderLayout br1=new BorderLayout();
      clusterPanel.setLayout(br1);
      clusterPanel.setBackground(Color.PINK);
      clusterPanel.setFont(new Font("Arial",Font.BOLD,16));
      clusterPanel.add(cd,BorderLayout.NORTH);

      //clusterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      //clusterTable.getColumnModel().getColumn(0).setPreferredWidth(27);
      //clusterTable.getColumnModel().getColumn(1).setPreferredWidth(120);

      //Resizing Cluster Column Widths
     clusterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
     for (int i = 0; i < clusterTable.getColumnCount(); i++) {
         int w=i*400+50;
      DefaultTableColumnModel colModel = (DefaultTableColumnModel) clusterTable.getColumnModel();
      TableColumn col = colModel.getColumn(i);
      int width = 0;

      TableCellRenderer renderer = col.getHeaderRenderer();
      for (int r = 0; r < clusterTable.getRowCount(); r++) {
        renderer = clusterTable.getCellRenderer(r, i);
        Component comp = renderer.getTableCellRendererComponent(clusterTable, clusterTable.getValueAt(r, i),
            false, false, r, i);
        width = Math.max(width, comp.getPreferredSize().width);
      }
         col.setPreferredWidth(width + w);
         }
   
       
//Adding Table to Panel
      jsp1=new JScrollPane(clusterTable);
      clusterPanel.add(jsp1,BorderLayout.CENTER);
      clusterPanel.setPreferredSize(new Dimension(400,screenSize.height-100));
      add(clusterPanel,BorderLayout.EAST);
    
     }
     //**************************************************************
 
     public void start(){
         Thread th=new Thread(this);
         th.start();
         
     }
     public void run() {
         
         while(true){
             repaint();

         }
     
     }
     public void stop()
    {

         this.dispose();
         //this.setVisible(false);
     }
     //************************************************************
    @Override
     public void update(Graphics g){
         paint(g);
     }
    public void paint(Graphics g) {
                Image img1=Toolkit.getDefaultToolkit().getImage(imagePath);
                g.drawImage(img1,250, 100, this);
                

  }
    

  //    public void getSelectedItem(ItemEvent ie) throws FileNotFoundException, IOException{
       // String set=(String) ie.getSource();
        //CommunityDetection.Support.DatasetFile df=new CommunityDetection.Support.DatasetFile(set);

//     }
//*******************************************************************

     public void itemStateChanged(ItemEvent e) {
       
         //Temporary Array
         double Temp[][];
          if(e.getSource().equals(cluster))
          {
              k=cluster.getSelectedIndex()+2;
                      

          }
          if(datasetList.getSelectedItem().equals("Karate Club"))
          {
           
           imagePath="..\\Images\\KC.png";
           DatasetFile ds=new DatasetFile("Karate Club");
                    try {
                        Temp = ds.getContactGrapd();
                         AssignTemp2A(Temp,34);
                        } catch (FileNotFoundException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
           }
          else if(datasetList.getSelectedItem().equals("Les Miserables"))
              {
                
                imagePath="LM.png";
                DatasetFile ds=new DatasetFile("Les Miserables");
                    try {
                        Temp = ds.getContactGrapd();
                          AssignTemp2A(Temp,77);
                        } catch (FileNotFoundException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
          }

        else if(datasetList.getSelectedItem().equals("Third Grade")){

                    imagePath="RD.png";
                    DatasetFile ds=new DatasetFile("thirdGrade");
                    try {
                        Temp = ds.getContactGrapd();
                         AssignTemp2A(Temp,22);
                    
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CDSInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }

                   
           }

          //Conditions for algorithms
          if(algorithmsList.getSelectedItem().equals("GirvanNewman")){
            GirvanNewman gn=new GirvanNewman(A, size, k);
            basecd=gn;
               
          }

         else if(algorithmsList.getSelectedItem().equals("HAC/SL"))
         {
              HAC hac1=new HAC(A,k,size,'S');
              basecd=hac1;
        
          }
          else if(algorithmsList.getSelectedItem().equals("HAC/CL"))
         {
              HAC hac2=new HAC(A,k,size,'C');
              basecd=hac2;

          }
          else if(algorithmsList.getSelectedItem().equals("HAC/GAL"))
         {
              HACGA hac4=new HACGA(A,k,size,'A');
              basecd=hac4;

          }
           else if(algorithmsList.getSelectedItem().equals("HAC/CTL"))
         {
              HAC hac3=new HAC(A,k,size,'T');
              basecd=hac3;

          }


         else if(algorithmsList.getSelectedItem().equals("NSpectral"))
         {
             NRSpecClustering nrs=new NRSpecClustering(A,size,k);
             basecd=nrs;
          }
         
         else if(algorithmsList.getSelectedItem().equals("UnNSpectral"))
         {
            USpecClustering uns=new USpecClustering(A,size,k);
            basecd=uns;

          }
         else if(algorithmsList.getSelectedItem().equals("SymNSpectral"))
         {
            NSSpecClustering nss=new NSSpecClustering(A,size,k);
            basecd=nss;

          }
         else if(algorithmsList.getSelectedItem().equals("KLAlgorithm"))
         {
            KLAlgorithm kl=new KLAlgorithm(A,size,k);
            basecd=kl;

          }

     }
     public void actionPerformed(ActionEvent ae){
        //Run the set scenario
          if(ae.getSource()==run){
              //Entry in result table
                r=0; c=0;
              //Clearing Contents of a table
                for(int i=0;i<15;i++)//rows
                    for(int j=0;j<2;j++)//columns
                        rowNo[i][j]="";
                        clusterTable.repaint();
              //Text Wrapping in Table
                 basecd.runClustering();
                rowData[row][col]=algoresult;
                rowData[row][++col]=modresult;
                rowData[row][++col]=comresult;
                rowData[row][++col]=expresult;
                rowData[row][++col]=condresult;
                row=row+1; col=0;
                resultTable.repaint();
                clusterTable.repaint();

              }
          //Clear All Entries in the GUI
         else if(ae.getSource() == clear)
         {
          clearAll();
         }

    }
     //Assigning Temporary double[][] array to A
     public void AssignTemp2A(double [][]Temp,int sz)
    {
         size=sz;
         A=new double[size][size];
         for(int i=0;i<size;i++)
          for(int j=0;j<size;j++)
          A[i][j]=Temp[i][j];
     }
     public void clearAll()
    {
         A=null;
         this.size=0;
         this.imagePath="abc.png";
         //Removeing data from cluste table
          for(int i=0;i<15;i++)//rows
                    for(int j=0;j<2;j++)//columns
                        rowNo[i][j]="";
         r=c=0;
          clusterTable.repaint();
         //Removing data from result Table
          for(int i=0;i<9;i++)//rows
                    for(int j=0;j<5;j++)//columns
                        rowData[i][j]="";
         resultTable.repaint();
         row=col=0;


     }
}
