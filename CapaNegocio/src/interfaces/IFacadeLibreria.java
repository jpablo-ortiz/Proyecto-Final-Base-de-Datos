package interfaces;

import entidades.Denominacion;
import entidades.DtoResumen;
import entidades.Libro;
import entidades.Linea;
import entidades.Prestamo;
import java.util.List;

public interface IFacadeLibreria
{
    public List<Libro> crearCatalogo();
    
    public boolean crearNuevoPrestamo();
    
    public DtoResumen agregarLinea(Libro libro, int cantidad);
    
    public List<Libro> getCatalogo();

    public String getMensajePrestamo();
    
    public List<Denominacion> getDenominaciones(); 
    
    public Prestamo getPrestamoActual();
    
    public DtoResumen eliminarLinea(Linea linea);
    
    public DtoResumen introducirMoneda(Denominacion denominacion, int cantiMonedas);
    
    public DtoResumen terminarPrestamo();
    
    public DtoResumen consultarPrestamo(int numeroPrestamo);
}
