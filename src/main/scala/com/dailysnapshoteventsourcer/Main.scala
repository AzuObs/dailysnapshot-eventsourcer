package com.dailysnapshoteventsourcer

import Data.dailySnapshots

object Main
  extends App
    with Aggregator {

  aggregateMonthlyConsumptions(dailySnapshots).foreach(println)

}
