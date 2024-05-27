package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class FlyingPlannerMainPartBC {

	public static void main(String[] args) {
		// Creating a FlyingPlanner object
		FlyingPlanner fp = new FlyingPlanner();
		Journey journey = null;

		try {
			// Filling in fp with flights information
			fp.populate(new FlightsReader());

			// Creating scanner object
			Scanner sc = new Scanner(System.in);

			System.out.println("1)Part B");
			System.out.println("2)Part C");
			System.out.println("Choose one of the above :");
			int part = sc.nextInt();
			if (part != 1 && part != 2) {
				System.out.println("Invalid Choice");
			} else if (part == 1) {
				// Asking user to enter the departure airport code
				System.out.println("\nEnter departure airport code: ");
				String from = sc.next().toUpperCase();
				// Asking user to enter the destination airport code
				System.out.println("Enter destination airport code: ");
				String to = sc.next().toUpperCase();

				// Asking whether the user wants least cost or least stops
				System.out.println("\n1)Least Cost");
				System.out.println("2)Least Stops");
				System.out.print("Enter Your Choice : ");
				int choice = sc.nextInt();
				// If the input is not valid then exiting the code
				if (choice != 1 && choice != 2) {
					System.out.println("Invalid Input");
					System.exit(0);
				}

				// Ask whether the user wants to exclude any airports
				System.out.println("\nWanna exclude any aiport(s)(Y/N) : ");
				String exclude = sc.next().toUpperCase();

				// Check if the user selects "yes" for excluding airports
				if (exclude.equals("Y")) {
					// Ask the user to enter the airports they want to exclude
					System.out.println("\nPlease enter the codes of the airports you want to exclude:");

					// List to store all the airports to be excluded
					List<String> exAirports = new ArrayList<String>();

					// Initialize the code variable
					String exCode;

					// Variable to store user's decision on whether to continue entering airport
					// codes
					String continueEntering;

					// Keep running the loop until the user decides to stop entering airport codes
					do {
						// Take the user input
						exCode = sc.next().toUpperCase();

						// Retrieve the airport object for the entered code
						Airport airport = fp.airport(exCode);

						// Check if the airport object is not null and if the entered code is not equal
						// to the start or end airport code
						if (airport != null && !exCode.equalsIgnoreCase(from) && !exCode.equalsIgnoreCase(to)) {
							exAirports.add(exCode);
						} else {
							// Print an error message for invalid input
							System.out.println("Invalid Input!!");
						}

						// Ask the user if they want to continue entering airport codes
						System.out.print("Wanna enter more airport codes? (Y/N): ");
						continueEntering = sc.next().toUpperCase();

					} while (continueEntering.equalsIgnoreCase("Y"));

					// Calling the function to print the whole journey
					if (choice == 1) {
						// Find the least cost journey between the two airport codes
						journey = fp.leastCost(from, to, exAirports);
					} else {
						journey = fp.leastHop(from, to, exAirports);
					}
				}
				// if the user doesn't want to exclude airports
				else if (exclude.equals("N")) {
					if (choice == 1) {
						// Find the least cost journey between the two airport codes
						journey = fp.leastCost(from, to);
					} else if (choice == 2) {

						journey = fp.leastHop(from, to);
					}
					// If the input is not valid then exiting the code
				} else {
					System.out.println("Invalid Input");
					System.exit(0);
				}

				// Printing journey details
				System.out.println("\nJourney for " + fp.airport(from).getName() + " (" + from + ") to "
						+ fp.airport(to).getName() + " (" + to + ") ");

				// Format for displaying the journey
				String format = "%-4s %-15s %-4s   %-8s %-15s %-4s    \n";
				System.out.format(format, "Leg", "Leave", "At", "On", "Arrive", "At");

				// Getting list of flights for journey
				List<String> flights = journey.getFlights();

				// Iterating through the flights and printing details
				for (int i = 0; i < flights.size(); i++) {
					// Getting flight object corresponding to current flight code
					Flight flight = fp.flight(flights.get(i));

					// Getting departure city name and airport code
					String depCity = flight.getFrom().getName() + " (" + flight.getFrom().getCode() + ")";

					// Getting arrival city name and airport code
					String arrCity = flight.getTo().getName() + " (" + flight.getTo().getCode() + ")";

					// Printing flight details in the required format
					System.out.format(format, i + 1, depCity,
							String.format("%4s", flight.getFromGMTime()).replace(' ', '0'), flight.getFlightCode(),
							arrCity, String.format("%4s", flight.getToGMTime()).replace(' ', '0'));
				}

				// Printing total journey cost
				System.out.printf("\nTotal Journey Cost =  £%d", journey.totalCost());
				// printing the time spent in the air
				System.out.printf("\nTotal Time Airtime = %d minutes", journey.airTime());
				// printing the total time of the journey
				System.out.printf("\nTotal Time Journey Duration = %d minutes", journey.totalTime());
				// Printing the total number of airmiles
				System.out.printf("\nAirmiles = %d\n\n", journey.totalAirmiles());

			}
			if (part == 2) {
				System.out.println("\n1)Directly Connected ");
				System.out.println("2)Meetups ");
				System.out.println("Choose one of the above:");
				int choice = sc.nextInt();
				if (choice != 1 && choice != 2) {
					System.out.println("Invalid Choice");
				} else if (choice == 1) {
					// Asking user to enter the departure airport code
					System.out.println("\nEnter airport code: ");
					String air = sc.next().toUpperCase();

					// Find and display the airports directly connected to the departure airport
					Airport currentAirport = fp.airport(air);
					if (currentAirport != null) {
						Set<Airport> directlyConnected = fp.directlyConnected(currentAirport);
						System.out.printf("\nAirports Directly Connected To %s (%s): \n", currentAirport.getName(),
								air);

						int connectedCount = 0;
						Object[] directlyConnectedArray = directlyConnected.toArray();
						for (int j = 0; j < directlyConnectedArray.length; j++) {
							Airport connectedAirport = (Airport) directlyConnectedArray[j];
							if (connectedAirport != null && connectedAirport.getName() != null) {
								System.out.printf("%s (%s)\n", connectedAirport.getName(), connectedAirport.getCode());
								connectedCount++;
							}
						}
						if (connectedCount == 0) {
							System.out.println("No Directly Connected Airports!!");
						}
					}
					System.out.println();
				} else if (choice == 2) {
					// Asking user to enter the departure airport code
					System.out.println("\nEnter departure airport code: ");
					String from = sc.next().toUpperCase();
					// Asking user to enter the destination airport code
					System.out.println("Enter destination airport code: ");
					String to = sc.next().toUpperCase();
					// Printing least cost and least hop meetup
					System.out.printf("\nThe Cost Efficient Meetup Point Would Be : %s(%s) \n",
							fp.leastCostMeetUp(from, to));
					System.out.printf("The Journey Efficient Meetup Point Would Be : %s(%s) \n",
							fp.leastHopMeetUp(from, to));
				}

			}

		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}
	}
}
