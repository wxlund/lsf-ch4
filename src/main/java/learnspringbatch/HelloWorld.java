package learnspringbatch;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@EnableBatchProcessing
@SpringBootApplication
public class HelloWorld {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public CompositeJobParametersValidator validator() {
		CompositeJobParametersValidator validator =
				new CompositeJobParametersValidator();

		DefaultJobParametersValidator defaultJobParametersValidator =
				new DefaultJobParametersValidator(
						new String[] {"fileName"},
						new String[] {"name", "run.id", "currentDate", "executionDate", "-spring.output.ansi.enabled" });

		defaultJobParametersValidator.afterPropertiesSet();

		validator.setValidators(
				Arrays.asList(new ParameterValidator(), 
					defaultJobParametersValidator));

		return validator;
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("basicJob")
				.start(step1())
				.validator(validator())
				.incrementer(new DailyJobTimestamper())
				.listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
				.build();
	} 


	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
				.tasklet(helloWorldTasklet(null, null))
				.build();
	}

	@StepScope
	@Bean
	public Tasklet helloWorldTasklet(
			@Value("#{jobParameters['name']}") String name,
			@Value("#{jobParameters['fileName']}") String fileName) {

		return (contribution, chunkContext) -> {
			
				System.out.println(
						String.format("Hello, %s!", name));
				System.out.println(
						String.format("fileName = %s", fileName));
				
				return RepeatStatus.FINISHED;
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorld.class, args);
	}
}

