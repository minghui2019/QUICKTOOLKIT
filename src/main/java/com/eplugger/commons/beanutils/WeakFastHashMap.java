package com.eplugger.commons.beanutils;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

class WeakFastHashMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 7064000633721039972L;

	private Map<K, V> map = null;

	private boolean fast = false;

	public WeakFastHashMap() {
		super();
		this.map = createMap();
	}

	public WeakFastHashMap(int capacity) {
		super();
		this.map = createMap(capacity);
	}

	public WeakFastHashMap(int capacity, float factor) {
		super();
		this.map = createMap(capacity, factor);
	}

	public WeakFastHashMap(Map<K, V> map) {
		super();
		this.map = createMap(map);
	}

	public boolean getFast() {
		return (this.fast);
	}

	public void setFast(boolean fast) {
		this.fast = fast;
	}

	public V get(Object key) {
		if (fast) {
			return (map.get(key));
		} else {
			synchronized (map) {
				return (map.get(key));
			}
		}
	}

	public int size() {
		if (fast) {
			return (map.size());
		} else {
			synchronized (map) {
				return (map.size());
			}
		}
	}

	public boolean isEmpty() {
		if (fast) {
			return (map.isEmpty());
		} else {
			synchronized (map) {
				return (map.isEmpty());
			}
		}
	}

	public boolean containsKey(Object key) {
		if (fast) {
			return (map.containsKey(key));
		} else {
			synchronized (map) {
				return (map.containsKey(key));
			}
		}
	}

	public boolean containsValue(Object value) {
		if (fast) {
			return (map.containsValue(value));
		} else {
			synchronized (map) {
				return (map.containsValue(value));
			}
		}
	}

	public V put(K key, V value) {
		if (fast) {
			synchronized (this) {
				Map<K, V> temp = cloneMap(map);
				V result = temp.put(key, value);
				map = temp;
				return (result);
			}
		} else {
			synchronized (map) {
				return (map.put(key, value));
			}
		}
	}

	public void putAll(Map<? extends K, ? extends V> in) {
		if (fast) {
			synchronized (this) {
				Map<K, V> temp = cloneMap(map);
				temp.putAll(in);
				map = temp;
			}
		} else {
			synchronized (map) {
				map.putAll(in);
			}
		}
	}

	public V remove(Object key) {
		if (fast) {
			synchronized (this) {
				Map<K, V> temp = cloneMap(map);
				V result = temp.remove(key);
				map = temp;
				return (result);
			}
		} else {
			synchronized (map) {
				return (map.remove(key));
			}
		}
	}

	public void clear() {
		if (fast) {
			synchronized (this) {
				map = createMap();
			}
		} else {
			synchronized (map) {
				map.clear();
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		// Simple tests that require no synchronization
		if (o == this) {
			return (true);
		} else if (!(o instanceof Map)) {
			return (false);
		}
		Map<K, V> mo = (Map<K, V>) o;

		// Compare the two maps for equality
		if (fast) {
			if (mo.size() != map.size()) {
				return (false);
			}
			Iterator<Entry<K, V>> i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry<K, V> e = i.next();
				Object key = e.getKey();
				Object value = e.getValue();
				if (value == null) {
					if (!(mo.get(key) == null && mo.containsKey(key))) {
						return (false);
					}
				} else {
					if (!value.equals(mo.get(key))) {
						return (false);
					}
				}
			}
			return (true);

		} else {
			synchronized (map) {
				if (mo.size() != map.size()) {
					return (false);
				}
				Iterator<Entry<K, V>> i = map.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry<K, V> e = i.next();
					Object key = e.getKey();
					Object value = e.getValue();
					if (value == null) {
						if (!(mo.get(key) == null && mo.containsKey(key))) {
							return (false);
						}
					} else {
						if (!value.equals(mo.get(key))) {
							return (false);
						}
					}
				}
				return (true);
			}
		}
	}

	public int hashCode() {
		if (fast) {
			int h = 0;
			Iterator<Entry<K, V>> i = map.entrySet().iterator();
			while (i.hasNext()) {
				h += i.next().hashCode();
			}
			return (h);
		} else {
			synchronized (map) {
				int h = 0;
				Iterator<Entry<K, V>> i = map.entrySet().iterator();
				while (i.hasNext()) {
					h += i.next().hashCode();
				}
				return (h);
			}
		}
	}

	public Object clone() {
		WeakFastHashMap<K, V> results = null;
		if (fast) {
			results = new WeakFastHashMap<K, V>(map);
		} else {
			synchronized (map) {
				results = new WeakFastHashMap<K, V>(map);
			}
		}
		results.setFast(getFast());
		return (results);
	}

	public Set entrySet() {
		return new EntrySet();
	}

	public Set keySet() {
		return new KeySet();
	}

	public Collection values() {
		return new Values();
	}

	protected Map<K, V> createMap() {
		return new WeakHashMap<K, V>();
	}

	protected Map<K, V> createMap(int capacity) {
		return new WeakHashMap<K, V>(capacity);
	}

	protected Map<K, V> createMap(int capacity, float factor) {
		return new WeakHashMap<K, V>(capacity, factor);
	}

	protected Map<K, V> createMap(Map<K, V> map) {
		return new WeakHashMap<K, V>(map);
	}

	protected Map<K, V> cloneMap(Map<K, V> map) {
		return createMap(map);
	}

	private abstract class CollectionView implements Collection<K> {

		public CollectionView() {
		}

		protected abstract Collection get(Map<K, V> map);

		protected abstract Object iteratorNext(Map.Entry entry);

		public void clear() {
			if (fast) {
				synchronized (WeakFastHashMap.this) {
					map = createMap();
				}
			} else {
				synchronized (map) {
					get(map).clear();
				}
			}
		}

		public boolean remove(Object o) {
			if (fast) {
				synchronized (WeakFastHashMap.this) {
					Map<K, V> temp = cloneMap(map);
					boolean r = get(temp).remove(o);
					map = temp;
					return r;
				}
			} else {
				synchronized (map) {
					return get(map).remove(o);
				}
			}
		}

		public boolean removeAll(Collection o) {
			if (fast) {
				synchronized (WeakFastHashMap.this) {
					Map<K, V> temp = cloneMap(map);
					boolean r = get(temp).removeAll(o);
					map = temp;
					return r;
				}
			} else {
				synchronized (map) {
					return get(map).removeAll(o);
				}
			}
		}

		public boolean retainAll(Collection o) {
			if (fast) {
				synchronized (WeakFastHashMap.this) {
					Map<K, V> temp = cloneMap(map);
					boolean r = get(temp).retainAll(o);
					map = temp;
					return r;
				}
			} else {
				synchronized (map) {
					return get(map).retainAll(o);
				}
			}
		}

		public int size() {
			if (fast) {
				return get(map).size();
			} else {
				synchronized (map) {
					return get(map).size();
				}
			}
		}

		public boolean isEmpty() {
			if (fast) {
				return get(map).isEmpty();
			} else {
				synchronized (map) {
					return get(map).isEmpty();
				}
			}
		}

		public boolean contains(Object o) {
			if (fast) {
				return get(map).contains(o);
			} else {
				synchronized (map) {
					return get(map).contains(o);
				}
			}
		}

		public boolean containsAll(Collection o) {
			if (fast) {
				return get(map).containsAll(o);
			} else {
				synchronized (map) {
					return get(map).containsAll(o);
				}
			}
		}

		public Object[] toArray(Object[] o) {
			if (fast) {
				return get(map).toArray(o);
			} else {
				synchronized (map) {
					return get(map).toArray(o);
				}
			}
		}

		public Object[] toArray() {
			if (fast) {
				return get(map).toArray();
			} else {
				synchronized (map) {
					return get(map).toArray();
				}
			}
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (fast) {
				return get(map).equals(o);
			} else {
				synchronized (map) {
					return get(map).equals(o);
				}
			}
		}

		public int hashCode() {
			if (fast) {
				return get(map).hashCode();
			} else {
				synchronized (map) {
					return get(map).hashCode();
				}
			}
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection c) {
			throw new UnsupportedOperationException();
		}

		public Iterator iterator() {
			return new CollectionViewIterator();
		}

		private class CollectionViewIterator implements Iterator {

			private Map<K, V> expected;
			private Map.Entry lastReturned = null;
			private Iterator iterator;

			public CollectionViewIterator() {
				this.expected = map;
				this.iterator = expected.entrySet().iterator();
			}

			public boolean hasNext() {
				if (expected != map) {
					throw new ConcurrentModificationException();
				}
				return iterator.hasNext();
			}

			public Object next() {
				if (expected != map) {
					throw new ConcurrentModificationException();
				}
				lastReturned = (Map.Entry) iterator.next();
				return iteratorNext(lastReturned);
			}

			public void remove() {
				if (lastReturned == null) {
					throw new IllegalStateException();
				}
				if (fast) {
					synchronized (WeakFastHashMap.this) {
						if (expected != map) {
							throw new ConcurrentModificationException();
						}
						WeakFastHashMap.this.remove(lastReturned.getKey());
						lastReturned = null;
						expected = map;
					}
				} else {
					iterator.remove();
					lastReturned = null;
				}
			}
		}
	}

	private class KeySet extends CollectionView implements Set<K> {
		@Override
		protected Collection<K> get(Map<K, V> map) {
			return map.keySet();
		}

		protected Object iteratorNext(Map.Entry entry) {
			return entry.getKey();
		}
	}

	private class Values extends CollectionView {
		protected Collection get(Map<K, V> map) {
			return map.values();
		}

		protected Object iteratorNext(Map.Entry entry) {
			return entry.getValue();
		}
	}

	private class EntrySet extends CollectionView implements Set<K> {
		protected Collection get(Map<K, V> map) {
			return map.entrySet();
		}

		protected Object iteratorNext(Map.Entry entry) {
			return entry;
		}
	}
}
