

public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int nextIndex;
    // TODO: implement your code here

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        nextIndex = 0;
    }

    @Override
    public Integer get(int index){
        if (index < 0 || index >= arr.length)
        	throw new IllegalArgumentException("Index is illegal");
        return arr[index];
    }

    @Override
      public Integer search(int k) {
        for (int i = 0; i < nextIndex; i++) // Iterating the array until nextIndex
        	if (arr[i] == k)
        		return i;
        return -1;
    }

    @Override
    public void insert(Integer x) {
        if (nextIndex == arr.length)
        	throw new IllegalArgumentException("Array is full");
        int[] memo = {1, x, nextIndex}; // 1 = insert
        stack.push(memo);
        arr[nextIndex] = x; // Inserting in the next available spot
        nextIndex++;
    }

    @Override
    public void delete(Integer index) {
        if (index < 0 || nextIndex <= index)
        	throw new IllegalArgumentException("Index is out of bounds");
        int[] memo = {0, arr[index], index}; // 0 = delete
        stack.push(memo);
        arr[index] = arr[nextIndex - 1]; // Moving last element in the array to the new empty spot
        nextIndex--;
    }

    @Override
    public Integer minimum() {
        if (nextIndex == 0)
        	throw new IllegalArgumentException("Array is empty");
        int min = 0;
        for (int i = 1; i < nextIndex; i++) { // Iterating the array to find minimal value
        	if (arr[i] <= arr[min])
        		min = i;
        }
        return min;
    }

    @Override
    public Integer maximum() {
    	if (nextIndex == 0)
        	throw new IllegalArgumentException("Array is empty");
    	int max = 0;
        for (int i = 1; i < nextIndex; i++) { // Iterating the array to find maximal value
        	if (arr[i] > arr[max])
        		max = i;
        }
        return max;
    }

    @Override
    public Integer successor(Integer index) {
    	if (nextIndex == 0)
    		throw new IllegalArgumentException("Array is empty");
    	if (index < 0 | index >= nextIndex)
    		throw new IllegalArgumentException("Index is illegal");
     	if (index == maximum())
    		throw new IllegalArgumentException("There is no successor");
        int secc = maximum();
        for (int i = 0; i < nextIndex; i++) { // Iterating the array to find smaller maximum
        	if (arr[i] > arr[index] & arr[i] < arr[secc])
        		secc = i;
        }
        return secc;
    }

    @Override
    public Integer predecessor(Integer index) {
    	if (nextIndex == 0)
    		throw new IllegalArgumentException("Array is empty");
    	if (index < 0 | index >= nextIndex)
    		throw new IllegalArgumentException("Index is illegal");
    	if (index == minimum())
    		throw new IllegalArgumentException("There is no predecessor");
        int pred = minimum();
        for (int i = 0; i < nextIndex; i++) { // // Iterating the array to find larger minimum
        	if (arr[i] < arr[index] & arr[i] > arr[pred])
        		pred = i;
        }
        return pred;
    }

    @Override
    public void backtrack() {
        if (stack.isEmpty())
        	return;
        int[] memo = (int[]) stack.pop();
        if (memo[0] == 0) { // So this item was deleted
        	insert(arr[memo[2]]);
        	arr[memo[2]] = memo[1];
        	stack.pop();
        }
        else { // So this item was inserted
        	delete(memo[2]);
        	stack.pop();
        }
    }

    @Override
    public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
    }

    @Override
    public void print() {
        String ans = "";
        for (int i = 0; i < nextIndex; i++)
        	ans += arr[i] + " ";
        System.out.println(ans);
    }
   
}
