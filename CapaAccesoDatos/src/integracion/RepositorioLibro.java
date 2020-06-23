package integracion;

import entidades.Libro;
import entidades.Linea;
import integracion.exceptions.NonexistentEntityException;
import integracion.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author LomitoFrito
 */
public class RepositorioLibro implements Serializable, IGestionLibro
{

    private EntityManagerFactory emf = null;

    public RepositorioLibro(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    //METODOS IMPLEMENTADOS
    @Override
    public List<Libro> cargarLibros()
    {
        return findLibroEntities();
    }

    @Override
    public boolean crearLibro(Libro libro)
    {
        try
        {
            create(libro);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }


    //METODOS AUTOGENERADOS    
    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Libro libro) throws PreexistingEntityException, Exception
    {
        if(libro.getLineaList() == null)
        {
            libro.setLineaList(new ArrayList<Linea>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Linea> attachedLineaList = new ArrayList<Linea>();
            for(Linea lineaListLineaToAttach : libro.getLineaList())
            {
                lineaListLineaToAttach = em.getReference(lineaListLineaToAttach.getClass(), lineaListLineaToAttach.getLineaPK());
                attachedLineaList.add(lineaListLineaToAttach);
            }
            libro.setLineaList(attachedLineaList);
            em.persist(libro);
            for(Linea lineaListLinea : libro.getLineaList())
            {
                Libro oldLibroisbnOfLineaListLinea = lineaListLinea.getLibroisbn();
                lineaListLinea.setLibroisbn(libro);
                lineaListLinea = em.merge(lineaListLinea);
                if(oldLibroisbnOfLineaListLinea != null)
                {
                    oldLibroisbnOfLineaListLinea.getLineaList().remove(lineaListLinea);
                    oldLibroisbnOfLineaListLinea = em.merge(oldLibroisbnOfLineaListLinea);
                }
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            if(findLibro(libro.getIsbn()) != null)
            {
                throw new PreexistingEntityException("Libro " + libro + " already exists.", ex);
            }
            throw ex;
        }
        finally
        {
            if(em != null)
            {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getIsbn());
            List<Linea> lineaListOld = persistentLibro.getLineaList();
            List<Linea> lineaListNew = libro.getLineaList();
            List<Linea> attachedLineaListNew = new ArrayList<Linea>();
            for(Linea lineaListNewLineaToAttach : lineaListNew)
            {
                lineaListNewLineaToAttach = em.getReference(lineaListNewLineaToAttach.getClass(), lineaListNewLineaToAttach.getLineaPK());
                attachedLineaListNew.add(lineaListNewLineaToAttach);
            }
            lineaListNew = attachedLineaListNew;
            libro.setLineaList(lineaListNew);
            libro = em.merge(libro);
            for(Linea lineaListOldLinea : lineaListOld)
            {
                if(!lineaListNew.contains(lineaListOldLinea))
                {
                    lineaListOldLinea.setLibroisbn(null);
                    lineaListOldLinea = em.merge(lineaListOldLinea);
                }
            }
            for(Linea lineaListNewLinea : lineaListNew)
            {
                if(!lineaListOld.contains(lineaListNewLinea))
                {
                    Libro oldLibroisbnOfLineaListNewLinea = lineaListNewLinea.getLibroisbn();
                    lineaListNewLinea.setLibroisbn(libro);
                    lineaListNewLinea = em.merge(lineaListNewLinea);
                    if(oldLibroisbnOfLineaListNewLinea != null && !oldLibroisbnOfLineaListNewLinea.equals(libro))
                    {
                        oldLibroisbnOfLineaListNewLinea.getLineaList().remove(lineaListNewLinea);
                        oldLibroisbnOfLineaListNewLinea = em.merge(oldLibroisbnOfLineaListNewLinea);
                    }
                }
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if(msg == null || msg.length() == 0)
            {
                String id = libro.getIsbn();
                if(findLibro(id) == null)
                {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
        finally
        {
            if(em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro;
            try
            {
                libro = em.getReference(Libro.class, id);
                libro.getIsbn();
            }
            catch(EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<Linea> lineaList = libro.getLineaList();
            for(Linea lineaListLinea : lineaList)
            {
                lineaListLinea.setLibroisbn(null);
                lineaListLinea = em.merge(lineaListLinea);
            }
            em.remove(libro);
            em.getTransaction().commit();
        }
        finally
        {
            if(em != null)
            {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities()
    {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult)
    {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
            Query q = em.createQuery(cq);
            if(!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    public Libro findLibro(String id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Libro.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getLibroCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }

}
