package utils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DescendantsCheck {
	
	public Set<Long> findAllDescendants(
			Map<Long, Set<Long>> childrenParentsMap, Long transactionId) {

		Set<Long> directDescendants = new LinkedHashSet<>();
		if(childrenParentsMap.get(transactionId) == null){
			return directDescendants;
		}
		directDescendants.addAll(childrenParentsMap.get(transactionId));
		
		Set<Long> indirectDescendants = new LinkedHashSet<>();

		for (Long child : directDescendants) {
			if (childrenParentsMap.get(child) != null) {
				indirectDescendants.addAll(findAllDescendants(
						childrenParentsMap, child));
			}
		}
		indirectDescendants.addAll(directDescendants);

		return indirectDescendants;
	}

}
