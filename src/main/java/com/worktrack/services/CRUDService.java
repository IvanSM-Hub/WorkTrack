package com.worktrack.services;

public interface CRUDService<E> {
    public E create(E entity);
    E read(Object key);
    E update(E entity);
    E delete(Object key);
}
