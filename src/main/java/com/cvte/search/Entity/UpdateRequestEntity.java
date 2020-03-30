package com.cvte.search.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpdateRequestEntity implements Serializable {
	
	private static final long serialVersionUID = -738830380370226045L;

	private Map<String,Object> fieldMap =new HashMap<>();

    private String docId;

    private String index;


	public void setFieldMap(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}


	public void setDocId(String docId) {
		this.docId = docId;
	}


    public UpdateRequestEntity setIndex(String index){
        this.index=index;
        return this;
    }


    public String getIndex() {
        return index;
    }

    public String getDocId() {
        return docId;
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }
}
