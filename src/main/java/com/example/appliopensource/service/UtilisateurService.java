package com.example.appliopensource.service;

import java.util.ArrayList;
import java.util.List;

import com.example.appliopensource.model.Utilisateur;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

	private static List<Utilisateur> utilisateurs = new ArrayList<>();

	static {
		utilisateurs.add(new Utilisateur("azerty", "Toto"));
		utilisateurs.add(new Utilisateur("qwerty", "Tutu"));
	}

	public List<Utilisateur> findAll() {
		return utilisateurs;
	}

	public Utilisateur whoIam() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.getName();
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setIdep(authentication.getName());
		return utilisateur;
	}

}
