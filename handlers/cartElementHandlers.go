package handlers

import (
	"io/ioutil"
	"net/http"
	"strconv"

	"encoding/json"

	"github.com/carts/models"
	"github.com/carts/services"
)

//HandleCartElementGet handles GET request by retrieving and retruning the cart of the given customer
func HandleCartElementGet(client *services.RedisClient) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		customerID := r.URL.Query().Get("customerID")
		elementID := r.URL.Query().Get("elementID")
		key, err := strconv.Atoi(customerID)
		failOnError(err)
		id, err := strconv.Atoi(elementID)
		failOnError(err)
		element := client.GetCartElement(key, id)
		elementJSON, err := json.Marshal(element)
		failOnError(err)
		w.Header().Set("Content-Type", "application/json")
		w.Write([]byte(elementJSON))

	}
}

//HandleCartElementPost handles POST request by storing the given custmer cart
func HandleCartElementPost(client *services.RedisClient) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

		payloadJSON, err := ioutil.ReadAll(r.Body)
		failOnError(err)
		payload := &models.ElementPayload{}
		json.Unmarshal([]byte(payloadJSON), payload)
		client.AddCartElement(payload.CustomerID, payload.CartElement)
		message := "{\"message\" : \"OK\"}"
		w.Header().Set("Content-Type", "application/json")
		w.Write([]byte(message))

	}

}

//HandleCartElementPut handles PUT request by updating the customer with the given cart
func HandleCartElementPut(client *services.RedisClient) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

		payloadJSON, err := ioutil.ReadAll(r.Body)
		failOnError(err)
		payload := &models.ElementPayload{}
		json.Unmarshal([]byte(payloadJSON), payload)
		client.ModifyCartElement(payload.CustomerID, payload.CartElement.ElementID, payload.CartElement)
		message := "{\"message\" : \"OK\"}"
		w.Header().Set("Content-Type", "application/json")
		w.Write([]byte(message))

	}
}

//HandleCartElementDelete handdles DELETE request by removing the cart of the given customer
func HandleCartElementDelete(client *services.RedisClient) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

		customerID := r.URL.Query().Get("customerID")
		elementID := r.URL.Query().Get("elementID")
		c, err := strconv.Atoi(customerID)
		failOnError(err)
		e, err := strconv.Atoi(elementID)
		failOnError(err)
		client.RemoveCartElement(c, e)
		message := "{\"message\" : \"OK\"}"
		w.Header().Set("Content-Type", "apllication/json")
		w.Write([]byte(message))
	}
}
