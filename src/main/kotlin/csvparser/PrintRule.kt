package csvparser

/**
 * PrintRule Annotation
 *
 * @property allowPrint Option to cancel inner printing (such errors), default is true
 * @property fixedSize Option for fixed size spacing between elements, default is 5
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PrintRule (
        val allowPrint: Boolean=true, val fixedSize:Int=5)
