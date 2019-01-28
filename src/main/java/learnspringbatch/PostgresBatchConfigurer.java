package learnspringbatch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

// @EnableBatchProcessing
// @Component
class PostgresBatchConfigurer extends DefaultBatchConfigurer {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private PlatformTransactionManager transactionManager;

	public PostgresBatchConfigurer() {
		super();
	}

	public PostgresBatchConfigurer(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected JobRepository createJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.setDatabaseType("POSTGRES");
		factory.afterPropertiesSet();
		return factory.getObject();
	}
}

