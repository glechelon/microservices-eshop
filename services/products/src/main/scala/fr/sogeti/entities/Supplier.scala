package fr.sogeti.entities

import javax.persistence.{Entity, Table, Id, Basic, Column, GeneratedValue, GenerationType}
import com.google.gson.annotations.Expose

/**
 * Supplier
 * @param company the supplier's company name
 * @param mail the supplier's mail
 * @param phone the supplier's phone 
 */
@Entity
@Table(name = "supplier")
class Supplier(company : String, mail : String, phone : String) {
    /**
     * The suppliers's id
     */
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private var id : Integer = _;
    
    /**
     * the suppliers company name
     */
    @Column(name = "company_name")
    @Expose
    private var companyName : String = company;
    
    /**
     * the supplier's email
     */
    @Column(name = "email")
    @Expose
    private var email : String = mail;
    
    /**
     * the supplier's phone number
     */
    @Column(name = "phone_number")
    @Expose
    private var phoneNumber : String = phone;
    
    /**
     * the default empty construct
     */
    def this() = this(null,null,null);

    override def toString = "id : %d, company name : %s, email : %s, phone number : %s".format(id, companyName, email, phoneNumber)

}