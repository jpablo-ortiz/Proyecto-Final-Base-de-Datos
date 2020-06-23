package integracion;

import entidades.Libro;
import java.util.List;

public interface IGestionLibro
{
    //METODOS A IMPLEMENTAR
    public List<Libro> cargarLibros();
    
    public boolean crearLibro(Libro libro);
    

}
