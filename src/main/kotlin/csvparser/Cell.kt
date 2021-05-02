package csvparser

/**
 * This class hold cell data.
 * @property row index start at 0
 * @property col index start at 0
 * @property value String inside a Cell.*/
data class Cell(
    @JvmField val row: Int,
    @JvmField val col:Int,
    val value:String){

    override fun toString(): String {
        return "Cell($row,$col) = $value"
    }
}