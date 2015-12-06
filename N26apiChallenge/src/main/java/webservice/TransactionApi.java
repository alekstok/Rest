package webservice;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utils.DescendantsCheck;

import data.Parents;
import data.TransactionsList;

@Path("/transactionservice")
public class TransactionApi {

	@GET
	@Path("/transaction/{transactionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransactionDetails(
			@PathParam("transactionId") long transactionId)
			throws JSONException {

		if (TransactionsList.getTransactionById(transactionId) == null) {
			return Response.status(200).entity("{}").build();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("amount",
				TransactionsList.getTransactionById(transactionId).getAmount());
		jsonObject.put("type",
				TransactionsList.getTransactionById(transactionId).getType());
		
		if(TransactionsList.getTransactionById(transactionId).getParent_id() != null){
			jsonObject.put("parent_id",
					TransactionsList.getTransactionById(transactionId)
							.getParent_id());
		}

		return Response.status(200).entity(jsonObject).build();
	}

	@GET
	@Path("/types/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfTransactionTypes(@PathParam("type") String type)
			throws JSONException {

		JSONArray jsonArray = new JSONArray();

		for (long key : TransactionsList.getTransactionMap().keySet()) {
			String temporaryType = TransactionsList.getTransactionMap()
					.get(key).getType();

			if (temporaryType.equalsIgnoreCase(type)) {
				jsonArray.put(key);
			}
		}

		return Response.status(200).entity(jsonArray).build();
	}

	@GET
	@Path("/sum/{transactionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSumOfTransactionsById(
			@PathParam("transactionId") long transactionId)
			throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sum", calculateSumOftransactions(transactionId));

		return Response.status(200).entity(jsonObject).build();
	}

	@PUT
	@Path("/transaction/{transactionId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTransaction(
			@PathParam("transactionId") long transactionId, String jsonUserInput)
			throws JSONException {

		String requestResult = "OK";
		JSONObject jsonInput;
		double amount;
		String type;
		long parentId;
		
		try {
			jsonInput = new JSONObject(jsonUserInput);
		} catch (JSONException jsonEx) {
			return Response.status(404).entity(jsonEx.getMessage()).build();
		}

		try {
			amount = Double.parseDouble(jsonInput.get("amount").toString());
			type = jsonInput.get("type").toString();

			TransactionsList.addTransaction(transactionId, amount, type);

			if (jsonInput.has("parent_id")) {
				parentId = Long
						.parseLong(jsonInput.get("parent_id").toString());
				TransactionsList.addTransaction(transactionId, amount, type,
						parentId);
			}
		} catch (NumberFormatException e) {
			requestResult = "Transaction not added. Invalid type of argument";
			;
		} catch (Exception parentChildException) {
			requestResult = parentChildException.getMessage();
		}

		JSONObject jsonOutput = new JSONObject();
		jsonOutput.put("status", requestResult);

		return Response.status(200).entity(jsonOutput).build();
	}

	private double calculateSumOftransactions(long transactionId) {

		if (TransactionsList.getTransactionById(transactionId) == null) {
			return 0;
		}

		Map<Long, Set<Long>> childrenParentsMap = Parents.getChildrenMap();
		double sumAmount = 0;
		Set<Long> allDescendantsIds = new LinkedHashSet<>();

		if (childrenParentsMap.get(transactionId) != null) {
			allDescendantsIds = new DescendantsCheck().findAllDescendants(
					childrenParentsMap, transactionId);
			for (Long sum : allDescendantsIds) {
				sumAmount += TransactionsList.getTransactionById(sum)
						.getAmount();
			}
		}
		sumAmount += TransactionsList.getTransactionById(transactionId)
				.getAmount();

		return sumAmount;
	}
}
