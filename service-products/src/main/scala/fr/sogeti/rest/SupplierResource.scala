package fr.sogeti.rest

import io.vertx.scala.ext.web.Router
import fr.sogeti.services.SupplierService
import fr.sogeti.entities.Supplier
import fr.sogeti.rest.common.BaseHandler
import io.vertx.scala.ext.web.RoutingContext

class SupplierResource(router : Router, supplierService : SupplierService) extends GenericService[Supplier](router, supplierService, classOf[Supplier]) {
  
  /**
   * manage a get request on suppliers to find a specific product
   * get the id parameter
   */
  router.get("/supplier/:id").handler(new BaseHandler {
    override def handle( context : RoutingContext ) = findById(context)
  } )
  
  /**
   * manage a get request on suppliers to get all the products
   */
  router.get("/supplier").handler( new BaseHandler {
    override def handle( context : RoutingContext ) = getAll(context)
  } )
  
  /**
   * manage a post request on suppliers to create a new one
   */
  router.post("/supplier").handler( new BaseHandler {
    override def handle( context : RoutingContext ) = create(context)
  } )
  
  /**
   * manage a put request on suppliers to update a product 
   */
  router.put("/supplier").handler( new BaseHandler {
    override def handle( context : RoutingContext ) = update(context)
  } )
  
  /**
   * manage a delete request on suppliers to update a product
   */
  router.delete("/supplier/:id").handler( new BaseHandler {
    override def handle( context : RoutingContext ) = delete(context)
  } )
}