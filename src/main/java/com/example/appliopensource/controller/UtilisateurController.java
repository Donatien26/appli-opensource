package com.example.appliopensource.controller;

import java.util.List;

import com.example.appliopensource.model.Utilisateur;
import com.example.appliopensource.service.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("utilisateurs")
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;

	@GetMapping
	public List<Utilisateur> findUtilisateur() {
		return utilisateurService.findAll();
	}

	@GetMapping(path = "/whoiam")
	public Utilisateur whoIam() {
		return utilisateurService.whoIam();
	}

}
