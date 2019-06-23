## Project alias: NZR
## Purpose: incremental backup for Netezza DW Appliance
## Description: 

Incrementally captures changes in the data and data structures that occured in a Source Netezza database, stages and sequentially propagates them to a configured Target system. Also computes corrective DDL if any structural changes were detected.
   

Architecture:
• Source database (from which data and structural changes are captured)
• Target database (to which data and srtuctural changes are propagated)
• Repository database (stores system configuration, batch execution results, and contains "work" tables for data dictionary snapshots).
•	Capture process (can run on the Source or another server that has a network connection to the Source and the Repository database)
•	Propagation process (can run on Target or another server that has a network connection to the Target server and the Repository database
•	Administration Tool (user-facing web app, acesses Repository database)

Data Capture process pulls data increments -records that were inserted or deleted from the Source during the time that elapsed  since a previous movement, via external tables. Data Set is an arbitrary named set of tables whose data will be captured; multiple data sets can be defined, and each set will be tied to its own schedule. 

Structural changes in the Source system are determined prior to each data movement by comparing a previous data dictionary snapshot to the current one, and correctlive DDL is generated if needed. 

The incremental process is based on a presence of a stable transaction id (XID).  Each transaction in Netezza (including selects) increments the internal counter. For capture a previous batch high XID is captured and used as current low XID and current max XID as high XID. The select statement captures data that falls within a given XID range. The resulting file format can be CSV or "internal" with optional compression. 

Three types of Capture batches are defined: Incremental, Baseline and Synchronization. 

Incremental Batch runs on a schedule infinitely or until the schedule is deleted or disabled.

Baseline Batch captures all of the data in the Source database (for the given Data Set) and, optionally, generates “CREATE TABLE” statements to create the objects in the Target. 

Synchronization batch can be used in cases where data was moved manually to create a new batch entry in the Repository database to capture the current XID to be used later in Incremental Batches. 

Degree of parallelism is tied to the  number of threads started per table capture (tied to Netezza-specific “slice id”). 

Propagation  is a background process; it asynchronously and sequentially restores captured data and structural changes to a Target system. 

Administration User Interface (source code is in another repository) is a user-facing web applicaion that allows a user to configure the system, define data sets, associate them with schedules, create propagation subscriptions and monitor batch execution.

Technologies - Java/Spring/Spring JDBC, Quartz scheduler, SQL
