package com.andframe.entity;


import java.io.Serializable;

public interface EntityBase<Model> extends Serializable
{
    /**
     * 获取Model
     * @return
     */
    public Model getModel();
    
}
