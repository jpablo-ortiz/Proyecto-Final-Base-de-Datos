package entidades;

import entidades.Linea;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-06-03T18:55:33")
@StaticMetamodel(Libro.class)
public class Libro_ { 

    public static volatile SingularAttribute<Libro, BigInteger> unidadesdisponibles;
    public static volatile SingularAttribute<Libro, Double> preciobase;
    public static volatile SingularAttribute<Libro, String> isbn;
    public static volatile ListAttribute<Libro, Linea> lineaList;
    public static volatile SingularAttribute<Libro, BigInteger> numeroimagenes;
    public static volatile SingularAttribute<Libro, BigInteger> numerovideos;

}