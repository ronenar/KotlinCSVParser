package csvparser

/**
 * The class CSVFileException and its subclasses are a form of
 * Throwable that indicates conditions that a reasonable
 * application might want to catch.
 */
class CSVFileException: Throwable {

    /**
     * @constructor - Constructs a new exception.
     * The cause is not initialized.
     */
    constructor() : super()

    /**
     * @constructor - Constructs a new exception with the specified detail message.
     * The cause is not initialized.
     * @param   message   the detail message.
     */
    constructor(message: String) : super(message)

    /**
     * @constructor - Constructs a new exception with the specified detail message and
     * cause.
     * @param  message the detail message
     * @param  cause the cause
     */
    constructor(message: String, cause: Throwable) : super(message, cause)

    /**
     * @constructor - Constructs a new exception with the specified
     * cause and a detail message
     * @param  cause the cause
     */
    constructor(cause: Throwable) : super(cause)
}

