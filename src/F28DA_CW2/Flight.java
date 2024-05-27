package F28DA_CW2;

public class Flight implements IFlight {

	// Instance variables
	private String flightCode;
	private Airport from;
	private Airport to;
	private String departureTime;
	private String arrivalTime;
	private int cost;

	// Constructor for Flight class
	public Flight(String flightCode, Airport from, String departureTime, Airport to, String arrivalTime, int cost) {
		this.flightCode = flightCode;
		this.from = from;
		this.departureTime = departureTime;
		this.to = to;
		this.arrivalTime = arrivalTime;
		this.cost = cost;
	}

	@Override
	// Getting the flight code
	public String getFlightCode() {
		return flightCode;
	}

	@Override
	// Getting the arrival location
	public Airport getTo() {
		return to;
	}

	@Override
	// Getting the departure location
	public Airport getFrom() {
		return from;
	}

	@Override
	// Getting the departure time
	public String getFromGMTime() {
		return departureTime;
	}

	@Override
	// Getting the arrival time
	public String getToGMTime() {
		return arrivalTime;
	}

	@Override
	// Getting the journey cost
	public int getCost() {
		return cost;
	}

}
