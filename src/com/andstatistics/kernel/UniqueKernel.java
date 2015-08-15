package com.andstatistics.kernel;

import android.content.Context;

import com.andframe.caches.AfJsonCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.util.java.AfDateGuid;

public class UniqueKernel
{
	private static final String CACHE_NAME = "08083855820292705102";

	private AfJsonCache mDurableUnique = null;
	private AfJsonCache mPrivateUnique = null;

	public UniqueKernel(Context context){
		// TODO Auto-generated constructor stub
		mDurableUnique = UniqueCache.getInstance(CACHE_NAME);
		mPrivateUnique = AfPrivateCaches.getInstance(CACHE_NAME);
	}

	public String getUniqueId() {
		String puniqueId = mPrivateUnique.getString(CACHE_NAME,null);
		String duniqueId = mDurableUnique.getString(CACHE_NAME,null);
		if (puniqueId == null && duniqueId == null){
			String uniqueId = AfDateGuid.NewReverseID();
			mPrivateUnique.put(CACHE_NAME,uniqueId);
			mDurableUnique.put(CACHE_NAME,uniqueId);
			return uniqueId;
		}
		if (puniqueId == null){
			puniqueId = duniqueId;
			mPrivateUnique.put(CACHE_NAME,puniqueId);
		}else if(duniqueId == null){
			duniqueId = puniqueId;
			mDurableUnique.put(CACHE_NAME,duniqueId);
		}
		return puniqueId;
	}
}

