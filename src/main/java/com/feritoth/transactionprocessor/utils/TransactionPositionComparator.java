package com.feritoth.transactionprocessor.utils;

import java.util.Comparator;

import com.feritoth.transactionprocessor.jsoncore.SerializableTransaction;

public class TransactionPositionComparator implements Comparator<SerializableTransaction> {

	@Override
	public int compare(SerializableTransaction o1, SerializableTransaction o2) {
		return o1.getInputPosition().compareTo(o2.getInputPosition());
	}

}