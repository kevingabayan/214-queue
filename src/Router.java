import java.util.Collection;
import java.util.Vector;

/**
 * The <code>Router<code> class represents a router in the network, which is ultimately a queue.
*    e-mail: kevin.gabayan@stonybrook.edu
*    Stony Brook ID: 111504873
 */
public class Router extends Vector {
	Vector<Packet> vector = new Vector<Packet>();
	/**
	 * This is a constructor for the Router class.
	 */
	public Router() {
	}
	/**
	 * This method adds a new Packet to the end of the buffer router. 
	 * @param p
	 * The new Packet
	 */
	public void enqueue(Packet p) {
		vector.addElement(p);
	}
	/**
	 * This method removes the first Packet in the router buffer.
	 * @return
	 * The first Packet in the router buffer that was removed.
	 */
	public Packet dequeue() {
		return vector.remove(0);
	}
	/**
	 * This method returns, but does not remove the first Packet in the router buffer.
	 * @return
	 * The first Packet in the router buffer.
	 */
	public Packet peek() {
		return vector.firstElement();
	}
	/**
	 * This method returns a String representation of the router buffer
	 * in a specific format.
	 */
	public String toString() {
		String finale = "{";
		for(int i = 0; i < vector.size(); i++) {
			finale += vector.get(i).toString();
			if(i != vector.size()-1) {
				finale += ", ";
			}
		}
		finale += "}";
		return finale;
	}
	
	/**
	 * This method loops through the list of Intermediate routers, finds
	 * the router with the most free buffer space, and returns the index of the router.
	 * @throws FullBufferException
	 * Indicates that all router buffers are full.
	 */
	public static int sendPacketTo(Router[] collection) throws FullBufferException {
		Router MostSpace = null;
		int index = 1;
		for(int i = 1; i < collection.length; i++) {
			if(i == 1) {
				MostSpace = collection[i];
			}
			else {
				if(collection[i].vector.size() < MostSpace.vector.size()) {
					index = i;
				}
			}
		}
		return index;
	}
	
}
