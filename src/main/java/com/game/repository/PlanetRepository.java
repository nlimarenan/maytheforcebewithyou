package com.game.repository;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.game.entity.Planet;

@EnableScan
public interface PlanetRepository extends CrudRepository<Planet, String> {

	Optional<Planet> findById(String id);
	
}
