/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author LomitoFrito
 */
@Entity
@Table(name = "LINEA")
@NamedQueries(
{
    @NamedQuery(name = "Linea.findAll", query = "SELECT l FROM Linea l")
})
public class Linea implements Serializable
{

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LineaPK lineaPK;
    @Column(name = "CANTIDAD")
    private BigInteger cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SUBTOTAL")
    private Double subtotal;
    @JoinColumn(name = "LIBROISBN", referencedColumnName = "ISBN")
    @ManyToOne
    private Libro libroisbn;
    @JoinColumn(name = "PRESTAMONUMERO", referencedColumnName = "NUMERO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Prestamo prestamo;

    public Linea()
    {
    }

    public Linea(LineaPK lineaPK)
    {
        this.lineaPK = lineaPK;
    }

    public Linea(BigInteger id, BigInteger prestamonumero)
    {
        this.lineaPK = new LineaPK(id, prestamonumero);
    }

    public LineaPK getLineaPK()
    {
        return lineaPK;
    }

    public void setLineaPK(LineaPK lineaPK)
    {
        this.lineaPK = lineaPK;
    }

    public BigInteger getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad)
    {
        this.cantidad = cantidad;
    }

    public Double getSubtotal()
    {
        return subtotal;
    }

    public void setSubtotal(Double subtotal)
    {
        this.subtotal = subtotal;
    }

    public Libro getLibroisbn()
    {
        return libroisbn;
    }

    public void setLibroisbn(Libro libroisbn)
    {
        this.libroisbn = libroisbn;
    }

    public Prestamo getPrestamo()
    {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo)
    {
        this.prestamo = prestamo;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (lineaPK != null ? lineaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Linea))
        {
            return false;
        }
        Linea other = (Linea) object;
        if((this.lineaPK == null && other.lineaPK != null) || (this.lineaPK != null && !this.lineaPK.equals(other.lineaPK)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Linea{" + "lineaPK=" + lineaPK + ", cantidad=" + cantidad + ", subtotal=" + subtotal + ", libroisbn=" + libroisbn + ", prestamo=" + prestamo + '}';
    }


    
}
