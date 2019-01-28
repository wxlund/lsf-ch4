package learnspringbatch;

import java.util.Random;

import javax.batch.runtime.JobExecution;
import javax.batch.runtime.StepExecution;

import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

// Listing 4-35. RandomDecider
public class RandomDecider implements JobExecutionDecider {

	private Random random = new Random();

	public FlowExecutionStatus decide(JobExecution jobExecution,
			StepExecution stepExecution) {

		if (random.nextBoolean()) {
			return new
					FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName());
		} else {
			return new
					FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
		}
	}

	@Override
	public FlowExecutionStatus decide(org.springframework.batch.core.JobExecution jobExecution,
			org.springframework.batch.core.StepExecution stepExecution) {
		if (random.nextBoolean()) {
			return new
					FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName());
		} else {
			return new
					FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
		}
	}
}


