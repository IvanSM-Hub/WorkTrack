package com.worktrack.services.implementations;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.services.CRUDService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CRUDServiceImpl<E, ID extends Serializable> implements CRUDService<E>  {

    
    protected E entity;
    protected Class<E> entityClass;
    
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
    
    protected E createNewEntity() throws Exception {
        try {
            Constructor<E> constructor = getEntityClass().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            log.error("Error creating new entity instance", e);
            throw new Exception("Error generating the entity", e);
        }
    }

    protected abstract JpaRepository<E, ID> getRepository();
    
    @SuppressWarnings("unchecked")
    public Class<E> getEntityClass() {
        if (this.entityClass == null) {
            ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<E> cls = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
            this.entityClass = cls;
        }
        return this.entityClass;
    }
    
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public E create(E entity) {
        return getRepository().save(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E read(Object key) {
        return getRepository().findById((ID) key).orElse(null);
    }

    @Override
    public E update(E entity) {
        return getRepository().save(entity);
    }

    @Override
    public E delete(Object key) {
        E entity = this.read(key);
        if (entity == null) {
            return null;
        }
        getRepository().delete(entity);
        return entity;
    }

    protected <T> T mapTo(E entity, Function<E, T> mapper) {
        return mapper.apply(entity);
    }
    
}
