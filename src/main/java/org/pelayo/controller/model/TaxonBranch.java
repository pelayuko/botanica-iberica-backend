package org.pelayo.controller.model;

import java.util.List;

public class TaxonBranch {

	private String label;
	private List<TaxonBranch> children;
	private String info;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<TaxonBranch> getChildren() {
		return children;
	}

	public void setChildren(List<TaxonBranch> children) {
		this.children = children;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
