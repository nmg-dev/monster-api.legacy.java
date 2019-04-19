package com.fsn.nmg.monster.datamodel;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public abstract class AbstractModel<Parent extends AbstractModel, Child extends AbstractModel> {
	protected static final String ATTRIBUTE_ID = "id";
	
//	protected String _id;
	protected final TreeMap<String,String> _attrs = new TreeMap<String, String>();
	protected Parent _parent;
	protected TreeMap<String,Child> _children;
	
	public AbstractModel(String id) {
		_attrs.put(ATTRIBUTE_ID, id);
		for(String attKey : initiateAttributes()) {
			_attrs.put(attKey, null);
		};
	}
	
	/**
	 * 
	 */
	protected abstract String[] initiateAttributes();
	protected abstract Child createChild(String id);
	
	protected Parent getParent() {
		return _parent;
	}
	
	@SuppressWarnings("unchecked")
	protected Child addChild(String id) {
		if(_children == null)
			_children = new TreeMap<String,Child>();
		if(!_children.containsKey(id)) {
			Child ch = createChild(id);
			ch._parent = this;
			_children.put(id, ch);
		}
		return _children.get(id);
	}
	
	protected Set<Entry<String,Child>> items() {
		return _children.entrySet();
	}
	
	protected Set<String> childIds() {
		if(_children!=null)
			return _children.keySet();
		else
			return null;
	}
	
	protected Collection<Child> children() {
		if(_children != null)
			return _children.values();
		else
			return null;
	}
	
	protected boolean hasChild(String childId) {
		return (_children!=null && _children.containsKey(childId));
	}
	
	
	protected Child getChild(String childId) {
		if(_children!=null && _children.containsKey(childId)) {
			return _children.get(childId);
		} else {
			return null;
		}
	}
	
	
	public void setId(String id) {
		setAttribute(ATTRIBUTE_ID, id);
	}
	
	public String getId() {
		return getAttribute(ATTRIBUTE_ID);
	}
	 
	public boolean hasAttribute(String key) {
		return _attrs.containsKey(key);
	}
	
	public void setAttribute(String key, String value) {
		if(_attrs.containsKey(key)) {
			_attrs.put(key, value);
		}
	}
	
	public String getAttribute(String key) {
		return _attrs.get(key);
	}

}
