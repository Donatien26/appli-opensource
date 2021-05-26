package com.example.appliopensource.model;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

	private String idep;
	private String nom;
	private List<String> roleApp = new ArrayList<>();
	private String phone;

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

	public List<String> getRoleApp() {
		return this.roleApp;
	}

	public void setRoleApp(List<String> roleApp) {
		this.roleApp = roleApp;
	}

	public void addRoleApp(String roleApp) {
		this.roleApp.add(roleApp);
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
