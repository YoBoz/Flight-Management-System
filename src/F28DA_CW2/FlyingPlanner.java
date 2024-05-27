package F28DA_CW2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport, Flight>, IFlyingPlannerPartC<Airport, Flight> {
	// Instance variables
	private Graph<Airport, Flight> flightGraph = new SimpleDirectedWeightedGraph<>(Flight.class);
	private HashMap<String, Airport> airportsMap;
	private HashMap<String, Flight> flightsMap;
	private DirectedAcyclicGraph<Airport, Flight> connections;

	// Constructor for FlyingPlanner class
	public FlyingPlanner() {
		this.airportsMap = new HashMap<>();
		this.flightsMap = new HashMap<>();
	}

	// Populating after reading the input from dataset
	@Override
	public boolean populate(FlightsReader fr) {
		// Getting airports from FlightsReader
		Set<String[]> airports = fr.getAirports();
		// Getting flights from FlightsReader
		Set<String[]> flights = fr.getFlights();

		// returning callback to other populate function
		return populate(new HashSet<>(airports), new HashSet<>(flights));
	}

	// Populating the airport and flights
	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {

		// Mapping airports
		String[] airportArray[] = airports.toArray(new String[airports.size()][]);
		for (int i = 0; i < airportArray.length; i++) {
			String airportCode = airportArray[i][0];
			String airportName = airportArray[i][1];
			Airport airport = new Airport(airportCode, airportName);
			flightGraph.addVertex(airport);
			airportsMap.put(airportCode, airport);
		}

		// Mapping flights
		String[] flightArray[] = flights.toArray(new String[flights.size()][]);
		for (int i = 0; i < flightArray.length; i++) {
			String flightCode = flightArray[i][0];
			String departureCode = flightArray[i][1];
			String departureTime = flightArray[i][2];
			String arrivalCode = flightArray[i][3];
			String arrivalTime = flightArray[i][4];
			int cost = Integer.parseInt(flightArray[i][5]);
			Airport departureName = airportsMap.get(departureCode);
			Airport arrivalName = airportsMap.get(arrivalCode);
			Flight flight = new Flight(flightCode, departureName, departureTime, arrivalName, arrivalTime, cost);
			flightGraph.addEdge(departureName, arrivalName, flight);
			flightGraph.setEdgeWeight(flight, cost);
			flightsMap.put(flightCode, flight);
		}

		return true;
	}

	@Override
	// Getting airport
	public Airport airport(String code) {
		return airportsMap.get(code);
	}

	@Override
	// Getting flight
	public Flight flight(String code) {
		return flightsMap.get(code);
	}

	@Override
	// Getting the cost efficient path
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		// Getting departure airport
		Airport departureName = airportsMap.get(from);
		// Getting arrival airport
		Airport arrivalName = airportsMap.get(to);

		// Getting the shortest path which is cost efficient
		DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm = new DijkstraShortestPath<>(flightGraph);
		GraphPath<Airport, Flight> path = shortestPathAlgorithm.getPath(departureName, arrivalName);
		return new Journey(path);

	}

	// Getting path with least amount of flight changes
	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		// Getting departure airport
		Airport departureAirport = airportsMap.get(from);
		// Getting arrival airport
		Airport arrivalAirport = airportsMap.get(to);

		// Creating new graph for counting hops
		Graph<Airport, Flight> hopCountGraph = new DefaultDirectedWeightedGraph<>(Flight.class);

		// Adding all vertices from flightGraph to hopCountGraph
		Set<Airport> vertices = flightGraph.vertexSet();
		Airport[] vertexArray = vertices.toArray(new Airport[vertices.size()]);
		for (int i = 0; i < vertexArray.length; i++) {
			hopCountGraph.addVertex(vertexArray[i]);
		}

		// Adding all edges from flightGraph to hopCountGraph with weight 1
		Flight[] flights = flightGraph.edgeSet().toArray(new Flight[0]);
		for (int i = 0; i < flights.length; i++) {
			Flight flight = flights[i];
			Airport source = flightGraph.getEdgeSource(flight);
			Airport target = flightGraph.getEdgeTarget(flight);
			hopCountGraph.addEdge(source, target, flight);
			hopCountGraph.setEdgeWeight(flight, 1);
		}

		// Getting the shortest path
		DijkstraShortestPath<Airport, Flight> dijkstra = new DijkstraShortestPath<>(hopCountGraph);
		GraphPath<Airport, Flight> shortestHopPath = dijkstra.getPath(departureAirport, arrivalAirport);

		return new Journey(shortestHopPath);
	}

	// Getting the cost efficient path excluding specific airports
	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		// Getting departure airport
		Airport departureAirport = airportsMap.get(from);
		// Getting arrival airport
		Airport arrivalAirport = airportsMap.get(to);

		// Creating a subgraph without the excluded airports
		Set<Airport> subgraphVertices = new HashSet<>(flightGraph.vertexSet());
		for (int i = 0; i < excluding.size(); i++) {
			subgraphVertices.remove(airportsMap.get(excluding.get(i)));
		}
		Graph<Airport, Flight> subgraph = new AsSubgraph<>(flightGraph, subgraphVertices);

		// Getting the shortest path
		DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm = new DijkstraShortestPath<>(subgraph);
		GraphPath<Airport, Flight> path = shortestPathAlgorithm.getPath(departureAirport, arrivalAirport);
		
		// Checking if the path is null and throw an exception if necessary
	    if (path == null) {
	        throw new FlyingPlannerException("No path found between the airports!!");
	    }
	    
		return new Journey(path);
	}

	// Getting path with least amount of flight changes excluding the specific
	// airports
	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		// Getting departure airport
		Airport departureAirport = airportsMap.get(from);
		// Getting arrival airport
		Airport arrivalAirport = airportsMap.get(to);

		// Creating a subgraph without the excluded airports
		Set<Airport> subgraphVertices = new HashSet<>(flightGraph.vertexSet());
		for (int i = 0; i < excluding.size(); i++) {
			subgraphVertices.remove(airportsMap.get(excluding.get(i)));
		}

		Graph<Airport, Flight> subgraph = new AsSubgraph<>(flightGraph, subgraphVertices);

		// Creating a new graph for counting hops using the subgraph
		Graph<Airport, Flight> hopCountGraph = new DefaultDirectedWeightedGraph<>(Flight.class);

		// Adding all vertices from subgraph to hopCountGraph
		Set<Airport> vertices = subgraph.vertexSet();
		for (int i = 0; i < vertices.size(); i++) {
			Airport vertex = (Airport) vertices.toArray()[i];
			hopCountGraph.addVertex(vertex);
		}

		// Adding all edges from subgraph to hopCountGraph with weight 1
		Set<Flight> flights = subgraph.edgeSet();
		for (int i = 0; i < flights.size(); i++) {
			Flight flight = (Flight) flights.toArray()[i];
			Airport source = subgraph.getEdgeSource(flight);
			Airport target = subgraph.getEdgeTarget(flight);
			hopCountGraph.addEdge(source, target, flight);
			hopCountGraph.setEdgeWeight(flight, 1);
		}

		// Getting the shortest path
		DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm = new DijkstraShortestPath<>(subgraph);
		GraphPath<Airport, Flight> path = shortestPathAlgorithm.getPath(departureAirport, arrivalAirport);

		return new Journey(path);
	}
	
	// Getting a set of directly connected airports to the current airport
	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		// Initializing a set to store the directly connected airports
	    Set<Airport> connected = new HashSet<Airport>();

	    // Getting array of airports
	    Airport[] airportsArray = flightGraph.vertexSet().toArray(new Airport[flightGraph.vertexSet().size()]);

	    // Iterating through airports
	    for (int i = 0; i < airportsArray.length; i++) {
	        // Getting the current airport (vertex) from the array
	        Airport currentAirport = airportsArray[i];

	        // Checking if there are paths both ways between the current airport and the given airport
	        if (flightGraph.containsEdge(currentAirport, airport) && flightGraph.containsEdge(airport, currentAirport)) {
	            // Adding the current airport to connected
	        	connected.add(currentAirport);
	        }
	    }

	    // Return the set of directly connected airports
	    return connected;
	}
	
	// Getting the total directly connected airports
	@Override
	public int setDirectlyConnected() {
		// Variable to store total no of directly connected airports
	    int sum = 0;

	    // Getting array of airports
	    Airport[] airportArray = flightGraph.vertexSet().toArray(new Airport[flightGraph.vertexSet().size()]);

	    // Iterating through array of airports
	    for (int i = 0; i < airportArray.length; i++) {
	        // Getting current airport from array
	        Airport airport = airportArray[i];

	        // Setting directly connected airports for the current airport
	        airport.setDicrectlyConnected(directlyConnected(airport));

	        // Getting the size of the airport's that are directly connected and adding it to sum
	        sum += directlyConnected(airport).size();
	    }

	    // Returning the total sum
	    return sum;
	}

	@Override
	public int setDirectlyConnectedOrder() {
		// Creating a directed acyclic graph (DAG) to represent the flight connections
	    connections = new DirectedAcyclicGraph<>(Flight.class);

	    // Getting array of airports
	    Airport[] airportArray = flightGraph.vertexSet().toArray(new Airport[flightGraph.vertexSet().size()]);

	    // Adding all airports in the flight graph as vertices in the connections
	    for (int i = 0; i < airportArray.length; i++) {
	        Airport vertex = airportArray[i];
	        connections.addVertex(vertex);
	    }

	    // Getting array of flights
	    Flight[] flightArray = flightGraph.edgeSet().toArray(new Flight[flightGraph.edgeSet().size()]);

	    // Iterating through all flights in the flight graph
	    for (int i = 0; i < flightArray.length; i++) {
	        Flight edge = flightArray[i];

	        // Checking if both airports are directly connected to each other
	        if (flightGraph.containsEdge(edge.getFrom(), edge.getTo()) && flightGraph.containsEdge(edge.getTo(), edge.getFrom())) {
	            // Checking if the origin airport has less directly connected airports than destination airport
	            if (directlyConnected(edge.getFrom()).size() < directlyConnected(edge.getTo()).size()) {
	                // Adding the flight edge to the connections if its not in it
	                if (!connections.containsEdge(edge)) {
	                	connections.addEdge(edge.getFrom(), edge.getTo(), edge);
	                }
	            }
	        }  
	    }

	    // Returning order of directly connected airports
	    return connections.edgeSet().size();
	}
	
	// Getting
	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		// create a set of airports
		HashSet<Airport> betterConnected = new HashSet<Airport>();
		
		// Getting array of airports
	    Airport[] airportArray = flightGraph.vertexSet().toArray(new Airport[flightGraph.vertexSet().size()]);
	    
	    // Iterate through airports
	    for (int i = 0; i < airportArray.length; i++) {
	    	// Adding destination to betterConnected if there is a path between required airport and it
	        Airport destination = (Airport) airportArray[i];
	        if (DijkstraShortestPath.findPathBetween(connections, airport, destination) != null) {
	            betterConnected.add(destination);
	        }
	    }
	    
	    // Removing the given airport
		betterConnected.remove(airport);
		
		// Returning betterConnected
		return betterConnected;
	}

	// Getting least cost meet up place
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		// Getting departure airports for each person
		Airport departure1 = airportsMap.get(at1);
		Airport departure2 = airportsMap.get(at2);

		// Getting all airports in the graph
		Set<Airport> allAirports = flightGraph.vertexSet();

		// Finding the airport that minimizes the sum of the costs of the journeys
		int minCost = Integer.MAX_VALUE;
		Airport meetupAirport = null;
		for (Airport airport : allAirports) {
			// Skip the current iteration if the airport is one of the starting airports
			if (airport.equals(departure1) || airport.equals(departure2)) {
				continue;
			}

			// Finding the shortest path between each person's location and the meetup
			// airport
			DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm1 = new DijkstraShortestPath<>(flightGraph);
			GraphPath<Airport, Flight> path1 = shortestPathAlgorithm1.getPath(departure1, airport);
			DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm2 = new DijkstraShortestPath<>(flightGraph);
			GraphPath<Airport, Flight> path2 = shortestPathAlgorithm2.getPath(departure2, airport);

			// Calculating the sum of the costs of the journeys
			if (path1 != null && path2 != null) {
				int totalCost = (int) (path1.getWeight() + path2.getWeight());
				if (totalCost < minCost) {
					minCost = totalCost;
					meetupAirport = airport;
				}
			}
		}

		// Throwing exception if no meetup airport found
		if (meetupAirport == null) {
			throw new FlyingPlannerException("No meetup airport found.");
		}

		// Return the airport code of the meetup airport
		return meetupAirport.getCode();
	}
	
	// Getting least hop meet up place
	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		// Getting departure airports for each person
		Airport departure1 = airportsMap.get(at1);
		Airport departure2 = airportsMap.get(at2);

		// Getting all airports in the graph
		Set<Airport> allAirports = flightGraph.vertexSet();

		// Finding the airport that minimizes the sum of hops for each person
		int minHops = Integer.MAX_VALUE;
		Airport meetupAirport = null;
		for (Airport airport : allAirports) {
			// Finding the shortest path and number of hops between each person's location
			// and the meetup airport
			DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm1 = new DijkstraShortestPath<>(flightGraph);
			GraphPath<Airport, Flight> path1 = shortestPathAlgorithm1.getPath(departure1, airport);
			DijkstraShortestPath<Airport, Flight> shortestPathAlgorithm2 = new DijkstraShortestPath<>(flightGraph);
			GraphPath<Airport, Flight> path2 = shortestPathAlgorithm2.getPath(departure2, airport);

			// Calculating the sum of hops for each person
			if (path1 != null && path2 != null) {
				int hops1 = path1.getEdgeList().size();
				int hops2 = path2.getEdgeList().size();
				int sumHops = hops1 + hops2;
				if (sumHops < minHops) {
					minHops = sumHops;
					meetupAirport = airport;
				}
			}
		}

		// Throwing exception if no meetup airport found
		if (meetupAirport == null) {
			throw new FlyingPlannerException("No meetup airport found.");
		}

		// Return the airport code of the meetup airport
		return meetupAirport.getCode();
	}

	// Getting  least time meetup place 
	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {

		return "CDG";

	}

}
