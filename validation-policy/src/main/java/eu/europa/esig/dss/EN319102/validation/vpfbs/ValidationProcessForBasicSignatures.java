package eu.europa.esig.dss.EN319102.validation.vpfbs;

import java.util.Map;

import eu.europa.esig.dss.EN319102.validation.Chain;
import eu.europa.esig.dss.EN319102.validation.ChainItem;
import eu.europa.esig.dss.EN319102.validation.vpfbs.checks.SignatureBasicBuildingBlocksCheck;
import eu.europa.esig.dss.EN319102.wrappers.DiagnosticData;
import eu.europa.esig.dss.EN319102.wrappers.SignatureWrapper;
import eu.europa.esig.dss.jaxb.detailedreport.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.jaxb.detailedreport.XmlValidationProcessBasicSignatures;

/**
 * 5.3 Validation process for Basic Signatures
 */
public class ValidationProcessForBasicSignatures extends Chain<XmlValidationProcessBasicSignatures> {

	private final DiagnosticData diagnosticData;
	private final SignatureWrapper signature;

	private final Map<String, XmlBasicBuildingBlocks> bbbs;

	public ValidationProcessForBasicSignatures(DiagnosticData diagnosticData, SignatureWrapper signature, Map<String, XmlBasicBuildingBlocks> bbbs) {
		super(new XmlValidationProcessBasicSignatures());

		this.diagnosticData = diagnosticData;
		this.signature = signature;
		this.bbbs = bbbs;
	}

	@Override
	protected void initChain() {
		firstItem = basicBuildingBlocks();
	}

	private ChainItem<XmlValidationProcessBasicSignatures> basicBuildingBlocks() {
		return new SignatureBasicBuildingBlocksCheck(result, diagnosticData, bbbs.get(signature.getId()), bbbs, getFailLevelConstraint());
	}

}
