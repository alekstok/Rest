package data;

public class Transaction {

	private double amount;
	private String type;
	private Long parent_id;
	
	public Transaction(double amount, String type, long parent_id){
		this.amount = amount;
		this.type = type;
		this.parent_id = parent_id;
	}
	
	public Transaction(double amount, String type){
		this.amount = amount;
		this.type = type;
		this.parent_id = null;
	}

	public double getAmount() {
		return amount;
	}

	public String getType() {
		return type;
	}

	public Long getParent_id() {
		return parent_id;
	}
	

}
