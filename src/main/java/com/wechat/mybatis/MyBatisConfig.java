package com.wechat.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * MyBatis配置文件
 * @author cpr
 * @since 2016-04-08 21:50
 */

@Configuration
@ConfigurationProperties(prefix="mybatis")
public class MyBatisConfig {

	private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);

	@Autowired
    private DataSource dataSource;
    
    private String typeAliasesPackage;
	
    private String mapperLocations;
    
    private String configLocation;

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	@Bean(name = "sqlSessionFactory")
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory() {
		try {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource);
			sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
			sessionFactory
					.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
			sessionFactory
					.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));

			return sessionFactory.getObject();
		} catch (Exception e) {
			logger.warn("Could not confiure mybatis session factory");
			return null;
		}
	}

}
