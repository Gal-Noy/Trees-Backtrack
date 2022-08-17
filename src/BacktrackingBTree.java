import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingBTree() {
		super();
	}

	public BacktrackingBTree(int order) {
		super(order);
	}

	public void Backtrack() { // Backtracking insertion action
		if (root == null)
			return;
		
		Object curr = this.memos.removeFirst();
		T toDelete;
		
		if (!(curr instanceof LinkedList)) { // So last inserted key didn't cause any node splits
			toDelete = (T) curr;
			Node<T> target = this.getNode(toDelete);
			if (target != null)
				target.removeKey(toDelete);
			if (this.memos.isEmpty())
				root = null;
		}
		
		else { // So last inserted key caused at least one node split in the BTree
			LinkedList<T> memo = (LinkedList<T>) curr;
			toDelete = memo.removeFirst();
		
			// First, delete the last inserted key from its current node
			Node<T> target = this.getNode(toDelete);
			target.removeKey(toDelete);
			
			int numOfSplits = memo.size();  // The keys left in memo are the specific keys which raised above at each node split
			
			for (int i = 0; i < numOfSplits; i++) {
				Node<T> currParent = target.parent; // Must have a parent because it was previously split
				T median = memo.removeFirst();
				
				// Locate the node which contains median and the key's index
				while (currParent.indexOf(median) == -1)
					currParent = currParent.parent;
				int medianIndex = currParent.indexOf(median);
				
				// Mark the previously-split half nodes
				Node<T> toMerge1 = currParent.getChild(medianIndex);
				Node<T> toMerge2 = currParent.getChild(medianIndex+1);
				
				// Remove median from its current node
				currParent.removeKey(median);
				
				// Merge the nodes with median in the middle to the original node before the split (with their children as well)
				Node<T> beforeSplit = mergeNodes(toMerge1, median, toMerge2);
				
				// Remove the "half-nodes" and their separated children
				currParent.removeChild(toMerge1);
				currParent.removeChild(toMerge2);
				
				// Case 1 - currParent is the root
				if (currParent.parent == null) {
					if (root.numOfKeys == 0)
						root = beforeSplit;
					else
						root.addChild(beforeSplit);
					if (root.parent != null)
						root.parent = null;
				}
				
				// Case 2 - currParent is an inner node
				else
					currParent.addChild(beforeSplit);
				target = currParent;
			}
		}
		this.size--;
	}
	
	private Node<T> mergeNodes(Node<T> toMerge1, T median, Node<T> toMerge2){
		int maxDegree = toMerge1.numOfKeys + toMerge2.numOfKeys + 2;
		Node<T> output = new Node<T>(toMerge1.parent, maxDegree);
		
		// Add keys by their order from both nodes and the median key
		for (int i = 0; i < toMerge1.numOfKeys; i++)
			output.addKey(toMerge1.getKey(i));
		output.addKey(median);
		for (int i = 0; i < toMerge2.numOfKeys; i++)
			output.addKey(toMerge2.getKey(i));
		
		// Adding both nodes children according to their correct index in the children array
		for (int i = 0; i < toMerge1.numOfChildren; i++)
			output.addChild(toMerge1.getChild(i));
		for (int i = 0; i < toMerge2.numOfChildren; i++)
			output.addChild(toMerge2.getChild(i));
		
		return output;
	}
}
