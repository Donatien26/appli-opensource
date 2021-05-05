package com.example.appliopensource.model;

import java.util.List;

public class Utilisateur {

	private String idep;
	private String nom;
	private List<String> roles;

	public Utilisateur() {
	}

	public Utilisateur(String idep, String nom) {
		this.idep = idep;
		this.nom = nom;
	}

	public String getIdep() {
		return idep;
	}

	public void setIdep(String idep) {
		this.idep = idep;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
