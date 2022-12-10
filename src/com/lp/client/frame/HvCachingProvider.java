package com.lp.client.frame;
import java.util.HashMap;
import java.util.Map;

public class HvCachingProvider<K,V> {

	private Map<K, V> cache = new HashMap<K, V>();

	public void put(K key,V value) throws ExceptionLP {
		cache.put(key, value);
	}

	public void remove(K key) throws ExceptionLP {
		cache.remove(key);
	}
	
	public V getValueOfKey(K key) throws ExceptionLP {
		return cache.get(key);
	}

	public void clear() {
		cache.clear();
	}

	public boolean containsKey(K key) throws ExceptionLP {
		return cache.containsKey(key) ;
	}

	public int getSize(){
		return cache.size();
	}

}

