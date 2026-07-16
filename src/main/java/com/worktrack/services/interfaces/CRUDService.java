package com.worktrack.services.interfaces;

public interface CRUDService<E> {
    E create(E entity);
    E read(Object key);
    E update(E entity);
    E delete(Object key);
}
