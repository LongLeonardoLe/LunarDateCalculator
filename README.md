# LunarDateCalculator
More like a generator for generating a list of lunar dates, given a starting `Date` and a `RRule`, return a `DateList` of lunar dates
The application is explicit done for Vietnamese Lunar Calendar, which means the default time zone is GMT +7.
Might not be working properly for dates before 1900

## Dependencies
* [iCal4j](https://github.com/ical4j/ical4j) - iCalendar parser and object model
* slf4j-api [required]
* commons-lang3 [required]
* commons-collections4 [required]
