/**
 * The <code>Packet<code> class represents a packet that will be sent through the network.
*    e-mail: kevin.gabayan@stonybrook.edu
*    Stony Brook ID: 111504873
 */
public class Packet {
	private static int packetCount = 0;
	private int id;
	private int packetSize;
	private int timeArrive;
	private int timeToDest;
	private int intermediateQueueIndex;
	/**
	 * Packet variables
	 * @param packetCount
	 * Assigns an id to a newly created packet. It will start with the value zero, and
	 * every time a new packet object is created, increment this counter and assign the
	 * value as the id of the Packet.
	 * @param id
	 * A unique identifier for the packet, automatically determined through packetCount.
	 * @param packetSize
	 * The size of the packet being sent... automatically determined by the simulator using
	 * Math.random().
	 * @param timeArrive
	 * The time this packet is created.
	 * @param timeToDest
	 * Contains the number of simulation units that it takes for a packet to arrive at the
	 * destination router. The value starts at one hundredth of the packet size (packetSize/100).
	 * At every simulation time unit, this value decreases. 0 = packet has arrived. 
	 * @param intermediateQueueIndex
	 * Contains the Intermediate Router Index the Packet belongs to. Used for the special fake destination queue.
	 */
	
	/**
	 * This method retrieves the packet count. 
	 * @return
	 * The packet count. 
	 */
	public static int getPacketCount() {
		return packetCount;
	}
	/**
	 * This method retrieves the intermediate router the packet belongs to.
	 * @return
	 * The router the packet belongs to.
	 */
	public int getIntermediateQueueIndex() {
		return intermediateQueueIndex;
	}
	/**
	 * This method sets the intermediate router that the packet belongs to.
	 * @param intermediateQueue
	 * The index of the intermediate router to be set.
	 */
	public void setIntermediateQueueIndex(int intermediateQueueIndex) {
		this.intermediateQueueIndex = intermediateQueueIndex;
	}
	/**
	 * This method sets the packet count.
	 * @param packetCount
	 * The new packet count to be set.
	 */
	public static void setPacketCount(int packetCount) {
		Packet.packetCount = packetCount;
	}
	/**
	 * This method retrieves the id.
	 * @return
	 * The id of the Packet.
	 */
	public int getId() {
		return id;
	}
	/**
	 * This method sets the id of the packet.
	 * @param id
	 * The new id to be set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * This method retrieves the packet size.
	 * @return
	 * The packet size.
	 */
	public int getPacketSize() {
		return packetSize;
	}
	/**
	 * This method sets the packet size.
	 * @param packetSize
	 * The new packet size to be set.
	 */
	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	/**
	 * This method retrieves the time the packet was created.
	 * @return
	 * The time the packet was created.
	 */
	public int getTimeArrive() {
		return timeArrive;
	}
	/**
	 * This method sets the time the packet was created.
	 * @param timeArrive
	 * The time the packet was created to be set.
	 */
	public void setTimeArrive(int timeArrive) {
		this.timeArrive = timeArrive;
	}
	/**
	 * This method retrieves the number of simulation units it takes for a packet to arrive at the router.
	 * @return
	 * The number of simulation units it takes... etc.
	 */
	public int getTimeToDest() {
		return timeToDest;
	}
	/**
	 * This method sets the number of simulation units it takes for a packet to arrive at the router.
	 * @param timeToDest
	 * The number of simulation units to be set.
	 */
	public void setTimeToDest(int timeToDest) {
		this.timeToDest = timeToDest;
	} 
	/**
	 * This is a constructor for the Packet class. 
	 * Author's note: An extra overloaded constructor may need to be included.
	 * @param timeArrive
	 * The timeArrive to be set.
	 * @param packetSize
	 * The packetSize to be set.
	 */
	public Packet(int packetSize, int timeArrive) {
		packetCount++;
		this.id = packetCount;
		
		this.timeArrive = timeArrive;
		this.packetSize = packetSize;
		this.timeToDest = packetSize/100;
	}
	/**
	 * This is a toString method for the Packet object in the format
	 * [id, timeArrive, timeToDest].
	 */
	public String toString() {
		return "[" + id + ", " + timeArrive + ", " + timeToDest + "]";
	}
}
