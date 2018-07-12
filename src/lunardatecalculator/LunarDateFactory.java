/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lunardatecalculator;

import net.fortuna.ical4j.model.Date;

import java.text.ParseException;
import java.util.ListIterator;
import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.RRule;

/**
 *
 * @author Long Le
 */
public class LunarDateFactory {

    /**
     * Calculate the Julius day of a given solar Date
     *
     * @param solar the given solar date
     * @return the Julius days
     */
    public int getJuliusDay(Date solar) {
        // parse the date into day, month, and year
        String sdate = solar.toString();
        int yy = Integer.parseInt(sdate.substring(0, 4));
        int mm = Integer.parseInt(sdate.substring(4, 6));
        int dd = Integer.parseInt(sdate.substring(6, 8));

        int a = (14 - mm) / 12;
        int y = yy + 4800 - a;
        int m = mm + 12 * a - 3;

        int juliusDay = dd + ((153 * m + 2) / 5) + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
        // Check for dates before Gergorian Calendar
        if (juliusDay < 2299161) {
            juliusDay = dd + ((153 * m + 2) / 5) + 365 * y + y / 4 - 32083;
        }

        return juliusDay;
    }

    /**
     * Given a number of lunar months from 1990 January 1, calculate the start
     * date (New Moon day) of its lunar month
     *
     * @param numOfLunarMonths the given number of lunar months from 1900
     * January 1
     * @param timeZone integer represent the time zone
     * @return the Julius day of the start date
     */
    public int getNewMoonDay(int numOfLunarMonths, int timeZone) {
        double centuriesTime = (double) numOfLunarMonths / 1236.85; // Time in Julius centuries from midday of 1990 January 1
        double centuriesTimePow2 = Math.pow(centuriesTime, 2.0);
        double centuriesTimePow3 = Math.pow(centuriesTime, 3.0);
        double degree = Math.PI / 180;

        // astronomy stuff
        double meanNewMoon = 2415020.75933 + 29.53058868 * numOfLunarMonths + 0.0001178 * centuriesTimePow2 - 0.000000155 * centuriesTimePow3;
        meanNewMoon += 0.00033 * Math.sin((166.56 + 132.87 * centuriesTime - 0.009173 * centuriesTimePow2) * degree);

        double sunMeanAnomaly = 359.2242 + 29.10535608 * numOfLunarMonths - 0.0000333 * centuriesTimePow2 - 0.00000347 * centuriesTimePow3;
        double moonMeanAnomaly = 306.0253 + 385.81691806 * numOfLunarMonths + 0.0107306 * centuriesTimePow2 + 0.00001236 * centuriesTimePow3;

        double moonArgsLatitude = 21.2964 + 390.67050646 * numOfLunarMonths - 0.0016528 * centuriesTimePow2 - 0.00000239 * centuriesTimePow3;

        double C1 = (0.1734 - 0.000393 * centuriesTime) * Math.sin(sunMeanAnomaly * degree) + 0.0021 * Math.sin(2 * degree * sunMeanAnomaly);
        C1 = C1 - 0.4068 * Math.sin(moonMeanAnomaly * degree) + 0.0161 * Math.sin(degree * 2 * moonMeanAnomaly);
        C1 = C1 - 0.0004 * Math.sin(degree * 3 * moonMeanAnomaly);
        C1 = C1 + 0.0104 * Math.sin(degree * 2 * moonArgsLatitude) - 0.0051 * Math.sin(degree * (sunMeanAnomaly + moonMeanAnomaly));
        C1 = C1 - 0.0074 * Math.sin(degree * (sunMeanAnomaly - moonMeanAnomaly)) + 0.0004 * Math.sin(degree * (2 * moonArgsLatitude + sunMeanAnomaly));
        C1 = C1 - 0.0004 * Math.sin(degree * (2 * moonArgsLatitude - sunMeanAnomaly)) - 0.0006 * Math.sin(degree * (2 * moonArgsLatitude + moonMeanAnomaly));
        C1 = C1 + 0.0010 * Math.sin(degree * (2 * moonArgsLatitude - moonMeanAnomaly)) + 0.0005 * Math.sin(degree * (2 * moonMeanAnomaly + sunMeanAnomaly));

        double deltaCT;
        if (centuriesTime < -11.0) {
            deltaCT = 0.001 + 0.000839 * centuriesTime + 0.0002261 * centuriesTimePow2 - 0.00000845 * centuriesTimePow3 - 0.000000081 * centuriesTime * centuriesTimePow3;
        } else {
            deltaCT = -0.000278 + 0.000265 * centuriesTime + 0.000262 * centuriesTimePow2;
        }

        double juliusDayOfNewMoon = (meanNewMoon + C1 - deltaCT + 0.5 + (double) (timeZone) / 24.0);

        return (int) juliusDayOfNewMoon;
    }

    /**
     * Calculate the Sun longitude given a Julius day and the time zone
     *
     * @param juliusDay the given Julius day
     * @param timeZone integer represent the time zone
     * @return the Sun longitude of the given Julius day
     */
    public int getSunLongitude(int juliusDay, int timeZone) {
        double centuriesTime = ((double) juliusDay - 2451545.5 - ((double) timeZone) / 24) / 36525;
        double centuriesTimePow2 = Math.pow(centuriesTime, 2.0);
        double degree = Math.PI / 180;

        double meanAnomaly = 357.52910 + 35999.05030 * centuriesTime - 0.0001559 * centuriesTimePow2 - 0.00000048 * centuriesTime * centuriesTimePow2;
        double meanLongitude = 280.46645 + 36000.76983 * centuriesTime + 0.0003032 * centuriesTimePow2;

        double meanDegree = (1.914600 - 0.004817 * centuriesTime - 0.000014 * centuriesTimePow2) * Math.sin(degree * meanAnomaly);
        meanDegree += (0.019993 - 0.000101 * centuriesTime) * Math.sin(degree * 2 * meanAnomaly) + 0.000290 * Math.sin(degree * 3 * meanAnomaly);

        double trueLongitude = meanLongitude + meanDegree;
        trueLongitude = trueLongitude * degree;
        trueLongitude = trueLongitude - Math.PI * 2 * ((int) (trueLongitude / (Math.PI * 2)));

        return (int) (trueLongitude / Math.PI * 6);
    }

    /**
     * Get the Julius day of "November" (thang 11) in the given year
     *
     * @param year the given year
     * @param timeZone integer represent the time zone
     * @return Julius day of November
     * @throws ParseException
     */
    public int getLunarMonth11(int year, int timeZone) throws ParseException {
        StringBuilder builder = new StringBuilder("");
        builder.append(year);
        builder.append("1231");
        Date solarEndOfYear = new Date(builder.toString());

        int juliusDay = this.getJuliusDay(solarEndOfYear);
        int off = juliusDay - 2415021;
        int numOfLunarMonths = (int) ((double) off / 29.530588853);

        int newMoonDay = this.getNewMoonDay(numOfLunarMonths, timeZone);
        if (newMoonDay == juliusDay) {
            juliusDay--;
            off = juliusDay - 2415021;
            numOfLunarMonths = (int) ((double) off / 29.530588853);
            newMoonDay = this.getNewMoonDay(numOfLunarMonths, timeZone);
        } else if (newMoonDay > juliusDay) {
            numOfLunarMonths--;
            newMoonDay = this.getNewMoonDay(numOfLunarMonths, timeZone);
        }

        int sunLongitude = this.getSunLongitude(newMoonDay, timeZone);
        if (sunLongitude >= 9) {
            newMoonDay = this.getNewMoonDay(numOfLunarMonths - 1, timeZone);
        }

        return newMoonDay;
    }

    /**
     * Get the offset of the leap month from November in range [1,13],
     * otherwise, no leap month in the year
     *
     * @param month11 the Julius day of November
     * @param timezone integer represent the time zone
     * @return offset of leap month
     */
    public int getLeapMonthOffset(int month11, int timezone) {
        int numOfLunarMonths = (int) (((double) month11 - 2415021.076998695) / 29.530588853 + 0.5);
        int last = 0;
        int i = 1;
        int sunLongitude = this.getSunLongitude(this.getNewMoonDay(numOfLunarMonths + i, timezone), timezone);
        do {
            last = sunLongitude;
            i++;
            sunLongitude = this.getSunLongitude(this.getNewMoonDay(numOfLunarMonths + i, timezone), timezone);
        } while (sunLongitude != last && i < 14);
        return i - 1;
    }

    /**
     * Get the Lunar Date of the given Solar Date
     *
     * @param solar the given solar date
     * @param timeZone integer represent the time zone
     * @return a Date object
     * @throws ParseException
     */
    public Date fromSolarToLunar(Date solar, int timeZone) throws ParseException {
        String sdate = solar.toString();
        int yy = Integer.parseInt(sdate.substring(0, 4));
        int lunarYear;
        int lunarMonth;
        int lunarDay;

        int juliusDay = this.getJuliusDay(solar);

        int numOfLunarMonths = (int) (((double) juliusDay - 2415021.076998695) / 29.530588853);
        int newMoonDay;

        if (numOfLunarMonths >= 0) {
            newMoonDay = this.getNewMoonDay(numOfLunarMonths + 1, timeZone);
            if (newMoonDay > juliusDay) {
                newMoonDay = this.getNewMoonDay(numOfLunarMonths, timeZone);
            }
        } 
        else {
            newMoonDay = this.getNewMoonDay(numOfLunarMonths, timeZone);
            if (newMoonDay > juliusDay) {
                newMoonDay = this.getNewMoonDay(numOfLunarMonths - 1, timeZone);
            }
        }

        int month11 = this.getLunarMonth11(yy, timeZone);
        int bmonth11 = month11;
        if (month11 >= newMoonDay) {
            lunarYear = yy;
            month11 = this.getLunarMonth11(yy - 1, timeZone);
        } else {
            lunarYear = yy + 1;
            bmonth11 = this.getLunarMonth11(yy + 1, timeZone);
        }

        lunarDay = juliusDay - newMoonDay + 1;

        int diff = (newMoonDay - month11) / 29;

        lunarMonth = diff + 11;
        if (bmonth11 - month11 > 365) {
            int leapMonthDiff = this.getLeapMonthOffset(month11, timeZone);
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
            }
        }

        if (lunarMonth > 12) {
            lunarMonth -= 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(lunarYear);
        if (lunarMonth < 10) {
            builder.append(0);
            builder.append(lunarMonth);
        } else {
            builder.append(lunarMonth);
        }
        if (lunarDay < 10) {
            builder.append(0);
            builder.append(lunarDay);
        } else {
            builder.append(lunarDay);
        }

        return new Date(builder.toString());
    }

    /**
     * Generate a DateList object containing lunar dates with a given starting
     * date and a set of rules
     *
     * @param start starting date
     * @param rule RRule of ical4
     * @return DateList object
     * @throws ParseException
     */
    public DateList getLunarDateList(Date start, RRule rule) throws ParseException {
        if (rule.getRecur().getUntil() == null) {
            rule.getRecur().setUntil(start);
        }
        Date until = rule.getRecur().getUntil();

        DateList lunarDateList = rule.getRecur().getDates(start, until, Value.DATE);
        ListIterator<Date> litr = lunarDateList.listIterator();
        while (litr.hasNext()) {
            lunarDateList.set(litr.nextIndex(), this.fromSolarToLunar(litr.next(), 7));
        }

        return lunarDateList;
    }
}
