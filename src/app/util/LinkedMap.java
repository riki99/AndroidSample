package app.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 格納した順序をほじするマップです。
 */
public class LinkedMap implements Map, Cloneable, Externalizable {

    private static class Entry implements Map.Entry {

        private final Object key;
        private Object value;

        Entry next = null;
        Entry prev = null;

        /**
         *
         * @param key
         * @param value
         */
        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
        /**
         * @return
         */
        public Object getKey() {
          return this.key;
        }
        /**
         * @return
         */
        public Object getValue() {
            return this.value;
        }
        /**
         * @param value
         * @return
         */
        public Object setValue(Object value) {
            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        /**
         * @return
         */
        public int hashCode() {
            return ((getKey() == null ? 0 : getKey().hashCode()) ^
                    (getValue()==null ? 0 : getValue().hashCode()));
        }
        /**
         * @return
         * @param obj
         */
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(obj == this) return true;
            if(!(obj instanceof Map.Entry)) return false;

            Map.Entry other = (Map.Entry)obj;

            return((getKey() == null ?
                  other.getKey() == null :
                  getKey().equals(other.getKey()))  &&
                 (getValue() == null ?
                  other.getValue() == null :
                  getValue().equals(other.getValue())));
        }
        /**
         * @return
         */
        public String toString() {
            return "[" + getKey() + "=" + getValue() + "]";
        }

    }

    /**
     *
     * @return
     */
    private static final Entry createSentinel() {

        Entry s = new Entry(null, null);
        s.prev = s;
        s.next = s;
        return s;

    }

    /**
     *
     */
    private Entry sentinel;

    /**
     *
     */
    private HashMap entries;

    /**
     *
     */
    public LinkedMap() {
        sentinel = createSentinel();
        entries = new HashMap();
    }

    /**
     *
     * @param keyset
     */
    public LinkedMap(Object[][] keyset) {
        this();
        for (int i = 0; i < keyset.length; i++) {
           put(keyset[i][0],keyset[i][1]);
       }
   }

   /**
     *
     * @param initialSize
     */
    public LinkedMap(int initialSize) {
        sentinel = createSentinel();
        entries = new HashMap(initialSize);
    }

    /**
     *
     * @param initialSize
     * @param loadFactor
     */
    public LinkedMap(int initialSize, float loadFactor) {
        sentinel = createSentinel();
        entries = new HashMap(initialSize, loadFactor);
    }

    /**
     *
     * @param m
     */
    public LinkedMap(Map m) {
        this();
        putAll(m);
    }

    /**
     *
     * @param entry
     */
    private void removeEntry(Entry entry) {
        entry.next.prev = entry.prev;
        entry.prev.next = entry.next;
    }

    /**
     *
     * @param entry
     */
    private void insertEntry(Entry entry) {
        entry.next = sentinel;
        entry.prev = sentinel.prev;
        sentinel.prev.next = entry;
        sentinel.prev = entry;
    }

    /**
     *
     * @return
     */
    public int size() {
        return entries.size();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean containsKey(Object key) {
        return entries.containsKey(key);
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean containsValue(Object value) {
        if(value == null) {
            for(Entry pos = sentinel.next; pos != sentinel; pos = pos.next) {
                if(pos.getValue() == null) return true;
            }
        }
        else {
            for(Entry pos = sentinel.next; pos != sentinel; pos = pos.next) {
                if(value.equals(pos.getValue())) return true;
            }
        }
        return false;
    }

    /**
     *
     * @param o
     * @return
     */
    public Object get(Object o) {
        Entry entry = (Entry)entries.get(o);
        if(entry == null) return null;
        return entry.getValue();
    }

    /**
     *
     * @return
     */
    public Map.Entry getFirst() {
        return (isEmpty()) ? null : sentinel.next;
    }

    /**
     * @return
     */
	public synchronized String toString() {

	StringBuffer buf = new StringBuffer("{");
	Iterator itr = keySet().iterator();
	while (itr.hasNext()) {
		Object key = itr.next();
		Object value = get(key);
		buf.append( key+ "=" + value);
		buf.append(", ");
	}
	buf.append("}");
	return buf.toString();
	}

    /**
     *
     * @return
     */
    public Object getFirstKey() {
        return sentinel.next.getKey();
    }

    /**
     *
     * @return
     */
    public Object getFirstValue() {
        return sentinel.next.getValue();
    }

    /**
     *
     * @return
     */
    public Map.Entry getLast() {
        return (isEmpty()) ? null : sentinel.prev;
    }

    /**
     *
     * @return
     */
    public Object getLastKey() {
        return sentinel.prev.getKey();
    }
    /**
     *
     * @return
     */
    public Object getLastValue() {
        return sentinel.prev.getValue();
    }
    /**
     * @param key
     * @param value
     * @return
     */
    public Object put(Object key, Object value) {

        Object oldValue = null;

        Entry e = (Entry)entries.get(key);

        if(e != null) {
        removeEntry(e);

        oldValue = e.setValue(value);

        } else {
          e = new Entry(key, value);
          entries.put(key, e);
        }
        insertEntry(e);

        return oldValue;
    }

    /**
     * @param key
     * @return
     */
    public Object remove(Object key) {
        Entry e = (Entry)entries.remove(key);
        if(e == null) return null;
        removeEntry(e);
        return e.getValue();
    }

    /**
     *
     * @param t
     */
    public void putAll(Map t) {
        Iterator iter = t.entrySet().iterator();
        while(iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          put(entry.getKey(), entry.getValue());
        }
    }

    /**
     *
     */
    public void clear() {
        entries.clear();

        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /**
     *
     * @return
     */
    public Set keySet() {
        return new AbstractSet() {

            public Iterator iterator() {
                return new OrderedIterator(KEY);
            }
            public boolean remove(Object o) {
                return LinkedMap.this.remove(o) != null;
            }

            public void clear() {
                LinkedMap.this.clear();
            }
            public int size() {
                return LinkedMap.this.size();
            }
            public boolean isEmpty() {
                return LinkedMap.this.isEmpty();
            }
            public boolean contains(Object o) {
                return LinkedMap.this.containsKey(o);
            }

        };
    }

    /**
     *
     * @return
     */
    public Collection values() {
        return new AbstractCollection() {

            public Iterator iterator() {
                return new OrderedIterator(VALUE);
            }
            public boolean remove(Object value) {
               if(value == null) {
                   for(Entry pos = sentinel.next; pos != sentinel; pos = pos.next) {
                        if(pos.getValue() == null) {
                          LinkedMap.this.remove(pos.getKey());
                          return true;
                        }
                   }
                }
                else {
                    for(Entry pos = sentinel.next; pos != sentinel; pos = pos.next) {
                        if(value.equals(pos.getValue())) {
                            LinkedMap.this.remove(pos.getKey());
                            return true;
                        }
                    }
                }

                return false;
            }

            public void clear() {
                LinkedMap.this.clear();
            }
            public int size() {
                return LinkedMap.this.size();
            }
            public boolean isEmpty() {
                return LinkedMap.this.isEmpty();
            }
            public boolean contains(Object o) {
                return LinkedMap.this.containsValue(o);
            }
        };
    }

    /**
     *
     * @return
     */
    public Set entrySet() {
        return new AbstractSet() {
          private Entry findEntry(Object o) {
            if(o == null) return null;
            if(!(o instanceof Map.Entry)) return null;

            Map.Entry e = (Map.Entry)o;
            Entry entry = (Entry)entries.get(e.getKey());
            if(entry != null && entry.equals(e)) return entry;
            else return null;
          }

          // required impl
          public Iterator iterator() {
            return new OrderedIterator(ENTRY);
          }
          public boolean remove(Object o) {
            Entry e = findEntry(o);
            if(e == null) return false;

            return LinkedMap.this.remove(e.getKey()) != null;
          }

          // more efficient impls than abstract collection
          public void clear() {
            LinkedMap.this.clear();
          }
          public int size() {
            return LinkedMap.this.size();
          }
          public boolean isEmpty() {
            return LinkedMap.this.isEmpty();
          }
          public boolean contains(Object o) {
            return findEntry(o) != null;
          }
        };
      }

    private static final int KEY = 0;
    private static final int VALUE = 1;
    private static final int ENTRY = 2;
    private static final int REMOVED_MASK = 0x80000000;

    private class OrderedIterator implements Iterator {

        private int returnType;

        /**
         *  Holds the "current" position in the iterator.  when pos.next is the
         *  sentinel, we've reached the end of the list.
         **/
        private Entry pos = sentinel;

        /**
         *  Construct an iterator over the sequenced elements in the order in which
         *  they were added.  The {@link #next()} method returns the type specified
         *  by <code>returnType</code> which must be either {@link #KEY}, {@link
         *  #VALUE}, or {@link #ENTRY}.
         * @param returnType
         **/
        public OrderedIterator(int returnType) {
          //// Since this is a private inner class, nothing else should have
          //// access to the constructor.  Since we know the rest of the outer
          //// class uses the iterator correctly, we can leave of the following
          //// check:
          //if(returnType >= 0 && returnType <= 2) {
          //  throw new IllegalArgumentException("Invalid iterator type");
          //}

          // Set the "removed" bit so that the iterator starts in a state where
          // "next" must be called before "remove" will succeed.
          this.returnType = returnType | REMOVED_MASK;
        }

        /**
         *  Returns whether there is any additional elements in the iterator to be
         *  returned.
         *
         *  @return <code>true</code> if there are more elements left to be
         *  returned from the iterator; <code>false</code> otherwise.
         **/
        public boolean hasNext() {
          return pos.next != sentinel;
        }

        /**
         *  Returns the next element from the iterator.
         *
         *  @return the next element from the iterator.
         *
         *  @exception NoSuchElementException if there are no more elements in the
         *  iterator.
         **/
        public Object next() {
          if(pos.next == sentinel) {
            throw new NoSuchElementException();
          }

          // clear the "removed" flag
          returnType = returnType & ~REMOVED_MASK;

          pos = pos.next;
          switch(returnType) {
          case KEY:
            return pos.getKey();
          case VALUE:
            return pos.getValue();
          case ENTRY:
            return pos;
          default:
            // should never happen
            throw new Error("bad iterator type: " + returnType);
          }

        }

    /**
     *  Removes the last element returned from the {@link #next()} method from
     *  the sequenced map.
     *
     *  @exception IllegalStateException if there isn't a "last element" to be
     *  removed.  That is, if {@link #next()} has never been called, or if
     *  {@link #remove()} was already called on the element.
     **/
    public void remove() {
      if((returnType & REMOVED_MASK) != 0) {
        throw new IllegalStateException("remove() must follow next()");
      }

      // remove the entry
      LinkedMap.this.remove(pos.getKey());

      // set the removed flag
      returnType = returnType | REMOVED_MASK;
    }
  }

  // APIs maintained from previous version of LinkedHashMapX for backwards
  // compatibility

  /**
   * Creates a shallow copy of this object, preserving the internal structure
   * by copying only references.  The keys and values themselves are not
   * <code>clone()</code>'d.  The cloned object maintains the same sequence.
   *
   * @return A clone of this instance.
   *
   * @exception CloneNotSupportedException if clone is not supported by a
   * subclass.
   */
  public Object clone () throws CloneNotSupportedException {
    // yes, calling super.clone() silly since we're just blowing away all
    // the stuff that super might be doing anyway, but for motivations on
    // this, see:
    // http://www.javaworld.com/javaworld/jw-01-1999/jw-01-object.html
    LinkedMap map = (LinkedMap)super.clone();

    // create new, empty sentinel
    map.sentinel = createSentinel();

    // create a new, empty entry map
    // note: this does not preserve the initial capacity and load factor.
    map.entries = new HashMap();

    // add all the mappings
    map.putAll(this);

    // Note: We cannot just clone the hashmap and sentinel because we must
    // duplicate our internal structures.  Cloning those two will not clone all
    // the other entries they reference, and so the cloned hash map will not be
    // able to maintain internal consistency because there are two objects with
    // the same entries.  See discussion in the Entry implementation on why we
    // cannot implement a clone of the Entry (and thus why we need to recreate
    // everything).

    return map;
  }

  /**
   *  Returns the Map.Entry at the specified index
   *
   * @param index
   * @return
   **/
  private Map.Entry getEntry(int index) {
    Entry pos = sentinel;

    if(index < 0) {
      throw new ArrayIndexOutOfBoundsException(index + " < 0");
    }

    // loop to one before the position
    int i = -1;
    while(i < (index-1) && pos.next != sentinel) {
      i++;
      pos = pos.next;
    }
    // pos.next is the requested position

    // if sentinel is next, past end of list
    if(pos.next == sentinel) {
      throw new ArrayIndexOutOfBoundsException(index + " >= " + (i + 1));
    }

    return pos.next;
  }

  /**
   * インデックス番目に格納されている「キー」を取得する。
   * Returns the key at the specified index.
   *
   * @param index
   * @return
   */
  public Object get (int index)
  {
    return getEntry(index).getKey();
  }

  /**
   *
   * @param index
   * @return
   */
  public Object getValue (int index)
  {
    return getEntry(index).getValue();
  }

  /**
   *
   * @param key
   * @return
   */
  public int indexOf (Object key)
  {
    Entry e = (Entry)entries.get(key);
    int pos = 0;
    while(e.prev != sentinel) {
      pos++;
      e = e.prev;
    }
    return pos;
  }

  /**
   *
   * @return
   */
  public Iterator iterator ()
  {
    return keySet().iterator();
  }

  /**
   *
   * @param key
   * @return
   */
  public int lastIndexOf (Object key)
  {
    // keys in a map are guarunteed to be unique
    return indexOf(key);
  }

  /**
   * Returns a List view of the keys rather than a set view.  The returned
   * list is unmodifiable.  This is required because changes to the values of
   * the list (using {@link java.util.ListIterator#set(Object)}) will
   * effectively remove the value from the list and reinsert that value at
   * the end of the list, which is an unexpected side effect of changing the
   * value of a list.  This occurs because changing the key, changes when the
   * mapping is added to the map and thus where it appears in the list.
   *
   * <P>An alternative to this method is to use {@link #keySet()}
   *
   * @see #keySet()
   * @return The ordered list of keys.
   */
  public List sequence()
  {
    List l = new ArrayList(size());
    Iterator iter = keySet().iterator();
    while(iter.hasNext()) {
      l.add(iter.next());
    }

    return Collections.unmodifiableList(l);
  }

  /**
   * Removes the element at the specified index.
   *
   * @param index The index of the object to remove.
   * @return      The previous value coressponding the <code>key</code>, or
   *              <code>null</code> if none existed.
   *
   * @exception ArrayIndexOutOfBoundsException if the <code>index</code> is
   * <code>&lt; 0</code> or <code>&gt;</code> the size of the map.
   */
  public Object remove (int index)
  {
    return remove(get(index));
  }

  /**
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void readExternal( ObjectInput in )
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    for(int i = 0; i < size; i++)  {
      Object key = in.readObject();
      Object value = in.readObject();
      put(key, value);
    }
  }
  /**
   * @param out
   * @throws IOException
   */
  public void writeExternal( ObjectOutput out ) throws IOException {
    out.writeInt(size());
    for(Entry pos = sentinel.next; pos != sentinel; pos = pos.next) {
      out.writeObject(pos.getKey());
      out.writeObject(pos.getValue());
    }
  }

  private static final long serialVersionUID = 3380552487888102930L;

}
