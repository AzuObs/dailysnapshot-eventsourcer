package com.dailysnapshoteventsourcer

import java.time.ZonedDateTime
import scala.annotation.tailrec
import Types._

trait Aggregator {

  def aggregateMonthlyConsumptions(snapshots: Seq[DailySnapshot]): Seq[MonthlyConsumption] = {
    @tailrec
    def go(snapshots: Seq[DailySnapshot], consumptions: Seq[MonthlyConsumption])
      : Seq[MonthlyConsumption] =
        snapshots match {
          case Nil | _ :: Nil => consumptions
          case currentSnapshot :: nextSnapshot :: rest =>
            val year = ZonedDateTime.parse(currentSnapshot._1).getYear
            val month = ZonedDateTime.parse(currentSnapshot._1).getMonthValue
            val delta = getDailyConsumption(currentSnapshot._2, nextSnapshot._2)
            val updatedConsumptions = getUpdatedConsumptions(consumptions, year, month, delta)

            go(nextSnapshot +: rest, updatedConsumptions)
        }

    go(snapshots, Seq.empty[MonthlyConsumption])
  }

  val KWH_OVERFLOW = 10000d

  private def getDailyConsumption(nowKWh: KiloWatts, nextKWh: KiloWatts): Watts =
    (if (nextKWh - nowKWh < 0d) KWH_OVERFLOW - nowKWh + nextKWh else nextKWh - nowKWh) * 1000 toInt

  private def getUpdatedConsumptions(
    consumptions: Seq[MonthlyConsumption], year: Int, month: Int, dailyConsumption: Watts
  ): Seq[MonthlyConsumption] = consumptions match {
    case Nil =>
      Seq(MonthlyConsumption(year, month, dailyConsumption))
    case MonthlyConsumption(`year`, `month`, consumption) :: restConsumptions =>
      MonthlyConsumption(year, month, consumption + dailyConsumption) +: restConsumptions
    case restConsumptions =>
      MonthlyConsumption(year, month, dailyConsumption) +: restConsumptions
  }

}
