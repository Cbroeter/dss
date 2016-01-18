package eu.europa.esig.dss.EN319102.validation.vpfltvd.checks;

import java.util.Date;

import eu.europa.esig.dss.MessageTag;
import eu.europa.esig.dss.EN319102.validation.ChainItem;
import eu.europa.esig.dss.EN319102.wrappers.CertificateWrapper;
import eu.europa.esig.dss.jaxb.detailedreport.XmlConstraintsConclusion;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class BestSignatureTimeNotBeforeCertificateIssuanceCheck<T extends XmlConstraintsConclusion> extends ChainItem<T> {

	private final Date bestSignatureTime;
	private final CertificateWrapper signingCertificate;

	public BestSignatureTimeNotBeforeCertificateIssuanceCheck(T result, Date bestSignatureTime, CertificateWrapper signingCertificate,
			LevelConstraint constraint) {
		super(result, constraint);

		this.bestSignatureTime = bestSignatureTime;
		this.signingCertificate = signingCertificate;
	}

	@Override
	protected boolean process() {
		return !bestSignatureTime.before(signingCertificate.getNotBefore());
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.TSV_IBSTAIDOSC;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.TSV_IBSTAIDOSC_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return Indication.INDETERMINATE;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return SubIndication.NOT_YET_VALID;
	}

}
