package com.game.request;

import com.game.entity.Planet;

public class SavePlanetRequest {
	private String nome;
	private String clima;
	private String terreno;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClima() {
		return clima;
	}
	public void setClima(String clima) {
		this.clima = clima;
	}
	public String getTerreno() {
		return terreno;
	}
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	
	public static Planet convertRequestToEntity(Integer qtdAparicoes, SavePlanetRequest request) {
		
		Planet aNewPlanetIsBorning = new Planet();
		aNewPlanetIsBorning.setNome(request.getNome());
		aNewPlanetIsBorning.setClima(request.getClima());
		aNewPlanetIsBorning.setTerreno(request.getTerreno());
		aNewPlanetIsBorning.setQtdAparicoes(String.valueOf(qtdAparicoes));
		
		return aNewPlanetIsBorning;
	}
}
