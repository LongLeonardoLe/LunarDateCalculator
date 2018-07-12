/*
 * Copyright 2018 Long Le <longlnt@vng.com.vn>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package LunarDateCalculatorTester;

import lunardatecalculator.LunarDateFactory;
import java.util.ArrayList;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.*;

import java.text.ParseException;

/**
 *
 * @author Long Le <longlnt@vng.com.vn>
 */
public class LunarDateFactoryTester {

    private LunarDateFactory ldFactory;

    public LunarDateFactoryTester() {
        this.ldFactory = new LunarDateFactory();
    }

    /**
     * Test cases for February 29 in leap years 2000, 2004, 2008, 2012, 2016
     * @throws ParseException 
     */
    public void testFebruaryLeapYear() throws ParseException {
        // input strings for dates
        ArrayList<String> stringDateList = new ArrayList<String>();
        stringDateList.add("20000229");
        stringDateList.add("20040229");
        stringDateList.add("20080229");
        stringDateList.add("20120229");
        stringDateList.add("20160229");

        // expected lunar dates corresponding to the input
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("20000125");
        expectedResults.add("20040210");
        expectedResults.add("20080123");
        expectedResults.add("20120208");
        expectedResults.add("20160122");

        // initial DateList and RRule objects
        DateList dateList = new DateList();
        RRule rule = new RRule();
        rule.getRecur().setFrequency("YEARLY");
        rule.getRecur().setInterval(4);
        rule.getRecur().setCount(dateList.size());
        for (int i = 0; i < stringDateList.size(); ++i) {
            dateList.add(new Date(stringDateList.get(i)));
        }
        rule.getRecur().setUntil(new Date("20161231"));

        DateList results = this.ldFactory.getLunarDateList(dateList.get(0), rule);
        
        System.out.println("===== February 29 Test =====");

        int count = 0;
        for (int i = 0; i < results.size(); ++i) {
            if (!results.get(i).toString().equals(expectedResults.get(i))) {
                System.out.println("Result: " + results.get(i).toString() + ", while expected: " + expectedResults.get(i));
            }
            ++count;
        }
        
        System.out.println(count + "/" + results.size() + " PASSED.");
    }
    
    public void testYearBefore1900() throws ParseException {
        // input strings for dates
        ArrayList<String> stringDateList = new ArrayList<String>();
        stringDateList.add("18880229");
        stringDateList.add("18900519");
        stringDateList.add("18880820");
        stringDateList.add("17650816");
        stringDateList.add("14280315");

        // expected lunar dates corresponding to the input
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("18880118");
        expectedResults.add("18900401");
        expectedResults.add("18880713");
        expectedResults.add("17650701");
        expectedResults.add("14280230");

        // initial DateList and RRule objects
        DateList dateList = new DateList();
        DateList results = new DateList();
        for (int i = 0; i < stringDateList.size(); ++i) {
            Date date = new Date(stringDateList.get(i));
            dateList.add(date);
            Date lunarDate = this.ldFactory.fromSolarToLunar(date, 7);
            results.add(lunarDate);
        }
        
        System.out.println("===== Years Before 1900 Test =====");

        int count = 0;
        for (int i = 0; i < results.size(); ++i) {
            String resultString = results.get(i).toString();
            if (resultString.length() > 8) {
                resultString = resultString.substring(0, 8);
            }
            if (!resultString.equals(expectedResults.get(i))) {
                System.out.println("Result: " + resultString + ", while expected: " + expectedResults.get(i));
                continue;
            }
            ++count;
        }
        
        System.out.println(count + "/" + results.size() + " PASSED.");
    }
    
    public void testUsualDate() throws ParseException {
        // input strings for dates
        ArrayList<String> stringDateList = new ArrayList<String>();
        stringDateList.add("20200331");
        stringDateList.add("20370727");
        stringDateList.add("20291231");
        stringDateList.add("20300101");
        stringDateList.add("20300831");

        // expected lunar dates corresponding to the input
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("20200308");
        expectedResults.add("20370615");
        expectedResults.add("20291127");
        expectedResults.add("20291128");
        expectedResults.add("20300803");

        // initial DateList and RRule objects
        DateList dateList = new DateList();
        DateList results = new DateList();
        for (int i = 0; i < stringDateList.size(); ++i) {
            Date date = new Date(stringDateList.get(i));
            dateList.add(date);
            Date lunarDate = this.ldFactory.fromSolarToLunar(date, 7);
            results.add(lunarDate);
        }
        
        System.out.println("===== Usual Date Test =====");

        int count = 0;
        for (int i = 0; i < results.size(); ++i) {
            String resultString = results.get(i).toString();
            if (resultString.length() > 8) {
                resultString = resultString.substring(0, 8);
            }
            if (!resultString.equals(expectedResults.get(i))) {
                System.out.println("Result: " + resultString + ", while expected: " + expectedResults.get(i));
                continue;
            }
            ++count;
        }
        
        System.out.println(count + "/" + results.size() + " PASSED.");
    }

    public static void main(String[] argv) {
        LunarDateFactoryTester tester = new LunarDateFactoryTester();
        try {
            tester.testFebruaryLeapYear();
            tester.testYearBefore1900();
            tester.testUsualDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
