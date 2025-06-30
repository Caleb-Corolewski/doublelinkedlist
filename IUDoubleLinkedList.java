import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double linked list implementation of an indexed unsorted list.
 * @author Caleb Corolewski
 * @Date June 2025
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>
{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Default constructor for a single linked list
     */
    public IUDoubleLinkedList()
    {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    /*
     * Adds the specified element to the front of this list. 
     *
     * @param element the element to be added to the front of this list    
     */
    @Override
    public void addToFront(T element) 
    {
        Node<T> newNode = new Node<T>(element);
        newNode.setNext(head);
        if(head != null)
        {
            head.setPrev(newNode);
        } else
        {
            tail = newNode;
        }
        head = newNode;
        size++;
        modCount++;
    }

    /*
     * Adds the specified element to the rear of this list. 
     *
     * @param element the element to be added to the rear of this list    
     */
    @Override
    public void addToRear(T element) 
    {
        Node<T> newNode = new Node<T>(element);
        newNode.setPrev(tail);
        if(tail != null)
        {
            tail.setNext(newNode);
        } else
        {
            head = newNode;
        }
        tail = newNode;
        size++;
        modCount++;
    }

    /* 
     * Adds the specified element to the rear of this list. 
     *
     * @param element  the element to be added to the rear of the list    
     */
    @Override
    public void add(T element) 
    {
        addToRear(element);
    }

    /* 
     * Adds the specified element after the first element of the list matching the specified target. 
     *
     * @param element the element to be added after the target
     * @param target  the target is the item that the element will be added after
     * @throws NoSuchElementException if target element is not in this list
     */
    @Override
    public void addAfter(T element, T target) 
    {
        if(indexOf(target) == -1)
        {
            throw new NoSuchElementException();
        }
        Node<T> newNode = new Node<T>(element);
        Node<T> prevNode = head;
        while(!prevNode.getElement().equals(target))
        {
            prevNode = prevNode.getNext();
        }
        newNode.setNext((prevNode.getNext()));
        newNode.setPrev(prevNode);
        if(prevNode.getNext() != null)
        {
            prevNode.getNext().setPrev(newNode);
        } else
        {
            tail = newNode;
        }
        prevNode.setNext(newNode);
        size++;
        modCount++;
    }

    /*  
     * Inserts the specified element at the specified index. 
     * 
     * @param index   the index into the array to which the element is to be inserted. 
     * @param element the element to be inserted into the array
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size)
     */
    @Override
    public void add(int index, T element) 
    {
         if(index < 0 || index > size)
        {
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> itr = listIterator(0);
        for(int i = 0; i < index; i++)
        {
            itr.next();
        }
        itr.add(element);
    }

    /*  
     * Removes and returns the first element from this list. 
     * 
     * @return the first element from this list
     * @throws NoSuchElementException if list contains no elements
     */
    @Override
    public T removeFirst() 
    {
        if(size == 0)
        {
            throw new NoSuchElementException();
        }
        T retVal = head.getElement();
        if(size == 1)
        {
            head = tail = null;
        } else
        {
        head.getNext().setPrev(null);
        head = head.getNext();
        }
        modCount++;
        size--;
        return retVal;
    }

    /*
     * Removes and returns the last element from this list. 
     *
     * @return the last element from this list
     * @throws NoSuchElementException if list contains no elements
     */
    @Override
    public T removeLast() 
    {
        if(size == 0)
        {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if(size == 1)
        {
            head = tail = null;
        } else
        {
        tail.getPrev().setNext(null);
        tail = tail.getPrev();
        }
        modCount++;
        size--;
        return retVal;
    }

    /*  
     * Removes and returns the first element from the list matching the specified element.
     *
     * @param element the element to be removed from the list
     * @return removed element
     * @throws NoSuchElementException if element is not in this list
     */
    @Override
    public T remove(T element) 
    {
        if(indexOf(element) == -1)
        {
            throw new NoSuchElementException();
        }
        Node<T> currentNode = head;
        while(!currentNode.getElement().equals(element))
        {
            currentNode = currentNode.getNext();
        }
        T retVal = currentNode.getElement();
        if(currentNode == head)
        {
            head = currentNode.getNext();
            if(head != null)
            {
                head.setPrev(null);
            } else
            {
                tail = null;
            }
        } else if(currentNode == tail)
        {
            tail = currentNode.getPrev();
            tail.setNext(null);
        } else
        {
            currentNode.getPrev().setNext(currentNode.getNext());
            currentNode.getNext().setPrev(currentNode.getPrev());
        }
        size--;
        modCount++;
        return retVal;
    }

    /*
     * Removes and returns the element at the specified index. 
     *
     * @param index the index of the element to be retrieved
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    @Override
    public T remove(int index) 
    {
        if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> itr = listIterator(0);
        for(int i = 0; i < index; i++)
        {
            itr.next();
        }
        T retVal = itr.next();
        itr.remove();
        return retVal;
    }

     /* 
     * Replace the element at the specified index with the given element. 
     *
     * @param index   the index of the element to replace
     * @param element the replacement element to be set into the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    @Override
    public void set(int index, T element) 
    {
       if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0)
        {
            head.setElement(element);
        } else
        {
        ListIterator<T> itr = listIterator(0);
        for(int i = 0; i < index; i++)
        {
            itr.next();
        }
        itr.set(element);
        }
    }

    /* 
     * Returns a reference to the element at the specified index. 
     *
     * @param index  the index to which the reference is to be retrieved from
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    @Override
    public T get(int index) 
    {
        if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for(int i = 0; i < index; i++)
        {
            currentNode = currentNode.getNext();
        }
        return currentNode.getElement();
    }

    /*
     * Returns the index of the first element from the list matching the specified element. 
     *
     * @param element  the element for the index is to be retrieved
     * @return the integer index for this element or -1 if element is not in the list    
     */
    @Override
    public int indexOf(T element) 
    {
        Node<T> currentNode = head;
        int currentIndex = 0;
        int returnIndex =-1;
        while(currentNode != null && returnIndex < 0)
        {
            if(currentNode.getElement().equals(element))
            {
                returnIndex = currentIndex;
            } else 
            {
                currentIndex++;
                currentNode = currentNode.getNext();
            }
        }
        return returnIndex;
    }

    /*  
     * Returns a reference to the first element in this list. 
     *
     * @return a reference to the first element in this list
     * @throws NoSuchElementException if list contains no elements
     */
    @Override
    public T first() 
    {
        if(isEmpty())
        {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    /*
     * Returns a reference to the last element in this list. 
     *
     * @return a reference to the last element in this list
     * @throws NoSuchElementException if list contains no elements
     */
    @Override
    public T last() 
    {
        if(isEmpty())
        {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    /*
     * Returns true if this list contains the specified target element. 
     *
     * @param target the target that is being sought in the list
     * @return true if the list contains this element, else false
     */
    @Override
    public boolean contains(T target) 
    {
        return indexOf(target) > -1;
    }

    /*  
     * Returns true if this list contains no elements. 
     *
     * @return true if this list contains no elements
     */
    @Override
    public boolean isEmpty() 
    {
       return size == 0;
    }

    /* 
     * Returns the number of elements in this list. 
     *
     * @return the integer representation of number of elements in this list
     */
    @Override
    public int size() 
    {
        return size;
    }

    /*  
     * Returns a string representation of this list. 
     *
     * @return a string representation of this list
     */
    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder("[");
        Node<T> currentNode = head;
        for(int i = 0; i < size; i++)
        {   if(currentNode == null)
            {
                break;
            }
            if(i < size - 1)
            {
                string.append(currentNode.getElement());
                string.append(",");
            }if (i == size -1) {
                string.append(currentNode.getElement());                
            }
            currentNode = currentNode.getNext();
        }
		string.append("]");
		
		return string.toString();
    }

    /*  
     * Returns an Iterator for the elements in this list. 
     *
     * @return an Iterator over the elements in this list
     */
    @Override
    public Iterator<T> iterator() 
    {
        return new DLLListIterator(0);
    }

    /* 
     * Returns a ListIterator for the elements in this list. 
     *
     * @return a ListIterator over the elements in this list
     *
     * @throws UnsupportedOperationException if not implemented
     */
    @Override
    public ListIterator<T> listIterator() 
    {
        return new DLLListIterator(0);
    }

    /* 
     * Returns a ListIterator for the elements in this list, with
     * the iterator positioned before the specified index. 
     *
     * @return a ListIterator over the elements in this list
     *
     * @throws UnsupportedOperationException if not implemented
     */
    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        if(startingIndex < 0 || startingIndex > size)
        {
            throw new IndexOutOfBoundsException();
        }
        return new DLLListIterator(startingIndex);
    }

    private class DLLListIterator implements ListIterator<T>
    {
        //variables
        private Node<T> nextNode;
        private int nextIndex;
        private int iterModCount;
        private boolean canChange;
        private Node<T> lastReturned;

        //constructor
        public DLLListIterator(int index)
        {
            nextNode = head;
            int count = 0;
            while(count != index)
            {
                nextNode = nextNode.getNext();
                count++;
            }
            nextIndex = index;
            this.iterModCount = modCount;
            canChange = false;
            lastReturned = null;
        }

        @Override
        public boolean hasNext() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            canChange = true;
            return lastReturned.getElement();
        }

        @Override
        public boolean hasPrevious() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            return nextNode != null ? nextNode.getPrev() != null : tail != null;
        }

        @Override
        public T previous() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            if(!hasPrevious())
            {
                throw new NoSuchElementException();
            }
            if (nextNode == null)
            {
                nextNode = tail;
            } else
            {
                nextNode = nextNode.getPrev();
            }
            lastReturned = nextNode;
            nextIndex--;
            canChange = true;
            return lastReturned.getElement();
        }

        @Override
        public int nextIndex() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void remove() 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            if(!canChange || lastReturned == null)
            {
                throw new IllegalStateException();
            }
            Node<T> removed = lastReturned;
            if(removed == nextNode)
            {
                nextNode = nextNode.getNext();
            } else
            {
                nextIndex--;
            }
            if(removed.getPrev() == null)
            {
                head = removed.getNext();
            }else
            {
                removed.getPrev().setNext(removed.getNext());
            }
            if(removed.getNext() == null)
            {
                tail = removed.getPrev();
            } else
            {
                removed.getNext().setPrev(removed.getPrev());
            }
            size--;
            modCount++;
            iterModCount++;
            lastReturned = null;
            canChange = false;

        }

        @Override
        public void set(T e) 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            if(!canChange || lastReturned == null)
            {
                throw new IllegalStateException();
            }
            lastReturned.setElement(e);
            modCount++;
            iterModCount++;
            canChange = false;
        }

        @Override
        public void add(T e) 
        {
            if(iterModCount != modCount)
            {
                throw new ConcurrentModificationException();
            }
            Node<T> newNode = new Node<T>(e);
            if(nextNode == null)
            {
                if(tail == null)
                {
                    head = tail = newNode;
                } else
                {
                    tail.setNext(newNode);
                    newNode.setPrev(tail);
                    tail = newNode;
                }
            } else
            {
                Node<T> prevNode = nextNode.getPrev();
                newNode.setNext(nextNode);
                newNode.setPrev(prevNode);
                nextNode.setPrev(newNode);
                if(prevNode == null)
                {
                    head = newNode;
                } else
                {
                    prevNode.setNext(newNode);
                }
            }
            nextIndex++;
            size++;
            modCount++;
            iterModCount++;
            lastReturned = null;
            canChange = false;
        }
        
    }
}