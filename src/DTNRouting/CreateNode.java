//PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;


//*****************************************************************************
// START OF CREATENODE CLASS
public class CreateNode extends dtnrouting  implements ItemListener, ActionListener, TextListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Instance variables
	public Random rand=new Random();
	public JFrame jf=new JFrame("Create Node");
	public Label nodeType=new Label("User Nodes");
	public Label nodeS=new Label("Number of Nodes");
	public Label nodesubcategory=new Label("Sub category");
	public Choice subcategory=new Choice();
	public Label sourceNodes= new Label("Source Nodes");

	public TextField source_node= new TextField("0");
	public Label desregular_node= new Label("Dest. Nodes");
	public TextField dest_node= new TextField("0");
	public TextField regular_node=new TextField("0");

	public Label lspeed=new Label("Speed(m/s)");
	public Choice cspeed=new Choice();
	public Label radiorange=new Label("Radio Range(m)");
	public Choice cradiorange=new Choice();
	public Label queuesize=new Label("QueueSize(MB)");
	public Choice cqueuesize=new Choice();
	public Button Add=new Button("Add");
	public Button Close=new Button("Close");
	public Choice cnType=new Choice();

	//In order to pass values to the object of CreateNode`
	public int numberofnodes, speedofnode, radiorangeofnode;
	public UAVPlacement uav_placement = new UAVPlacement();
	

	//other variables
	public String nameofnode;
	public DSPath objDSPath;
	//******************************************************************************

	//CONSTRUCTOR
	public CreateNode()
	{}

	public void GenerateFrame() {
		jf.setLayout(new GridLayout(10,2,5,5));

		//Add speeds to the Speed choice box
		for(int speed=0;speed<10;speed++)
			cspeed.add((speed+1)*10+""); // setting speed of the mobile node

		//Add radio ranges and queue sizes to their respective choice boxes
		for(int l=1;l<=10;l=l+1)
		{
			cradiorange.add(l+"");//add Radio Range indices
			cqueuesize.add(l +"");
		}

		cnType.add("Synthetic");
		cnType.add("Dataset");
		subcategory.add("Static"); 



		//Components in Frame window

		dest_node.setEnabled(false);
		jf.add(sourceNodes); 		jf.add(source_node); //source nodes
		jf.add(nodeType);           jf.add(cnType); //end nodes
		jf.add(nodesubcategory);    jf.add(subcategory);
		jf.add(nodeS);              jf.add(regular_node);
		jf.add(queuesize);          jf.add(cqueuesize);
		jf.add(lspeed);             jf.add(cspeed);
		jf.add(radiorange);         jf.add(cradiorange);
		jf.add(desregular_node); 	jf.add(dest_node); //source nodes
		jf.add(Add);                jf.add(Close);

		//registering events
		subcategory.addItemListener(this);
		source_node.addTextListener((TextListener) this);
		dest_node.addTextListener((TextListener) this);
		cnType.addItemListener(this);
		Add.addActionListener(this);
		Close.addActionListener(this);
		regular_node.addTextListener((TextListener) this);

		//Frame metrics
		jf.setSize(new Dimension(300,300));
		jf.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		jf.setVisible(true);
		jf.setResizable(false);

	}

	//******************************************************************************

	public void itemStateChanged(ItemEvent e)
	{
		Object b=e.getSource();
		//A regular node can be an end device, it can be either held by
		//a pedestrian or installed by a car or tram
		if(b.toString().contains("Synthetic"))
		{
			subcategory.removeAll();
			nodeS.setEnabled(true);                regular_node.setEnabled(true);
			dest_node.setEnabled(true);
			nodesubcategory.setEnabled(true);      subcategory.setEnabled(true);
			//Types of nodes in a map
			subcategory.add("Static");
			subcategory.add("Walking/Running");
			subcategory.add("Cycling");   
			subcategory.add("Driving");
		}
		// A real world dataset
		else if(b.toString().contains("Dataset"))
		{
			subcategory.removeAll();
			regular_node.setEnabled(false);
			dest_node.setEnabled(true);
			subcategory.setEnabled(true);
			subcategory.add("St.Andrew Uni"); // 25 nodes	  
			subcategory.add("iMote Traces");  // 36 nodes
			cspeed.removeAll();
			for(int j=1;j <= 15;j=j+2)
				cspeed.add(j+"");
		}

		//Movement speeds
		if(b.toString().contains("Walking/Running"))
		{
			cspeed.removeAll();
			for(int j=1;j <= 5;j=j+1)
				cspeed.add(j+"");
		}else if(b.toString().contains("Cycling"))
		{
			cspeed.removeAll();
			for(int j=10;j <= 25;j=j+2)
				cspeed.add(j+"");
		}else if(b.toString().contains("Driving"))
		{
			cspeed.removeAll();
			for(int j=40;j <= 80;j=j+10)
				cspeed.add(j+"");
		}else if(b.toString().contains("Static")){
			cspeed.removeAll();
			cspeed.add(0+"");
		}else if(b.toString().contains("St.Andrew Uni"))
			regular_node.setText("25");
		else if(b.toString().contains("iMote Traces"))
			regular_node.setText("36");


	}

	//******************************************************************************

	public void actionPerformed(ActionEvent e)
	{

		String action;
		action=e.getActionCommand();

		if(action.equals("Close"))  jf.dispose();

		//Set metrics of new node
		else if (action.equals("Add"))
		{
			// THE SOURCE NODES
			if(source_node.getText().equals("0"))
				JOptionPane.showMessageDialog(jf,"Put number of nodes");
			else
			{
				dest_node.setEnabled(true);
				for (int l=0;l<Integer.parseInt(source_node.getText());l++)
				{
					Node node=new Node();
					// Keep reliability maximum for the source node
					node.reliability=4;
					Node.ID_INCREMENTER+=1;
					node.ID=Node.ID_INCREMENTER;
					node.name="S"+node.ID;
					node.speed=0;
					dtnrouting.allSources.add(node);
					node.setRadioRange(5);
					node.wholeQueueSize=node.queueSizeLeft=500;
					dtnrouting.allNodes.add(node);
					node.nodePosition();
				}
			}


			// THE USER NODES (REGULAR)
			if(regular_node.getText().equals("0"))
				JOptionPane.showMessageDialog(jf,"Put number of nodes");
			else
			{
				first_regular_node_index=dtnrouting.allNodes.size();
				for (int l=0;l<Integer.parseInt(regular_node.getText());l++)
				{
					Node node=new Node();
					Node.ID_INCREMENTER+=1;
					node.ID=Node.ID_INCREMENTER;
					node.name="R"+ node.ID;
					node.speed=Integer.parseInt(cspeed.getSelectedItem());
					nameofnode=cnType.getSelectedItem();
					node.setRadioRange(Integer.parseInt(cradiorange.getSelectedItem()));
					//Limited buffer-size  5 times maximum packet size
					node.wholeQueueSize=node.queueSizeLeft=500;//0.02048;
					dtnrouting.allNodes.add(node);
					node.nodePosition();
					node.reliability = rand.nextInt(4)+1;
					if(node.reliability==5) {
					  node.reliability =4;}
				}  

				// If from data sets
				if(subcategory.equals("St.Andrew Uni")) 
				{
					objDSPath = new DSPath("Datasets\\SA6.txt",25);
					try {
						dtnrouting.dataset_simulation_index = rand.nextInt(dtnrouting.allNodes.get(dtnrouting.first_regular_node_index).x_coord.size());
						objDSPath.Paths_allNodes();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if(subcategory.equals("iMote Traces")) 
				{
					objDSPath = new DSPath("Datasets\\ITC6.txt",25);
					try {
						dtnrouting.dataset_simulation_index = rand.nextInt(dtnrouting.allNodes.get(dtnrouting.first_regular_node_index).x_coord.size());
						objDSPath.Paths_allNodes();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			// THE DESTINATION NODES: Choose randomly from regular user nodes
			if(dest_node.getText().equals("0"))
				JOptionPane.showMessageDialog(jf,"Put number of nodes");
			else
			{

				// Randomly choose the destination nodes
				int total_size = dtnrouting.allNodes.size();
				int s=Integer.parseInt(dest_node.getText());
				if(s > total_size)
					s = total_size;
				ArrayList<Integer> num = new ArrayList<Integer>();

				for (int i=0; i<s; i++ ) {
					int rand_number = rand.nextInt(total_size);
					//If the node is already chosen as destination
					// or the chosen node is source then do selection again
					while(num.contains(rand_number)==true || 
					dtnrouting.allNodes.get(rand_number).name.substring(0,1).equals("S")) 
						rand_number = rand.nextInt(total_size); 
					num.add(rand_number);
					Node node=dtnrouting.allNodes.get(rand_number);
					node.name = "D"+node.name.substring(1); //Rename it.
					dtnrouting.allDestinations.add(node);
					RequirementsofDestination(node);
					PacketsforDestination(node);
				}
					
			}
			

		}	   

		else if (action.equals("Clear"))  
			Resetmetrics();


	}
   
	// *****************************************************************************
	public void RequirementsofDestination(Node node) {
		node.neworkMetricRequirements[0] = rand.nextInt(10) + 1;                   // 0 = hopCount
		node.neworkMetricRequirements[1] = rand.nextDouble() * (10.0 - 1.0) + 1.0; // 1 = informationUtility 
		node.neworkMetricRequirements[2] = rand.nextDouble() * (2.0 - 0.1) + 0.1;  // 2 = bandwidth = 0.1 Mbps or 100 Kbps
		node.neworkMetricRequirements[3] = rand.nextInt(4) + 1;                    // 3 = pathIntegrity		
		
		try {
			UserRequirements_wrt_NetworkMetricValues.writeToFile(node.ID-1, node.neworkMetricRequirements[2], node.neworkMetricRequirements[0],
					node.neworkMetricRequirements[3], node.neworkMetricRequirements[1]);
		} catch (IOException e) { e.printStackTrace(); }
	}
	// *****************************************************************************

	//Packets destinations
	public void PacketsforDestination(Node node) // reset metrics of a node
	{

		// Specify the packet destined for this node
		// Move this code to when destinations are created
		node.num_packets = rand.nextInt(10)+5; //rand.nextInt(30)+10;
		node.packets_ttl = rand.nextInt(20)+40;

		//Below code generates packets for each destination				
		for(int j=0; j< node.num_packets; j++) {//number of packets that each source will transmit..
			Packet p =new Packet();
			Packet.packetID=Packet.packetID+1;
			p.packetName ="p"+Packet.packetID;
			p.maxTTL=node.packets_ttl;
			p.destNode_ofpacket=node;
			p.refreshPacketSettings();
			node.nodePackets.add(p);
			dtnrouting.arePacketsDelivered.add(p);}
    }

//******************************************************************************
    public void CreateUAV(int num_uav) {
    Node.ID_INCREMENTER = dtnrouting.allNodes.size();
    dtnrouting.uav_index =new int[num_uav];
	for (int l=0;l < num_uav;l++)
	{
		dtnrouting.uav_index[l]= dtnrouting.allNodes.size();
		
		Node node=new Node();
		Node.ID_INCREMENTER+=1;
		node.ID=Node.ID_INCREMENTER;
		node.name="U"+ node.ID;
		node.speed=0;
		nameofnode=cnType.getSelectedItem();
		node.setRadioRange(5);
		node.wholeQueueSize=node.queueSizeLeft=500;
		dtnrouting.allNodes.add(node);
		
		//Random position of uav
		// but can be updated according to old adjacency matrix
		//node.nodePosition();
		node.location = uav_placement.getUAVLocation(node, dtnrouting.allNodes);
		node.reliability = 4;
	} 
}
	//******************************************************************************

	//RESET metrics OF CREATE NODE CLASS
	public void Resetmetrics() // reset metrics of a node
	{
		regular_node.setText("0");             cnType.select(0);
		cradiorange.select(0);                 cspeed.select(0);
		cqueuesize.select(0);					source_node.setText("0");
		dest_node.setText("0");
		nodeS.setEnabled(true);                regular_node.setEnabled(true);


	}

	//******************************************************************************

	public void textValueChanged(TextEvent e)
	{
		if(e.getSource()==regular_node)
		{
			try{
				Integer.parseInt(regular_node.getText());
			}catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(jf, "Put only integer values");
				regular_node.setText("0");     }
		}
	}
	//******************************************************************************

} //END OF CREATENODE CLASS