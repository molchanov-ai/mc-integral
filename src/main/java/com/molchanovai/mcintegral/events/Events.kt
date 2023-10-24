package com.molchanovai.mcintegral.events

import com.molchanovai.mcintegral.Cell

sealed class EventBase {
  data class EventTerminate(val cell: Cell): EventBase()
  data class BranchEvent(private val parent: Cell, private val child: Cell): EventBase()
}