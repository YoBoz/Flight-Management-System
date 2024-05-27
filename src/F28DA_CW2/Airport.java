package F28DA_CW2;

import java.util.HashSet;
import java.util.Set;

public class Airport implements IAirportPartB, IAirportPartC {

	// Instance variables
	private String airportCode;
	private String airportName;
	private Set<Airport> DirectlyConnected;
	private int DirectlyConnectedOrder;

	// Constructor for Airport class
	public Airport(String apcode, String apname) {
		this.airportCode = apcode;
		this.airportName = apname;
		this.DirectlyConnected = new HashSet<>();
		this.DirectlyConnectedOrder = 0;
	}

	@Override
	// Getting the airport code
	public String getCode() {
		return airportCode;
	}

	@Override
	// Getting the airport name
	public String getName() {
		return airportName;
	}

	@Override
	// Setting directly connected airports
	public void setDicrectlyConnected(Set<Airport> dicrectlyConnected) {
	    DirectlyConnected = dicrectlyConnected;

	}

	@Override
	// Getting directly connected airports
	public Set<Airport> getDicrectlyConnected() {
		return DirectlyConnected;
	}

	@Override
	// Setting directly connected order
	public void setDicrectlyConnectedOrder(int order) {
		DirectlyConnectedOrder = order;
	}

	@Override
	// Getting directly connected order
	public int getDirectlyConnectedOrder() {
		return DirectlyConnectedOrder;
	}

}
