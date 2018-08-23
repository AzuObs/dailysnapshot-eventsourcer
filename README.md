# Things not talked about during the interview

## DailySnapshots use Decimals, MonthlyConsumptions use Whole Numbers
We can't help the fact that DailySnapshots contain decimal as those are the readings fetch from our user's meters.
We should however try to convert those decimal KilyWatts values into whole Watts values to avoid having to code around lossy decimal values.

## Seq[(Year, Month, Watts)] vs Map[(Year, Month), Watts]
Although it is a constraint of the exercise to output Seq[MonthlyConsumption] it could sometimes be more efficient to actually store the data in a Map depending on how we want to access the data.
The whole exercise only runs in O(n) because we expect that the DailySnapshots are given to us in sequential order, which means the MonthlyConsumptions can be built by always accessing the latest month (the head of the sequence).
The model we build would be a lot more computationally expensive to build using the approach if the DailySnapshots were out of order O(n * m) where n is the number of snapshot and m is the number of monthly consumptions, because we would have to keep traversing the monthly consumptions to find the existing consumption to update.
 
## Trait Aggregator vs Object Aggregator
This is by personal preference (and I understand that it is to some extend idiomatic to do so?) to compose my application using Traits rather than Objects.
