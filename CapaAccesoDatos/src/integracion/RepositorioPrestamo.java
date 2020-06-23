/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integracion;

import entidades.Libro;
import entidades.Linea;
import entidades.LineaPK;
import entidades.Moneda;
import entidades.Prestamo;
import integracion.exceptions.NonexistentEntityException;
import integracion.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class RepositorioPrestamo implements Serializable, IGestionPrestamo
{

    private EntityManagerFactory emf = null;

    public RepositorioPrestamo(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    //METODOS IMPLEMENTADOS
    @Override
    public boolean crearPrestamo(Prestamo prestamo)
    {
        try
        {
            create(prestamo);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean crearLinea(Linea linea)
    {
        try
        {
            create(linea);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public Prestamo buscarPrestamo(int numeroPrestamo)
    {
        try
        {
            EntityManager em = emf.createEntityManager();
            Query result = em.createNativeQuery("SELECT * FROM Prestamo WHERE numero = '"+numeroPrestamo+"'");
            result.setParameter("numeroP", numeroPrestamo);
            System.out.println("NumeroPrestamos: " + result.getSingleResult());
            return (Prestamo) result.getSingleResult();
        }
        catch(Exception e)
        {
            return null;
        }

    }

    @Override
    public BigInteger ultimoNumeroPrestamo()
    {
        EntityManager em = emf.createEntityManager();
        Query result = em.createQuery("SELECT max(p.numero) FROM Prestamo p");
        System.out.println("ultimoNumeroPrestamos: " + result.getSingleResult());
        return (BigInteger) result.getSingleResult();
    }

    @Override
    public BigDecimal ultimoIdLinea()
    {
        EntityManager em = emf.createEntityManager();
        Query result = em.createNativeQuery("SELECT max(l.id) FROM Linea l");
        System.out.println("ultimoNumeroLinea: " + result.getSingleResult());
        return (BigDecimal) result.getSingleResult();
    }

    @Override
    public BigDecimal ultimoIdMoneda()
    {
        EntityManager em = emf.createEntityManager();
        Query result = em.createNativeQuery("SELECT max(m.id) FROM Moneda m");
        System.out.println("ultimoNumeroMoneda: " + result.getSingleResult());
        return (BigDecimal) result.getSingleResult();
    }

    //METODOS AUTOGENERADOS PARA LIBRO
    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Prestamo prestamo) throws PreexistingEntityException, Exception
    {
        if(prestamo.getMonedaList() == null)
        {
            prestamo.setMonedaList(new ArrayList<Moneda>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Moneda> attachedMonedaList = new ArrayList<Moneda>();
            for(Moneda monedaListMonedaToAttach : prestamo.getMonedaList())
            {
                monedaListMonedaToAttach = em.getReference(monedaListMonedaToAttach.getClass(), monedaListMonedaToAttach.getId());
                attachedMonedaList.add(monedaListMonedaToAttach);
            }
            prestamo.setMonedaList(attachedMonedaList);
            em.persist(prestamo);
            for(Moneda monedaListMoneda : prestamo.getMonedaList())
            {
                Prestamo oldPrestamonumeroOfMonedaListMoneda = monedaListMoneda.getPrestamonumero();
                monedaListMoneda.setPrestamonumero(prestamo);
                monedaListMoneda = em.merge(monedaListMoneda);
                if(oldPrestamonumeroOfMonedaListMoneda != null)
                {
                    oldPrestamonumeroOfMonedaListMoneda.getMonedaList().remove(monedaListMoneda);
                    oldPrestamonumeroOfMonedaListMoneda = em.merge(oldPrestamonumeroOfMonedaListMoneda);
                }
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            if(findPrestamo(prestamo.getNumero()) != null)
            {
                throw new PreexistingEntityException("Prestamo " + prestamo + " already exists.", ex);
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

    public void edit(Prestamo prestamo) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo persistentPrestamo = em.find(Prestamo.class, prestamo.getNumero());
            List<Moneda> monedaListOld = persistentPrestamo.getMonedaList();
            List<Moneda> monedaListNew = prestamo.getMonedaList();
            List<Moneda> attachedMonedaListNew = new ArrayList<Moneda>();
            for(Moneda monedaListNewMonedaToAttach : monedaListNew)
            {
                monedaListNewMonedaToAttach = em.getReference(monedaListNewMonedaToAttach.getClass(), monedaListNewMonedaToAttach.getId());
                attachedMonedaListNew.add(monedaListNewMonedaToAttach);
            }
            monedaListNew = attachedMonedaListNew;
            prestamo.setMonedaList(monedaListNew);
            prestamo = em.merge(prestamo);
            for(Moneda monedaListOldMoneda : monedaListOld)
            {
                if(!monedaListNew.contains(monedaListOldMoneda))
                {
                    monedaListOldMoneda.setPrestamonumero(null);
                    monedaListOldMoneda = em.merge(monedaListOldMoneda);
                }
            }
            for(Moneda monedaListNewMoneda : monedaListNew)
            {
                if(!monedaListOld.contains(monedaListNewMoneda))
                {
                    Prestamo oldPrestamonumeroOfMonedaListNewMoneda = monedaListNewMoneda.getPrestamonumero();
                    monedaListNewMoneda.setPrestamonumero(prestamo);
                    monedaListNewMoneda = em.merge(monedaListNewMoneda);
                    if(oldPrestamonumeroOfMonedaListNewMoneda != null && !oldPrestamonumeroOfMonedaListNewMoneda.equals(prestamo))
                    {
                        oldPrestamonumeroOfMonedaListNewMoneda.getMonedaList().remove(monedaListNewMoneda);
                        oldPrestamonumeroOfMonedaListNewMoneda = em.merge(oldPrestamonumeroOfMonedaListNewMoneda);
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
                BigInteger id = prestamo.getNumero();
                if(findPrestamo(id) == null)
                {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
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

    public void destroy(BigDecimal id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo prestamo;
            try
            {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getNumero();
            }
            catch(EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            List<Moneda> monedaList = prestamo.getMonedaList();
            for(Moneda monedaListMoneda : monedaList)
            {
                monedaListMoneda.setPrestamonumero(null);
                monedaListMoneda = em.merge(monedaListMoneda);
            }
            em.remove(prestamo);
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

    public List<Prestamo> findPrestamoEntities()
    {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult)
    {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
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

    public Prestamo findPrestamo(BigInteger id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Prestamo.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getPrestamoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        }
        finally
        {
            em.close();
        }
    }

    //METODOS AUTOGENERADOS PARA LINEA
    public void create(Linea linea) throws PreexistingEntityException, Exception
    {
        if(linea.getLineaPK() == null)
        {
            linea.setLineaPK(new LineaPK());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libroisbn = linea.getLibroisbn();
            if(libroisbn != null)
            {
                libroisbn = em.getReference(libroisbn.getClass(), libroisbn.getIsbn());
                linea.setLibroisbn(libroisbn);
            }
            em.persist(linea);
            if(libroisbn != null)
            {
                libroisbn.getLineaList().add(linea);
                libroisbn = em.merge(libroisbn);
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            if(findLinea(linea.getLineaPK()) != null)
            {
                throw new PreexistingEntityException("Linea " + linea + " already exists.", ex);
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

    public void edit(Linea linea) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Linea persistentLinea = em.find(Linea.class, linea.getLineaPK());
            Libro libroisbnOld = persistentLinea.getLibroisbn();
            Libro libroisbnNew = linea.getLibroisbn();
            if(libroisbnNew != null)
            {
                libroisbnNew = em.getReference(libroisbnNew.getClass(), libroisbnNew.getIsbn());
                linea.setLibroisbn(libroisbnNew);
            }
            linea = em.merge(linea);
            if(libroisbnOld != null && !libroisbnOld.equals(libroisbnNew))
            {
                libroisbnOld.getLineaList().remove(linea);
                libroisbnOld = em.merge(libroisbnOld);
            }
            if(libroisbnNew != null && !libroisbnNew.equals(libroisbnOld))
            {
                libroisbnNew.getLineaList().add(linea);
                libroisbnNew = em.merge(libroisbnNew);
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if(msg == null || msg.length() == 0)
            {
                LineaPK id = linea.getLineaPK();
                if(findLinea(id) == null)
                {
                    throw new NonexistentEntityException("The linea with id " + id + " no longer exists.");
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

    public void destroy(LineaPK id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Linea linea;
            try
            {
                linea = em.getReference(Linea.class, id);
                linea.getLineaPK();
            }
            catch(EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The linea with id " + id + " no longer exists.", enfe);
            }
            Libro libroisbn = linea.getLibroisbn();
            if(libroisbn != null)
            {
                libroisbn.getLineaList().remove(linea);
                libroisbn = em.merge(libroisbn);
            }
            em.remove(linea);
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

    public List<Linea> findLineaEntities()
    {
        return findLineaEntities(true, -1, -1);
    }

    public List<Linea> findLineaEntities(int maxResults, int firstResult)
    {
        return findLineaEntities(false, maxResults, firstResult);
    }

    private List<Linea> findLineaEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Linea.class));
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

    public Linea findLinea(LineaPK id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Linea.class, id);
        }
        finally
        {
            em.close();
        }
    }

    public int getLineaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Linea> rt = cq.from(Linea.class);
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
