
package app.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import app.db.domain.User;
import app.db.dto.ProductDto;

public abstract class BasicDAO<T> {
	private Class<T> entityClass;

	public BasicDAO(Class<T> eClass) {
		this.entityClass = eClass;
	}

	public abstract EntityManager getEntityManager();

	public boolean create(T entity) throws Exception {
		EntityManager em = getEntityManager();
		try {

			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public boolean update(T entity) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public boolean delete(T entity, int entityId) throws Exception {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.remove((T) em.find(this.entityClass, entityId));
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public T find(int id) throws Exception {
		EntityManager em = getEntityManager();
		T ret = null;
		try {
			ret = (T) em.find(this.entityClass, id);
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public User findUserByUsernameAndPassword(String username, String pssword) throws Exception {
//		EntityManager em = getEntityManager();
//		User user = null;
//		try {
//			Metamodel m = em.getMetamodel();
//			EntityType<User> User_ = m.entity(User.class);
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery cq = cb.createQuery();
//			Root<T> root = cq.from(User_);
//			List<Predicate> predicates = new ArrayList<Predicate>();
//			predicates.add(cb.equal(root.get("userName"), username));
//			predicates.add(cb.equal(root.get("userPassword"), pssword));
//			cq.select(root).where(predicates.toArray(new Predicate[] {}));
//			user = (User) em.createQuery(cq).getSingleResult();
//			System.out.println("retretuser::" + user);
//		} catch (Exception e) {
//			// em.getTransaction().rollback();
//			e.printStackTrace();
//			return null;
//		} finally {
//			em.close();
//		}
//		return user;
		return new User();
	}

	public List<T> findAll() throws Exception {
		EntityManager em = getEntityManager();
		List<T> returnValues = null;
		try {
			CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			cq.select(cq.from(this.entityClass));
			returnValues = em.createQuery(cq).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return returnValues;
	}

	public List executeQuery(String query) throws Exception {
		EntityManager em = getEntityManager();
		List list = null;
		try {
			Query q = em.createNativeQuery(query);
			list = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return list;
	}

	public List<T> executeQuery(String query, T entity) throws Exception {
		EntityManager em = getEntityManager();
		List<T> list = new ArrayList<T>();
		try {
			Query q = em.createNativeQuery(query);
			list = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return list;
	}
	
	public List<?> executeQuery(String query, Class<?> resultClass) throws Exception {
		EntityManager em = getEntityManager();
		List<?> list = new ArrayList<>();
		try {
			Query q = em.createNativeQuery(query, resultClass.getClass());
			//JavaBeanResult.setQueryResultClass(q, resultClass.getClass()); 
			list = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return list;
	}
	
	public List<T> executeQuery(String query, String mappingName) {
		EntityManager em = getEntityManager();
		List<T> list = new ArrayList<>();
		try {
			Query q = em.createNativeQuery(query, mappingName);
			list = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
		return list;
	}
}
