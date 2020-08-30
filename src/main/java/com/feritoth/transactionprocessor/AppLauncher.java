package com.feritoth.transactionprocessor;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feritoth.transactionprocessor.utils.TransactionProcessor;

public class AppLauncher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppLauncher.class);
	
	private AppLauncher() {
		super();
	}

	public static void main(String[] args) {		
		List<String> allInputFiles = Arrays.asList(args);
		TransactionProcessor testProcessor = new TransactionProcessor();
		String testResult = testProcessor.processInputFiles(allInputFiles);
		BasicConfigurator.configure();
		LOGGER.info("The test result is:\n" + testResult);
    }
}