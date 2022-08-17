public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    private int nextIndex;
    // TODO: implement your code here

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        nextIndex = 0;
    }
    
    @Override
    public Integer get(int index){
    	if (index < 0 || index >= arr.length | arr[index] == 0)
        	throw new IllegalArgumentException("Index is illegal");
        return arr[index];   
        }
        
    @Override
    public Integer search(int k) {
    	// Binary search
        int low = 0;
        int high = nextIndex - 1;
        while (high >= low) {
            int mid = (high + low) / 2;
            if (arr[mid] == k)
                return mid;
            else if (arr[mid] < k)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    @Override
    public void insert(Integer x) {
    	if (nextIndex == arr.length)
        	throw new IllegalArgumentException("Array is full");
        int index = nextIndex - 1;
        while(index >= 0 && arr[index] > x) { // Shifting the array in order to place the new element in its correct, sorted spot
            arr[index + 1] = arr[index];
            index--;
        }
        arr[index + 1] = x;
        int[] memo = {1, index+1}; // 1 = insert
        stack.push(memo);
        nextIndex++;
    }

    @Override
    public void delete(Integer index) {
    	if (index < 0 || index >= arr.length || nextIndex <= index)
    		throw new IllegalArgumentException("Index is out of bounds");
        int[] memo = {0, arr[index]}; // 0 = delete
        stack.push(memo);
        for (int i = index; i < nextIndex; i++) { // shifting the array in order to delete the element in the specific index
        	if (i+1 < arr.length)
        		arr[i] = arr[i+1];
        	else
        		arr[i] = 0;
        }
        nextIndex--;
    }

    @Override
    public Integer minimum() {
        if (nextIndex == 0)
        	throw new IllegalArgumentException("Array is empty");
        return 0; // first Index = minimal element
    }

    @Override
    public Integer maximum() {
    	if (nextIndex == 0)
        	throw new IllegalArgumentException("Array is empty");
    	return nextIndex - 1; // Last inserted index = maximal element
    }

    @Override
    public Integer successor(Integer index) {
    	if (nextIndex == 0)
    		throw new IllegalArgumentException("Array is empty");
    	if (index < 0 | index >= nextIndex)
    		throw new IllegalArgumentException("Index is illegal");
     	if (index == maximum())
    		throw new IllegalArgumentException("There is no successor");
     	return index + 1; // The next cell in the sorted array
    }

    @Override
    public Integer predecessor(Integer index) {
    	if (nextIndex == 0)
    		throw new IllegalArgumentException("Array is empty");
    	if (index < 0 | index >= nextIndex)
    		throw new IllegalArgumentException("Index is illegal");
     	if (index == minimum())
    		throw new IllegalArgumentException("There is no predecessor");
     	return index -1; // The previous cell in the sorted array
    }

    @Override
    public void backtrack() {
        if (stack.isEmpty())
        	return;
        int[] memo = (int[]) stack.pop();
        if (memo[0] == 0) { // So this item was deleted
        	insert(memo[1]);
        	stack.pop();
        }
        else { // So this item was inserted
        	delete(memo[1]);
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