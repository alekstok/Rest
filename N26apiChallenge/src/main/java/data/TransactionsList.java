package data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import utils.DescendantsCheck;

public class TransactionsList {
	
	private static Map<Long, Transaction> transactionMap = new HashMap<Long, Transaction>();
	
	public static Map<Long, Transaction> getTransactionMap() {
		return transactionMap;
	}

	public static void addTransaction(long transactionId, double amount, String type, long parentId) throws Exception{
		
		if (isParentAChild(transactionId, parentId)){
			throw new Exception("Transaction's parent is already a transaction's child. \nCannot add such transaction.");
		}
		transactionMap.put(transactionId, new Transaction(amount, type, parentId));
		Parents.addChild(parentId , transactionId);
	}
	
	
	public static void addTransaction(long transactionId, double amount, String type){
		if(checkIfParentExist(transactionId)){
			removeChildFromParentsTable(transactionId);
		}
		transactionMap.put(transactionId, new Transaction(amount, type));	
	}

	

	public static Transaction getTransactionById(long transactionId){
		return getTransactionMap().get(transactionId);
	}
	
	private static boolean isParentAChild(long transactionId, long parentId) {
		Set<Long> allDescendantsIds = new DescendantsCheck().findAllDescendants(
				Parents.getChildrenMap(), transactionId);
		
		for (Long id : allDescendantsIds){
			if (id.equals(parentId)){
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean checkIfParentExist(long transactionId) {
		
		if (getTransactionById(transactionId) != null
				&& getTransactionById(transactionId).getParent_id() != null){
			return true;
		}	
		return false;
	}
	
	private static void removeChildFromParentsTable(long transactionId) {
		
		long parentId = getTransactionById(transactionId).getParent_id();
		Set<Long> children = Parents.getChildrenMap().get(parentId);
		children.remove(transactionId);
	}
}


