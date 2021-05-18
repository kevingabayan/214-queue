import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * The <code>Simulator<code> class contains the main method to test the simulation.
 * All values should be received from user input.
*    e-mail: kevin.gabayan@stonybrook.edu
*    Stony Brook ID: 111504873
 */
public class Simulator {
	
	public static final int MAX_PACKETS = 3;
	/**
	 * Simulator Variables (used throughout the entire Simulator class), too lazy to delete
	 * @param dispatcher
	 * Level 1 Router
	 * @param routers
	 * Level 2 routers (Intermediate Routers)
	 * @param totalServiceTime
	 * The running sum of the total time each packet is in the network.
	 * @param totalPacketsArrived
	 * Contains the total number of packets successfully forwarded to the destination.
	 * Note: Can only happen when sendPacketTo(Collection routers) throws an exception.
	 * @param arrivalProb
	 * The probability of a new packet arriving at the Dispatcher.
	 * @param numIntRouters
	 * The number of Intermediate routers in the network.
	 * @param maxBufferSize
	 * THe max number of Packets a Router can accommodate for.
	 * @param minPacketSize
	 * The minimum size of a Packet.
	 * @param bandwidth
	 * The max number of Packets the Destination router can accept at a simulation unit.
	 * @param duration
	 * The number of simulation units.
	 * @param averageServiceTime
	 * Calculates the average service time per packet.
	 * @param MAX_PACKETS
	 * Tells you that a maximum of 3 packets can arrive per time unit.
	 */
	
	/**
	 * This method conducts the simulation for the routers and packets.
	 * The cases are implemented in the following order:
	 * Decide whether packets have arrived at the Dispatcher.
	 * If the Dispatcher contains unsent packets, send them off to one of the 
	 * Intermediate routers. sendPacketTo(Collection intRouters) decides which router the packet should
	 * be forwarded to.
	 * Decrement all packets counters in the beginning of the Q at each router.
	 * If any packets are read to be forwarded to the Destination router, do so.
	 * Once a packet has arrived at the Destination router, take note of its arrival
	 * by recording the total time in the network. (BE CAREFUL OF BANDWIDTH)
	 * @return
	 * @throws FullBufferException
	 * The author thought this was necessary at first, but it is not. It is left there
	 * for historical purposes.
	 */
	
	public static double simulate(int numIntRouters, double arrivalProb, int maxBufferSize,
	  int minPacketSize, int maxPacketSize, int bandwidth, int duration) throws FullBufferException {
		
		double averageServiceTime = 0;
		int packetsDropped = 0; // Implementation Complete
		int totalPacketsArrived = 0; // Implementation Complete
		int totalServiceTime = 0; // Implementation Complete
	
		// This creates the dispatcher router.
		Router dispatcher = new Router();
		
		// This creates a collection of routers, ensuring that indexing starts at 1 by making index 0 null.
		Router[] routers = new Router[numIntRouters + 1];
		routers[0] = null;
		for(int i = 1; i < routers.length; i++) {
			routers[i] = new Router();
		}
	
		// This creates a special fake destination queue for usage when packets are arriving towards the destination.
		Router destinationQueue = new Router();
		
		
		int simulationUnit = 1;
		// Everything that goes on at a given simulation unit.
		while(simulationUnit <= duration) {
		
			// A counter for the bandwidth.
			int bandwidthCounter = 0; 
			
			// Prints out the simulationUnit you are at. 
			System.out.println("Time: " + simulationUnit);
			
			// Packet Creation to the destination router.
			int packetArrived = 0;
			for(int i = 0; i < MAX_PACKETS; i++) {
				if(Math.random() < arrivalProb) {
					int packetSize = randInt(minPacketSize,maxPacketSize);
					Packet p = new Packet(packetSize, simulationUnit);
					dispatcher.enqueue(p);
					System.out.println("Packet " + p.getId() + " arrives at dispatcher with size "
					  + p.getPacketSize());
					packetArrived++;
				}
			}
			if(packetArrived == 0)
				System.out.println("No packets arrived.");
			
			// For sending packets to their Intermediate Routers
			while(!dispatcher.vector.isEmpty()) {
				Packet tosend = dispatcher.dequeue();
				int index = Router.sendPacketTo(routers);
				if(routers[index].vector.size() == maxBufferSize) {
					packetsDropped++;
					System.out.println("Network is congested. Packet " + tosend.getId() +
					  " is dropped.");
				}
				else {
					routers[index].enqueue(tosend);
					System.out.println("Packet " + tosend.getId() + " sent to Router " + index + ".");
				}
			}
			
			// Decrementing all packets counters in the beginning of the queue at each Intermediate Router.
			for(int i = 1; i < numIntRouters + 1; i++) {
				if(!routers[i].vector.isEmpty()) {
					int timeToDest = (routers[i].peek().getTimeToDest())-1;
					routers[i].peek().setTimeToDest(timeToDest);
				}
			}
			
			// Sending Routers to their respective destinations whilst respecting concurring bandwidth variables.
			// Note: The idea for a special fake destination queue was given by TA Johnny So on 214 Piazza.
			for(int i = 1; i < numIntRouters + 1; i++) {
				if(!routers[i].vector.isEmpty()) {
					if(routers[i].peek().getTimeToDest() == 0) {
						routers[i].peek().setIntermediateQueueIndex(i);
						Packet toBeSent = routers[i].peek();
						destinationQueue.enqueue(toBeSent);
					}
				}
			}	
			while(!destinationQueue.vector.isEmpty() && bandwidthCounter < bandwidth) {
				
				bandwidthCounter++;
				totalPacketsArrived++;
					
				Packet dequeued = destinationQueue.dequeue();
				routers[dequeued.getIntermediateQueueIndex()].dequeue();
				int travelTime = simulationUnit - dequeued.getTimeArrive();
				totalServiceTime += travelTime;
					
				System.out.println("Packet " + dequeued.getId() + " has successfully reached its destination: +" 
					 + travelTime);
			}
			
			// Prints out the layout for each intermediate router.
			for(int i = 1; i < numIntRouters + 1; i++) {
				System.out.println("R" + i + ": " + routers[i].toString());
			}	
			simulationUnit++;
			System.out.println();
		}
		
		System.out.println("Simulation ending...");
		System.out.println("Total service time: " + totalServiceTime);
		System.out.println("Total packets served: " + totalPacketsArrived);
		System.out.printf("Average time per packet: %.2f\n", (double)totalServiceTime/(double)totalPacketsArrived);
		System.out.println("Total packets dropped: " + packetsDropped);
		return averageServiceTime;
	}
	
	
	/**
	 * This helper method generates a random number between minVal and maxVal, inclusively.
	 * Author's note: Created with assistance from CSE 114 lecture notes :]
	 * @param minVal
	 * The minimum value for the helper method.
	 * @param maxVal
	 * The maximum value for the helper method.
	 * @return
	 * The randomly generated number.
	 */
	private static int randInt(int minVal, int maxVal) {
		return minVal + (int)(Math.random() * ((maxVal-minVal) + 1));
	}
	
	/**
	 * This method provides user input and conducts the simulation as necessary.
	 */
	public static void main(String[] args) throws FullBufferException {
		System.out.println("Starting simulator...");
		
		boolean end = false;
		while(end != true) {
			int numIntRouters;
			double arrivalProb;
			int maxBufferSize;
			int minPacketSize;
			int maxPacketSize;
			int bandwidth;
			int duration;
			Scanner input = new Scanner(System.in);
			try {
			System.out.print("Enter the number of Intermediate routers: ");
			numIntRouters = input.nextInt();
			if(numIntRouters < 0) {
				System.out.println();
				System.out.println("Nice one! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the arrival probability of a packet: ");
			arrivalProb = input.nextDouble();
			if(arrivalProb < 0) {
				System.out.println();
				System.out.println("Nice one! Try again.");
				input.nextLine();
				
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the maximum buffer size of a router: ");
			maxBufferSize = input.nextInt();
			if(maxBufferSize < 0) {
				System.out.println();
				System.out.println("Nice one! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the minimum size of a packet: ");
			minPacketSize = input.nextInt();
			if(minPacketSize< 0) {
				System.out.println();
				System.out.println("Nice meme! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the maximum size of a packet: ");
			maxPacketSize = input.nextInt();
			if(maxPacketSize < 0) {
				System.out.println();
				System.out.println("Nice one! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			if(maxPacketSize < minPacketSize) {
				System.out.println();
				System.out.println("Why is your min packet size greater than your max? Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the bandwidth size: ");
			bandwidth = input.nextInt();
			if(bandwidth < 0) {
				System.out.println();
				System.out.println("This isn't the real world! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			try {
			System.out.print("Enter the simulation duration: ");
			duration = input.nextInt();
			if(duration < 0) {
				System.out.println();
				System.out.println("Nice one! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
			}
			catch(InputMismatchException e) {
				System.out.println();
				System.out.println("Wrong type! Try again.");
				input.nextLine();
				System.out.println();
				continue;
			}
		
			simulate(numIntRouters, arrivalProb, maxBufferSize, minPacketSize, maxPacketSize,
			  bandwidth, duration);
		
			System.out.print("Type 'n' to close the program;"
			  + " otherwise, a new simulation will begin upon next user input: ");
			input.nextLine();
			String yesorno = input.nextLine();
			if(yesorno.equals("n")) {
				end = true;
			}
			System.out.println();
		}
		System.out.println("Program terminating successfully...");
	}

}
