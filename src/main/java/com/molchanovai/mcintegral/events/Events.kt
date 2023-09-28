package com.molchanovai.mcintegral.events

sealed class EventBase {
  data object EventTerminate: EventBase()
}