package DTNRouting;


public class Location {
		public double x;
		public double y;

		public Location() {}
	   
		public Location(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public Location getMidpoint(Location pt2) {
			Location mPt = new Location((x+pt2.x)/2.0, (y+pt2.y)/2.0);
			return mPt;
		}
		
		public String toString() {
			String result = Double.toString(x) + ":" + Double.toString(y);
			return result;
		}
}
