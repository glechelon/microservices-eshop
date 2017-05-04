package services;

import java.util.Objects;
import java.util.Optional;

import dao.AuthDAO;
import dao.CustomerDAO;
import dao.DAO;
import domain.Credentials;
import domain.Customer;
import elements.AuthStatus;
import elements.AuthToken;
import elements.Error;

/**
 * Class containing the different services dealing with customers.
 * 
 * @author guillaume
 */
public class AuthService {

    /**
     * DAO class referencing the DB.
     */
    private DAO dao;
    /**
     * DAO class dealing with the customer map.
     */
    private CustomerDAO customerDAO;
    /**
     * DAO class dealign with the authentication.
     */
    private AuthDAO authDAO;

    /**
     * 
     * @param dao
     */
    public AuthService(DAO dao) {
	this.setDao(dao);
	this.customerDAO = new CustomerDAO(dao);
	this.authDAO = new AuthDAO(dao);

    }

    /**
     * Tries to authenticate a customer using the given credentials.
     * 
     * @param credentials
     * @return token or error in JSON format.
     */
    public String authentification(Credentials credentials) {

	System.out.println(credentials.getEmail() + " _____ "  + credentials.getPassWord());
	Optional<Customer> optCustomer = customerDAO.retreiveElementByEmail(credentials.getEmail());

	if (!optCustomer.isPresent()) {

	    return new Error(Error.CODE_NOT_FOUND, Error.MSG_NOT_FOUND).toJson();
	    

	} else {

	    Customer customer = optCustomer.get();

	    if (customer.getCredentials().getPassWord().equals(credentials.getPassWord())) {

		String token = new AuthToken(customer.getId()).encodeToJWT();
		authDAO.addElement(customer.getId(), token);

		String jsonResponse = String.format("{ \"code\": 0, \"token\": \"%s\"}", token);

		return jsonResponse;

	    }

	    else {

		return new Error(Error.CODE_PWD_MISMATCH, Error.MSG_PWD_MISMATCH).toJson();
	    }

	}

    }

    /**
     * Retrieves the authStatus of a customer using the given token.
     * 
     * @param token
     * @return AuthStatus
     */
    public AuthStatus retreiveAuthStatus(String token) {

	AuthToken authToken = new AuthToken();
	authToken.decodeToken(token);
	String storedToken = authDAO.retrieveElement(authToken.getCustomerID());
	System.out.println(authToken.getCustomerID());

	if (storedToken != null && storedToken.equals(token) && !Objects.isNull(authToken.getCustomerID())) {

	    System.out.println("auth");
	    return new AuthStatus(AuthStatus.CODE_AUTH, AuthStatus.MSG_AUTH);

	} else {

	    System.out.println("not auth");
	    return new AuthStatus(AuthStatus.CODE_NOT_AUTH, AuthStatus.MSG_NOT_AUTH);
	}

    }

    /**
     * Deletes the stored token.
     * 
     * @param token
     * @return message of type string.
     */
    public String deleteAuthToken(String token) {

	AuthToken authToken = new AuthToken(token);
	authDAO.removeElement(authToken.getCustomerID());
	return "Authentification token as successfully been deleted";

    }

    /**
     * 
     * @return dao of type DAO
     */
    public DAO getDao() {

	return dao;
    }

    /**
     * 
     * @param dao
     */
    public void setDao(DAO dao) {

	this.dao = dao;
    }

}
