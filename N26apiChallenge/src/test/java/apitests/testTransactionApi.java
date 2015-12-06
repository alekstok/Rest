package apitests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import webservice.TransactionApi;

import data.TransactionsList;

public class testTransactionApi {

	private TransactionApi transactionApi = new TransactionApi();
	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {

		try {
			TransactionsList.addTransaction(1, 1000, "car", 10);
			TransactionsList.addTransaction(12, 7000, "car", 1);
			TransactionsList.addTransaction(3, 1000, "phone", 1);
			TransactionsList.addTransaction(8, 1000, "car", 12);
			TransactionsList.addTransaction(10, 5000, "TV");
			TransactionsList.addTransaction(11, 10000, "TV", 10);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testGetTransactionDetails() throws JsonProcessingException,
			IOException, JSONException {
		assertEquals(
				mapper.readTree("{\"amount\":1000,\"type\":\"car\",\"parent_id\":10 }"),
				mapper.readTree(((JSONObject) transactionApi
						.getTransactionDetails(1).getEntity()).toString()));

	}

	@Test
	public void testAddToListOfTransactionsWithoutParent() throws JSONException, JsonProcessingException, IOException {
		
		long transactionId = 7L;
		String requestBody = "{\"amount\":5000,\"type\":\"cars\"}";
		
		assertEquals(transactionApi.addTransaction(transactionId, requestBody).getStatus(), 200);
		
		assertEquals(mapper.readTree(((JSONObject) transactionApi
				.getTransactionDetails(transactionId).getEntity()).toString()).toString(),
				requestBody);
	}
	
	@Test
	public void testAddToListOfTransactionsWithParent() throws JSONException, JsonProcessingException, IOException {
		
		long transactionId = 7L;
		String requestBody = "{\"amount\":5000,\"type\":\"cars\",\"parent_id\":36}";
		
		assertEquals(transactionApi.addTransaction(transactionId, requestBody).getStatus(), 200);
		
		assertEquals(mapper.readTree(((JSONObject) transactionApi
				.getTransactionDetails(transactionId).getEntity()).toString()).toString(),
				requestBody);
	}

	@Test
	public void testSumOfTransactions() throws JsonProcessingException,
			IOException, JSONException {

		assertEquals(mapper.readTree("{\"sum\":25000}"), 
				mapper.readTree(((JSONObject) transactionApi
						.getSumOfTransactionsById(10).getEntity()).toString()));

		assertEquals(mapper.readTree("{\"sum\":10000}"),
				mapper.readTree(((JSONObject) transactionApi
						.getSumOfTransactionsById(11).getEntity()).toString()));

	}

	@Test
	public void testTransactionTypes() throws JsonProcessingException,
			IOException, JSONException {

		assertEquals(mapper.readTree("[1,8,12]"),
				mapper.readTree(((JSONArray) transactionApi
						.getListOfTransactionTypes("car").getEntity())
						.toString()));
	}
}
