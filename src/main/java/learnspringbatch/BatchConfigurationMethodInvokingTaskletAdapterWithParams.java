package learnspringbatch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Listing 4-24
// @EnableBatchProcessing
// @Configuration
public class BatchConfigurationMethodInvokingTaskletAdapterWithParams {

        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public CustomService service() {
                return new CustomService();
        }

        @Bean
        public MethodInvokingTaskletAdapter tasklet(
                        @Value("#{jobParameters[message]}") Object[] message) {
                MethodInvokingTaskletAdapter tasklet = new MethodInvokingTaskletAdapter();

                tasklet.setTargetObject(service());
                tasklet.setTargetMethod("serviceMethod");
                tasklet.setArguments(message);

                return tasklet;
        }

        @Bean
        public Job methodInvokingJob() {
                return (Job) this.jobBuilderFactory.get("methodInvokingJob")
                                        .start(step1())
                                        .build();
        }

        @Bean
        public Step step1() {
                return this.stepBuilderFactory.get("step1")
                                        .tasklet(tasklet(null))
                                        .build();
        }
}

