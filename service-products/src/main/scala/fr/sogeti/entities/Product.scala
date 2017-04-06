package fr.sogeti.entities

import javax.persistence.{Id, Basic, Column, JoinColumn, ManyToOne, Table, Entity, GeneratedValue, GenerationType}
import scala.beans.BeanProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Product
 * @param ref the reference
 * @param desc the description
 * @param price1 the price
 * @param img the image
 * @param idSup the id of it's supplier
 * @param cat the category
 */
@Entity
@Table(name= "product")
class Product(ref : String, design : String, desc : String, price1 : Double, img : String, idSup : Integer, cat : Category) {
    
    /**
     * the product's id
     */
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    @BeanProperty
    var id : Int = _;
    
    /**
     * the product's reference
     */
    @Column(name = "reference")
    @Expose
    @BeanProperty
    var reference : String = ref;
    
    /**
     * the product's name
     */
    @Column(name = "designation")
    @Expose
    @BeanProperty
    var designation : String = design;
    
    /**
     * the product's description
     */
    @Column(name = "description")
    @Expose
    @BeanProperty
    var description : String = desc;
    
    /**
     * the product's price
     */
    @Column(name = "price")
    @Expose
    @BeanProperty
    var price : Double = price1;
    
    /**
     * the id of the product's supplier
     */
    @Basic(optional = false)
    @Column(name = "id_supplier")
    @Expose
    @SerializedName("id_supplier")
    @BeanProperty
    var idSupplier : Integer = idSup
    
    /**
     * the product's image 
     */
    @Column(name = "image")
    @Expose
    @BeanProperty
    var image : String = img
    
    /**
     * the product's category
     */
    @JoinColumn(name = "id_category", referencedColumnName = "id", insertable = false, updatable = false)
    @Expose(serialize = false, deserialize = false)
    @ManyToOne
    @BeanProperty
    var category : Category = cat;
    
    
    @Column(name = "id_category")
    @Expose
    @SerializedName("id_category")
    @BeanProperty
    var idCategory : Integer = _
    
    /**
     * the default empty constructor
     */
    def this() = this(null, null, null, -1, null, -1, null);

    override def toString = "id : %d, reference : %s, designation : %s, description : %s, price : %s, idSup : %d, image : %s, category : %s" .format(id, reference, designation, description, price, idSupplier, image, idCategory);
}