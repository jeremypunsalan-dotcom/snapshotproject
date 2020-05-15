package com.jeremypunsalan.takehome.snapshotproject.list;

import java.util.List;

public interface SnapshotList<E> extends List<E> {
	
	/**
     * Removes all versions prior to the specified one.
     *
     * The version must be no greater than the current version.
     */
    void dropPriorSnapshots(int version);

    /**
     * Retrieves the element at the specified position for the version.
     * 
     * @param index the specified position in the list
     * @param version the specified snapshot
     */
    E getAtVersion(int index, int version);

    /**
     * Creates a snapshot of the current <pre>List</pre>.
     * 
     * @return the version after the snapshot
     */
    int snapshot();

    /**
     * Indicates the version of the current <pre>SnapshotList</pre>; it may
     * also be regarded as the number of times that {@link #snapshot()} was
     * called on the instance.
     *
     * @return the version of the instance
     */
    int version();
    
    /**
     * added for testing purposes
     */
    SnapshotList<E> getListAtVersion(int version);

}
