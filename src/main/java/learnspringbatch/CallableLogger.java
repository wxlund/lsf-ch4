package learnspringbatch;

import java.util.concurrent.Callable;
import org.springframework.batch.repeat.RepeatStatus;

// Code copied from Pro Spring Batch for Listing 4-22
class CallableLogger implements Callable<RepeatStatus> {
	public RepeatStatus call() throws Exception {
		System.out.println("Listing 4-22 This was executed in another thread");

		return RepeatStatus.FINISHED;
	}
}
