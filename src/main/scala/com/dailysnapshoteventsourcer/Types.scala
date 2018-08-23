package com.dailysnapshoteventsourcer

object Types {

  type KiloWatts = Double
  type Watts = Int

  type DailySnapshot = (String, KiloWatts)

  case class MonthlyConsumption(
    year: Int,
    month: Int = 1,
    kWhConsumption: Watts
  )

}
