package com.dailysnapshoteventsourcer

import org.scalatest._
import Types._

class AggregatorSpec
  extends FlatSpec
    with Matchers
    with Aggregator {

  "aggregateMonthlyConsumptions" should
    "aggregate DailySnapshots within the same month and year to a MonthlyConsumption" in {

      aggregateMonthlyConsumptions(Seq(
        ("2018-01-01T00:00:00z", 100d),
        ("2018-01-02T00:00:00z", 200d),
        ("2018-01-03T00:00:00z", 300d)
      )) should equal (Seq(
        MonthlyConsumption(2018, 1, 200000)
      ))

    }

  it should
    "aggregate DailySnapshots into different months" in {

    aggregateMonthlyConsumptions(Seq(
      ("2018-01-30T00:00:00z", 200d),
      ("2018-01-31T00:00:00z", 300d),
      ("2018-02-01T00:00:00z", 400d),
      ("2018-02-02T00:00:00z", 500d),
      ("2018-02-03T00:00:00z", 600d)
    )) should equal (Seq(
      MonthlyConsumption(2018, 2, 200000),
      MonthlyConsumption(2018, 1, 200000)
    ))

  }

  it should
    "aggregate DailySnapshots into different years and months" in {

      aggregateMonthlyConsumptions(Seq(
        ("2018-12-30T00:00:00z", 200d),
        ("2018-12-31T00:00:00z", 300d),
        ("2019-01-01T00:00:00z", 400d),
        ("2019-01-02T00:00:00z", 500d),
        ("2019-01-03T00:00:00z", 600d)
      )) should equal (Seq(
        MonthlyConsumption(2019, 1, 200000),
        MonthlyConsumption(2018, 12, 200000)
      ))

    }

  it should
    "consider the case where a DailySnapshot meter reading can overflow" in {

      aggregateMonthlyConsumptions(Seq(
        ("2019-01-01T00:00:00z", 9900d),
        ("2019-01-02T00:00:00z", 0d),
        ("2019-01-03T00:00:00z", 100d)
      )) should equal (Seq(
        MonthlyConsumption(2019, 1, 200000)
      ))

    }

  it should
    "return an empty MonthlyConsumption if no DailySnapshots are provided" in {

      aggregateMonthlyConsumptions(Seq.empty[DailySnapshot]) should equal (Seq.empty[MonthlyConsumption])

    }

  it should
    "return an empty MonthlyConsumption if only one DailySnapshot is provided" in {

      aggregateMonthlyConsumptions(Seq(
        ("2019-01-01T00:00:00z", 9900d)
      )) should equal (Seq.empty[MonthlyConsumption])

    }

  it should
    "return a MonthlyConsumption if at least two DailySnapshots are provided" in {

      aggregateMonthlyConsumptions(Seq(
        ("2019-01-01T00:00:00z", 9900d),
        ("2019-01-02T00:00:00z", 0d)
      )) should equal (Seq(
        MonthlyConsumption(2019, 1, 100000)
      ))

    }

}
