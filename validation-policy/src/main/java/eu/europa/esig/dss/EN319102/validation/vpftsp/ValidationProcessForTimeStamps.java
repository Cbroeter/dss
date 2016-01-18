package eu.europa.esig.dss.EN319102.validation.vpftsp;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.EN319102.validation.Chain;
import eu.europa.esig.dss.EN319102.validation.ChainItem;
import eu.europa.esig.dss.EN319102.validation.vpftsp.checks.TimestampBasicBuildingBlocksCheck;
import eu.europa.esig.dss.EN319102.wrappers.TimestampWrapper;
import eu.europa.esig.dss.jaxb.detailedreport.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.jaxb.detailedreport.XmlValidationProcessTimestamps;

/**
 * 5.4 Validation process for time-stamps
 */
public class ValidationProcessForTimeStamps extends Chain<XmlValidationProcessTimestamps> {

	private static final Logger logger = LoggerFactory.getLogger(ValidationProcessForTimeStamps.class);

	private final TimestampWrapper timestamp;
	private final Map<String, XmlBasicBuildingBlocks> bbbs;

	public ValidationProcessForTimeStamps(TimestampWrapper timestamp, Map<String, XmlBasicBuildingBlocks> bbbs) {
		super(new XmlValidationProcessTimestamps());

		this.timestamp = timestamp;
		this.bbbs = bbbs;
	}

	@Override
	protected void initChain() {
		XmlBasicBuildingBlocks tspBBB = bbbs.get(timestamp.getId());
		if (tspBBB != null) {
			firstItem = timestampBasicBuildingBlocksValid(tspBBB);
		} else {
			logger.error("Basic Building Blocks for timestamp " + timestamp.getId() + " not found!");
		}
	}

	@Override
	protected void addAdditionalInfo() {
		result.setId(timestamp.getId());
		result.setType(timestamp.getType());
	}

	private ChainItem<XmlValidationProcessTimestamps> timestampBasicBuildingBlocksValid(XmlBasicBuildingBlocks timestampBBB) {
		return new TimestampBasicBuildingBlocksCheck(result, timestampBBB, getFailLevelConstraint());
	}

}
