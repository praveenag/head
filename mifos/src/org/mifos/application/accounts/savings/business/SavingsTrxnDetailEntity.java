package org.mifos.application.accounts.savings.business;

import org.mifos.application.accounts.business.AccountActionEntity;
import org.mifos.application.accounts.business.AccountPaymentEntity;
import org.mifos.application.accounts.business.AccountTrxnEntity;
import org.mifos.application.accounts.exceptions.AccountException;
import org.mifos.application.accounts.savings.util.helpers.SavingsHelper;
import org.mifos.application.accounts.util.helpers.AccountConstants;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.master.persistence.MasterPersistence;
import org.mifos.application.personnel.business.PersonnelBO;
import org.mifos.framework.exceptions.PersistenceException;
import org.mifos.framework.util.helpers.Money;

public class SavingsTrxnDetailEntity extends AccountTrxnEntity {

	private final Money depositAmount;

	private final Money withdrawlAmount;

	private final Money interestAmount;

	private final Money balance;

	protected SavingsTrxnDetailEntity() {
		depositAmount = null;
		withdrawlAmount = null;
		interestAmount = null;
		balance = null;
	}

	public SavingsTrxnDetailEntity(Money depositAmount, Money withdrawlAmount,
			Money interestAmount, Money balance) {
		this.depositAmount = depositAmount;
		this.withdrawlAmount = withdrawlAmount;
		this.interestAmount = interestAmount;
		this.balance = balance;
	}

	public Money getDepositAmount() {
		return depositAmount;
	}

	public Money getWithdrawlAmount() {
		return withdrawlAmount;
	}

	public Money getBalance() {
		return balance;
	}

	public Money getInterestAmount() {
		return interestAmount;
	}

	public SavingsTrxnDetailEntity(AccountPaymentEntity accountPaymentEntity,
			CustomerBO customer, AccountActionEntity accountActionEntity,
			Money amount, Money balance, PersonnelBO createdBy,
			java.util.Date dueDate, java.util.Date transactionDate,
			Short installmentId, String comment) {
		super(accountPaymentEntity, accountActionEntity, installmentId,
				dueDate, createdBy, customer, transactionDate, amount, comment,
				null);
		this.balance = balance;
		if (accountActionEntity.getId().equals(
				AccountConstants.ACTION_SAVINGS_WITHDRAWAL)) {
			this.depositAmount = new Money();
			this.withdrawlAmount = amount;
			this.interestAmount = new Money();
		} else if (accountActionEntity.getId().equals(
				AccountConstants.ACTION_SAVINGS_DEPOSIT)) {
			this.depositAmount = amount;
			this.withdrawlAmount = new Money();
			this.interestAmount = new Money();
		} else if (accountActionEntity.getId().equals(
				AccountConstants.ACTION_SAVINGS_INTEREST_POSTING)) {
			this.depositAmount = new Money();
			this.withdrawlAmount = new Money();
			this.interestAmount = amount;
		} else {
			this.depositAmount = new Money();
			this.withdrawlAmount = new Money();
			this.interestAmount = new Money();
		}
	}

	public SavingsTrxnDetailEntity(AccountPaymentEntity accountPaymentEntity,
			AccountActionEntity accountActionEntity, Money amount,
			Money balance, PersonnelBO createdBy, CustomerBO customer,
			java.util.Date dueDate, java.util.Date transactionDate,
			String comments, AccountTrxnEntity relatedTrxn) {
		super(accountPaymentEntity, accountActionEntity, null, dueDate,
				createdBy, customer, transactionDate, amount, comments,
				relatedTrxn);
		this.balance = balance;
		Short lastAccountAction = new SavingsHelper()
				.getPaymentActionType(accountPaymentEntity);
		if (lastAccountAction
				.equals(AccountConstants.ACTION_SAVINGS_WITHDRAWAL)) {
			this.depositAmount = new Money();
			this.withdrawlAmount = amount;
			this.interestAmount = new Money();
		} else if (lastAccountAction
				.equals(AccountConstants.ACTION_SAVINGS_DEPOSIT)) {
			this.depositAmount = amount;
			this.withdrawlAmount = new Money();
			this.interestAmount = new Money();
		} else if (lastAccountAction
				.equals(AccountConstants.ACTION_SAVINGS_INTEREST_POSTING)) {
			this.depositAmount = new Money();
			this.withdrawlAmount = new Money();
			this.interestAmount = amount;
		} else {
			this.depositAmount = new Money();
			this.withdrawlAmount = new Money();
			this.interestAmount = new Money();
		}
	}

	@Override
	public AccountTrxnEntity generateReverseTrxn(String adjustmentComment)
			throws AccountException {
		try {
			MasterPersistence masterPersistence = new MasterPersistence();
			SavingsTrxnDetailEntity reverseAccntTrxn = null;
			Money balAfterAdjust = null;
			if (getAccountActionEntity().getId().equals(
					AccountConstants.ACTION_SAVINGS_DEPOSIT)) {
				balAfterAdjust = getBalance().subtract(getDepositAmount());
				reverseAccntTrxn = new SavingsTrxnDetailEntity(
						getAccountPayment(),
						(AccountActionEntity) masterPersistence
								.getPersistentObject(
										AccountActionEntity.class,
										AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
						getDepositAmount().negate(), balAfterAdjust,
						getPersonnel(), getCustomer(), getDueDate(),
						getActionDate(), adjustmentComment, this);
			} else if (getAccountActionEntity().getId().equals(
					AccountConstants.ACTION_SAVINGS_WITHDRAWAL)) {
				balAfterAdjust = getBalance().add(getWithdrawlAmount());
				reverseAccntTrxn = new SavingsTrxnDetailEntity(
						getAccountPayment(),
						(AccountActionEntity) masterPersistence
								.getPersistentObject(
										AccountActionEntity.class,
										AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
						getWithdrawlAmount().negate(), balAfterAdjust,
						getPersonnel(), getCustomer(), getDueDate(),
						getActionDate(), adjustmentComment, this);

			} else {
				reverseAccntTrxn = new SavingsTrxnDetailEntity(
						getAccountPayment(),
						(AccountActionEntity) masterPersistence
								.getPersistentObject(
										AccountActionEntity.class,
										AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
						getAmount().negate(), balAfterAdjust, getPersonnel(),
						getCustomer(), getDueDate(), getActionDate(),
						adjustmentComment, this);
			}
			return reverseAccntTrxn;
		} catch (PersistenceException e) {
			throw new AccountException(e);
		}
	}
}
