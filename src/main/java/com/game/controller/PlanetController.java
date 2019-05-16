package com.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.game.request.SavePlanetRequest;
import com.game.response.SavePlanetResponse;
import com.game.response.ShowPlanetResponse;
import com.game.service.PlanetService;

@RestController
public class PlanetController {
	
	@Autowired
	PlanetService service;
	
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<String> ping() {
		
		return ResponseEntity.ok().body("May The Force Be With You!");
	}
	
	@RequestMapping(value = "/savePlanet", method = RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<SavePlanetResponse> createPlanet(@RequestBody SavePlanetRequest request) {
		SavePlanetResponse response = this.service.savePlanet(request);
		
		return ResponseEntity.ok().body(response);
	}

	@RequestMapping(value = "/showLocalPlanets", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<List<ShowPlanetResponse>> showPlanets() {
		List<ShowPlanetResponse> response = this.service.showLocalPlanets();
		return ResponseEntity.ok().body(response);
	}
	
	@RequestMapping(value = "/showAllPlanetsFromApi/page/{pageNumber}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<String> showAllPlanetsPageByPage(@PathVariable Integer pageNumber) {
		return this.service.showAllPlanets(pageNumber);
	}
	
	@RequestMapping(value = "/showPlanetFromApi/{id}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<String> showPlanet(@PathVariable Integer id){
		
		return this.service.showPlanet(id);
	}
	
	@RequestMapping(value = "/showPlanetByName/{planetName}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<ShowPlanetResponse> showPlanetByName(@PathVariable String planetName) {
		ShowPlanetResponse response = this.service.showPlanetByName(planetName);
		return ResponseEntity.ok().body(response);
	}
	
	@RequestMapping(value = "/showPlanetById/{id}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public ResponseEntity<ShowPlanetResponse> showAPlanetById(@PathVariable Integer id) {
		ShowPlanetResponse response = this.service.showPlanetById(id);
		return ResponseEntity.ok().body(response);
	}
	
	@RequestMapping(value = "/destroyPlanetById/{id}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public void destroyPlanetById(@PathVariable Integer id) {
		this.service.destroyPlanetById(id);
	}
	
	@RequestMapping(value = "/destroyPlanetName/{planetName}", method = RequestMethod.GET, produces="application/json", consumes="application/json")
	public void destroyPlanetByName(@PathVariable String planetName) {
		this.service.destroyPlanetByName(planetName);
	}

}
