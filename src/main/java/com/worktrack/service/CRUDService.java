package com.worktrack.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CRUDService<E> implements Serializable {

    protected E entity;
    private Class<E> entityClass = null;

    public E getEntity() throws Exception {
        if (entity == null) {
            try {
                entity = (E) createNewEntity();
            } catch (Exception e) {
                log.error("Error generating the entity");
                throw new Exception("Error generating the entity", e);
            }
        }
        return entity;
    }

    protected E createNewEntity() {
        Object object = getEntityClass().newInstance();
    }

    public Class<E> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        
        this.entityClass = entityClass;
    }
    
}
