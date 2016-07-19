/**
 * 
 */
package com.flatironschool.javacs;

import java.util.List;
import java.util.Map;

/**
 * Implementation of a HashMap using a collection of MyLinearMap and
 * resizing when there are too many entries.
 * 
 * @author downey
 * @param <K>
 * @param <V>
 *
 */
public class MyHashMap<K, V> extends MyBetterMap<K, V> implements Map<K, V> {
	
	// average number of entries per map before we rehash
	protected static final double FACTOR = 1.0;

	//Fixing perfomance bug -  make size() take constant time
	private int totalSize = 0;

	@Override
	public V put(K key, V value) {
		MyLinearMap<K, V> chosenMap = chooseMap(key);
		totalSize -= chosenMap.size();
		V oldValue = super.put(key, value);
		totalSize += chosenMap.size();
		
		//System.out.println("Put " + key + " in " + map + " size now " + map.size());
		
		// check if the number of elements per map exceeds the threshold
		if (size() > maps.size() * FACTOR) {
			totalSize = 0; //Fixing performance bug
			rehash();
		}
		return oldValue;
	}

	/**
	 * Fixing perfomance bug in clear() in MyBetterMap
	 *
	 */
	@Override
	public void clear() {
		super.clear();
		totalSize = 0;
	}


	/** 
	 * Fixes perfomance bug in remove() in MyBetterMap
	 *
	 */
	@Override
	public V remove(Object key) {
		MyLinearMap<K, V> map = chooseMap(key);
		totalSize -= map.size();
		V previous = map.remove(key);
		totalSize += map.size();
		return previous;
	}


	/**
	 * Removes performance bug in size() iin MyBetterMap
	 * 
	 */
	@Override
	public int size() {
		return totalSize;
	}

	/**
	 * Doubles the number of maps and rehashes the existing entries.
	 */
	/**
	 * 
	 */
	protected void rehash() {
		List<MyLinearMap<K, V>> previousMaps = maps;
        int newSize = maps.size() * 2;
        makeMaps(newSize);

        for(MyLinearMap<K, V> map: previousMaps) {
        	for(Map.Entry<K, V> entry: map.getEntries()) {
        		put(entry.getKey(), entry.getValue());
        	}
        }    
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyHashMap<String, Integer>();
		for (int i=0; i<10; i++) {
			map.put(new Integer(i).toString(), i);
		}
		Integer value = map.get("3");
		System.out.println(value);
	}
}
