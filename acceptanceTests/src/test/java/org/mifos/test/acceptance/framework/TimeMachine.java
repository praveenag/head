/*
 * Copyright (c) 2005-2009 Grameen Foundation USA
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

package org.mifos.test.acceptance.framework;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.thoughtworks.selenium.Selenium;

public class TimeMachine {

    private static final String MAX_WAIT_FOR_PAGE_TO_LOAD_IN_MILLISECONDS = "30000";
    protected Selenium selenium;
    
    public TimeMachine(Selenium selenium) {
        this.selenium = selenium;
    }

    public TimeMachinePage setDateTime(DateTime dateTime) {
        DateTimeFormatter formatter = ISODateTimeFormat.basicDateTimeNoMillis();
        selenium.open("dateTimeUpdate.ftl?dateTime=" + formatter.print(dateTime.getMillis()));
        waitForPageToLoad();
        return new TimeMachinePage(selenium);        
    }
    
    public TimeMachinePage resetDateTime() {
        selenium.open("dateTimeUpdate.ftl?dateTime=system");
        waitForPageToLoad();
        return new TimeMachinePage(selenium);        
        
    }

    protected void waitForPageToLoad() {
        selenium.waitForPageToLoad(MAX_WAIT_FOR_PAGE_TO_LOAD_IN_MILLISECONDS);
    }
    
}
