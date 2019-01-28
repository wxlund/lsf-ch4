package learnspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Listing 4-20
// @EnableBatchProcessing
// @Configuration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job").start(step1()).build();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
				.tasklet(helloWorldTasklet())
				.build();
	}

	@Bean
	public Tasklet helloWorldTasklet() {
		return new HelloWorld();
	}

	public static class HelloWorld implements Tasklet {
		private static final String HELLO_WORLD = "Listing 4-20 Hello, %s";

		public RepeatStatus execute(StepContribution step, ChunkContext context) throws Exception {
			String name = (String) context.getStepContext().getJobParameters().get("name");

			ExecutionContext jobContext = context.getStepContext().getStepExecution().getExecutionContext();
			jobContext.put("name", name);

			System.out.println(String.format(HELLO_WORLD, name));

			return RepeatStatus.FINISHED;
		}


	}
}
