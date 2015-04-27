package org.pelayo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pelayo.controller.model.TaxonBranch;
import org.pelayo.controller.model.TaxonLeaf;
import org.pelayo.dao.TaxonesTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxonesTreeController {

	@Autowired
	private TaxonesTreeRepository taxonesTreeRepository;

	@RequestMapping("/listTaxonTreeAll")
	public List<TaxonBranch> listTaxonTreeAll() {
		return getChildren("grupo");
	}

	private List<TaxonBranch> getChildren(String level) {
		return getChildren("grupo", null);
	}

	private List<TaxonBranch> getChildren(String level, String parent) {
		List<TaxonLeaf> leaves;
		String prefix = "";
		switch (level) {
		case "grupo":
			prefix = "Grup. ";
			leaves = taxonesTreeRepository.taxonLeafsByGroup();
			break;
		case "family":
			prefix = "Fam. ";
			leaves = taxonesTreeRepository.taxonLeafsByFamily(parent);
			break;
		case "subfamily":
			prefix = "Subf. ";
			leaves = taxonesTreeRepository.taxonLeafsBySubfamily(parent);
			break;
		case "tribu":
			prefix = "Trib. ";
			leaves = taxonesTreeRepository.taxonLeafsByTribu(parent);
			break;
		case "genus":
			prefix = "Gén. ";
			leaves = taxonesTreeRepository.taxonLeafsByGenus(parent);
			break;
		case "species":
			prefix = "Espec. ";
			leaves = taxonesTreeRepository.taxonLeafsBySpecy(parent);
			break;
		default:
			leaves = Collections.emptyList();
		}

		List<TaxonBranch> list = new ArrayList<TaxonBranch>();
		for (TaxonLeaf tl : leaves) {
			TaxonBranch tb = new TaxonBranch();

			tb.setInfo(prefix);
			tb.setLabel(tl.getcode());
			tb.setChildren(getChildren(tl.getnextLevel(), tl.getcode()));

			list.add(tb);
		}

		return list;
	}

	@RequestMapping("/listTaxonTreeByGrupo")
	public List<TaxonLeaf> listTaxonTreeByGroup() {
		return taxonesTreeRepository.taxonLeafsByGroup();
	}

	@RequestMapping("/listTaxonTreeByFamily")
	public List<TaxonLeaf> listTaxonTreeByFamily(@RequestParam(value = "parent", required = true) String parent) {
		return taxonesTreeRepository.taxonLeafsByFamily(parent);
	}
	
	@RequestMapping("/listTaxonTreeBySubfamily")
	public List<TaxonLeaf> listTaxonTreeBySubfamily(@RequestParam(value = "parent", required = true) String parent) {
		return taxonesTreeRepository.taxonLeafsBySubfamily(parent);
	}

	@RequestMapping("/listTaxonTreeByTribu")
	public List<TaxonLeaf> listTaxonTreeByTribu(@RequestParam(value = "parent", required = true) String parent) {
		return taxonesTreeRepository.taxonLeafsByTribu(parent);
	}

	@RequestMapping("/listTaxonTreeByGenus")
	public List<TaxonLeaf> listTaxonTreeByGenus(@RequestParam(value = "parent", required = true) String parent) {
		return taxonesTreeRepository.taxonLeafsByGenus(parent);
	}

	@RequestMapping("/listTaxonTreeBySpecies")
	public List<TaxonLeaf> listTaxonTreeBySpecy(@RequestParam(value = "parent", required = true) String parent) {
		return taxonesTreeRepository.taxonLeafsBySpecy(parent);
	}
}
