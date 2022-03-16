package telran.util.concurrent;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class MyListBlockingQueue<E> implements BlockingQueue<E> {
	
	private Lock monitor = new ReentrantLock();
	private Condition consumerWaitingCondition = monitor.newCondition();
	private Condition producerWaitingCondition = monitor.newCondition();
	private LinkedList<E> queue = new LinkedList<>();
	private int capacityLimit;

	public MyListBlockingQueue(int capacityLimit) {
		this.capacityLimit = capacityLimit;
	}

	

	@Override
	public E remove() {

		try {
			monitor.lock();
			E result = queue.remove();
			producerWaitingCondition.signal();
			return result;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E poll() {
		try {
			monitor.lock();
			E result = queue.poll();
			if (result != null) {
				producerWaitingCondition.signal();
			}
			return result;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E element() {
		try {
			monitor.lock();
			return queue.element();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E peek() {
		try {
			monitor.lock();
			return queue.peek();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public int size() {
		try {
			monitor.lock();

			return queue.size();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			monitor.lock();
			return queue.isEmpty();
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		
		return null;
	}

	@Override
	public Object[] toArray() {
		
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public void clear() {
		monitor.lock();
		try {
			queue.clear();
			producerWaitingCondition.signal();
		} finally {
			monitor.unlock();
		}

	}

	@Override
	public boolean add(E e) {
		try {
			monitor.lock();
			boolean res = queue.add(e);
			consumerWaitingCondition.signal();
			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean offer(E e) {
		try {
			monitor.lock();
			boolean res = queue.offer(e);
			if (res) {
				consumerWaitingCondition.signal();
			}
			return res;
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public void put(E e) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.size() == capacityLimit) {
				producerWaitingCondition.await();
			}
			queue.add(e);
			consumerWaitingCondition.signal();
			
		} finally {
			monitor.unlock();
		}

	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.size() == capacityLimit) {
				if(!producerWaitingCondition.await(timeout, unit)) {
					return false;
				}
			}
			queue.add(e);
			consumerWaitingCondition.signal();
			return true;
			
		} finally {
			monitor.unlock();
		}

	}

	@Override
	public E take() throws InterruptedException {
		try {
			monitor.lock();
			while (queue.isEmpty()) {
				consumerWaitingCondition.await();
			}
			E res = queue.poll();
			producerWaitingCondition.signal();
			return res;
			
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		try {
			monitor.lock();
			while (queue.isEmpty()) {
				if(!consumerWaitingCondition.await(timeout, unit)) {
					return null;
				}
			}
			E res = queue.poll();
			producerWaitingCondition.signal();
			return res;
			
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public int remainingCapacity() {
		
		return 0;
	}

	@Override
	public boolean remove(Object o) {
		try {
			monitor.lock();
			
			boolean res = queue.remove(o);
			if (res) {
				producerWaitingCondition.signal();
			}
			
			return res;
			
		} finally {
			monitor.unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		
		return false;
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		
		return 0;
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		
		return 0;
	}

}
