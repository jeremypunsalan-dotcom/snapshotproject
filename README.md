This is the implementation of the SynchronizedUnbalancedSnapshotList which implements
the SnapshotList interface. This project is compiled under Maven build (so we need to have mvn installed).

There are three java files on this project:
In src/main/java:
com.jeremypunsalan.takehome.snapshotproject.list.SnapshotList
com.jeremypunsalan.takehome.snapshotproject.list.SynchronizedUnbalancedSnapshotList
In src/test/java:
com.jeremypunsalan.takehome.snapshotproject.SnapshotTests

To get the project source code:
Either you can download it using git (https://github.com/jeremypunsalan-dotcom/snapshotproject.git),
or download from google drive (https://drive.google.com/open?id=1oPgygLwapbYMeBigcMxdAyUkTJITdVKg).

To compile:
1. run mvn clean install on the project folder

To get the jar
1. go to the target folder

To use the object class
1. Import the jar on your project using your favorite IDE


OVERVIEW

The implementation is using CopyOnWriteArrayList class as the parent class, since it is known that
this list is Thread safe and a better implementation of ArrayList with synchronization. This list 
keeps the list in a snapshot array form and it is immutable. 
Also take note that this object keeps a ConcurrentHashMap of snapshots that can be retrieved using
get method.

PROS
Thread Safe
Implements ArrayList with Synchronization (faster data access)

CONS
Performance will be slower
Insertion of objects in specified index is not allowed
