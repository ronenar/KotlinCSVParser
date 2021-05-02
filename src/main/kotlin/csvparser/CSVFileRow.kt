package csvparser

/**
 * Class to hold row's data
 *
 * @property row
 */
@PrintRule(fixedSize = 5)
data class CSVFileRow(val row: Array<String>) {

    private val COMMA = ","
    private val SPACE = ' '

    /**
     * @return size of row
     */
    fun size(): Int {
        return row.size
    }

    /**
     * Convert row from array to MutableList of strings
     *
     * @return MutableList<String> object
     */
    fun toListOfStringItems(): MutableList<String> {
        val colList: MutableList<String> = mutableListOf<String>();
        val size = row.size

        for (i in row.indices) {
            colList.add(row[i])
        }
        return colList
    }

    /**
     * Create comma seperated string from raw array
     *
     * @return comma seperated string
     */
    fun toCommaSeperatedString(): String {
        var buf:StringBuffer = StringBuffer()
        for (item in row){
            if (buf.length>0){
                buf.append(COMMA)
            }
            buf.append(item)
        }
        return buf.toString()
    }

    /**
     * Overiding toString
     *
     * @return nice printing for row
     */
    override fun toString(): String {
        var fixedSize = Utils.getPrintFixedSize(this)
        var buf: StringBuffer = StringBuffer()
        var size = row.size
        for (i in 0..size - 1) {
            if (buf.length > 0) {
                buf.append(COMMA)
            }
            buf.append(row[i].padStart(fixedSize, SPACE))
        }
        return buf.toString()
    }
}
