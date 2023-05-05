package com.sui.pojo;
// Date structure used in multiple responses
public class DateInfo {
    public int year; // Nth year since 1900
    public int month; // Month starting from 0
    public int date; // Date
    public int day; // Day of the week
    public int hours; // Hours in Beijing time
    public int minutes; // Minutes
    public int seconds; // Seconds
    public int time; // Unix timestamp * 1000
    public int timezoneOffset; // Difference in hours from UTC time
}