package core

/**
 * Mapper for converting value of type [T] to [R]
 */
fun interface Mapper<T, R> {
  
  /** Converts [value] of type [T] to result [R] */
  fun map(value: T): R
}