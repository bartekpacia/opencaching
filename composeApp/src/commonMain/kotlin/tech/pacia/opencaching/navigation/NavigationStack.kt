package tech.pacia.opencaching.navigation

import androidx.compose.runtime.mutableStateListOf

class NavigationStack<T>(vararg initial: T) {
    val stack = mutableStateListOf(*initial)

    fun push(t: T) {
        stack.add(t)
    }

    fun pop() {
        if (stack.size > 1) {
            // Always keep one element on the view stack
            stack.removeLast()
        }
    }

    fun lastWithIndex() = stack.withIndex().last()
}