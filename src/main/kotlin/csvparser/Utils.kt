package csvparser

import java.util.regex.Pattern

object Utils {

    private val DOUBLE_NEGATIVE = "^-(\\d*\\.)?\\d+$";
    private val DOUBLE_POSITIVE = "^(\\d*\\.)?\\d+$";
    private val INTEGER_NEGATIVE = "^-[1-9]\\d*$"
    val EMPTY = ""

    /**
     * Check if string is number
     *
     * @param str - string to check if represent a number
     * @return true of false according to string param
     */
    fun isNumber(str: String?): Boolean {
        return if (str.isNullOrEmpty()) false else str.trim().all { Character.isDigit(it) }
    }

    /**
     * Check if string is negative number
     *
     * @param str - string to check if represent a negative number
     * @return true of false according to string param
     */
    fun isNegativeNumber(str: String):Boolean{
        var isDoubleNegative = Pattern.matches(DOUBLE_NEGATIVE, str.trim())
        var isIntegerNegative = Pattern.matches(INTEGER_NEGATIVE, str.trim())
        return isDoubleNegative || isIntegerNegative
    }

    /**
     * Check if string is positive double number
     *
     * @param str - string to check if represent a number
     * @return true of false according to string param
     */
    fun isPositiveDoubleNumber(str: String):Boolean{
        return Pattern.matches(DOUBLE_POSITIVE, str.trim())
    }

    /**
     * Check if string is double positive number
     *
     * @param str - string to check if represent a number
     * @return true of false according to string param
     */
    fun checkIfNumber(str: String):Boolean{
        val str = str.trim()
        return !(str == EMPTY ||
                (!isNumber(str) &&
                        !isNegativeNumber(str) &&
                        !isPositiveDoubleNumber(str)))
    }

    /**
     * Print Cells unfo of missing data
     *
     * @param data - csv data
     */
    @JvmStatic
    @Throws(CSVFileException::class)
    fun printMissingCells(data: CSVFileData){

        println("PrintMissingCells():")
        try{
            val list = data.getMissingCells()
            if (list.isNotEmpty()){
                println("Found missingCells:")
                for (row in list) {
                    println(row)
                }
            }
            else{
                println("Not found missing cells\n")
            }
        }catch (e: Exception){
            throw CSVFileException("Error: CSVFileData is not initalized", e)
        }
    }

    /**
     * This function decide if prints inside a received class
     * will be printed
     * @param classType caller class
     * @return true of false according to annotation parameter
     */
    fun isPrintable(classType: Any): Boolean{

        var ret: Boolean = true // if not defined annotation at all on caller class
        try {

            val cJava = Class.forName(classType.javaClass.name)
            val annotation = cJava.getAnnotation(PrintRule::class.java)
            val allow = annotation.allowPrint
            ret = allow
        }catch (e: NullPointerException){
            // Nothings - means no annotation declared on caller class
        }
        return ret
    }

    /**
     * Return fix size spacing length
     *
     * @param classType caller class
     * @return fixed size spacing between elements
     */
    fun getPrintFixedSize(classType: Any): Int {

        var ret: Int = 10 // if not defined annotation at all on caller class
        try {

            val cJava = Class.forName(classType.javaClass.name)
            val annotation = cJava.getAnnotation(PrintRule::class.java)
            val fixedSize = annotation.fixedSize
            ret = fixedSize

        }catch (e: NullPointerException){
            // Nothings - means no annotation declared on caller class
        }
        return ret
    }

    /**
     * Converts ArrayList of strings to Array of Doubles
     *
     * @param strArray
     * @return Array if Doubles
     */
    fun convertToDouble(strArray: ArrayList<String>): Array<Double> {
        val list = strArray.filter { checkIfNumber(it) }
        return list.map { it.toDouble() }.toTypedArray()
    }
}

