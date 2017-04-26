package services

import (
	"encoding/json"
	"strconv"

	"github.com/carts/models"
	"github.com/go-redis/redis"
)

//AddCart adds a cart to redis KV store
func (c *RedisClient) AddCart(cart *models.Cart, gateWayClient *GateWayClient) {

	if gateWayClient.CheckcartValidity(cart) {

		key := strconv.Itoa(cart.CustomerID)
		cart.ComputeTotalPrice()
		value, err := json.Marshal(cart)
		failOnError(err)
		err = c.Client.Set(key, value, 0).Err()
		failOnError(err)

	}

}

//RemoveCart removes a car from redis KV store
func (c *RedisClient) RemoveCart(clientID int) {

	key := strconv.Itoa(clientID)
	c.Client.Del(key)
}

//GetCart retrieves the cart of a client in the redis KV store
func (c *RedisClient) GetCart(clientID int) (string, bool) {

	found := false
	key := strconv.Itoa(clientID)
	value, err := c.Client.Get(key).Result()
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
