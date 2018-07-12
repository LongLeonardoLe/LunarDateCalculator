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
 * @author Long Le
 */
public class LunarDateCalculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            /*
            Date until = new Date("20180813");
            Date start = new Date();

            RRule rule = new RRule();
            rule.getRecur().setFrequency("MONTHLY");
            rule.getRecur().setUntil(until);
            LunarDateFactory factory = new LunarDateFactory();
            DateList lunarList = factory.getLunarDateList(start, rule);
            System.out.println(lunarList.toString());
            */
            LunarDateFactory factory = new LunarDateFactory();
            Date date = new Date("16480415");
            System.out.println(factory.fromSolarToLunar(date, 7).toString());
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
