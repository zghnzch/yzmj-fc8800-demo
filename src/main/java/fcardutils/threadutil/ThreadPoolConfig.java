package fcardutils.threadutil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
/**
 * @author caikaizhen
 * @Description:
 */
public class ThreadPoolConfig {
	/**
	 * CUP_POOL_SIZE
	 */
	private static final Integer CUP_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
	/**
	 * IO_POOL_SIZE
	 */
	private static final Integer IO_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
	private static final Integer CPU_MAX_MUM_POOL_SIZE = 10;
	private static final Integer IO_MAX_MUM_POOL_SIZE = 30;
	/**
	 * CPU密集型
	 * @return pool ExecutorService
	 */
	public ExecutorService buildCpuThreadPool() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("CPU-thread-%d").build();
		ExecutorService pool = new ThreadPoolExecutor(CUP_POOL_SIZE, CPU_MAX_MUM_POOL_SIZE, 300L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
	/**
	 * IO密集型
	 * @return pool ExecutorService
	 */
	public ExecutorService buildIoThreadPool() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("IO-thread-%d").build();
		ExecutorService pool = new ThreadPoolExecutor(IO_POOL_SIZE, IO_MAX_MUM_POOL_SIZE, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
	/**
	 * 优先级密集型
	 * @return pool ExecutorService
	 */
	public ExecutorService buildPriorityThreadPool() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Priority-thread-%d").build();
		ExecutorService pool = new ThreadPoolExecutor(CUP_POOL_SIZE, 10, 60L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		if (pool.isShutdown()) {
			return null;
		}
		return pool;
	}
}
