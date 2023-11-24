package com.unilock.activator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unilock.activator.model.Key;

@Repository
public interface KeyRepositiry extends JpaRepository<Key, Long> {

}
