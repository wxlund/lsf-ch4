package learnspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Listing 4-39. Ending a Job in the Stopped State
// @EnableBatchProcessing
// @Configuration
class BatchConfigStoppedState { 

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet passTasklet() {
        LogicTasklet tasklet = new LogicTasklet();

        tasklet.setSuccess(true);

        return tasklet;
    }
/*
    @Bean
    public Tasklet passTasklet() {
        LogicTasklet tasklet = new LogicTasklet();

        tasklet.setSuccess(true);

        return tasklet;
    }
*/
	
    @Bean
    public Tasklet successTasklet() {
        MessageTasklet tasklet = new MessageTasklet();

        tasklet.setMessage("The step succeeded!");

        return tasklet;
    }

    @Bean
    public Job conditionalStepLogicJob() {
		System.out.println ("Listing 4-39. Ending a Job in the Stopped State");
        return this.jobBuilderFactory.get("conditionalStepLogicJob")
                                .start(step1())
                                .on("*").to(step2a())
                                .from(step1()).on("FAILED").stopAndRestart(step2a())
                                .end()
                                .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                        .tasklet(passTasklet())
                        .build();
    }

    @Bean
    public Step step2a() {
        return this.stepBuilderFactory.get("step2a")
                        .tasklet(successTasklet())
                        .build();
    }
}
