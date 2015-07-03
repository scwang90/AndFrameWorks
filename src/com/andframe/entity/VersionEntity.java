package com.andframe.entity;

import com.andframe.database.AfDbOpenHelper;

/**
 * orm版本实体类
 */
public class VersionEntity
{
    public int Version = AfDbOpenHelper.DATABASE_VERSION;
}
