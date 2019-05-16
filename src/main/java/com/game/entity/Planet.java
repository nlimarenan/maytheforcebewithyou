package com.game.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.game.response.ShowPlanetResponse;


@DynamoDBTable(tableName = "PLANET")
public class Planet {
	private String id;
	private String nome;
	private String clima;
	private String terreno;
	private String qtdAparicoes;
	
	@DynamoDBHashKey
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@DynamoDBAttribute
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@DynamoDBAttribute
	public String getClima() {
		return clima;
	}
	public void setClima(String clima) {
		this.clima = clima;
	}
	
	@DynamoDBAttribute
	public String getTerreno() {
		return terreno;
	}
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	
	public String getQtdAparicoes() {
		return qtdAparicoes;
	}

	public void setQtdAparicoes(String qtdAparicoes) {
		this.qtdAparicoes = qtdAparicoes;
	}

	public static ShowPlanetResponse convertEntityToResponse(Planet planet) {
		
		ShowPlanetResponse response = new ShowPlanetResponse();
		response.setClima(planet.getClima());
		response.setNome(planet.getNome());
		response.setTerreno(planet.getTerreno());
		response.setQtdAparicoes(planet.getQtdAparicoes());
		
		return response;
	}
	
}
