package fcardutils.threadutil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
/**
 * @author huangyu
 * @date 15/9/10
 */
public class ThreadPoolUtils {
	private static int maxPoolSize = 10000;
	/**
	 * Create a thread factory that names threads with a prefix and also sets the threads to daemon.
	 */
	public static ThreadFactory namedThreadFactory(String prefix) {
		return new ThreadFactoryBuilder().setDaemon(true).setNameFormat(prefix + "-%d").build();
	}
	/**
	 * Create a cached thread pool whose max number of threads is `maxThreadNumber`. Thread names
	 * are formatted as prefix-ID, where ID is a unique, sequentially assigned integer.
	 */
	public static ThreadPoolExecutor newDaemonCachedThreadPool(String prefix, int maxThreadNumber) {
		ThreadFactory threadFactory = namedThreadFactory(prefix);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(0, maxThreadNumber, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
		if (pool.getMaximumPoolSize() >= maxPoolSize) {
			return null;
		}
		return pool;
	}
	/**
	 * Wrapper over newFixedThreadPool. Thread names are formatted as prefix-ID, where ID is a
	 * unique, sequentially assigned integer.
	 */
	public static ThreadPoolExecutor newDaemonFixedThreadPool(int nThreads, String prefix) {
		ThreadFactory threadFactory = namedThreadFactory(prefix);
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads, threadFactory);
		if (pool.getMaximumPoolSize() >= maxPoolSize) {
			return null;
		}
		return pool;
	}
	/**
	 * Wrapper over newSingleThreadExecutor.
	 */
	public static ExecutorService newDaemonSingleThreadExecutor(String threadName) {
		ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
		ExecutorService pool = Executors.newSingleThreadExecutor(threadFactory);
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
	/**
	 * Wrapper over newSingleThreadExecutor.
	 */
	public static ExecutorService newDaemonMultipleThreadExecutor(String threadName) {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(String.format("demo-pool-%s", threadName)).build();
		//Common Thread Pool
		ExecutorService pool = new ThreadPoolExecutor(50, 800, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		//  pool.execute(()-> System.out.println(Thread.currentThread().getName()));
		//gracefully shutdown
		//  关闭 pool.shutdown()
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
	/**
	 * Wrapper over newSingleThreadScheduledExecutor.
	 */
	public static ScheduledExecutorService newDaemonSingleThreadScheduledExecutor(String threadName) {
		ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
		ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor(threadFactory);
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
	/**
	 * Wrapper over newCachedThreadPool. Thread names are formatted as prefix-ID, where ID is a
	 * unique, sequentially assigned integer.
	 */
	public ThreadPoolExecutor newDaemonCachedThreadPool(String prefix) {
		ThreadFactory threadFactory = namedThreadFactory(prefix);
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool(threadFactory);
		if (pool.getMaximumPoolSize() >= maxPoolSize) {
			return null;
		}
		return pool;
	}
}
