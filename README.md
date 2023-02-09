# uoy-weeks
A simple and dirty java program to output an ICS file with semester dates for UoY

# How to use
You can add new semesters, providing start and end dates, vacation dates and indicating wheter there is a consolidation week. The program assumes 11 teaching weeks (week 0 is freshers/refreshers).

You can run the program and save the output directly to an ics file with a pipe, e.g.

```
$ javac CreateCal.java
$ java CreateCal > uoy-weeks.ics
```
