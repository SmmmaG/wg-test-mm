package ru.iia.test.wg.scheduler

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WaitTimeChecker : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun cancel() {
        job.cancel()
    }

    fun schedule() = launch { // launching the coroutine
        var seconds = 1
        while (true) {
            delay(1000)
            if (seconds++ == 5) {
            }
        }
    }
}