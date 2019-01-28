package learnspringbatch;

import javax.annotation.Resource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

// Listing 4-29
// @EnableBatchProcessing
// @Configuration

class BatchConfigTimeoutWithCommitCount {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public FlatFileItemReader<String> itemReader(
	@Value("#{jobParameters['inputFile']}") org.springframework.core.io.Resource inputFile) {
		return new FlatFileItemReaderBuilder<String>()
		.name("itemReader")
		.resource(inputFile)
		.lineMapper(new PassThroughLineMapper())
		.build();
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<String> itemWriter(
	@Value("#{jobParameters['outputFile']}") org.springframework.core.io.Resource outputFile) {
		return new FlatFileItemWriterBuilder<String>()
		.name("itemWriter")
		.resource(outputFile)
		.lineAggregator(new PassThroughLineAggregator<>())
		.build();
	}

	@Bean
	public CompletionPolicy completionPolicy() {
		CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

		policy.setPolicies(
			new TimeoutTerminationPolicy(3),
			new SimpleCompletionPolicy(200)
		);

		return policy;
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
		.start(copyStep())
		.build();
	}

	@Bean
	public Step copyStep() {
		return this.stepBuilderFactory.get("copyStep")
		.<String, String>chunk(completionPolicy())
		.reader(itemReader(null))
		.writer(itemWriter(null))
		.build();
	}
}
