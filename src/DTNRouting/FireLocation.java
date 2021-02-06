package DTNRouting;

import java.util.ArrayList;
import java.util.Random;

public class FireLocation {
	//******************************************************************************

	//EMPTY CONSTRUCTOR
	
	public static int points = 0;
	
	public static ArrayList<Double> x_fire = new ArrayList<Double>();
	public static ArrayList<Double> y_fire = new ArrayList<Double>();
	public static ArrayList<Double> tmp_x_fire = new ArrayList<Double>();
	public static ArrayList<Double> tmp_y_fire = new ArrayList<Double>();
	public static int radius = 2;
	FireMode fire_model =new FireMode();
	Random rand=new Random();
	public  FireLocation() {

				
	}

	//******************************************************************************
	public void firePosition() {
	points = rand.nextInt(10)+25;
	for(int i =0;i < points;i++) {
		x_fire.add(dtnrouting.x_start + (dtnrouting.width - 0) * rand.nextDouble());
		y_fire.add(dtnrouting.y_start + (dtnrouting.height - 0) * rand.nextDouble());//+this.getRadioRange();
		
	
	}}
	
	public void fireSpread() {
		tmp_x_fire.clear();	
		tmp_y_fire.clear();
		
		double fire_spread= 2;// fire_model.call_allFunctions();
		//System.out.println("fs"+fire_spread);
	    this.radius = this.radius+(int)fire_spread;
	    
		//for(int i =0;i < points;i++) {
			
			//++
			/*
			 * tmp_x_fire.add(x_fire.get(i) + (int) fires_spread);
			 * tmp_y_fire.add(y_fire.get(i) + (int) fires_spread); //--
			 * tmp_x_fire.add(x_fire.get(i) - (int) fires_spread);
			 * tmp_y_fire.add(y_fire.get(i) - (int) fires_spread); //+-
			 * tmp_x_fire.add(x_fire.get(i) + (int) fires_spread);
			 * tmp_y_fire.add(y_fire.get(i) - (int) fires_spread); //-+
			 * tmp_x_fire.add(x_fire.get(i) - (int) fires_spread);
			 * tmp_y_fire.add(y_fire.get(i) + (int) fires_spread);
			 */
		//}
		
		/*
		 * x_fire.clear(); y_fire.clear();
		 * 
		 * x_fire=(ArrayList<Double>) tmp_x_fire.clone();
		 * y_fire=(ArrayList<Double>)tmp_y_fire.clone();
		 * 
		 * System.out.println("last:"+x_fire.size()); points= x_fire.size();
		 */
		
	}
	public void deadNodes()
	{
		boolean hasAnyNodeDead=false;

	     for(int n=0; n < dtnrouting.allNodes.size();n++) {
	    	Node node= dtnrouting.allNodes.get(n);
	    	if(!dtnrouting.deadNodes.contains(node)) {
	    	for(int i =0;i < points;i++) {
	        double x = x_fire.get(i);
	        double y = y_fire.get(i);
	 	    double d = Math.sqrt(Math.pow((x- node.location.x),2) + Math.pow((y- node.location.y),2));
            //System.out.println(d+":"+"radius:"+radius);
	 	    if(d<=radius) {
	 	    	dtnrouting.deadNodes.add(node);
	 	    	System.out.println("\nNode "+node.name+" is dead");
	 	    	
	 	    	dtnrouting.liveNodes.remove(node);
	 	    	if(node.name.substring(0, 1).equals("S"))
	 	    		dtnrouting.liveSources.remove(node);
	 	    	if(node.name.substring(0, 1).equals("D"))
	 	    		dtnrouting.liveDestinations.remove(node);
	 	    	hasAnyNodeDead=true;
	 	    	break;}

	 	    	
	    	}}
	    	
	 	    }
	     //System.out.println("allnodes:"+dtnrouting.allNodes.size());
	     //System.out.println("livenodes:"+dtnrouting.liveNodes.size());
	     //System.out.println("deadnodes:"+dtnrouting.deadNodes.size());
	     
	     // Even if a single node dies, Reset the paths
	     if(hasAnyNodeDead & dtnrouting.SIMULATION_PART!=1) {
	     QoITPATH pathObj = new QoITPATH ();
	     pathObj.ShortestPathsSD();
	     pathObj.changeSource();}
}
	

}// End of class
