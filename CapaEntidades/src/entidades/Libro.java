/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author LomitoFrito
 */
@Entity
@Table(name = "LIBRO")
@NamedQueries(
{
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
})
public class Libro implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ISBN")
    private String isbn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIOBASE")
    private Double preciobase;
    @Column(name = "UNIDADESDISPONIBLES")
    private BigInteger unidadesdisponibles;
    @Column(name = "NUMEROIMAGENES")
    private BigInteger numeroimagenes;
    @Column(name = "NUMEROVIDEOS")
    private BigInteger numerovideos;
    @OneToMany(mappedBy = "libroisbn")
    private List<Linea> lineaList;

    public Libro()
    {
    }

    public Libro(String isbn)
    {
        this.isbn = isbn;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public Double getPreciobase()
    {
        return preciobase;
    }

    public void setPreciobase(Double preciobase)
    {
        this.preciobase = preciobase;
    }

    public BigInteger getUnidadesdisponibles()
    {
        return unidadesdisponibles;
    }

    public void setUnidadesdisponibles(BigInteger unidadesdisponibles)
    {
        this.unidadesdisponibles = unidadesdisponibles;
    }

    public BigInteger getNumeroimagenes()
    {
        return numeroimagenes;
    }

    public void setNumeroimagenes(BigInteger numeroimagenes)
    {
        this.numeroimagenes = numeroimagenes;
    }

    public BigInteger getNumerovideos()
    {
        return numerovideos;
    }

    public void setNumerovideos(BigInteger numerovideos)
    {
        this.numerovideos = numerovideos;
    }

    public List<Linea> getLineaList()
    {
        return lineaList;
    }

    public void setLineaList(List<Linea> lineaList)
    {
        this.lineaList = lineaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Libro))
        {
            return false;
        }
        Libro other = (Libro) object;
        if((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Libro{" + "isbn=" + isbn+ "}";
    }


    
}
