package com.arsvechkarev.testcommon

import org.junit.Assert

infix fun <T> List<T>.isEquivalentTo(other: List<T>): Boolean {
  return this.containsAll(other) && other.containsAll(this)
}

infix fun <T> List<T>.doesNotContain(element: T): Boolean {
  return !this.contains(element)
}

infix fun <T> List<T>.shouldContainAll(other: List<T>) {
  Assert.assertTrue(size == other.size)
  other.forEach { otherElement -> Assert.assertTrue(this.contains(otherElement)) }
}

fun verify(condition: () -> Boolean) {
  Assert.assertTrue(condition())
}

fun verifyFalse(condition: () -> Boolean) {
  Assert.assertFalse(condition())
}
