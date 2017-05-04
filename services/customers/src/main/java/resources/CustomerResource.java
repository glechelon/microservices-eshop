package resources;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import domain.Customer;
import services.CustomerServices;

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
/**
 * Resource dealing with the customers.
 * 
 * @author guillaume
 *
 */
public class CustomerResource {

    /**
     * Service dealing with the customers.
     */
    private CustomerServices customerServices;

    /**
     * 
     * @param customerServices
     */
    public CustomerResource(CustomerServices customerServices) {
	this.customerServices = customerServices;
    }

    @GET
    @Path("/{customerID}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Handles the GET method.
     * 
     * @param customerID
     * @return customer in JSON format.
     */
    public Customer getCustomer(@PathParam("customerID") String customerID) {

	return customerServices.getCustomer(customerID);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Handles the GET method.
     * 
     * @return a list of customers.
     */
    public ArrayList<Customer> getCustomers() {

	return customerServices.getCustomers();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * Handles the POST method.
     * 
     * @param customer
     * @return customerID of type int.
     */
    public String postCustomer(Customer customer) {

	customer = customerServices.addCustomer(customer);
	return "{\"id\":" + "\"" + customer.getId() + "\"}";

    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * Handles the PUT method.
     * 
     * @param customer
     * @return customerID of type int
     */
    public String putCustomer(Customer customer) {

	customerServices.modifyCustomer(customer);
	return "{\"id\":" + "\"" + customer.getId() + "\"}";

    }

    @DELETE
    @Path("/{customerID}")
    /**
     * Handles the DELETE method.
     * 
     * @param customerID
     * @return a message of type string.
     */
    public String deleteCustomer(@PathParam("customerID") String customerID) {

	customerServices.removeCustomer(customerID);
	return "Client has been successfully deleted.";
    }

    /**
     * 
     * @return a customerService.
     */
    public CustomerServices getCustomerServices() {

	return customerServices;
    }

    /**
     * 
     * @param customerServices
     */
    public void setCustomerServices(CustomerServices customerServices) {

	this.customerServices = customerServices;
    }

}
