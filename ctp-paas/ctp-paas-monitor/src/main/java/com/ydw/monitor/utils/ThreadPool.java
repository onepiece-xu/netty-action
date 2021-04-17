package com.ydw.monitor.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 维护一个多线程类
 * @author xulh
 *
 */
@Component
public class ThreadPool {

	@Value("${threadpool.coreNum}")
	private Integer coreNum;

	@Value("${threadpool.maxNum}")
	private Integer maxNum;

	private ThreadPoolExecutor pool;

	@PostConstruct
	public void init(){
        pool = new ThreadPoolExecutor(coreNum, maxNum, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	}

	public void submit(Runnable runnable){
		pool.submit(runnable);
	}

	public void execute(Runnable runnable){
		pool.execute(runnable);
	}

	public <V> Future<V> submit(Callable<V> call){
		return pool.submit(call);
	}
}
