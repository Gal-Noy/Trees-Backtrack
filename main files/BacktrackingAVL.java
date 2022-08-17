import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BacktrackingAVL extends AVLTree {
    // For clarity only, this is the default ctor created implicitly.
    public BacktrackingAVL() {
        super();
    }

    //Change the list returned to a list of integers answering the requirements
    public static List<Integer> AVLTreeBacktrackingCounterExample() {
        List<Integer> output = new LinkedList<Integer>();
        output.add(1);
        output.add(2);
        output.add(3);
        output.add(4);
        output.add(6);
        output.add(8);
        return output;
    }

    //You are to implement the function Backtrack.
    public void Backtrack() {
        if(root == null)
            return;
        //for every insertion - one rotation or non accrued
        Object[] temp = (Object[]) stack.pop();
        Node toDelete =toDeleteFinder((int)temp[1]);
        Node node = (AVLTree.Node) temp[0];

        //backtrack the rotation cases to the original state
        ImbalanceCases rotation = (ImbalanceCases) temp[2];
        switch (rotation) {
            case RIGHT_RIGHT:
                right_right(node);
                break;
            case LEFT_RIGHT:
                left_right(node);
                break;
            case RIGHT_LEFT:
                right_left(node);
                break;
            case LEFT_LEFT:
                left_left(node);
                break;
            case NO_IMBALANCE:
                break;
        }

        if (toDelete.parent == null){//case the node is the root
            root = null;
            return;
        }
        else if (toDelete.value > toDelete.parent.value){ // case the node is the right son of this parent
            toDelete.parent.right = null;}
        else{ // case the node is the left son of this parent
            toDelete.parent.left = null;}

        //update heights to nodes above the deleted one
        toDelete.updateSize();
        while (toDelete!=root){
            toDelete = toDelete.parent;
            //update node inner values
            toDelete.updateHeight();
        }
    }

    private void left_left(Node node) {
        Node newRoot = null;
        if (node.parent == null) { //case the node is the root
            newRoot = rotateRight(node);
            newRoot.parent = null;
            root = newRoot;
        } else { // case the node is a junction
            Node parent = node.parent;
            newRoot = rotateRight(node);
            newRoot.parent = parent;
            if (newRoot.value > parent.value) {
                parent.right = newRoot;
            } else {
                parent.left = newRoot;
            }
            //inner values update
            parent.updateHeight();
        }

        //inner values update
        node.updateHeight();
        newRoot.updateHeight();
        node.updateSize();
        newRoot.updateSize();
    }

    private void right_right(Node node) {
        Node newRoot = null;
        if (node.parent == null) { //case the node is the root
            newRoot = rotateLeft(node);
            newRoot.parent = null;
            root = newRoot;
        } else { // case the node in a junction
            Node parent = node.parent;
            newRoot = rotateLeft(node);
            newRoot.parent = parent;
            if (newRoot.value > parent.value) {
                parent.right = newRoot;
            } else {
                parent.left = newRoot;
            }
            parent.updateHeight();
        }
        //inner values update
        node.updateHeight();
        newRoot.updateHeight();
        node.updateSize();
        newRoot.updateSize();
    }

    private void left_right(Node node) {
        //performs 2 backtrack rotations
        right_right(node);
        left_left(node);
    }

    private void right_left(Node node) {
        //performs 2 backtrack rotations
        left_left(node);
        right_right(node);
    }

    //standard binary search on avl
    private Node toDeleteFinder (int value){
        Node curr = root;
        while (curr.value!=value){
            if (curr.value>value)
                curr = curr.left;
            else curr = curr.right;
        }
        return curr;
    }

    //recursive help function
    public int Select(int index) {
        if (root ==null)
            return -1;
        Node temp =  select(root,index);
        return temp.value;
    }

    //recursive function return the node itself
    private Node select(Node node,int index){
        //calculate current node size
        int currRank = 1;
        if (node.left!=null)
            currRank = node.left.size+1;
        if (currRank == index) {
            return node;
        }
        else if (index< currRank)
            if (node.left==null)
                return node;
            else
                return select(node.left,index);
        else
            if (node.right==null)
                return node;
            else
                return select(node.right,index-currRank);
    }

    //recursive help function
    public int Rank(int value) {
        if (root == null)
            return 0;
        return rank(root, value);
    }

    private int rank(Node node, int value){
        //case node value = value - dont need to count it
        if (node.value == value)
            if (node.left!=null) {return node.left.size;}
            else {return 0;}

        //case node value > value - need to count only left rank if exist
        else if (node.value > value)
            if (node.left!=null) {return rank(node.left,value);}
            else {return 0;}

        //case node value < value - need to count both right rank, left size and the node itself
        else
            if (node.right==null || node.left == null){
                if (node.right==null && node.left == null) {return 1;}
                else if (node.left== null) {return rank(node.right,value) +1;}
                else {return node.left.size+1;}}
            else {return rank(node.right,value) + node.left.size+1;}
    }

}