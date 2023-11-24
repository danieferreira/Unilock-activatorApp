package com.unilock.activator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unilock.activator.model.Key;
import com.unilock.activator.repository.KeyRepositiry;

@Service
public class KeyService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	KeyRepositiry keyRepositiry;
	
	public void add(Key thisKey) {
		keyRepositiry.save(thisKey);
	}
	
	public List<Key> getAll() {
		return 	keyRepositiry.findAll();
	}
	
}
