package data;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Parents {
	
	private static Map<Long, Set<Long>> childrenParentsMap = new HashMap<Long, Set<Long>>();
		
	public static Map<Long, Set<Long>> getChildrenMap() {
		return childrenParentsMap;
	}
	
	public static void addChild(long parentId, long childId){
		Set<Long> children ;
			
		if (childrenParentsMap.containsKey(parentId)){
			children = childrenParentsMap.get(parentId);
		}
		else {
			children = new LinkedHashSet<>();		
		}
		children.add(childId);
		childrenParentsMap.put(parentId, children);
	}
}
