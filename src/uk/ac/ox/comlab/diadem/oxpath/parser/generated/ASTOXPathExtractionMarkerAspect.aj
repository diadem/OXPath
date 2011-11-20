/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.core.PositionFuncEnum;
import uk.ac.ox.comlab.diadem.oxpath.model.language.OXPathExtractionMarker;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathExtractionMarker;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTOXPathExtractionMarkerAspect {

	public void ASTOXPathExtractionMarker.setExtractionMarker(OXPathExtractionMarker iMarker) {
		this.marker = iMarker;
	}

	public OXPathExtractionMarker ASTOXPathExtractionMarker.getExtractionMarker() {
		return this.marker;
	}

	public void ASTOXPathExtractionMarker.setHasList(boolean list) {
		this.hasList = list;
	}

	public boolean ASTOXPathExtractionMarker.hasList() {
		return this.hasList;
	}

	private OXPathExtractionMarker ASTOXPathExtractionMarker.marker;
	private boolean ASTOXPathExtractionMarker.hasList = false;

	public void ASTOXPathExtractionMarker.setSetBasedEval(PositionFuncEnum set) {
		this.setEval = set;
	}

	public PositionFuncEnum ASTOXPathExtractionMarker.getSetBasedEval() {
		return this.setEval;
	}

	private PositionFuncEnum ASTOXPathExtractionMarker.setEval = PositionFuncEnum.NEITHER;

	public String ASTOXPathExtractionMarker.toString() {
		return this.getClass().getSimpleName() + "[label=" + this.getExtractionMarker().getLabel() + ",isAttribute=" + this.getExtractionMarker().isAttribute() + ",setEval=" + this.setEval + ",hasList=" + this.hasList + "]";
	}
}
