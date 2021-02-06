package DTNRouting;
import java.util.Random;

public class FireMode {

	Random rand = new Random();
	double wind_speed = 7.3 + (26.0 - 7.3) * rand.nextDouble(); // 10-80 km/hr
	double threshold_wind_speed = 5.0;         // km/hr
	double R_t = 48.0;  // m/hr
	double bias1 = 1.03, bias2 = 1.02, bias3 = 1.07;
	double fuel_height = 1.0 + (200.0 - 1.0) * rand.nextDouble();  // 1-200 cm
	double fuel_age = 0.0 + (20.0 - 0.0) * rand.nextDouble();  // 0-20 years
	double fuel_moisture_content  = 0.03 + (0.2 - 0.03) * rand.nextDouble();  // 3%-20%
	double slope =  0.00 + (30.0 - 0.00) * rand.nextDouble();  // 0.01-30 upwards degrees
	double exponent = 2.7182818284590452353;
	double head_fireSpreadRate = 0.0 + (1500.0 - 0.0) * rand.nextDouble(); // 0-1500 meters/hour
	int    elevated_fuel_height = rand.nextInt((200 - 10) + 1) + 10;   // 10-200 cm

	// Fuel hazard scores 
	double FHS_s  = FHS_s(fuel_age); //surface
	double FHS_ns = FHS_ns(fuel_age); //near surface
	double H_ns   = H_ns(fuel_age); // height

	// Functions variables
	double R_A_FHS_var, fuel_moisture_function_var,slope_function_var, fire_spread_rate_FHS_var, flame_height_var;
	

	public double call_allFunctions()
	{
		this.R_A_FHS_var = R_A_FHS(R_t, wind_speed, threshold_wind_speed, FHS_s, FHS_ns, H_ns, bias1); // adjusted rate of spread
		this.fuel_moisture_function_var = fuel_moisture(fuel_moisture_content);	// fule moister
		this.slope_function_var = slope(slope, exponent); 
		this.fire_spread_rate_FHS_var = fire_spread_rate_FHS(this.R_A_FHS_var, this.fuel_moisture_function_var, this.slope_function_var);
		//this.flame_height_var = flame_height(exponent, fuel_height, bias3, head_fireSpreadRate);
		return (this.fire_spread_rate_FHS_var);
	}
	
	//Sub functions
	public static double fuel_moisture(double fuel_moisture_content) {
		return ((18.35 * Math.pow(fuel_moisture_content, -1.495)));
	}

	public static double slope(double slope, double exponent) {
		return Math.pow(exponent, (0.069 * slope));
	}

	// Flame height
	public static double flame_height(double exponent, double fuel_height, double bias3, double head_fireSpreadRate) {
		return (0.0193 * Math.pow(head_fireSpreadRate, 0.723) * Math.pow(exponent, (0.0064 * fuel_height)) * bias3);
	}

	// Fuel Hazard Score on Surface (FHS_s)
	public static double FHS_s(double fuel_age) { 
		double FHS_s = 0.0;
		if(Double.compare(fuel_age, 3.0) < 0)
			FHS_s = 2.0;
		else if ((Double.compare(fuel_age, 4.0) == 0) || (Double.compare(fuel_age, 4.0) > 0 && Double.compare(fuel_age, 5.0) < 0) || Double.compare(fuel_age, 5.0) == 0)
			FHS_s = 2.5;
		else if ((Double.compare(fuel_age, 6.0) == 0) || (Double.compare(fuel_age, 6.0) > 0 && Double.compare(fuel_age, 10.0) < 0) || Double.compare(fuel_age, 10.0) == 0)
			FHS_s = 3.0;
		else if (Double.compare(fuel_age, 10.0) > 0)
			FHS_s = 3.5;	
		return FHS_s;
	}

	// Fuel Hazard Score on Near-Surface (FHS_ns)
	public static double FHS_ns(double fuel_age) { 
		double FHS_ns = 0.0;
		if(Double.compare(fuel_age, 3.0) < 0)
			FHS_ns = 1.5;
		else if ((Double.compare(fuel_age, 4.0) == 0) || (Double.compare(fuel_age, 4.0) > 0 && Double.compare(fuel_age, 5.0) < 0) || Double.compare(fuel_age, 5.0) == 0)
			FHS_ns = 2.0;
		else if ((Double.compare(fuel_age, 6.0) == 0) || (Double.compare(fuel_age, 6.0) > 0 && Double.compare(fuel_age, 10.0) < 0) || Double.compare(fuel_age, 10.0) == 0)
			FHS_ns = 2.5;
		else if (Double.compare(fuel_age, 10.0) > 0)
			FHS_ns = 3.0;	
		return FHS_ns;
	}

	// Fuel Height Near-Surface (H_ns)
	public static double H_ns(double fuel_age) { 
		double H_ns = 0.0;
		if(Double.compare(fuel_age, 3.0) < 0)
			H_ns = 15.0;
		else if ((Double.compare(fuel_age, 4.0) == 0) || (Double.compare(fuel_age, 4.0) > 0 && Double.compare(fuel_age, 5.0) < 0) || Double.compare(fuel_age, 5.0) == 0)
			H_ns = 17.5;
		else if ((Double.compare(fuel_age, 6.0) == 0) || (Double.compare(fuel_age, 6.0) > 0 && Double.compare(fuel_age, 10.0) < 0) || Double.compare(fuel_age, 10.0) == 0)
			H_ns = 20.0;
		else if (Double.compare(fuel_age, 10.0) > 0)
			H_ns = 25.0;	
		return H_ns;
	}

	// R_A w.r.t Fuel Hazard Score (1st Model)
	public static double R_A_FHS(double threshold_fire_spread_rate, double wind_speed, double threshold_wind_speed, double FHS_s, double FHS_ns, double H_ns, double bias1) {		
		return (threshold_fire_spread_rate + (1.5308 * Math.pow((wind_speed - threshold_wind_speed), 0.8576) * Math.pow(FHS_s, 0.9301) * Math.pow((FHS_ns * H_ns), 0.6366) * bias1));		
	}

	// Potential quasi-steady rate of fire spread w.r.t FHS (in meters/hour)
	public static double fire_spread_rate_FHS(double R_A_FHS, double fuel_moisture_function, double slope_function) {		
		return (R_A_FHS * fuel_moisture_function * slope_function);		
	}

	// FHR_s to surface_fuel_coefficient mapping
	public static double FHRs_surfaceFuelCoefficient_mapping(double FHR_s) {
		double surface_fuel_coefficient = 0.0;

		if(Double.compare(FHR_s, 1.0) == 0)      surface_fuel_coefficient = 0.0;
		else if(Double.compare(FHR_s, 2.0) == 0) surface_fuel_coefficient = 1.5608;
		else if(Double.compare(FHR_s, 3.0) == 0) surface_fuel_coefficient = 2.1412;
		else if(Double.compare(FHR_s, 4.0) == 0) surface_fuel_coefficient = 2.0548;
		else if(Double.compare(FHR_s, 5.0) == 0) surface_fuel_coefficient = 2.3251;

		return surface_fuel_coefficient;
	}
	
	// FHR_ns to nearSurface_fuel_coefficient mapping
	public static double FHRns_nearSurfaceFuelCoefficient_mapping(double FHR_ns) {
		double nearSurface_fuel_coefficient = 0.0;

		if(Double.compare(FHR_ns, 1.0) == 0)      nearSurface_fuel_coefficient = 0.4694;
		else if(Double.compare(FHR_ns, 2.0) == 0) nearSurface_fuel_coefficient = 0.7070;
		else if(Double.compare(FHR_ns, 3.0) == 0) nearSurface_fuel_coefficient = 1.2772;
		else if(Double.compare(FHR_ns, 4.0) == 0) nearSurface_fuel_coefficient = 1.7492;
		else if(Double.compare(FHR_ns, 5.0) == 0) nearSurface_fuel_coefficient = 1.2446;

		return nearSurface_fuel_coefficient;
	}
}
