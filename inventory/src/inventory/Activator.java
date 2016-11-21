package inventory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static EntityManager em;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		try {
			BundleContext context = Activator.getContext();
			ServiceReference<PersistenceProvider> serviceReference = context.getServiceReference(PersistenceProvider.class);
			PersistenceProvider persistenceProvider = context.getService(serviceReference);
			EntityManagerFactory factory = persistenceProvider.createEntityManagerFactory("inventoryEmbedded", null);
			em = factory.createEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		if(em != null)
			em.close();
		Activator.context = null;
		
	}

	public static EntityManager getEntityManager() {
		return em;
	}
}
