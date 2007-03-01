package org.mifos.application.accounts.business;

import java.util.Set;

import net.sourceforge.mayfly.Database;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mifos.application.accounts.savings.struts.action.SavingsDepositWithdrawalAction;
import org.mifos.application.master.business.LookUpValueEntity;
import org.mifos.application.master.business.LookUpValueLocaleEntity;
import org.mifos.application.master.business.MifosLookUpEntity;
import org.mifos.application.util.helpers.EntityType;
import org.mifos.framework.util.helpers.DatabaseSetup;

import junit.framework.TestCase;

/**
 * Also see {@link TestAccountActionEntity}.
 */
public class AccountActionEntityTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		DatabaseSetup.configureLogging();
		DatabaseSetup.initializeHibernate();
	}

	public void testBasics() throws Exception {
		SessionFactory factory = DatabaseSetup.mayflySessionFactory();
		Database database = new Database(DatabaseSetup.getStandardStore());
		
		Session session = factory.openSession(database.openConnection());
		AccountActionEntity action = (AccountActionEntity) 
			session.get(AccountActionEntity.class, (short)8);

		LookUpValueEntity lookUpValue = action.getLookUpValue();
		assertEquals(" ", lookUpValue.getLookUpName());
		assertEquals(new Integer(191), lookUpValue.getLookUpId());

		MifosLookUpEntity lookUpEntity = lookUpValue.getLookUpEntity();
		assertEquals(EntityType.ACCOUNT_ACTION.getValue(), 
			lookUpEntity.getEntityId());
		assertEquals("AccountAction", lookUpEntity.getEntityType());

		Set<LookUpValueLocaleEntity> valueLocales = 
			lookUpValue.getLookUpValueLocales();
		assertEquals(1, valueLocales.size());
		LookUpValueLocaleEntity valueLocale = valueLocales.iterator().next();
		assertEquals(1, (int)valueLocale.getLocaleId());
		assertEquals("Payment", valueLocale.getLookUpValue());

		assertEquals("Payment", action.getName((short)1));
		session.close();
	}

	/**
	 * Test based on {@link SavingsDepositWithdrawalAction#load} - simulates
	 * what happens when we read
	 * an AccountActionEntity, set its locale, put it into the HTTP 
	 * session, and then return to jsp-land.  */
	public void xtestNotLazy() throws Exception {
		SessionFactory factory = DatabaseSetup.mayflySessionFactory();
		Database database = new Database(DatabaseSetup.getStandardStore());
		
		Session session = factory.openSession(database.openConnection());
		AccountActionEntity action = (AccountActionEntity) 
			session.get(AccountActionEntity.class, (short)8);
		Short sampleLocale = 1;
		action.setLocaleId(sampleLocale);
		session.close();

		assertEquals("Payment", action.getName());
	}

}
