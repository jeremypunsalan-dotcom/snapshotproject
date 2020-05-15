package com.jeremypunsalan.takehome.snapshotproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.jeremypunsalan.takehome.snapshotproject.list.SnapshotList;
import com.jeremypunsalan.takehome.snapshotproject.list.SynchronizedUnbalancedSnapshotList;

class SnapshotTests {

	@Test
	void testBasicLogic() {

		SnapshotList<Integer> list = new SynchronizedUnbalancedSnapshotList<Integer>();
		// [1,2,3,4,5] - version 1
		// [1,2,3,4,5,6] - version 2
		// [1,2,3,4,5,6,7,8] - version 3
		// [1,2,3,4,5,6,7,8,9,10] - current version (4)
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		assertEquals(1, list.version());
		assertEquals(1, list.snapshot());

		list.add(6);
		assertEquals(2, list.snapshot());

		list.add(7);
		list.add(8);
		assertEquals(3, list.snapshot());

		list.add(9);
		list.add(10);

		System.out.println(list.getListAtVersion(1));
		System.out.println(list.getListAtVersion(2));
		System.out.println(list.getListAtVersion(3));
		System.out.println(list);

		// in version 1 there shouldnt be index 5
		assertThrows(IllegalArgumentException.class, () -> {
			list.getAtVersion(5, 1);
		});

		// in version 2 there shouldnt be index 6
		assertThrows(IllegalArgumentException.class, () -> {
			list.getAtVersion(6, 2);
		});

		// in version 3 there shouldnt be index 8
		assertThrows(IllegalArgumentException.class, () -> {
			list.getAtVersion(8, 3);
		});

		// current version should be 4
		assertEquals(4, list.version());

		// the element insert at specified index is not supported
		assertThrows(UnsupportedOperationException.class, () -> {
			list.add(1, 1);
		});

		// the list insert at specified index is not supported
		assertThrows(UnsupportedOperationException.class, () -> {
			list.addAll(1, list.getListAtVersion(1));
		});

		// the element / index remove is not supported
		assertThrows(UnsupportedOperationException.class, () -> {
			list.remove(1);
		});
		
		//drop versions 1 and 2
		list.dropPriorSnapshots(3);
		
		//this will expect to throw illegalargumentexception when getting version 1 and 2
		assertThrows(IllegalArgumentException.class, () -> {
			list.getAtVersion(1, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			list.getAtVersion(2, 0);
		});
		

	}
	
	@Test
	void testOptimization() {
		
		//simulate different readings and access them based on importance.
		//TRUE - temperature is turned on
		//FALSE - temperature is turned off
		
		// create list of sensors
		SnapshotList<Boolean> sensors = new SynchronizedUnbalancedSnapshotList<Boolean>();
		
		//add first reading 
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.FALSE);
		
		//create first snapshot
		sensors.snapshot();
		
		//add second reading
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		
		//create second snapshot
		sensors.snapshot();
		
		//create third reading
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		
		//create third snapshot
		sensors.snapshot();
		
		//add last reading
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.TRUE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		sensors.add(Boolean.FALSE);
		
		//test: access 2nd snapshot
		SnapshotList<Boolean> secondSnapshot = sensors.getListAtVersion(2);
		assertTrue(sensors.containsAll(secondSnapshot));
		
		
		
	}
	
	@Test
	void testConcurrency() throws InterruptedException {
		SnapshotList<Integer> list = new SynchronizedUnbalancedSnapshotList<Integer>();
		Thread thread1 = new Thread(() -> {
			list.add(20);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});
		
		Thread thread2 = new Thread(() -> {
			list.snapshot();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});
		
		Thread thread3 = new Thread(() -> {
			list.add(30);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});
		
		list.add(1);
		list.add(2);
		list.add(3);
		
		list.add(4);
		
		thread1.run();
		thread3.run();
		thread2.run();
		
		
		list.add(5);
		list.snapshot();
		
		Iterator<Integer> itr = list.iterator(); 
        while (itr.hasNext()) { 
            Integer i = (Integer)itr.next();
            System.out.println(i); 
            Thread.sleep(1000); 
        }
        
		
	}

}
