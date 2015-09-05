package com.andframe.entity;

import com.andframe.database.AfDbOpenHelper;

import java.io.Serializable;

/**
 * orm版本实体类
 */
public class VersionEntity implements Serializable
{
    public int Version = AfDbOpenHelper.DATABASE_VERSION;
}
