package fr.sogeti.dao.common

import scala.collection.immutable.List
import javax.persistence.{Query, EntityManager}
import scala.collection.JavaConversions._
import java.lang.Long


/**
 * Generic DAO
 * @param clazz Entity class
 * @param manager the entity manager
 */
class GenericDAO[Type >: Null, IdType](protected[this] val clazz : Class[Type],protected[this] val manager : EntityManager) {
  
  /**
   * List all the entities of the dao's type
   * @return a list of all the objects found for the given entity
   */
  def getAll(begin : Int, end : Int) : List[Type] = {
    var results : List[Type] = List()
    
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        val name : String = clazz.getName
        val query : Query = manager.createQuery( "SELECT e FROM %s e".format( name ), clazz )
        query.setFirstResult(begin)
        query.setMaxResults(end-begin)
        results = query.getResultList().toList.asInstanceOf[ List[Type] ]
      }
    }
    new TransactionStrategy(manager, strategy).execute
    return results
  }
  
  /**
   * Find
   * @param id the id that we want to find
   * @return the found entity or null else 
   */
  def find(id : IdType) : Type = {
    var found : Type = null
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        found = manager.find(clazz, id) 
      }
    }
    new TransactionStrategy(manager, strategy).execute
    return found
  }
  
  
  /**
   * Deletes the entity
   * @param id the id of the entity we want to delete
   */
  def deleteById(id : IdType) : Unit = {
    val todelete = find(id) 
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        manager.remove( todelete )        
      }
    }
    new TransactionStrategy(manager, strategy).execute
  }
  
  /**
   * Deletes the entity
   * @param entity the entity we want to delete 
   */
  def delete(entity : Type) : Unit = {
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        manager.remove( entity )        
      }
    }
    new TransactionStrategy(manager, strategy).execute
  }
  
  /**
   * Creates an entity
   * @param entity the entity that we want to create
   */
  def create(entity : Type) : Unit = {
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        manager.persist( entity )
      }
    }
    new TransactionStrategy(manager, strategy).execute
  }
  
  /**
   * Updates an entity
   * @param entity the entity to update
   */
  def update(entity : Type) : Unit = {
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        manager.merge( entity )
      }
    }
    new TransactionStrategy(manager, strategy).execute
  }
  
  
  /**
   * retrive the number of entities available in the database
   */
  def getCount : Int = {
    var found : Int = 0
    val strategy : ITransactionStrategy = new ITransactionStrategy {
      override def execute() : Unit = {
        val className : String = clazz.getName
        val query = manager.createQuery("SELECT COUNT(e) FROM %s e".format(className), classOf[Long])
        found = query.getSingleResult.intValue
      }
    }
    new TransactionStrategy(manager, strategy).execute
    return found
  }
}