package entidades;

import entidades.Denominacion;
import entidades.Prestamo;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-06-03T18:55:33")
@StaticMetamodel(Moneda.class)
public class Moneda_ { 

    public static volatile SingularAttribute<Moneda, BigDecimal> id;
    public static volatile SingularAttribute<Moneda, Denominacion> denominacionmoneda;
    public static volatile SingularAttribute<Moneda, Prestamo> prestamonumero;

}