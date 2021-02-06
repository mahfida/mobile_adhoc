package DTNRouting;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Random;
import java.util.Stack;

public class UAVPlacement {

	private double minDist_km = 6.0;
	//private double maxDist_km = 5.0;
	private double uav_speed_kph = 2.0; // 120.0
	//private double radioRange_km = 5.0;
	
	//private int bestTechnique = 0; // 0 - distance; 1 - Capacity:Distance ; 2 - Capacity:SINR
	//private static int nMovesUAV = 0;
	
//********************************************************************		
	// Distance between two locations
	double calcDistance_km(Location l1, Location l2) {
		double diffX = (l1.x-l2.x)*(l1.x-l2.x);
		double diffY = (l1.y-l2.y)*(l1.y-l2.y);
		
		double distanceKm = Math.sqrt(diffX + diffY);
		return distanceKm;
	}

//********************************************************************	
	//This methods returns the location for uav "thisUAV"
	// mid-point of largest empty space between two nodes 
	public Location getUAVLocation(Node thisUAV, ArrayList<Node> Nodes) {
		Location bestPoint = getLargestCircle(thisUAV, Nodes);
		return bestPoint;
	}
//********************************************************************		
	// This method seems to identify location of the closest uav to "thisUAV"
	// but does not use the information and instead switches to find 
	// mid point of largest empty circle
	// " AN INCOMPLETE FUNCTION"
	public Location getNewUAVLocationsDistances1(Node thisUAV, Location thisLocation, ArrayList<Node> Nodes) {


		//Location closestLocation = thisLocation;
		double closestUAVDist_km = Double.MAX_VALUE;
		Location closestUAV = thisLocation;
		int nUAVs = 0;
		/* Take into consideration other UAVs locations 
		 * , identify the UAV closet to this UAV*/
		for (Node destUAV : Nodes) {	
			if (destUAV.isUAV()) {
				nUAVs++;
				double dist = calcDistance_km(thisLocation, destUAV.getLocation());	
				if (dist < closestUAVDist_km && dist>0.0) {
					closestUAVDist_km = dist;
					closestUAV = destUAV.getLocation();
				}
			}
		}

		Location bestPoint = getLargestCircle(thisUAV, Nodes);

		return bestPoint;

	}

//********************************************************************	
	// Current code does not do anything
	/**
	 * This routine takes into consideration the nearest UAVS and Hull point to get the next optimal
	 * @param thisUAV
	 * @param thisLocation
	 * @param Nodes
	 * @param printListLimit
	 * @return
	 */
	public Location getNewUAVLocationsDistancesActual(Node thisUAV, Location thisLocation, ArrayList<Node> Nodes, int printListLimit) {


		Location closestLocation = thisLocation;
		double closestUAVDist_km = Double.MAX_VALUE;
		Location closestUAV = thisLocation;
		int nUAVs = 0;
		/* Take into consideration other UAVs locations */
		// It finds nearest uav to "thisUAV"
		for (Node destUAV : Nodes) {	
			if (destUAV.isUAV()) {
				nUAVs++;
				double dist = calcDistance_km(thisLocation, destUAV.getLocation());	
				if (dist < closestUAVDist_km && dist>0.0) {
					closestUAVDist_km = dist;
					closestUAV = destUAV.getLocation();
				}
			}
		}

		boolean toNode = true;

		setMinDist_km(0.0);
		if (closestUAVDist_km < getMinDist_km()) { // Firstly don't get too close to another Unit 
			toNode = false; 
//			closestLocation = Location.updatePosition(closestLocation, closestUAV, getMinDist_km(), toNode);
		}
		else {
//			closestLocation = Location.gotoNewLocation(thisUAV.getLocation(), bestPoint, getUav_speed_kph(), SimRun.getSim().getSimP().getUavupdatetimeSecs()/3600.0);
		}
		//	closestLocation = bestPoint; 
		return closestLocation;

	}
	

//********************************************************************
	
	/**
	 * Get the longest link between units' closest neighbours
	 * @param thisUAV
	 * @param Nodes
	 * @param nUAVs
	 * @return
	 */
	public Location getLongestCLosestLink(Node thisUAV, ArrayList<Node> Nodes, int nUAVs) {
		/* Get the largest minimum distance between two units */
		double maxDistance_km = 0.0;
		double networkDistance_km = 0.0;
		Location bestPoint = null;
		for (Node Unit1 : Nodes) {
			//	if (!Unit1.isUAV()) 
			if (Unit1 != thisUAV) {
				double minDistance_km =  Double.MAX_VALUE;
				Location bestMinPoint = null;
				for (Node Unit2 : Nodes) {	
					if (Unit1 != Unit2) {
						//		if (!Unit2.isUAV())
						if(Unit2 != thisUAV) {
							double dist = calcDistance_km(Unit1.getLocation(), Unit2.getLocation());	
							if (dist > networkDistance_km )  networkDistance_km = dist;
							if (dist < minDistance_km) {
								minDistance_km = dist;
								bestMinPoint = Unit1.getLocation().getMidpoint(Unit2.getLocation());
							}
						}
					}
				}
				if (minDistance_km >= maxDistance_km) {
					maxDistance_km = minDistance_km;
					bestPoint= bestMinPoint;
				}
			}

		}

		setMinDist_km(maxDistance_km/((double) (nUAVs+1))/2);
		//	minDist_km = 0.0;
		//		minDist_km = networkDistance_km/((double) (nUAVs+1));
		//		minDist_km = maxDistance_km;
		return (bestPoint);
	}

//********************************************************************	
	//The method is not doing anything.
	/**
	 * This routine takes the distance between units to get the new location
	 * @param thisUAV
	 * @param thisLocation
	 * @param Nodes
	 * @param GroupHull
	 * @param useHull
	 * @return
	 */
	public Location getNewUAVLocationsDistances(Node thisUAV, Location thisLocation, 
			                                    ArrayList<Node> Nodes, Stack<Location> GroupHull, 
			                                    boolean useHull) {
		Location closestLocation = thisLocation;
		double closestUAVDist_km = Double.MAX_VALUE;
		Location closestUAV = thisLocation;

		/* Take into consideration other UAVs locations */
		for (Node destUAV : Nodes) {	
			if (destUAV.isUAV()) {
				double dist = calcDistance_km(thisLocation, destUAV.getLocation());	

				if (dist < closestUAVDist_km && dist>0.0) {
					closestUAVDist_km = dist;
					closestUAV = destUAV.getLocation();
				}
			}
		}

		if (closestUAVDist_km < getMinDist_km()) { // Firstly don't get too close to another Unit 
			boolean toNode = false; 
//			closestLocation = Location.updatePosition(closestLocation, closestUAV, getMinDist_km(), toNode);
			closestLocation = closestLocation;
		}
		return closestLocation;
	}


//********************************************************************	
	
	/**
	 * This routine takes into consideration the nearest UAVS and Hull point to get the next optimal
	 * @param thisUAV
	 * @param thisLocation
	 * @param Nodes
	 * @param GroupHull
	 * @param useHull
	 * @return
	 */
	public Location getNewUAVLocationsDistancesActualOld(Node thisUAV, Location thisLocation, 
														ArrayList<Node> Nodes, Stack<Location> GroupHull, 
														boolean useHull) {


		Location closestLocation = thisLocation;
		double closestUAVDist_km = Double.MAX_VALUE;
		Location closestUAV = thisLocation;
		int nUAVs = 0;
		/* Take into consideration other UAVs locations */
		for (Node destUAV : Nodes) {	
			if (destUAV.isUAV()) {
				nUAVs++;
				double dist = calcDistance_km(thisLocation, destUAV.getLocation());	
				if (dist < closestUAVDist_km && dist>0.0) {
					closestUAVDist_km = dist;
					closestUAV = destUAV.getLocation();
				}
			}
		}
		//		Location bestPoint = getLongestCLosestLink(thisUAV, Nodes, nUAVs);
		Location bestPoint = getLargestCircle(thisUAV, Nodes);

		/* Take into consideration the convex hull */
		double closestHullDist_km = Double.MAX_VALUE;
		Location closestHull = thisLocation;
		if (useHull) {
			for (Location hullPt : GroupHull) {
				double dist =calcDistance_km(thisLocation, hullPt);	
/*				if (dist < closestHullDist_km || hullPt.getDegree()<1) {
					closestHullDist_km = dist;
					closestHull =  hullPt.getLocation();
				} */
			}
		}

		/* Take into consideration the convex hull */
		double closestUnitDist_km = Double.MAX_VALUE;
		Location closestUnit = thisLocation;

		for (Node thisUnit : Nodes) {	
			if (!thisUnit.isUAV()) {
				double dist = calcDistance_km(thisLocation, thisUnit.getLocation());	
				if (dist < closestUAVDist_km && dist>0.0) {
					closestUnitDist_km = dist;
					closestUnit = thisUnit.getLocation();
				}
			}
		}


		/* Take into consideration the convex hull */
		double closestUnitAll_km = Double.MAX_VALUE;
		Location closestAll = thisLocation;

		for (Node thisUnit : Nodes) {	
			double dist = calcDistance_km(closestLocation, thisUnit.getLocation());	
			if (dist < closestUnitAll_km && dist>0.0) {
				closestUnitAll_km = dist;
				closestAll = thisUnit.getLocation();
			}
		}

		double moveDist_km =  Double.MAX_VALUE;
		boolean toNode = true;

		if (closestUAVDist_km < minDist_km) { // Firstly don't get too close to another Unit 
			toNode = false; 
//			closestLocation = Location.updatePosition(closestLocation, closestUAV, minDist_km, toNode);
			closestLocation = closestLocation;
		}
		/*		else if (closestUnitAll_km < minDist_km) { // Firstly don't get too close to another Unit 
			toNode = false; 
			closestLocation = updatePosition(closestLocation, closestAll, minDist_km, toNode);
		} */

		else {
			//			closestLocation  = midPoint(closestHull, closestUnit);
			closestLocation = bestPoint;
		}
		//	closestLocation = bestPoint;

		/*				

		if (closestUAVDist_km < minDist_km) { // Firstly don't get to close to another UAV 
			closestLocation = closestUAV;
			moveDist_km = minDist_km;
			toNode = false; 
			closestLocation = updatePosition(thisLocation, closestLocation, moveDist_km, toNode);
		}
		else if (closestHullDist_km < minDist_km) {
				closestLocation = closestHull;
				moveDist_km = minDist_km;
				toNode = false;
				closestLocation = updatePosition(thisLocation, closestLocation, moveDist_km, toNode);
		} else  {
		//	closestLocation = closestHull;
	
		//	moveDist_km = closestHullDist_km;
		}
		*/

		return closestLocation;

	}

//********************************************************************	
	// Finds the largest distance between two set of nodes
	// such that their is no node between them
	public Location getLargestCircle(Node thisNode, ArrayList<Node> Nodes) {
		/* Get the largest minimum distance between two units */

		ArrayList<Node> usedNodes = new ArrayList<Node>();
		for (Node unit : Nodes) {
			if (unit != thisNode) {
				usedNodes.add(unit);
			}	
		}

		double biggestCircle_km = 0.0;
		Location bestPoint = null;
		for (Node unit1 : usedNodes) {
			for (Node unit2 : usedNodes) {	
				if (unit1 != unit2) {	
					double dist = calcDistance_km(unit1.getLocation(), unit2.getLocation());	
					if (dist > biggestCircle_km )  {
						Location mp = unit1.getLocation().getMidpoint(unit2.getLocation());
						boolean emptyCircle = isEmptyCircle(unit1, unit2, usedNodes, mp, dist/2.0);
						if (emptyCircle) {
							biggestCircle_km = dist;
							bestPoint= mp;
						}
					} 
				}
			}

		}
//		minDist_km = Math.max(biggestCircle_km/((double) (numNodes+1))/2, 0.2);
		return bestPoint;
	}
	
//********************************************************************	
	// Find if the circle with center "Center" and radius "radius_km"
	// has no other node and is empty.
	public boolean isEmptyCircle(Node Unit1, Node Unit2, ArrayList<Node> usedNodes, Location Centre, double radius_km) {
		boolean empty = true;
		for (Node Unit : usedNodes) {
			if ((Unit != Unit1) &&  (Unit != Unit2)) {
				double dist = calcDistance_km(Centre, Unit.getLocation());	
				if (dist < radius_km) return false;
			}	
		}
		return empty;
	}

//********************************************************************		
	public double getUav_speed_kph() {
		return uav_speed_kph;
	}

	public void setUav_speed_kph(double uav_speed_kph) {
		this.uav_speed_kph = uav_speed_kph;
	}

	public double getMinDist_km() {
		return minDist_km;
	}

	public void setMinDist_km(double minDist_km) {
		this.minDist_km = minDist_km;
	}
	
	
	
}

