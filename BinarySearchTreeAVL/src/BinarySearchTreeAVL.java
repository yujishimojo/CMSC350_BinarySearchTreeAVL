// File  : BinarySearchTreeAVL.java
// Author: Nicholas Duchon
// Date  : July 31, 2008
// updates: 
//        Nov 16, 2008
//        Nov 22, 2008
//        Apr 17, 2009 - fixed toTreeString
//        Apr 17, 2009 - added parents to nodes and \ and / to tree display.
//        Mar 18, 2012 - cleaned up some generic lint stuff in BSTNodeND declarations
//        May 13, 2012 - toTreeString - removed use of parent reference, added label parameter
//
// Simplified, linked version of a binary search tree
// uses generic class implementing Comparable
// no exceptions thrown
//   static fields
//       all associated with toString displays
//       .. DEPTHDELTA - used to present a tree-like display using spacing
//   This code implements the following public methods
//            T is a Comparable class
//       insert   (T)  : void
//       find     (T)  : T
//       max      ()   : T
//       getSize  ()   : int
//       remove   (T)  : void
//       toString ()   : String // to tree string
//
//    private mutator/accessor methods
//       insertValue (T, BSTNodeND < T >): void - uses comparator from T
//       findValue   (T, BSTNodeND < T >): T - finds an T, returns null if not found
//       findMax                       (): T - returns max value in this subtree
//       getSize        (BSTNodeND < T >): int
//       removeRoot     (BSTNodeND < T >): BSTNodeND < T >
//       remove      (T, BSTNodeND < T >): BSTNodeND < T >
//
//    private toString methods
//       toString            (BSTNodeND) : uses toTreeString (int) to create a tree-like display
//       toInOrderString     (BSTNodeND) : recursive in-order string
//       toPreOrderString    (BSTNodeND) : recursive pre-order string
//       toPostOrderString   (BSTNodeND) : recursive post-order string
//       toLevelOrderString  (BSTNodeND) : level-order string, uses a queue (ArrayDeque)
//       toTreeString        (int, BSTNodeND, char) : recursive in-order traversal with depth offsets and parent indicators
//
// Inside the BSTNodeND class:
//    Instance fields:
//       data  < L >   generic parameter of the BSTNodeND class, NOT the BinarySearchTreeAVL class
//       BSTNodeND left, right, parent - links to create the tree
//    constructors
//       < L > generic parameterized constructor, left and right are null
//       < L >, NSTNodeND - links to parent also, for tree display
//
//    import java.util.ArrayDeque;

   import java.util.Scanner;

   public class BinarySearchTreeAVL
      < K extends Comparable < ? super K > > 
      // ANY class that extends the base comparable type (K)
      // of this data structure instance may be inserted
   {
      static final int DEPTHDELTA = 5; // used to create a text tree display
   
      BSTNodeND < K > root = null;
      int balance = 0;
   
      public void insert (K d) {
         insertNode (new BSTNodeND < K > (d));
      } // end insert, public version
      
      public void insertNode (BSTNodeND < K > n) {
         if (root == null) root = n;
         else insertNode (n, root);
      } // end insert Node
   
      public K find (K d) {
         if (root == null) 
            return null;
         BSTNodeND < K > t = findValue (d, root);
         return (t==null)?null:t.data;
      } // end find method
      
      public K max () {
         if (root == null) 
            return null;
         return findMax(root).data;
      } // end max method
   
      public int getSize () {
         return getSize (root);}
   
      public void remove (K d) {
         root = remove (d, root);
      } // end remove data
   
      public String toString () {
         if (root == null) 
            return null;
         return toString(root);}
         
      private void insertNode (BSTNodeND < K > d, BSTNodeND < K > n) {
         d.parent = n;
         if (d.data.compareTo (n.data)  > 0) {
            if (n.right == null) {
            	if (checkBalance(d, n) == 1) {
            		balance++;
            	} else if (checkBalance(d, n) == -1) {
            		balance--;
            	}
            	rotation(d, n);
            	n.right = d;
            }
            else {
            	insertNode (d, n.right);
            }
         }
         else {
            if (n.left == null) {
            	if (checkBalance(d, n) == 1) {
            		balance++;
            	} else if (checkBalance(d, n) == -1) {
            		balance--;
            	}
            	rotation(d, n);
            	n.left = d;
            }
            else {
            	insertNode (d, n.left);
            }
         }
      } // end method insertNode
      
      private int checkBalance (BSTNodeND < K > d, BSTNodeND < K > n) {
         	if (n == root) {
            	if (d.data.compareTo (root.data) > 0) {
            		return 1; // if there is a bias toward right
            	} else if (d.data.compareTo (root.data) < 0) {
            		return -1; // if there is a bias toward left
            	}            		
        	} else {
            	if (d.data.compareTo (root.data) > 0 && n.right == null && n.left == null) {
            		return 1;
            	} else if (d.data.compareTo (root.data) < 0 && n.right == null && n.left == null) {
            		return -1;           		
            	}            		
        	}
         	return 0;
      } // end checkBalance
      
      private void rotation (BSTNodeND < K > d, BSTNodeND < K > n) {
          if (balance < -1) {
            	 if ((Integer) d.data < (Integer) d.parent.parent.data) {
            		 singleRightRotation(d, n);
            	 } else {
            		 doubleLeftRightRotation(d, n);
            	 }
             } else if (balance > 1) {
            	 if ((Integer) d.data > (Integer) d.parent.parent.data) {
            		 singleLeftRotation(d, n);
            	 } else {
            		 doubleRightLeftRotation(d, n);
            	 }
             }
      } // end rotation
      
      private void singleRightRotation (BSTNodeND < K > d, BSTNodeND < K > n) {
    	  if (n.parent.parent == root) {
        	  K temp1 = n.parent.right.data;
        	  K temp2 = n.parent.parent.data;
        	  K temp3 = n.parent.parent.right.data;

        	  remove (n.parent.right.data);
        	  removeRoot (n.parent.parent);
        	  insert (temp2);
        	  root.right.data = temp2;
        	  remove (root.right.left.data);
        	  insert (temp1);    	  
        	  insert (temp3);          	  
    	  } else {
    		  K temp1 = n.parent.right.data;
    		  K temp2 = n.parent.parent.data;
    		  K temp3 = n.parent.parent.right.data;    		  

    		  remove (n.parent.right.data);
    		  remove (n.parent.parent.data);
    		  insert (temp2);
    		  n.parent.right.data = temp2;
    		  remove (n.parent.right.left.data);
    		  insert (temp1);
    		  insert (temp3);
    	  }
    	  balance = 0;
      } // end singleRightRotation
      
      private void singleLeftRotation (BSTNodeND < K > d, BSTNodeND < K > n) {
    	  if (n.parent.parent == root) { 	  
        	  K temp1 = n.parent.left.data;
        	  K temp2 = n.parent.parent.data;
        	  K temp4 = n.parent.parent.left.data;
        	  K temp3 = n.parent.data;

        	  remove (n.parent.left.data); // temp1
        	  remove (n.parent.data); // temp3
        	  root.data = temp3;
        	  remove (root.left.data); // temp4
        	  insert (temp2);
        	  insert (temp1);
        	  insert (temp4);        	  
    	  } else {
        	  K temp1 = n.parent.left.data;
        	  K temp2 = n.parent.parent.left.data;
        	  K temp3 = n.parent.parent.data;
        	  
        	  remove (n.parent.left.data);
        	  remove (n.parent.parent.left.data);
        	  remove (n.parent.parent.data);
        	  insert (temp3);
        	  insert (temp2);
        	  insert (temp1);
    	  }
    	  balance = 0;
      } // end singleLeftRotation
      
      private void doubleLeftRightRotation(BSTNodeND < K > d, BSTNodeND < K > n) {
    	  System.out.println("doubleLeftRightRotation() is to be implemented");
      } // end doubleLeftRightRotation()
      
      private void doubleRightLeftRotation(BSTNodeND < K > d, BSTNodeND < K > n) {
    	  System.out.println("doubleRightLeftRotation() is to be implemented");
      } // end doubleRightLeftRotation()
      
      private BSTNodeND < K > findValue (K d, BSTNodeND < K > n) {
         if (n.data.compareTo(d) == 0) 
            return n;
         if (n.data.compareTo (d) > 0) 
            return (n.left==null)?null:findValue (d, n.left);
         return (n.right == null)?null:findValue(d, n.right);
      } // end findValue
      
      private BSTNodeND < K > findMax (BSTNodeND < K > n) {
         if (n.right == null) 
            return n;
         return findMax(n.right);
      } // end findValue
      
      private int getSize (BSTNodeND < K > t) {
         if (t == null) 
            return 0;
         return getSize (t.left) + getSize (t.right) + 1;
      } // end getSize node
      
      private BSTNodeND < K > removeRoot (BSTNodeND < K > t) {
         if (t.left  == null) {
            if (t.right != null) {
               t.right.parent = t.parent;
            }
            return t.right;
         }
         if (t.right == null) {
            t.left.parent = t.parent; // t.left != null because of earlier if test case
            return t.left;
         }
         BSTNodeND < K > newTop = findMax(t.left);
         remove (newTop.data, t); // lose the node instance, leave tree intact
         t.data = newTop.data;    // just replace the data at the internal node
         return t;
      } // end remove data, tree
   
      private BSTNodeND < K > remove (K d, BSTNodeND < K > t) {
         if (t == null) 
            return null;
         if (d.compareTo (t.data) < 0) {
            t.left  = remove (d, t.left );
         }
         else {
            if (d.compareTo (t.data)> 0) {
               t.right = remove (d, t.right);
            }
            else { // d equals t.data
               t = removeRoot (t);
            }
         }
         return t;
      } // end remove data, tree
   
      private String toString (BSTNodeND < K > n) {
         return toTreeString (5, n, '>'); 
      } // end toString
      
      // removed use of parent reference, added label parameter, 5/13/2012
      private String toTreeString (int depth, BSTNodeND < K > n, char label) { // depth = 0 is bad
         return
            ((n.left  == null)?"":toTreeString  (depth + DEPTHDELTA, n.left, '/'))
            + String.format ("%" + depth + "s%s\n", label, n) // ND: fixed 4/17/2009
            + ((n.right == null)?"":toTreeString (depth + DEPTHDELTA, n.right, '\\'));
      } // end method toTreeString
         
      private String toInOrderString (BSTNodeND < K > n) {
         return
              ((n.left  == null)?"":toInOrderString(n.left ))
            +  n + " "
            + ((n.right == null)?"":toInOrderString(n.right));
      } // end toInOrderString
         
      private String toPreOrderString (BSTNodeND < K > n) {
         return 
             n + " "
            + ((n.left  == null)?"":toPreOrderString(n.left ))
            + ((n.right == null)?"":toPreOrderString(n.right));
      } // end toPreOrderString
         
      private String toPostOrderString (BSTNodeND < K > n) {
         return
              ((n.left  == null)?"":toPostOrderString(n.left ))
            + ((n.right == null)?"":toPostOrderString(n.right))
            + n + " ";
      } // end to PostOrderString
         
         // See: http://en.wikipedia.org/wiki/Tree_traversal
      private String toLevelOrderString (BSTNodeND < K > n) {
         String st = "";
         BSTNodeND < K > node;
         java.util.ArrayDeque < BSTNodeND < K > > q 
               = new java.util.ArrayDeque < BSTNodeND < K > > ();
         q.add (n);          // start queue by adding this (root?) to queue
         while (q.size() > 0) { 
            node = q.remove();                          // remove the head of queue
            st += (node.data + " ");                // process head data to String
            if (node.left != null) q.add (node.left);   // insert left child at end of queue
            if (node.right != null) q.add (node.right); // insert right child at end or queue
         } // end queue processing
         return st.toString();
      } // end to LevelOrderString
         
      // main and example methods follow
      public static void main (String args []) {
         integerExample ();
//         stringExample ();
         Scanner st = new Scanner (System.in);
         while (menu(st));
         System.out.println ("...Bye");
      } // end main
   
      static boolean menu (Scanner st) {
         System.out.println ("enter integer values, q to quit:");
         String line = st.nextLine();
         if (line.length() == 0) 
            return true; // just <cr> causes crash on next line
            
         Scanner tokens = new Scanner (line).useDelimiter ("[,\\s]+"); // allow comma+space separators
         if (! tokens.hasNextInt())
            if (tokens.hasNext() && tokens.next().equalsIgnoreCase ("q")) 
               return false;
            else 
               return true;
         
         BinarySearchTreeAVL <Integer> tree = new BinarySearchTreeAVL <Integer> ();
         while (tokens.hasNextInt()) tree.insert (tokens.nextInt());
         System.out.println ("Tree:\n" + tree);
         System.out.println ("Tree in-order:\n"    + tree.toInOrderString   (tree.root));
         System.out.println ("Tree pre-order:\n"   + tree.toPreOrderString  (tree.root));
         System.out.println ("Tree post-order:\n"  + tree.toPostOrderString (tree.root));
         System.out.println ("Tree level-order:\n" + tree.toLevelOrderString(tree.root));
      
         if (tokens.hasNext() && tokens.next().equalsIgnoreCase ("q"))
            return false;
         return true;
      } // end menu
      
      public static void integerExample () {
         BinarySearchTreeAVL < Integer > x = new BinarySearchTreeAVL < Integer > ();         
         // In case of singleRightRotation
         int arr1[] = {7, 5, 9, 3, 6, 1};
//         int arr2[] = {9, 10, 7, 8, 6, 5};
//         int arr3[] = {18, 20, 12, 22, 19, 14, 8, 9, 4, 2};
//         int arr4[] = {25, 27, 21, 30, 26, 22, 18, 20, 16, 12, 8};
         
         // In case of singleLeftRotation
//         int arr5[] = {7, 9, 6, 12, 8, 14};
//         int arr6[] = {11, 14, 8, 19, 12, 20};
//         int arr7[] = {26, 28, 23, 32, 27, 24, 19, 36, 30, 39};
         
         // In case of doubleLeftRightRotation
//         int arr8[] = {9, 11, 4, 6, 3, 8, 5};
         
         // In case of doubleRightLeftRotation
//         int arr9[] = {12, 17, 9, 20, 15, 16, 13};
         
         int rem [] = {3, 5, 7};
         for (int y: arr1) {
        	 System.out.println ("Inserting: " + y);
        	 x.insert (y);
         }
         System.out.println ("X:\n" + x);
         System.out.println ("X in-order:\n"    + x.toInOrderString(x.root));
         System.out.println ("X pre-order:\n"   + x.toPreOrderString(x.root));
         System.out.println ("X post-order:\n"  + x.toPostOrderString(x.root));
         System.out.println ("X level-order:\n" + x.toLevelOrderString(x.root));
      
         Integer t = x.find(3);
         System.out.println ("find: " + t);
         System.out.println ("find: " + x.find(13));
         System.out.println ("Size: " + x.getSize());
         System.out.println ("MAX: " + x.max());
         for (int y: rem) {
            System.out.println ("Removing: " + y);
            x.remove (y);
            System.out.println ("result:\n" + x);
         }
//         System.out.println ("X:\n"  + x);
      } // end integerExample
       
      public static void stringExample () {
         // The following is an example using a user-defined class, see below
         // notice the use of the generic parameter Example
         String [] sa = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
         BinarySearchTreeAVL < Example > ex = new BinarySearchTreeAVL < Example > ();
         for (String s: sa) ex.insert (new Example (s));
         System.out.println ("Example using strings: \n" + ex);
      } // end stringExample
   
   // needs to be internal class so, eg, AVLNode in AVLND can extend this internal node class
      class BSTNodeND < L extends Comparable< ? super L > > {
         L data;
         BSTNodeND < L > left, right, parent;
      
         BSTNodeND (L d)                  {data = d;}
         BSTNodeND (L d, BSTNodeND <L> p) {data = d; parent = p;}
      
         public String toString () {
            return data.toString();} // end toString method         
      } // end class BSTNodeND
   } // end class BinarySearchTreeAVL
   
// A class that can be used with the BinarySearchTreeAVL data structure
// Notice the use of the generic parameter Example
   class Example implements Comparable < Example > {
      String data;
      public Example (String d) {
         data = d;}
      
   // you, of course, will want a more interesting compareTo method
      public int compareTo (Example e) {
         return data.compareTo (e.data);
      } // end compareTo method
      
      public String toString () {
         return data;
      } // end toString
   
   } // end class Example