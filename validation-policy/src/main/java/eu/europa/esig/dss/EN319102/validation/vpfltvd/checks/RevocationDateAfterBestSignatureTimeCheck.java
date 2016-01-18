package eu.europa.esig.dss.EN319102.validation.vpfltvd.checks;

import java.util.Date;

import eu.europa.esig.dss.MessageTag;
import eu.europa.esig.dss.EN319102.validation.ChainItem;
import eu.europa.esig.dss.EN319102.wrappers.CertificateWrapper;
import eu.europa.esig.dss.EN319102.wrappers.RevocationWrapper;
import eu.europa.esig.dss.jaxb.detailedreport.XmlValidationProcessLongTermData;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class RevocationDateAfterBestSignatureTimeCheck extends ChainItem<XmlValidationProcessLongTermData> {

	private final CertificateWrapper certificate;
	private final Date bestSignatureTime;

	public RevocationDateAfterBestSignatureTimeCheck(XmlValidationProcessLongTermData result, CertificateWrapper certificate, Date bestSignatureTime,
			LevelConstraint constraint) {
		super(result, constraint);

		this.certificate = certificate;
		this.bestSignatureTime = bestSignatureTime;
	}

	@Override
	protected boolean process() {
		RevocationWrapper revocationData = certificate.getRevocationData();
		boolean isRevocationDateAfterBestSignatureDate = revocationData != null && revocationData.getRevocationDate().after(bestSignatureTime);
		return !isRevocationDateAfterBestSignatureDate;
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.ADEST_IRTPTBST;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.ADEST_IRTPTBST_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return Indication.INDETERMINATE;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return SubIndication.REVOKED_NO_POE;
	}

}
