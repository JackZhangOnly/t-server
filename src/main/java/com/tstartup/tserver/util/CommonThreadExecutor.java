package com.tstartup.tserver.util;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class CommonThreadExecutor implements Executor {

	/**
	 * 执行器实例
	 */
    private static volatile Executor mExecutor;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();


    private static final int                     CORE_POOL_SIZE  = Math.max(8, Math.min(CPU_COUNT - 1, 8));;
    private static final int                     MAX_POOL_SIZE   = 20;
    private static final int                     KEEP_ALIVE_TIME = 120;
    private static final TimeUnit                TIME_UNIT       = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE      = new LinkedBlockingQueue<Runnable>();

    private static ThreadPoolExecutor mThreadPoolExecutor;

    private CommonThreadExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger num = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                int threadNum = num.incrementAndGet();
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("CommonThreadExecutor-thread-" + threadNum);
                return thread;
            }
        };

        mThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE * 2,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TIME_UNIT,
                WORK_QUEUE,
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void execute(final Runnable runnable) {
        mThreadPoolExecutor.submit(runnable);
    }

    public static Executor getInstance() {
        if (mExecutor == null) {
            mExecutor = new CommonThreadExecutor();
        }
        return mExecutor;
    }

}
