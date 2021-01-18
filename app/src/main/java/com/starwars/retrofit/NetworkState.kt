package com.starwars.retrofit

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState(val status: Status) {
    companion object {
        var LOADED: NetworkState = NetworkState(Status.SUCCESS)
        var LOADING: NetworkState = NetworkState(Status.RUNNING)
        var FAILED: NetworkState = NetworkState(Status.FAILED)
    }
}