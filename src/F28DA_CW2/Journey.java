package F28DA_CW2;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {
	// Instance variables
	private GraphPath<Airport, Flight> path;

	// Constructor for Journey class
	public Journey(GraphPath<Airport, Flight> path) {
		this.path = path;
	}

	// Getting list of airport codes for all stops in the journey
	@Override
	public List<String> getStops() {
		// List to hold the airport codes
		List<String> stopCodes = new ArrayList<String>();
		// Get the list of airports from the path
		List<Airport> stops = path.getVertexList();

		// Iterate through airports and add airport code to list of airport codes
		for (int i = 0; i < stops.size(); i++) {
			stopCodes.add(stops.get(i).getCode());
		}
		// Return stopCodes
		return stopCodes;
	}

	// Getting list of flight codes for all flights in the journey
	@Override
	public List<String> getFlights() {
		// List to hold the flight codes
		List<String> flightCodes = new ArrayList<String>();
		// List of flights from the path
		List<Flight> flights = path.getEdgeList();

		// Iterate through flights and add flight code to list of flight codes
		for (int i = 0; i < flights.size(); i++) {
			flightCodes.add(flights.get(i).getFlightCode());
		}
		// Return flightCodes
		return flightCodes;
	}

	// Getting total number of flights (hops) in the journey
	@Override
	public int totalHop() {
		return path.getEdgeList().size();
	}

	// Getting total cost of the journey
	@Override
	public int totalCost() {
		// Variable to store total cost
		int totalCost = 0;
		// Getting flights from path
		List<Flight> flights = path.getEdgeList();

		// Iterate through flights and add their cost to totalCost
		for (int i = 0; i < flights.size(); i++) {
			// Getting the current flight
			Flight flight = flights.get(i);
			// Adding cost of the flight to totalCost
			totalCost += flight.getCost();
		}

		// Return totalCost
		return totalCost;
	}

	// Getting the total time in air
	@Override
	public int airTime() {
		// variable to store total air time
		int totalAirTime = 0;

		// Getting flights from path
		List<Flight> flights = path.getEdgeList();

		// Iterate through flights
		for (int i = 0; i < flights.size(); i++) {
			// Get the current flight
			Flight flight = flights.get(i);

			// Get the departure and arrival times
			String departureTime = flight.getFromGMTime();
			String arrivalTime = flight.getToGMTime();

			// Converting hour and minute values from the departure time
			int departureHour = Integer.parseInt(departureTime.substring(0, 2));
			int departureMinute = Integer.parseInt(departureTime.substring(2));

			// Converting hour and minute values from the arrival time
			int arrivalHour = Integer.parseInt(arrivalTime.substring(0, 2));
			int arrivalMinute = Integer.parseInt(arrivalTime.substring(2));

			// Calculating the difference in hours and minutes
			int hours = arrivalHour - departureHour;
			int minutes = arrivalMinute - departureMinute;

			// If the arrival time is earlier than the departure time(ie.arrival is after
			// midnight)
			if (hours < 0 || (hours == 0 && minutes < 0)) {
				hours += 24;
			}

			// Adjusting minutes and hours if the difference in minutes is negative
			if (minutes < 0) {
				minutes += 60;
				hours--;
			}

			// Updating total air time
			totalAirTime += hours * 60 + minutes;
		}

		// Return totalAirTime
		return totalAirTime;
	}

	// Getting total connecting time between each flights
	@Override
	public int connectingTime() {
		// Variable to store total connecting time
		int totalConnectingTime = 0;
		// // Getting flights from path
		List<Flight> flights = path.getEdgeList();

		// Iterate through flights, except last flight
		for (int i = 0; i < flights.size() - 1; i++) {
			// Getting arrival time of current flight
			String current = flights.get(i).getToGMTime();
			// Getting departure time of next flight
			String next = flights.get(i + 1).getFromGMTime();

			// Converting hour and minute values from arrival time
			int currentHour = Integer.parseInt(current.substring(0, 2));
			int currentMinute = Integer.parseInt(current.substring(2));

			// Converting hour and minute values from departure time
			int nextHour = Integer.parseInt(next.substring(0, 2));
			int nextMinute = Integer.parseInt(next.substring(2));

			// Calculating the total number of minutes for the arrival and departure times
			int currentMinutes = (currentHour * 60) + currentMinute;
			int nextMinutes = (nextHour * 60) + nextMinute;

			// Calculating the duration between the arrival and departure times
			int duration;
			// Checking which is greater and performing required calculations
			if (currentMinutes > nextMinutes) {
				duration = ((24 * 60) - (currentMinutes - nextMinutes));
			}

			else {
				duration = (nextMinutes - currentMinutes);
			}

			// Updating total connecting time
			totalConnectingTime += duration;
		}

		// Returning totalConnectingTime
		return totalConnectingTime;
	}
	
	// Getting the total time of a journey
	@Override
	public int totalTime() {
		// Returning the total time
		return airTime() + connectingTime();
	}
	
	// Getting the airmiles earned
	@Override
	public int totalAirmiles() {
		// Getting the air time
		int totalAirTime = airTime();
		// Getting the total cost
		int totalCost = totalCost();
		// Calculating air miles using the specs provided
		int airmiles = (int) (((totalAirTime * (totalCost * 0.03))));
		// Returning the air miles
		return airmiles;
	}

}
