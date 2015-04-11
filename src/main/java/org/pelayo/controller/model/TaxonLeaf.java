package org.pelayo.controller.model;

public class TaxonLeaf {
	
	private String code;
	private String nextLevel;
	
	public TaxonLeaf(String code, String nextLevel){
		this.code = code;
		this.nextLevel = nextLevel;
	}

	public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}
	public String getnextLevel() {
		return nextLevel;
	}

	public void setnextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}
}

