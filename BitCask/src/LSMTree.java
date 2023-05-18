



import java.io.BufferedWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class LSMNode<T extends Comparable<T>, V>
{
    LSMNode left, right;
    T key;
    V value;

 //Constructor

    public LSMNode()
    {
        left = null;
        right = null;
        key = null;
        value = null;
    }
 //Constructor

    public LSMNode(T key, V value)
    {
        left = null;
        right = null;
        this.key = key;
        this.value = value;
    }
 //Function to set left node

    public void setLeft(LSMNode n)
    {
        left = n;
    }
 //Function to set right node

    public void setRight(LSMNode n)
    {
        right = n;
    }
 //Function to get left node

    public LSMNode getLeft()
    {
        return left;
    }
 //Function to get right node

    public LSMNode getRight()
    {
        return right;
    }
 //Function to set data to node

    public void set_KeyValue(T key, V value){}
    {
        this.key = key;
        this.value = value;
    }
 //Function to get data from node

    public V getValue()
    {
        return value;
    }

    public T getKey()
    {
        return key;
    }
}


class LSMTree<T extends Comparable<T>, V>
{
    private LSMNode root;

 //Constructor

    public LSMTree()
    {
        root = null;
    }
 //Function to check if tree is empty

    public boolean isEmpty()
    {
        return root == null;
    }
 //Functions to insert data

    public void insert(T key, V value)
    {
        root = insert(root, key, value);
    }
 //Function to insert data recursively

    private LSMNode insert(LSMNode node, T key, V value)
    {
        if (node == null)
            node = new LSMNode(key, value);
        else
        {
            if (key.compareTo((T) node.getKey()) <= 0)
                node.left = insert(node.left, key, value);
            else
                node.right = insert(node.right, key, value);
        }
        return node;
    }
 //Functions to delete data

    public void delete(T key)
    {
        if (isEmpty())
            System.out.println("Tree Empty");
        else if (search(key) == null)
            System.out.println("Sorry "+ String.valueOf(key) +" is not present");
        else
        {
            root = delete(root, key);
            System.out.println(String.valueOf(key)+ " deleted from the tree");
        }
    }
    private LSMNode delete(LSMNode root, T key)
    {
        LSMNode p, p2, n;
        if (root.getKey() == key)
        {
            LSMNode lt, rt;
            lt = root.getLeft();
            rt = root.getRight();
            if (lt == null && rt == null)
                return null;
            else if (lt == null)
            {
                p = rt;
                return p;
            }
            else if (rt == null)
            {
                p = lt;
                return p;
            }
            else
            {
                p2 = rt;
                p = rt;
                while (p.getLeft() != null)
                    p = p.getLeft();
                p.setLeft(lt);
                return p2;
            }
        }
        if (key.compareTo((T) root.getKey()) < 0 )
        {
            n = delete(root.getLeft(), key);
            root.setLeft(n);
        }
        else
        {
            n = delete(root.getRight(), key);
            root.setRight(n);
        }
        return root;
    }
 //Functions to count number of nodes

    public int countNodes()
    {
        return countNodes(root);
    }
 //Function to count number of nodes recursively

    private int countNodes(LSMNode r)
    {
        if (r == null)
            return 0;
        else
        {
            int l = 1;
            l += countNodes(r.getLeft());
            l += countNodes(r.getRight());
            return l;
        }
    }
 //Functions to search for an element

    public V search(T key)
    {
        return search(root, key, false);
    }
 //Function to search for an element recursively

    private V search(LSMNode r, T key, boolean found)
    {
        V val = null;
        while ((r != null) && !found)
        {
            T rval = (T) r.getKey();
            if (key.compareTo(rval) < 0)
                r = r.getLeft();
            else if (key.compareTo(rval) > 0)
                r = r.getRight();
            else
            {
                found = true;
                val = (V) r.getValue();
                break;
            }
            search(r, key, found);
        }
        return val;
    }
 //Function for inorder traversal

    public ArrayList<Map.Entry<T, V>> inorder()
    {
        ArrayList<Map.Entry<T, V>> inorder_data = new ArrayList<>();
        return inorder(root, inorder_data);
    }
    private ArrayList<Map.Entry<T, V>> inorder(LSMNode r, ArrayList<Map.Entry<T, V>> inorder_data)
    {
        if (r != null)
        {
            inorder(r.getLeft(), inorder_data);
            inorder_data.add(new AbstractMap.SimpleEntry<>((T) r.getKey(), (V)r.getValue()));
            inorder(r.getRight(), inorder_data);
        }
        return inorder_data;
    }
 //Function for preorder traversal

    public void preorder()
    {
        preorder(root);
    }
    private void preorder(LSMNode r)
    {
        if (r != null)
        {
            System.out.print( String.valueOf(r.getKey()) +" " + String.valueOf(r.getValue()));
            preorder(r.getLeft());
            preorder(r.getRight());
        }
    }
// Function for postorder traversal

    public void postorder()
    {
        postorder(root);
    }
    private void postorder(LSMNode r)
    {
        if (r != null)
        {
            postorder(r.getLeft());
            postorder(r.getRight());
            System.out.print( String.valueOf(r.getKey()) +" " + String.valueOf(r.getValue()));
        }
    }
}

