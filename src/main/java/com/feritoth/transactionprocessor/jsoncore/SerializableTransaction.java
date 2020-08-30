package com.feritoth.transactionprocessor.jsoncore;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SerializableTransaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String counterparty;
	private String clientPhoneNumber;
	private LocalDateTime executionTime;
	private Integer inputPosition;
	private Integer internalPosition;
	
	public SerializableTransaction() {
		super();
	}

	public SerializableTransaction(String name, String counterparty,
			           String clientPhoneNumber, LocalDateTime executionTime,
			           Integer inputPosition) {
		super();
		this.name = name;
		this.counterparty = counterparty;
		this.clientPhoneNumber = clientPhoneNumber;
		this.executionTime = executionTime;
		this.inputPosition = inputPosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((clientPhoneNumber == null) ? 0 : clientPhoneNumber
						.hashCode());
		result = prime * result
				+ ((counterparty == null) ? 0 : counterparty.hashCode());
		result = prime * result
				+ ((executionTime == null) ? 0 : executionTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializableTransaction other = (SerializableTransaction) obj;
		if (clientPhoneNumber == null) {
			if (other.clientPhoneNumber != null)
				return false;
		} else if (!clientPhoneNumber.equals(other.clientPhoneNumber))
			return false;
		if (counterparty == null) {
			if (other.counterparty != null)
				return false;
		} else if (!counterparty.equals(other.counterparty))
			return false;
		if (executionTime == null) {
			if (other.executionTime != null)
				return false;
		} else if (!executionTime.equals(other.executionTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCounterparty() {
		return counterparty;
	}

	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}

	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}

	public LocalDateTime getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(LocalDateTime executionTime) {
		this.executionTime = executionTime;
	}
	
	public Integer getInputPosition() {
		return inputPosition;
	}

	public void setInputPosition(Integer inputPosition) {
		this.inputPosition = inputPosition;
	}

	public Integer getInternalPosition() {
		return internalPosition;
	}

	public void setInternalPosition(Integer internalPosition) {
		this.internalPosition = internalPosition;
	}

	@Override
	public String toString() {
		return "Transaction [name=" + name + ", counterparty=" + counterparty + 
			   ", clientPhoneNumber=" + clientPhoneNumber + ", executionTime=" + executionTime + 
			   ", inputPosition=" + inputPosition + ", internalPosition=" + internalPosition + "]";
	}
}