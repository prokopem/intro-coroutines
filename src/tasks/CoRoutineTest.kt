package tasks

import contributors.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
    log("Start")
    runBlocking {
        log("Start runBlocking")
/*        launch { sendRequestsWithAsync() }
        launch { sendRequestsWithLaunch() }
        launch { sendRequestsWithoutLaunch() }*/
        log("Finish runBlocking")
        val vals = listOf<Long>(1,2,3,4,5,6,7,8,9,10)
        vals.asFlow()
            .onStart { log("Starting flow") }
            .transform<Long, Unit> { request(it * 1000) }
            .collect { log("Test $it") }
    }
    log("Finish")
}

suspend fun sendRequestsWithLaunch() = coroutineScope {
    log("sendRequestsWithLaunch() start")
    launch { request(1000L) }
    launch { request(3000L) }
    launch { request(500L) }
    log("sendRequestsWithLaunch() finish")
}

suspend fun sendRequestsWithoutLaunch() = coroutineScope {
    log("sendRequestsWithoutLaunch() start")
    request(1000L)
    request(3000L)
    request(500L)
    log("sendRequestsWithoutLaunch() finish")
}

suspend fun sendRequestsWithAsync() = coroutineScope {
    log("sendRequestsWithAsync() start")
    val req1 = async { request(1000L) }
    val req2 = async { request(3000L) }
    val req3 = async { request(500L) }
    log("awaiting req1 results")
    req1.await()
    log("awaiting req2 results")
    req2.await()
    log("awaiting req3 results")
    req3.await()
    log("sendRequestsWithAsync() finish")
}

suspend fun request(delayMs: Long) = delayMs
    .also { log("Making request for $it ms") }
    .let { delay(it) }
    .also { log("After delay of $delayMs ms") }


