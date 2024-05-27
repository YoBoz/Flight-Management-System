package F28DA_CW2;

import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlannerMainPartA {

	public static void main(String[] args) {

		// The following code is from HelloJGraphT.java of the org.jgrapth.demo package

		System.err.println("The example code is from HelloJGraphT.java from the org.jgrapt.demo package.");
		System.err.println("Use similar code to build the small graph from Part A by hand.");
		System.err.println(
				"Note that you will need to use a different graph class as your graph is not just a Simple Graph.");
		System.err.println(
				"Once you understand how to build such graph by hand, move to Part B to build a more substantial graph.");
		// Create a directed weighted graph
		Graph<String, DefaultWeightedEdge> flightsGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

		// Adding vertices
		String[] airports = { "Edinburgh", "Heathrow", "Dubai", "Sydney", "Kuala Lumpur" };
		for (String airport : airports) {
			flightsGraph.addVertex(airport);
		}

		// Adding edges and weights
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Edinburgh", "Heathrow"), 80);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Heathrow", "Edinburgh"), 80);

		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Heathrow", "Dubai"), 130);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Dubai", "Heathrow"), 130);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Heathrow", "Sydney"), 570);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Sydney", "Heathrow"), 570);

		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Dubai", "Kuala Lumpur"), 170);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Kuala Lumpur", "Dubai"), 170);

		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Dubai", "Edinburgh"), 190);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Edinburgh", "Dubai"), 190);

		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Kuala Lumpur", "Sydney"), 150);
		flightsGraph.setEdgeWeight(flightsGraph.addEdge("Sydney", "Kuala Lumpur"), 150);

		System.out.println("Following Airports Are Used :");
		String Airports = flightsGraph.vertexSet().toString();
		System.out.println(Airports.substring(1,Airports.length()-1));
		System.out.println();

		// Creating a scanner object
		Scanner scanner = new Scanner(System.in);

		// Asking user to enter start airport
		System.out.println("Please enter the start airport:");
		String startAirport = scanner.nextLine();

		// Asking user to enter destination airport
		System.out.println("Please enter the destination airport:");
		String destinationAirport = scanner.nextLine();

		scanner.close();

		// Finding the cheapest journey
		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(flightsGraph);
		GraphPath<String, DefaultWeightedEdge> cheapestPath = dijkstra.getPath(startAirport, destinationAirport);

		// Printing the cheapest path
		System.out.println("Cheapest path:");
		int step = 1;
		for (DefaultWeightedEdge edge : cheapestPath.getEdgeList()) {
			System.out.println(
					step + ". " + flightsGraph.getEdgeSource(edge) + " -> " + flightsGraph.getEdgeTarget(edge));
			step++;
		}

		// Printing number of plane changes
		System.out.println("Number of plane changes: " + (cheapestPath.getLength() - 1));

		// Printing cheapest path cost
		System.out.println("Cheapest path cost: " + cheapestPath.getWeight());

	}

}
