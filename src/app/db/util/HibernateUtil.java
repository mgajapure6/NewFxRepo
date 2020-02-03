package app.db.util;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory SESSIONFACTORY;
	private static final ThreadLocal<Session> THREADLOCAL;
	private static StandardServiceRegistry serviceRegistry;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("META-INF/hibernate.cfg.xml");
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			SESSIONFACTORY = configuration.buildSessionFactory(serviceRegistry);
			THREADLOCAL = new ThreadLocal<Session>();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return SESSIONFACTORY;
	}

	public static Session getSession() {
		Session session = THREADLOCAL.get();
		if (session == null) {
			session = SESSIONFACTORY.openSession();
			THREADLOCAL.set(session);
		}
		return session;
	}

	public static void closeSession() {
		Session session = THREADLOCAL.get();
		if (session != null) {
			session.close();
			THREADLOCAL.set(null);
		}
	}

	public static void closeSessionFactory() {
		SESSIONFACTORY.close();
		StandardServiceRegistryBuilder.destroy(serviceRegistry);
	}

}
