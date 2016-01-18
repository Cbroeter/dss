package eu.europa.esig.dss.EN319102.validation.vpfltvd.checks;

import java.util.Date;

import eu.europa.esig.dss.MessageTag;
import eu.europa.esig.dss.EN319102.policy.RuleUtils;
import eu.europa.esig.dss.EN319102.validation.ChainItem;
import eu.europa.esig.dss.EN319102.wrappers.SignatureWrapper;
import eu.europa.esig.dss.jaxb.detailedreport.XmlValidationProcessLongTermData;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.jaxb.policy.TimeConstraint;

public class TimestampDelayCheck extends ChainItem<XmlValidationProcessLongTermData> {

	private final SignatureWrapper signature;
	private final Date bestSignatureTime;
	private final TimeConstraint timeConstraint;

	public TimestampDelayCheck(XmlValidationProcessLongTermData result, SignatureWrapper signature, Date bestSignatureTime, TimeConstraint timeConstraint) {
		super(result, timeConstraint);

		this.signature = signature;
		this.bestSignatureTime = bestSignatureTime;

		this.timeConstraint = timeConstraint;
	}

	@Override
	protected boolean process() {
		Date signingTime = signature.getDateTime();
		if (signingTime == null) {
			return false;
		}
		long timestampDelay = RuleUtils.convertDuration(timeConstraint);
		return (signingTime.getTime() + timestampDelay) > bestSignatureTime.getTime();
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.ADEST_ISTPTDABST;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.ADEST_ISTPTDABST_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return Indication.INVALID;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return SubIndication.SIG_CONSTRAINTS_FAILURE;
	}

}
