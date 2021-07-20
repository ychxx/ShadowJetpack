package com.yc.shadowjetpack.test

import kotlinx.coroutines.flow.*

/**
 * Creator: yc
 * Date: 2021/7/19 18:20
 * UseDes:
 */
suspend fun main() {
    val flow1 = flow {
        emit("111")
    }
    println(flow1)
    val flow11 = flow1.test1()
    println(flow11)
    flow11.collect()
    val flow2 = flow {
        emit("222")
    }
    println(flow2)
    val flow22 = flow2.test2()
    println(flow22)
    flow22.collect()
}

fun <T> Flow<T>.test1(): Flow<T> {
    this.onStart {
        println("test1-onStart")
    }.onCompletion {
        println("test1-onCompletion")
    }
    return this
}

fun <T> Flow<T>.test2(): Flow<T> = onStart {
    println("test2-onStart")
}.onCompletion {
    println("test2-onCompletion")
}

class Test {

}