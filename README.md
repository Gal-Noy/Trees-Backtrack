# Trees and array Backtrack

As part of Data-Structures course, given partial implementation of a data structure, we needed to build a backtracking algorithm to "undo" the last insertion action performed on the structure and implement its function. The data structures in which we did this are simple ADTs (binary trees & sorted arrays) and self-balancing trees (AVL Trees & B-Trees).

We have been assigned a limitation of O(log n) for all binary trees and O(h) for B-tree, for performing a single backtrack.

* In order to implement the function in the described data structures, we also implemented the Insertion method itself along with other helping functions.
* In AVL Trees, we implemented Select & Rank functions as well (Order Statistic Tree functions), which helped us to build the final backtrack method.

The algorithm was developed and written by Gal Noy and Yonatan Balassiano.
