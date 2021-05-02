package csvparser

import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

/**
 * Class to hold file's data
 * contains methods for getting specific values of specific cells and for performing various calculations:
 * average of a specific column
 * min value of a specific column
 * max value of a specific column
 *
 * @property content Array list of CSVFileRow objects
 */
@PrintRule(allowPrint = true)
class CSVFileData(val content: ArrayList<CSVFileRow>) {

    private val matrix: ArrayList<CSVFileRow> = content
    private val size = matrix.size
    private var allowPrinting:Boolean = true

    init{
        allowPrinting = Utils.isPrintable(this)
    }
    /**
     * @return size of csv matrix
     */
    fun getRowsSize(): Int {
        return size
    }

    /**
     * @param row - matrix row number starting with 0
     * @param col - matrix col number starting with 0
     * @return Value at requested row and col
     * @exception CSVFileException thrown if data is missing on specific cell
     */
    @Throws(CSVFileException::class)
    public fun getCellValue(row: Int, col: Int): String {

        val cellData: String
        try {
            val rowData = matrix.get(row)
            cellData = rowData.row[col]

        } catch (e: Exception) {
            throw CSVFileException("Error: Failed to get data on Cell ($row,$col)",e)
        }
        return cellData
    }

    /**
     * Update new value on matrix
     *
     * @param row - matrix row number starting with 0
     * @param col- matrix col number starting with 0
     * @param value - new value to set
     */
    fun setCellValue(row: Int, col: Int, value: String) {

        val rowData = matrix.get(row)
        rowData.row[col] = value
    }

    /**
     * Extract row from csv file
     *
     * @param row - matrix row number starting with 0
     * @return CSVFileRow object with data of one row
     */
    fun getRowItems(row: Int): CSVFileRow {
        return matrix.get(row)
    }

    /**
     * Extract data from specific coloumn
     *
     * @param col- matrix col number starting with 0
     * @return array list of strings
     */
    private fun getColListItems(col: Int): ArrayList<String> {

        val numRows = matrix.size
        var list: ArrayList<String> = arrayListOf<String>()
        var cellValue: String = Utils.EMPTY

        for (row in 0..numRows - 1) {
            try {
                cellValue = getCellValue(row,col)
                list.add(cellValue)

            } catch (e: CSVFileException) {
                if(allowPrinting){
                    println(e.localizedMessage)
                }
            }
        }
        return list
    }

    /**
     * @return Immutable list of misssing cells in csv matrix
     */
    public fun getMissingCells(): List<Cell> {

        val cellList: MutableList<Cell> = mutableListOf<Cell>();
        var numRows = matrix.size
        var rowData: CSVFileRow
        var str:String? = null

        for (row in 0..numRows - 1) {
            rowData = getRowItems(row)
            for (col in 0..rowData.size() - 1) {
                str = rowData.row[col]
                if (str == Utils.EMPTY || !Utils.checkIfNumber(str)) {
                    cellList.add(Cell(row, col,str))
                }
            }
        }
        return Collections.unmodifiableList(cellList)
    }

    /**
     * Validate data of csv
     * If there's missing value or non number
     * result will be true
     *
     * @return Boolean value
     */
    fun checkValidData(): Boolean {

        var res: Boolean = true
        var numRows = matrix.size
        var rowData: CSVFileRow
        var rowDataString: MutableList<String>
        var listOrigSize: Int
        var filteredSize: Int

        for (row in 0..numRows - 1) {
            if (res) {
                rowData = getRowItems(row)
                rowDataString = rowData.toListOfStringItems()
                listOrigSize = rowDataString.size
                rowDataString =
                    (rowDataString.filter
                        { it -> it.isNotEmpty()
                                && it.length > 0
                                && Utils.checkIfNumber(it)
                             }).toMutableList()

                filteredSize = rowDataString.size
                if (filteredSize < listOrigSize) {
                    res = false
                }
            }
        }
        return res
    }

    /**
     * Calc average value at column
     *
     * @param col
     * @return max value
     * @exception CSVFileException
     */
    @Throws(CSVFileException::class)
    fun average(col: Int): String {

        var columnList = getColListItems(col)
        val doubleArray = Utils.convertToDouble(columnList)
        if (doubleArray.isEmpty()){
            throw CSVFileException("Error: Column $col is empty!!! - no average for this column.")
        }
        return doubleArray.average().toString()
    }

    /**
     * Calc minimum value at column
     *
     * @param col
     * @return minimum value
     * @exception CSVFileException
     */
    @Throws(CSVFileException::class)
    fun min(col: Int): String {

        val doubleArray = Utils.convertToDouble(getColListItems(col))
        if (doubleArray.isEmpty()){
            throw CSVFileException("Error: Column $col is empty!!! - no average.")
        }
        return doubleArray.minOrNull().toString()
    }

    /**
     * Calc maximum value at column
     *
     * @param col
     * @return maximum value
     * @exception CSVFileException
     */
    @Throws(CSVFileException::class)
    fun max(col: Int): String {

        val doubleArray = Utils.convertToDouble(getColListItems(col))
        if (doubleArray.isEmpty()){
            throw CSVFileException("Error: Column $col is empty!!! - no average.")
        }
        return doubleArray.maxOrNull().toString()
    }
}