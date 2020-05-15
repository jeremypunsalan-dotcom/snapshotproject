package com.jeremypunsalan.takehome.snapshotproject.list;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class SynchronizedUnbalancedSnapshotList<E> extends CopyOnWriteArrayList<E> implements SnapshotList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<Integer, SynchronizedUnbalancedSnapshotList<E>> snapshots;
	private AtomicInteger currentVersion;

	public SynchronizedUnbalancedSnapshotList() {
		super();
		currentVersion = new AtomicInteger(1);
		snapshots = new ConcurrentHashMap<Integer, SynchronizedUnbalancedSnapshotList<E>>();
	}

	public SynchronizedUnbalancedSnapshotList(Collection<? extends E> c) {
		super(c);
		currentVersion = new AtomicInteger(1);
		snapshots = new ConcurrentHashMap<Integer, SynchronizedUnbalancedSnapshotList<E>>();
	}

	public SynchronizedUnbalancedSnapshotList(E[] toCopyIn) {
		super(toCopyIn);
		currentVersion = new AtomicInteger(1);
		snapshots = new ConcurrentHashMap<Integer, SynchronizedUnbalancedSnapshotList<E>>();
	}

	@Override
	public void dropPriorSnapshots(int version) {
		if (version > currentVersion.get())
			throw new IllegalArgumentException("version is greater than the current version");

		synchronized (snapshots) {
			for (int idx = version - 1; idx > 0; idx--) {
				if (snapshots.contains(idx)) {
					snapshots.remove(idx);
				}
			}
		}

	}

	@Override
	public E getAtVersion(int index, int version) {
		if (this.getListAtVersion(version).size() <= index)
			throw new IllegalArgumentException("index specified not existing on version requested");
		return this.getListAtVersion(version).get(index);
	}

	@Override
	public int snapshot() {
		synchronized (snapshots) {
			snapshots.put(currentVersion.get(), new SynchronizedUnbalancedSnapshotList<E>(this));
		}
		return currentVersion.getAndIncrement();
	}

	@Override
	public int version() {
		return currentVersion.get();
	}

	@Override
	public SnapshotList<E> getListAtVersion(int version) {
		if (!snapshots.containsKey(version))
			throw new IllegalArgumentException("version not exists on the snapshots");
		return snapshots.get(version);
	}

	/**
	 * implemented methods that are not allowed for this use case.
	 * 
	 */

	@Override
	public void add(int index, E element) {
		// You can only append and cannot insert on a specified index
		throw new UnsupportedOperationException("Cannot insert element into specified index");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot insert collections into specified index");
	}

	@Override
	public E remove(int index) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot remove element");
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot remove element");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot remove element");
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot remove element");
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot replace all element");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Cannot retain all element");
	}

}
