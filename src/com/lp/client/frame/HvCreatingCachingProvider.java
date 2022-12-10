package com.lp.client.frame;

public abstract class HvCreatingCachingProvider<K, V> extends HvCachingProvider<K, V> {
//	private int readRequests ;
//	private int writeRequests ;

	@Override
	public V getValueOfKey(K key) throws ExceptionLP {
//		++readRequests ;

		K transformedKey = transformKey(key) ;
		if(containsKey(transformedKey)) {
			return super.getValueOfKey(transformedKey) ;
		}

		V value = provideValue(key, transformedKey) ;
		put(key, value);
		return value ;
	}

	@Override
	public void put(K key, V value) throws ExceptionLP {
//		++writeRequests ;
		super.put(transformKey(key), value);
	}

	public void remove(K key) throws ExceptionLP {
		super.remove(transformKey(key));
	}
	
	@Override
	public boolean containsKey(K key) throws ExceptionLP {
		K transformedKey = transformKey(key) ;
		return super.containsKey(transformedKey);
	}

	protected K transformKey(K key) throws ExceptionLP {
		return key ;
	}

	protected abstract V provideValue(K key, K transformedKey) throws ExceptionLP ;
}
