package com.co.services.sample.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
//kevault imports
import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.DefaultAzureCredentialBuilder;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.DeletedSecret;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

import javax.sql.DataSource;

@Configuration
public class Config {
     @Autowired
    private Environment environment;

    private final Logger log = LoggerFactory.getLogger(Config.class);

     


    // MS SQL Datasource
    @Primary
    @Bean("mainDataSource")
    public DataSource mainDataSource() {
    	//start of keyvault
    	String keyVaultName = System.getenv("KEY_VAULT_NAME");
        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

        System.out.printf("key vault name = %s and key vault URI = %s \n", keyVaultName, keyVaultUri);

        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl(keyVaultUri)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();

        Console con = System.console();  

        String secretName = "mySecret";

        System.out.println("Please provide the value of your secret > ");
        
        String secretValue = con.readLine();

        System.out.print("Creating a secret in " + keyVaultName + " called '" + secretName + "' with value '" + secretValue + "` ... ");

        secretClient.setSecret(new KeyVaultSecret(secretName, secretValue));

        System.out.println("done.");
        System.out.println("Forgetting your secret.");
        
        secretValue = "";
        System.out.println("Your secret's value is '" + secretValue + "'.");

        System.out.println("Retrieving your secret from " + keyVaultName + ".");

        KeyVaultSecret retrievedSecret = secretClient.getSecret(secretName);

        System.out.println("Your secret's value is '" + retrievedSecret.getValue() + "'.");
        System.out.print("Deleting your secret from " + keyVaultName + " ... ");

        SyncPoller<DeletedSecret, Void> deletionPoller = secretClient.beginDeleteSecret(secretName);
        deletionPoller.waitForCompletion();

        System.out.println("done.");
         //end of keyvault
        String serverName = environment.getProperty("environment.servername");
        String port = environment.getProperty("environment.dbport");
        String databaseName = environment.getProperty("environment.dbname");
        String userName = environment.getProperty("environment.username");
        String password = environment.getProperty("environment.password");
        String envSample = environment.getProperty("environment.username");
        log.info("********************************* logging env var from yml");
        log.info(envSample);
        log.info("********************************* Creating mainDataSource");
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
        String url = "jdbc:microsoft:sqlserver://" + serverName + ":" + port + "?useServerPrepStmts=false&rewriteBatchedStatements=true&jdbcCompliantTruncation=false";
        config.setJdbcUrl(url);
        config.addDataSourceProperty("serverName", serverName);
        config.addDataSourceProperty("portNumber", port);
        config.addDataSourceProperty("databaseName", databaseName);

        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(30);

        // Default is 60 seconds
        config.setConnectionTimeout(60*1000);

        try {
            HikariDataSource ds = new HikariDataSource(config);
            return ds;
        } catch (Exception ex) {
            log.info("********************************* Create mainDataSource FAILED {}", ex);
            return new HikariDataSource();
        }
    }

    @Bean("mainJdbcTemplate")
    public JdbcTemplate mainJdbcTemplate() {
        return new JdbcTemplate(mainDataSource());
    }
}
