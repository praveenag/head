package org.mifos.application.accounts.savings.business;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;

import org.mifos.application.accounts.business.AccountActionDateEntity;
import org.mifos.application.accounts.business.AccountActionEntity;
import org.mifos.application.accounts.business.AccountFeesActionDetailEntity;
import org.mifos.application.accounts.business.AccountPaymentEntity;
import org.mifos.application.accounts.business.AccountTrxnEntity;
import org.mifos.application.accounts.business.FeesTrxnDetailEntity;
import org.mifos.application.accounts.persistence.service.AccountPersistanceService;
import org.mifos.application.accounts.savings.util.helpers.SavingsHelper;
import org.mifos.application.accounts.util.helpers.AccountConstants;
import org.mifos.application.accounts.util.helpers.LoanPaymentData;
import org.mifos.application.accounts.util.helpers.SavingsPaymentData;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.master.persistence.service.MasterPersistenceService;
import org.mifos.application.personnel.business.PersonnelBO;
import org.mifos.framework.business.service.ServiceFactory;
import org.mifos.framework.exceptions.ApplicationException;
import org.mifos.framework.exceptions.ServiceException;
import org.mifos.framework.exceptions.SystemException;
import org.mifos.framework.util.helpers.Money;
import org.mifos.framework.util.helpers.PersistenceServiceName;

public class SavingsTrxnDetailEntity extends AccountTrxnEntity {
	
	private final Money depositAmount;

	private final Money withdrawlAmount;

	private final Money interestAmount;

	private final Money balance;
	
	protected SavingsTrxnDetailEntity(){
		depositAmount=null;
		withdrawlAmount=null;
		interestAmount=null;
		balance=null;
	}
	
	public SavingsTrxnDetailEntity(Money depositAmount,Money withdrawlAmount,Money interestAmount,Money balance){
		this.depositAmount=depositAmount;
		this.withdrawlAmount=withdrawlAmount;
		this.interestAmount=interestAmount;
		this.balance=balance;
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
			PersonnelBO personnel,
			java.util.Date transactionDate,
			AccountActionEntity accountActionEntity, Money depositAmount ,
			Short installmentId, java.util.Date actionDate) {
		super(accountPaymentEntity, accountActionEntity, installmentId, 
				 actionDate,personnel, transactionDate, depositAmount, "");
		this.balance = ((SavingsBO) getAccount()).getSavingsBalance();
		this.depositAmount = depositAmount;
		this.withdrawlAmount=new Money();
		this.interestAmount=new Money();
	}
	
	public SavingsTrxnDetailEntity(AccountPaymentEntity accountPaymentEntity,
			PersonnelBO personnel,
			java.util.Date transactionDate,
			AccountActionEntity accountActionEntity, Money withdrawAmount) {
		super(accountPaymentEntity, accountActionEntity, null, 
				transactionDate,personnel, transactionDate, withdrawAmount, "");
		this.balance = ((SavingsBO) getAccount()).getSavingsBalance();
		this.depositAmount = new Money();
		this.withdrawlAmount=withdrawAmount;
		this.interestAmount=new Money();
	
	}
	
	public SavingsTrxnDetailEntity(AccountPaymentEntity accountPaymentEntity,CustomerBO customer,
			AccountActionEntity accountActionEntity, Money amount, Money balance,
			PersonnelBO createdBy,java.util.Date dueDate,
			java.util.Date transactionDate,Short installmentId,String comment) throws SystemException {
		super(accountPaymentEntity,customer,accountActionEntity , installmentId, dueDate,
				createdBy, transactionDate, amount,comment);
		this.balance = balance;
		if (accountActionEntity.getId().equals(AccountConstants.ACTION_SAVINGS_WITHDRAWAL)){
			this.depositAmount = new Money();
			this.withdrawlAmount=amount;
			this.interestAmount=new Money();
		}
		else if (accountActionEntity.getId().equals(AccountConstants.ACTION_SAVINGS_DEPOSIT)){
			this.depositAmount = amount;
			this.withdrawlAmount=new Money();
			this.interestAmount=new Money();
		}
		else if (accountActionEntity.getId()
				.equals(AccountConstants.ACTION_SAVINGS_INTEREST_POSTING)){
			this.depositAmount = new Money();
			this.withdrawlAmount=new Money();
			this.interestAmount=amount;
		}else{
			this.depositAmount = new Money();
			this.withdrawlAmount=new Money();
			this.interestAmount=new Money();
		}
	}
	
	
	public SavingsTrxnDetailEntity(AccountPaymentEntity accountPaymentEntity,
			AccountActionEntity accountActionEntity, Money amount, Money balance,
			PersonnelBO createdBy,java.util.Date dueDate,
			java.util.Date transactionDate,String comments,AccountTrxnEntity relatedTrxn) {
		super(accountPaymentEntity,accountActionEntity , null, dueDate,
				createdBy, transactionDate, amount,comments,relatedTrxn);
		this.balance = balance;
		Short lastAccountAction=new SavingsHelper().getPaymentActionType(accountPaymentEntity);
		if (lastAccountAction.equals(AccountConstants.ACTION_SAVINGS_WITHDRAWAL)){
			this.depositAmount = new Money();
			this.withdrawlAmount=amount;
			this.interestAmount=new Money();
		}
		else if (lastAccountAction.equals(AccountConstants.ACTION_SAVINGS_DEPOSIT)){
			this.depositAmount = amount;
			this.withdrawlAmount=new Money();
			this.interestAmount=new Money();
		}
		else if (lastAccountAction.equals(AccountConstants.ACTION_SAVINGS_INTEREST_POSTING)){
			this.depositAmount = new Money();
			this.withdrawlAmount=new Money();
			this.interestAmount=amount;
		}else{
			this.depositAmount = new Money();
			this.withdrawlAmount=new Money();
			this.interestAmount=new Money();
		}
	}
		

	private AccountPersistanceService getAccountDBService()
			throws ServiceException {
		return (AccountPersistanceService) ServiceFactory.getInstance()
				.getPersistenceService(PersistenceServiceName.Account);
	}

	
	public AccountTrxnEntity generateReverseTrxn(String adjustmentComment)
			throws ApplicationException, SystemException {
		MasterPersistenceService masterPersistenceService = (MasterPersistenceService) ServiceFactory
		.getInstance().getPersistenceService(
				PersistenceServiceName.MasterDataService);
		SavingsTrxnDetailEntity reverseAccntTrxn = null;
		Money balAfterAdjust = null;
		if (getAccountActionEntity().getId().equals(
				AccountConstants.ACTION_SAVINGS_DEPOSIT)) {
			balAfterAdjust = getBalance().subtract(getDepositAmount());
			reverseAccntTrxn=new SavingsTrxnDetailEntity(
					getAccountPayment(),(AccountActionEntity) masterPersistenceService
					.findById(AccountActionEntity.class,
							AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
					getDepositAmount().negate(),
					balAfterAdjust, getPersonnel(),getDueDate(), getActionDate(),adjustmentComment,this);
		} else if (getAccountActionEntity().getId().equals(
				AccountConstants.ACTION_SAVINGS_WITHDRAWAL)) {
			balAfterAdjust = getBalance().add(getWithdrawlAmount());
			reverseAccntTrxn=new SavingsTrxnDetailEntity(
					getAccountPayment(),(AccountActionEntity) masterPersistenceService
					.findById(AccountActionEntity.class,
							AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
							getWithdrawlAmount().negate(),
					balAfterAdjust, getPersonnel(),getDueDate(), getActionDate(),adjustmentComment,this);
			
		}else{
			reverseAccntTrxn=new SavingsTrxnDetailEntity(
					getAccountPayment(),(AccountActionEntity) masterPersistenceService
					.findById(AccountActionEntity.class,
							AccountConstants.ACTION_SAVINGS_ADJUSTMENT),
							getAmount().negate(),
					balAfterAdjust, getPersonnel(),getDueDate(), getActionDate(),adjustmentComment,this);
		}
		return reverseAccntTrxn;
	}
}
