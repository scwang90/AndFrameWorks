package com.andframe.dao;

import android.content.Context;

import com.andframe.entity.VersionEntity;

import java.util.List;

public class AfVersionDao extends AfDao<VersionEntity> {
	
	public AfVersionDao(Context context) {
		super(context);
	}

	public VersionEntity getVersion() {
		List<Model> models = getModelsLimit("*", 1, 0);
		if (models.size() > 0) {
			return getEntityFromModel(models.get(0));
		} else {
			// 数据库没有数据 说明刚刚创建 添加版本信息
			VersionEntity tEntity = new VersionEntity();
			add(tEntity);
			return tEntity;
		}
	}

	/**
	 * 从Cursor中构造所以字段 返回 Entity
	 * @param model
	 * @return
	 */
	protected VersionEntity getEntityFromModel(Model model) {
		VersionEntity tEntity = new VersionEntity();
		tEntity.Version = model.getInt("Version");
		return tEntity;
	}
}
