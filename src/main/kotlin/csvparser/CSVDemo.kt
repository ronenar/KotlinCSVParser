package csvparser

import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

fun showMissingCells(data: CSVFileData){
    var cells = data.getMissingCells()
    for (cell in cells){
        var cellData = data.getCellValue(cell.row, cell.col)
        println("Get Data for specific cell ${cell.row},${cell.col} is $cellData")
    }
}

fun demoFunctions(data: CSVFileData){
    var col = 7
    println("Max for col $col is : ${data.max(col)}")
    col = 3
    println("Min for col $col is : ${data.min(col)}")
    col = 4
    println("Min for col $col is : ${data.min(col)}")
    col = 3
    println("Average for col $col is : ${data.average(col)}")

    try {
        col = 16
        println("Average for col $col is : ${data.average(col)}")

    }catch (e: CSVFileException) {
        println(e.localizedMessage)
        e.printStackTrace()
    }
}

fun main(args: Array<String>) {

    /*
    Map object specifies the column number in which a value might
    be missing and the strategy for filling the missing value
    (e.g. average, specific value, max, min etc..).
     */
    val map = mapOf(1 to "min", 2 to "specific value",
        3 to "average", 4 to "min", 7 to "max")

    var path = "./demo/data.csv";
    var name = "data.csv"
    var streamFile = "./demo/data.csv"
    var compiledFilename ="./demo/data_fixed.csv"

    try {
        val inputStream: InputStream = File(streamFile).inputStream()
        lateinit var file: CSVFile
        lateinit var data: CSVFileData

        try{
            file = CSVFile(inputStream)
            //file = CSVFile(Paths.get(path))
            //file = CSVFile(name)

            file.print()
            data = file.getCsvFileData()
            demoFunctions(data)
            file.compile(data)
            file.print()
        }
        catch (e: CSVFileException){

            try
            {
                println(e.localizedMessage)
                Utils.printMissingCells(data)
                showMissingCells(data)

                file.compile(data, map, 100)
                //file.compile(data, map) // using default value 1 for missing cells

                println("After compile:")
                Utils.printMissingCells(data) // print there's no missing cells
                file.print() // show after fix missing cells

                file.saveCompiledFile(compiledFilename)

            }catch (ex: CSVFileException){
                ex.printStackTrace()
            }
        }

    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}
