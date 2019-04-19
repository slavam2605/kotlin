// TARGET_BACKEND: JVM
// WITH_RUNTIME

typeclass interface Monoid<T> {
    fun T.combine(other: T): T
    fun empty(): T
}

typeclass interface PrettyPrintable<T> {
    fun T.prettyPrint(): String
}

object IntMonoid : Monoid<Int> {
    override fun Int.combine(other: Int) = this + other
    override fun empty() = 0
}

object DoublePrinter : PrettyPrintable<Double> {
    override fun Double.prettyPrint() = "<$this>"
}

fun <T> List<T>.combineAll(): String where typeclass Monoid<T>, typeclass PrettyPrintable<Double> {
    var acc = empty()
    this@combineAll.forEach { elem ->
        acc = acc.combine(elem)
    }
    return acc + 12.0.prettyPrint()
}

fun box(): String {
    val answer = listOf(1, 2, 3, 4, 5).combineAll<Int>()
    return answer.toString()
}