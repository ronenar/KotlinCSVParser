# Kotlin CSV Parser Library

## **Introduction**

The library developed in order to parse CSV files in a simple way.


**Main Capabilities**

The library should allow the developer who uses it to specify a CSV file and then retrieve various data from it, including the following:

A. Getting the specific value in a specific row and a specific column

B. Performing various calculations on specific columns, such as average and sum.

C. When a specific data in a specific cell is missing, either an exception will be thrown or a fillup (based on average values or another criteria) will take place

**Main Design**
1. The library include the definition of a specific exception class, such as CSVFileException. Whenever exception takes place the exception to be thrown should be of that specific exception class.

2. The library include the definition of the CSVFile class. When instantiating this class we should be capable passing over either the name of the CSV file or its path or a reference for InputStream through which the file data can be received.

3. Calling the compile method on a CSVFile we receive a CSVFileData object. If a specific cell data is missing then an exception will be thrown (exception of the specific type we defined). Calling the compile method we can pass over a Map object that specifies the column number in which a value might be missing and the strategy for filling the missing value (e.g. average, specific value, max, min etc..).

4. On the CSVFileData we call various methods for getting specific values of specific cells and for performing various calculations (e.g. average of a specific column, min value of a specific column, max value of a specific column…).


**Printing control:**
- @PrintRule(fixedSize = 5) - Control spacing when printing csv

- @PrintRule(allowPrint = true) - Control inner print of exceptions.

**Example:**

csv contnet:



    1, -5.2,    1,    7,    3,    2,    6,    1
    1, -1.0,    3,     ,    2,    2,    3,     
    2,    6,     ,    4,    2,    3,    4,    1



Max for col 7 is : 1.0

Min for col 3 is : 4.0

Min for col 4 is : 2.0

Average for col 3 is : 5.5

Error: Failed to get data on Cell (0,16)

Error: Failed to get data on Cell (1,16)

Error: Failed to get data on Cell (2,16)

Error: Failed to get data on Cell (0,16)

Error: Failed to get data on Cell (1,16)

Error: Failed to get data on Cell (2,16)

Error: Column 16 is empty!!! - no average for this column.

Error: Not Valid data - missing data

PrintMissingCells():

Found missingCells:

Cell(1,1) =  -1.0

Cell(1,3) =  

Cell(1,7) = 

Cell(2,2) =  

Get Data for specific cell 1,1 is  -1.0

Get Data for specific cell 1,3 is  

Get Data for specific cell 1,7 is 

Get Data for specific cell 2,2 is  

Strategy for column 1 is min 

Strategy for column 2 is specific value : 100

Strategy for column 3 is average 

Strategy for column 4 is min 

Strategy for column 7 is max 

After compile:

PrintMissingCells():

Not found missing cells

csv contnet:
csvparser.CSVFileException: Error: Column 16 is empty!!! - no average for this column.
	at csvparser.CSVFileData.average(CSVFileData.kt:174)
	at csvparser.CSVDemoKt.demoFunctions(CSVDemo.kt:27)
	at csvparser.CSVDemoKt.main(CSVDemo.kt:62)

    1, -5.2,    1,    7,    3,    2,    6,    1
    1, -5.2,    3,  5.5,    2,    2,    3,  1.0
    2,    6,  100,    4,    2,    3,    4,    1

Write to file: ./demo/data_fixed.csv

Write CSV successfully!

Process finished with exit code 0