package com.example.appliopensource.service;

import java.util.ArrayList;
import java.util.List;

import com.example.appliopensource.model.Utilisateur;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fr.insee.igesa.Personne;
import fr.insee.igesa.Personnes;

@Service
public class UtilisateurService {

	@Value("${com.example.igesa.base_url:}")
	public String igesa_url;

	public List<Utilisateur> findAll() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Personnes> response = restTemplate
				.getForEntity(igesa_url + "/recherche/utilisateur/tous?withGroup=true", Personnes.class);
		List<Utilisateur> utilisateurs = new ArrayList<>();
		List<Personne> personnes = response.getBody().getListe();
		for (Personne personne : personnes) {
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setIdep(personne.getUid());
			utilisateur.setNom(personne.getCn());
			utilisateur.setPhone(personne.getPhone());
			List<String> userGroup = new ArrayList<>();
			try {
				personne.getGroupe().stream().forEach(groupe -> userGroup.add(groupe.getCn()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			utilisateur.setRoleApp(userGroup);
			utilisateurs.add(utilisateur);

		}
		return utilisateurs;
	}

	public Utilisateur whoIam() {
		Utilisateur utilisateur = new Utilisateur();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		utilisateur.setIdep(authentication.getName());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Personnes> response = restTemplate.getForEntity(igesa_url + "/recherche/utilisateur/idep/"
				+ authentication.getName() + "?onlyWithMail=false&groupesAD=true", Personnes.class);
		utilisateur.setPhone(response.getBody().getListe().get(0).getPhone());
		List<String> userGroup = new ArrayList<>();
		try {
			response.getBody().getListe().get(0).getGroupe().stream().forEach(groupe -> userGroup.add(groupe.getCn()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		utilisateur.setRoleApp(userGroup);
		return utilisateur;
	}

}
