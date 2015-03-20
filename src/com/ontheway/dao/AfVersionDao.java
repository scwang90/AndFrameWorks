package com.ontheway.dao;

import android.content.Context;
import android.database.Cursor;

import com.ontheway.database.AfDbOpenHelper;
import com.ontheway.entity.VersionEntity;

public class AfVersionDao extends AfDao<VersionEntity> {
	
	public AfVersionDao(Context context) {
		// TODO Auto-generated method stub
		super(context, VersionEntity.class);
	}

	public VersionEntity getVersion() {
		// TODO Auto-generated method stub
		Cursor cur = getCursorLimit("*", 1, 0);
		if (cur.moveToNext()) {
			return getEntityFromCursor(cur);
		} else {
			// 数据库没有数据 说明刚刚创建 添加版本信息
			VersionEntity tEntity = new VersionEntity();
			add(tEntity);
			return tEntity;
		}
	}

	/**
	 * 从Cursor中构造所以字段 返回 Entity
	 * @param cur
	 * @return
	 */
	protected VersionEntity getEntityFromCursor(Cursor cur) {
		// TODO Auto-generated method stub
		VersionEntity tEntity = new VersionEntity();
		tEntity.Version = AfDbOpenHelper.getInt(cur, "Version");
		return tEntity;
	}
}
