package csvparser

import java.io.*
import java.nio.file.Path
import java.util.ArrayList

/**
 * CVSFile instantiate CSVFileData class from lines of a csv file
 * When instantiating this class we should be capable passing over either
 * the name of the CSV file or its path or a reference for InputStream through
 * which the file data can be received.
 *
 */
@PrintRule(allowPrint = true)
class CSVFile() {

    private lateinit var csvFileDate: CSVFileData
    private var allowPrinting:Boolean = true
    private val CSV_TOKEN = ","

    init{
        allowPrinting = Utils.isPrintable(this)
    }

    /**
     * @constructor - Constructs a new CSVFile by filename
     * The cause is not initialized.
     * @param   filename  Filename of csv file
     */
    constructor  (filename: String) : this(){
        readFileByFilename(filename)
    }

    /**
     * @constructor - Constructs a new CSVFile by filename
     * The cause is not initialized.
     * @param   fullpath  Fullpath of csv file
     */
    constructor  (fullpath: Path) : this(){

        val file: String = fullpath.toAbsolutePath().toString()
        readFileByFilename(file)
    }

    /**
     * @constructor - Constructs a new CSVFile by filename
     * The cause is not initialized.
     * @param   filStream  FilStream of csv file
     */
    constructor  (filStream: InputStream) : this() {
        readFileByInputStream(filStream)
    }

    /**
     * @return CSVFileData
     */
    fun getCsvFileData(): CSVFileData {
        return csvFileDate
    }

    /**
     * Save compiled file to disk
     *
     * @param filename Filename for compiled csv file
     * @exception CSVFileException Thrown if there's error while writing file.
     */
    @Throws(CSVFileException::class)
    public fun saveCompiledFile(filename: String){

        var fileWriter: FileWriter? = null
        println("Write to file: $filename")
        try {
            fileWriter = FileWriter(filename)
            var size = csvFileDate.getRowsSize()
            for (row in 0..size - 1) {
                var line = csvFileDate.getRowItems(row)
                fileWriter.append(line.toCommaSeperatedString()).append("\n")
            }

            if(allowPrinting){
                println("\nWrite CSV successfully!")
            }

        } catch (e: Exception) {

            throw CSVFileException("Writing CSV error!",e)

        } finally {
            try {

                fileWriter!!.flush()
                fileWriter.close()

            } catch (e: Exception) {

                if(allowPrinting){
                    println("Flushing/closing error!")
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Read csv file
     *
     * @param ins InputStream of file with csv data
     * @exception CSVFileException thrown if there's error while reading file.
     */
    @Throws(CSVFileException::class)
    private fun readFileByInputStream(ins: InputStream) {

        val out = mutableListOf<String>()
        val rows = ArrayList<CSVFileRow>()
        try {
            ins.bufferedReader().useLines { lines -> lines.forEach { out.add(it) } }
            out.forEach { it ->
                try{
                    rows.add(parseLineToRow(it))
                }catch (e: CSVFileException){
                    if(allowPrinting) {
                        print(e.localizedMessage)
                        e.printStackTrace()
                    }
            }}
        } catch (e: Exception) {

            throw CSVFileException("Reading CSV Error!",e)

        } finally {
            try {
                ins.close()
            } catch (e: IOException) {
                throw CSVFileException("Closing fileReader Error!",e)
            }
        }
        csvFileDate = CSVFileData(rows)
    }

    /**
     * Parse lines on file, each line instantiate a CSVFileRow class
     *
     * @param line - line of text, with tokens, create CSVFileRow to hold them
     * @return CSVFileRow object
     * @exception CSVFileException thrown if there's error while parsing lines.
     */
    private fun parseLineToRow(line: String): CSVFileRow {

        var row : CSVFileRow
        val tokens = line.split(CSV_TOKEN).toTypedArray()
        if (tokens.isNotEmpty() && line.length>0) {
            row = CSVFileRow(tokens)
        }else{
            throw CSVFileException("Found an empty row. removed")
        }
        return row
    }

    /**
     * Read csv file
     *
     * @param filename file with csv data
     * @exception CSVFileException thrown if there's error while parsing lines.
     */
    @Throws(CSVFileException::class)
    fun readFileByFilename(filename: String) {

        val rows = ArrayList<CSVFileRow>()
        var fileReader: BufferedReader? = null
        try {
            fileReader = BufferedReader(FileReader(filename))
            var line = fileReader.readLine()
            while (line != null) {
                try {
                    rows.add(parseLineToRow(line))

                }catch (e: CSVFileException){
                    if(allowPrinting){
                        println(e.localizedMessage)
                        e.printStackTrace()
                    }
                }
                line = fileReader.readLine()
            }

        } catch (e: Exception) {
            throw CSVFileException("Reading CSV Error!",e)

        } finally {

            try {
                    fileReader!!.close()

                } catch (e: Exception) {
                    throw CSVFileException("Closing fileReader Error!",e)
                }
        }
        csvFileDate = CSVFileData(rows)
    }

    /**
     * Print csv data
     */
    fun print() {

        println("csv contnet:")
        val size = csvFileDate.getRowsSize()
        var line: CSVFileRow
        for (row in 0..size - 1) {
            line = csvFileDate.getRowItems(row)
            println(line)
        }
        println()
    }

    /**
     * Compile method check if CSVFileData object has valid data
     *
     * @param data CSVFileData object
     * @exception CSVFileException thrown if a specific cell data is missing
     */
    @Throws(CSVFileException::class)
    fun compile(data: CSVFileData) {

        if (!data.checkValidData()) {
            throw CSVFileException("Error: Not Valid data - missing data");
        }
    }

    /**
     * Compile method with a startegy map.
     *
     * @param data
     * @param missingDataStrategy - Map object that specifies
     * the column number in which a value might be missing and the strategy for filling the missing value
     * (e.g. average, specific value, max, min etc..).
     * @param value - specific value integer, by default it's 1.
     * @return CSVFileData object
     * @exception CSVFileException thrown if a specific cell data is missing
     */
    @Throws(CSVFileException::class)
    fun compile(data: CSVFileData, missingDataStrategy: Map<Int, String>, value: Int=1): CSVFileData {

        var strategy: String = Utils.EMPTY
        try {
            if (!data.checkValidData()) {

                // each missing coloumn will recive value by strategy
                val emptyCells = data.getMissingCells()
                for (cell in emptyCells) {
                    strategy = missingDataStrategy[cell.col].toString()

                    var newValue: String = when (strategy) {
                        "average" -> data.average(cell.col)
                        "specific value" -> value.toString()
                        "max" -> data.max(cell.col)
                        "min" -> data.min(cell.col)
                        else -> ""
                    }
                    data.setCellValue(cell.row, cell.col, newValue.toString())
                }

                if(allowPrinting){
                    var str:String
                    var set = missingDataStrategy.keys
                    for (item in set){
                        if(missingDataStrategy[item]=="specific value"){
                            str = ": $value"
                        }
                        else{
                            str = Utils.EMPTY
                        }
                        println("Strategy for column $item is ${missingDataStrategy[item]} $str")
                    }
                }
            }
        } catch (e: Exception) {
            throw CSVFileException(e.localizedMessage,e)
        }
        return data
    }

    /**
     * Function for using default value withing java call
     *
     * @param data CSVFileData object
     * @param map - Map object that specifies
     * the column number in which a value might be missing and the strategy for filling the missing value
     * (e.g. average, specific value, max, min etc..).
     * @return CSVFileData object
     * @exception CSVFileException thrown if a specific cell data is missing
     */
    @Throws(CSVFileException::class)
    fun compileWithDefaultValue(data: CSVFileData, map: MutableMap<Int, String>): CSVFileData {
        return compile(data,map)
    }
}

