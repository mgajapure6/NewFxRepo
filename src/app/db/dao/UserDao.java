package app.db.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import app.db.domain.Customer;
import app.db.domain.Provider;
import app.db.domain.User;
import app.db.util.HibernateUtil;

public class UserDao {

	public Boolean saveUser(User user, Customer customer, Provider provider) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		if (customer != null) {
			session.save(customer);
			user.setCustomer(customer);
			user.setProvider(null);
		}

		if (provider != null) {
			session.save(provider);
			user.setCustomer(null);
			user.setProvider(provider);
		}

		session.saveOrUpdate(user);
		session.getTransaction().commit();

		return true;

	}

	public User getByUsername(String username) {
		Criteria criteria = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("userName", username));
		User user = (User) criteria.uniqueResult();
		return user;
	}

	public User getByUsernameAndPassword(String username, String password) {
		Criteria criteria = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("userName", username));
		criteria.add(Restrictions.eq("userPassword", password));
		User user = (User) criteria.uniqueResult();
		return user;
	}

}
