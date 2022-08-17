import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;


    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
        if (root == null) {
            throw new NoSuchElementException("empty tree has no root");
        }
        return root;
    }

    public Node search(int k) {
        // match to binary search
        BacktrackingBST.Node temp = root;
        while (temp != null) {
            if (temp.getKey() == k) {
                return temp;
            } else if (k > temp.getKey()) {
                temp = temp.right;
            } else {
                temp = temp.left;
            }
        }
        return null;
    }

    public void insert(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("cant insert node");
        }
        //insert a new leaf into the tree
        if (root == null) {
            root = node;
        } else {
            root.insert(node);
        }
        Object[] memo = {0, node};
        stack.push(memo);
    }

    public void delete(Node node) {
        if (root == null){
            throw new IllegalArgumentException("tree is empty");
        }
        if (node == null || search(node.getKey()) == null) {
            return;
        }
        //work on the correct node
        Node searchNode = search(node.getKey());
        int Case = 1;
        //assume that the node is part of the tree because every node as a unique key
        if (root.getKey() == node.getKey()) { // root is been deleted
            if (root.right == null | root.left == null) { // in case root has one son
                if (root.right == null & root.left == null) {
                    root = null;
                } else if (root.right != null) {
                    root = root.right;
                } else {
                    root = root.left;
                }
            } else {//root have 2 suns
                //find the successor, delete it, delete me and replace with successor
                Node temp = successor(searchNode);
                //special case right son is my successor
                if (searchNode.right == temp) {
                    Case++;
                }
                //save duplicated successor node So we can backtrack to the exact tree
                stack.push(new Object[]{Case, duplicateNode(temp)});
                delete(temp);
                stack.pop();

                //set new root parameters
                temp.parent = root.parent;
                temp.right = root.right;
                temp.left = root.left;
                root = temp;

                //set son's parents
                if (node.left != null) {
                    node.left.parent = temp;
                }
                if (node.right != null) {
                    node.right.parent = temp;
                }
            }
        } else { // node is a leaf or an inner junction
            if (searchNode.right == null | searchNode.left == null) {
                if (searchNode.right == null & searchNode.left == null) {
                    if (searchNode.parent.getKey() < searchNode.getKey()) {
                        searchNode.parent.right = null;
                    } else {
                        searchNode.parent.left = null;
                    }
                } else {
                    deleteOneSon(searchNode); //help function below
                }
            } else {
                deleteTwoSons(searchNode);//help function below
                Case++;
            }
        }

        //save node information
        stack.push(new Object[]{Case, searchNode});
    }

    private void deleteOneSon(Node node) { //HELP FUNCTION - assume not the root
        if (node.right == null) {//case node have right son
            if (node.parent.getKey() < node.getKey()) // case node is the right son of parent
            {
                node.parent.right = node.left;
                node.left.parent = node.parent;
            } else { // case node is the right son of parent
                node.parent.left = node.left;
                node.left.parent = node.parent;
            }
        } else { //case node have left son
            if (node.parent.getKey() < node.getKey()) {// case node is the right son of parent
                node.parent.right = node.right;
                node.right.parent = node.parent;
            } else {// case node is the left son of parent
                node.parent.left = node.right;
                node.right.parent = node.parent;
            }
        }
    }

    private void deleteTwoSons(Node node) { //HELP FUNCTION - assume not the root
        //save duplicated successor node So we can backtrack to the exact tree
        Node temp = successor(node);
        stack.push(new Object[]{1, duplicateNode(temp)});
        delete(temp);
        stack.pop();

        //set the parent links
        if (node.parent.getKey() < node.getKey()) {
            node.parent.right = temp;
        } else {
            node.parent.left = temp;
        }

        //set this node links
        temp.parent = node.parent;
        temp.left = node.left;
        temp.right = node.right;

        //set son's parents
        if (node.left != null) {
            node.left.parent = temp;
        }
        if (node.right != null) {
            node.right.parent = temp;
        }
    }

    private Node duplicateNode(Node node) { // HELP FUNCTION
        Node dup = new Node(node.getKey(), node.value);
        dup.right = node.right;
        dup.left = node.left;
        dup.parent = node.parent;
        return dup;
    }


    public Node minimum() {
        if (root == null) {
            throw new IllegalArgumentException("tree is empty");
        }
        return root.minimum();

    }

    public Node maximum() {
        if (root == null) {
            throw new IllegalArgumentException("tree is empty");
        }
        return root.maximum();
    }

    public Node successor(Node node) { // according to the algorithm learn in class
        if (node == null)
            throw new IllegalArgumentException("node is null");
        Node temp = node;
        if (temp.right != null)
            return temp.right.minimum();
        Node parent = temp.parent;
        while (parent != null & temp == parent.right) {
            temp = parent;
            parent = parent.parent;
        }
        return parent;
    }

    public Node predecessor(Node node) { // according to the algorithm learn in class
        if (node == null)
            throw new IllegalArgumentException("node is null");
        Node temp = node;
        if (temp.left != null)
            return temp.left.maximum();
        Node parent = temp.parent;
        while (parent != null & temp == parent.left) {
            temp = parent;
            parent = parent.parent;
        }
        return parent;
    }

    @Override
    public void backtrack() { // Backtracking both insertion and deletion actions
        if (stack.isEmpty()) {
            return;
        }
        Object[] temp = (Object[]) stack.pop();
        Node node = (Node) temp[1];
        if ((int) temp[0] >= 1) {// the node has been deleted. we need to get him back
            if (node.parent == null) { //case the node was the root
                if (node.left == null | node.right == null) {
                    if (node.left == null & node.right == null) { // was the single node in the tree
                        root = node;
                    } else { //had only one son or successor was right son
                        if ((int) temp[0] == 2) { //case successor was right son
                            backtrack();
                            node.right = root.right;
                            node.left = root.left;
                            root = node;
                        } else {// case had only one son
                            root.parent = node;
                            root = node;
                        }
                    }
                } else { //had 2 sons
                    backtrack();
                    root = node;
                }
            }
            //case the node is a leaf of an inner junction
            else {
                Node parentNode = node.parent;
                if (node.left == null | node.right == null) {
                    if (node.left == null & node.right == null) { //case the node was a leaf
                        insert(node);
                        stack.pop();
                    } else { // case the node had one son or his successor was his right son
                        if ((int) temp[0] == 2) {// case successor was his right son
                            if (parentNode.getKey() < node.getKey()) {// the node was the right son of his parent
                                //node.parent = parentNode;
                                parentNode.right.parent = node;
                                parentNode.right = node;
                            } else {//the node was the left son of his parent
                                Node toInsert = parentNode.left;
                                parentNode.left = node;
                            }
                            backtrack();
                        } else {
                            if (node.right != null) {// case the node had only right son
                                if (parentNode.getKey() < node.getKey()) {// the node was the right son of his parent
                                    parentNode.right = node;
                                } else { // the node was the left son of his parent
                                    parentNode.left = node;
                                }

                            } else { // case the node had only left son
                                Node tempNode = search(node.parent.getKey());
                                if (tempNode.getKey() < node.getKey()) {// the node was the right son of his parent
                                    tempNode.right = node;
                                } else { // the node was the left son of his parent
                                    tempNode.left = node;
                                }
                            }
                        }
                    }
                } else {// the node have 2 sons
                    backtrack();
                    if (parentNode.getKey() < node.getKey()) {// the node was the right son of his parent
                        parentNode.right.parent = node;
                        parentNode.right = node;
                    } else {//the node was the left son of his parent
                        parentNode.left.parent = node;
                        parentNode.left = node;
                    }
                }
            }
            temp[0] = 0;
            redoStack.push(temp);

        } else {// the node has been inserted. we need to delete iT
            temp[0] = 1;
            delete(node);
            stack.pop();
            redoStack.push(temp);
        }

    }

    @Override
    public void retrack() {
        if (redoStack.isEmpty()) {
            return;
        } else {
            stack.push(redoStack.pop());
            backtrack();

            //check if thr redostack doesnt contain false arguments
            Object[] temp2 = (Object[]) redoStack.pop();
            if ((int) temp2[0] != 0) {
                redoStack.push(temp2);
            }
        }
    }

    public void printPreOrder() {
        if (root != null) {
            System.out.println(root.preorder());
        }
    }

    @Override
    public void print() {
        printPreOrder();
    }

    public static class Node {
        // These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }
        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void insert(Node node) {
            if (node.getKey() < getKey()) {
                if (left == null) {//insert node
                    left = node;
                    left.parent = this;
                } else // go left
                    left.insert(node);
            } else {
                if (right == null) { // insert node
                    right = node;
                    right.parent = this;
                } else // go right
                    right.insert(node);
            }
        }

        public Node maximum() {
            if (this.right != null)
                return right.maximum();
            else return this;
        }

        public Node minimum() {
            if (this.left != null)
                return left.minimum();
            else
                return this;
        }

        public String preorder() {
            String ans = value.toString();
            if (left != null)
                ans = ans + " " + left.toString();
            if (right != null)
                ans = ans + " " + right.toString();
            return ans;
        }
        public String toString() {
            return String.valueOf(key);
        }
    }
}
