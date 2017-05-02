package services

import (
	"encoding/json"
	"log"

	"github.com/carts/models"
	"github.com/go-redis/redis"
)

//AddCart adds a cart to redis KV store
func (c *RedisClient) AddCart(cart *models.Cart, gateWayClient *GateWayClient) {

	if gateWayClient.CheckcartValidity(cart) {

		cart.ComputeTotalPrice()
		value, err := json.Marshal(cart)
		log.Println(value)
		failOnError(err)
		err = c.Client.Set(cart.CustomerID, value, 0).Err()
		failOnError(err)

	}

}

//RemoveCart removes a car from redis KV store
func (c *RedisClient) RemoveCart(clientID string) {

	c.Client.Del(clientID)
}

//GetCart retrieves the cart of a client in the redis KV store
func (c *RedisClient) GetCart(clientID string) (string, bool) {

	found := false
	value, err := c.Client.Get(clientID).Result()
	failOnError(err)

	if value != "" {

		found = true
	}
	return value, found
}

//failOnError checks for errors and panics if it's the case
func failOnError(err error) {

	if err != nil && err != redis.Nil {

		panic(err)
	}
}
