package services;

import dao.GenericDAO;
import domain.AuthStatus;
import domain.Credentials;

public class AuthService {

    private GenericDAO<Credentials> dao;

    public AuthService(GenericDAO<Credentials> dao) {
	this.dao = dao;
    }

    public void authentification(Credentials credentials) {

	// TODO : authentification + cr�ation du token -> stokage en BDD
    }

    public AuthStatus retreiveAuthStatus(String token) {

	// TODO : retourne si l'utilisateur est connecr� ou pas.

	return null;
    }
    
    public void deleteAuthToken(String token){
	
	//TODO : r�cup�re l'id utilisateur contenue dans le token et l'utilise comme cl� pour supprimer le token.
    }

    public GenericDAO<Credentials> getDao() {

	return dao;
    }

    public void setDao(GenericDAO<Credentials> dao) {

	this.dao = dao;
    }

}
