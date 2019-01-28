package learnspringbatch;

import javax.batch.runtime.StepExecution;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

// Listing 4-32
public class LoggingStepStartStopListener {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		System.out.println(stepExecution.getStepName() + " has begun!");
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println(stepExecution.getStepName() + " has ended!");

		return stepExecution.getExitStatus();
	}
}


