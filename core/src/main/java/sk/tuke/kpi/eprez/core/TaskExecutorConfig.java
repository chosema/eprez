package sk.tuke.kpi.eprez.core;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutorConfig.class);

	private static final int DEFAULT_CORE_POOL_SIZE = 2;
	private static final int DEFAULT_MAX_POOL_SIZE = 10;

	public @Bean ThreadPoolTaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

		LOGGER.info("Configuring taskExecutor with corePoolSize=" + DEFAULT_CORE_POOL_SIZE + " and maxPoolSize=" + DEFAULT_MAX_POOL_SIZE);
		taskExecutor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
		taskExecutor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
		return taskExecutor;
	}

	@Override
	public Executor getAsyncExecutor() {
		return taskExecutor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

}
