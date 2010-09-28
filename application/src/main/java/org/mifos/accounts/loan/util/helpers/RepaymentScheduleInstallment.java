/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.accounts.loan.util.helpers;

import org.mifos.application.master.business.MifosCurrency;
import org.mifos.framework.util.helpers.DateUtils;
import org.mifos.framework.util.helpers.Money;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class RepaymentScheduleInstallment implements Serializable {

    private Integer installment;

    private java.util.Date dueDateValue;

    private Money principal;

    private Money interest;

    private Money fees;

    private Money miscFees;

    private Money miscPenalty;

    private Locale locale;

    private Money totalValue;

    private String total;

    private String formatStr;

    public RepaymentScheduleInstallment(int installment, Date dueDateValue, Money principal, Money interest, Money fees,
                                        Money miscFees, Money miscPenalty, Locale locale) {
        this.installment = installment;
        this.dueDateValue = dueDateValue;
        this.principal = principal;
        this.interest = interest;
        this.fees = fees;
        this.miscFees = miscFees;
        this.miscPenalty = miscPenalty;
        this.totalValue = principal.add(interest).add(fees).add(miscFees).add(miscPenalty);
        this.total = this.totalValue.toString();
        this.locale = locale;
        String dateSeparator = DateUtils.getDateSeparatorByLocale(this.locale, DateFormat.MEDIUM);
        this.formatStr = String.format("dd%sMMM%syyyy", dateSeparator, dateSeparator);
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    public void setDueDateValue(java.util.Date dueDateValue) {
        this.dueDateValue = dueDateValue;
    }

    public void setPrincipal(Money principal) {
        this.principal = principal;
    }

    public void setInterest(Money interest) {
        this.interest = interest;
    }

    public void setFees(Money fees) {
        this.fees = this.fees.add(fees);
    }

    public Integer getInstallment() {
        return installment;
    }

    public java.util.Date getDueDateValue() {
        return dueDateValue;
    }

    public Money getPrincipal() {
        return principal;
    }

    public Money getInterest() {
        return interest;
    }

    public Money getFees() {
        return fees;
    }

    public Money getTotalValue() {
        return totalValue;
    }

    public String getDueDateInUserLocale() {
        return DateUtils.getDBtoUserFormatString(dueDateValue, locale);

    }

    public Locale getLocale() {
        return locale;
    }

    public Money getMiscFees() {
        return miscFees;
    }

    public void setMiscFees(Money miscFees) {
        this.miscFees = miscFees;
    }

    public Money getMiscPenalty() {
        return miscPenalty;
    }

    public void setMiscPenalty(Money miscPenalty) {
        this.miscPenalty = miscPenalty;
    }

    public String getDueDate() {
        return DateUtils.getDBtoUserFormatString(dueDateValue, locale);

    }

    public void setDueDate(String dueDate) {
        this.dueDateValue = DateUtils.getDate(dueDate, locale, formatStr);
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = new Money(getCurrency(), totalValue);
    }

    private MifosCurrency getCurrency() {
        return principal.getCurrency();
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
