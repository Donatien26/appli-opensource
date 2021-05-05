package com.example.appliopensource.service;

import java.util.List;

import com.example.appliopensource.model.Utilisateur;

public interface UtilisateurService {

	List<Utilisateur> findAll();

	Utilisateur whoIam();

}
