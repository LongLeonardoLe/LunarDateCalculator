/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lunardatecalculator;

import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.*;
import java.util.ListIterator;

import java.text.ParseException;
import net.fortuna.ical4j.model.parameter.Value;

/**
 *
 * @author cpu10360
 */
public class LunarDateCalculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Date until = new Date("20181231");
            Date start = new Date();

            RRule rule = new RRule();
            rule.getRecur().setFrequency("MONTHLY");
            rule.getRecur().setUntil(until);
            LunarDateFactory factory = new LunarDateFactory();
            DateList lunarList = factory.getLunarDateList(start, rule);
            System.out.println(lunarList.toString());
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
        try {
            Date date = new Date("20180711");
            LunarDateFactory ldfactory = new LunarDateFactory();
            DateList list = new DateList();
            list.add(ldfactory.fromSolarToLunar(date, 7));
            System.out.println(list.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
         */
    }
}
