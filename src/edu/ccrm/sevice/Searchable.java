package edu.ccrm.service;

import java.util.List;
import java.util.Optional;

/**
 * Interface demonstrating abstraction and polymorphism
 * Generic interface for searchable entities
 */
public interface Searchable<T> {
    List<T> search(String query);
    Optional<T> findById(String id);
}