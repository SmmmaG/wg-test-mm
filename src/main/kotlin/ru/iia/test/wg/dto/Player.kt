package ru.iia.test.wg.dto

import java.time.ZonedDateTime

class Player(val latency: Double, val skill: Double, val name: String) {
    val inTime: ZonedDateTime = ZonedDateTime.now()
    var skillNumber: Int = 0
    var latencyNumber: Int = 0
    var skillAverage: Double = 0.0
    var latencyAverage: Double = 0.0
    var skillLatencyMetric: Double = 0.0
    override fun toString(): String {
        return "$name $skill($skillNumber/${skillAverage*100}) $latency ($latencyNumber/${latencyAverage*100}) Metric: ${(skillLatencyMetric * 100)}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false

        if (latency != other.latency) return false
        if (skill != other.skill) return false
        if (name != other.name) return false
        if (inTime != other.inTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latency.hashCode()
        result = 31 * result + skill.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + inTime.hashCode()
        return result
    }


}