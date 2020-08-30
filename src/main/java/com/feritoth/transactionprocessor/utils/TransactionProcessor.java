package com.feritoth.transactionprocessor.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feritoth.transactionprocessor.jsoncore.SerializableTransaction;

public class TransactionProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessor.class);
	
	public static final Integer DEFAULT_POSITION = 1;
	public static final String SPECIAL_COUNTERPARTY = "Netflix";

	/**
	 * The main file processor method.
	 * 
	 * @param locations - the locations of the file in question
	 */
	public String processInputFiles(List<String> locations){
		//Read the input first
		List<String> fileInput = readFileContent(locations);
		//Process the tokens resulting from the input read
		Map<String, List<SerializableTransaction>> mappedTransactions = processInputTokens(fileInput);
		//Take the given map and unify the values
		List<SerializableTransaction> allTransactions = new ArrayList<>();
		for (String counterparty : mappedTransactions.keySet()) {
			allTransactions.addAll(mappedTransactions.get(counterparty));
		}
		//Sort out the given collection based on the position comparator
		Collections.sort(allTransactions, new TransactionPositionComparator());
		//Now go for the String build part to be returned
		String finalResult = createFinalOutput(allTransactions);
		return finalResult;
	}
	
	/**
	 * The following method will respond for the final conversion of the 
	 * given collection into the desired format.
	 * 
	 * @param allTransactions - the collection of transactions to be processed
	 * 
	 * @return the String representation of the given collection
	 */
	private String createFinalOutput(List<SerializableTransaction> allTransactions) {
		StringBuilder sb = new StringBuilder();
		for (SerializableTransaction transaction : allTransactions){
			String finalTransactionContent = null;
			String counterparty = transaction.getCounterparty();
			switch(counterparty){
			case SPECIAL_COUNTERPARTY:
				String transactionIndex = transaction.getInternalPosition() < 10 ? "0" + transaction.getInternalPosition() : transaction.getInternalPosition().toString();
				finalTransactionContent = counterparty + "|" + transactionIndex + "|" + transaction.getName();
				break;
			default:
				finalTransactionContent = counterparty + "|" + transaction.getInternalPosition() + "|" + transaction.getName();
				break;
			}
			sb.append(finalTransactionContent + "\n");
		}
		return sb.toString();
	}

	/**
	 * The transaction token processor method.
	 * 
	 * @param rawTransactionList - the raw transaction list
	 * 
	 * @return the map of transactions 
	 */
	private Map<String, List<SerializableTransaction>> processInputTokens(List<String> rawTransactionList) {
		Map<String, List<SerializableTransaction>> mappedTransactions = new HashMap<>();
		for (String rawTransaction : rawTransactionList) {
			//split the transaction token based on the main delimiter first
			String[] mainTransactionTokens = rawTransaction.split("/");
			//split next the obtained tokens into two parts based on the secondary delimiter - this is for the identification of the 
			//transaction attributes
			String[] secondaryTokensA = null;
			if (mainTransactionTokens[0].startsWith("\"")) {
				secondaryTokensA = splitDifferently(mainTransactionTokens[0]);
			} else {
				secondaryTokensA = mainTransactionTokens[0].split(",");
			}
			String[] secondaryTokensB = mainTransactionTokens[1].split(",");
			SerializableTransaction newTransaction = new SerializableTransaction(secondaryTokensA[0].trim(), secondaryTokensA[1].trim(), 
					                                     secondaryTokensB[0].trim(), convertTokenToLocalDateTime(secondaryTokensB[1].trim()), 
					                                     Integer.valueOf(rawTransactionList.indexOf(rawTransaction)));
			//retain the name of the involved counterparty
			String transactionCounterparty = secondaryTokensA[1].trim();			
			//check if the given counterparty is contained or not in the map and proceed accordingly
			if (!mappedTransactions.containsKey(transactionCounterparty)) {
				//new counterparty, no sorting or position rearrangement will be required for now
				List<SerializableTransaction> newTransactions = new ArrayList<SerializableTransaction>();
				newTransaction.setInternalPosition(DEFAULT_POSITION);
				newTransactions.add(newTransaction);
				mappedTransactions.put(transactionCounterparty, newTransactions);
			} else {
				//existing counterparty, sort of existing list and position rearrangement required after incorporating the new record
				List<SerializableTransaction> existingTransactions = mappedTransactions.get(transactionCounterparty);
				existingTransactions.add(newTransaction);
				Collections.sort(existingTransactions, new TransactionTimeComparator());
				for (SerializableTransaction t : existingTransactions) {
					t.setInternalPosition(existingTransactions.indexOf(t) + 1);
				}
				mappedTransactions.put(transactionCounterparty, existingTransactions);
			}
		}
		return mappedTransactions;
	}

	/**
	 * The file reader method - makes use of the new Java NIO features.
	 * 
	 * @param locations - the location of the input file
	 * 
	 * @return the content of all the input files, merged into one list
	 */
	private List<String> readFileContent(List<String> locations) {
		try {
			List<String> jointContent = new ArrayList<>();
			for (String location : locations) {
				jointContent.addAll(Files.readAllLines(Paths.get(location)));				
			}
		    return jointContent;
		} catch (IOException e) {
			LOGGER.error("The following exception has been detected for the current attempt:" + e.getMessage());
			throw new RuntimeException("The following exception has been detected for the current attempt:" + e.getMessage());
		}
	}
	
	/**
	 * The date time parser part.
	 * 
	 * @param dateTimeToken - the date and time to be parsed
	 * 
	 * @return the formatted date and time
	 */
	private LocalDateTime convertTokenToLocalDateTime(String dateTimeToken) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(dateTimeToken, dtf);
	}
	
	/**
	 * A method for checking if in the transaction name we have any quotation marks
	 * 
	 * @param tokenString - the token to be examined
	 * 
	 * @return the token array
	 */
	private String[] splitDifferently(String tokenString) {
		//take out the zone delimited by the quotation marks and replace it appropriately
		String firstToken = tokenString.substring(tokenString.indexOf("\"") + 1, tokenString.lastIndexOf("\""));
		tokenString = tokenString.replace(firstToken, "");
		//cut the remaining token into pieces
		String lastToken = tokenString.substring(tokenString.lastIndexOf(",") + 1).trim();
		//return the final result as follows
		String[] finalTokens = new String[2];
		finalTokens[0] = firstToken;
		finalTokens[1] = lastToken;
		return finalTokens;
	}

}