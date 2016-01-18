/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.EN319102.report;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.EN319102.wrappers.DiagnosticData;
import eu.europa.esig.dss.jaxb.detailedreport.DetailedReport;
import eu.europa.esig.dss.jaxb.simplereport.SimpleReport;

/**
 * This class is a container for all reports generated by the validation
 * process: diagnostic data, detailed report and simple report.
 */
public class Reports {

	private static final Logger logger = LoggerFactory.getLogger(Reports.class);

	/**
	 * This variable contains the reference to the diagnostic data object.
	 */
	protected eu.europa.esig.dss.jaxb.diagnostic.DiagnosticData diagnosticData;

	protected DiagnosticData diagnosticDataWrapper;

	/**
	 * This is the detailed report of the validation.
	 */
	protected DetailedReport detailedReport;

	/**
	 * This is the simple report generated at the end of the validation process.
	 */
	protected eu.europa.esig.dss.jaxb.simplereport.SimpleReport simpleReport;

	protected SimpleReport simpleReportWrapper;

	/**
	 * This variable defines the sequence of the reports related to a document
	 * to validate. It's only used with ASiC-E container.
	 */
	private Reports nextReports;

	private String xmlDiagnosticData;
	private String xmlDetailedReport;
	private String xmlSimpleReport;

	/**
	 * This is the default constructor to instantiate this container.
	 *
	 * @param diagnosticData
	 *            {@code DiagnosticData}
	 * @param detailedReport
	 *            {@code DetailedReport}
	 * @param simpleReport
	 *            {@code SimpleReport}
	 */
	public Reports(final eu.europa.esig.dss.jaxb.diagnostic.DiagnosticData diagnosticDataJaxb, final DetailedReport detailedReport,
			final eu.europa.esig.dss.jaxb.simplereport.SimpleReport simpleReport) {
		this.diagnosticData = diagnosticDataJaxb;
		this.diagnosticDataWrapper = new DiagnosticData(diagnosticDataJaxb);
		this.detailedReport = detailedReport;
		this.simpleReport = simpleReport;
	}

	/**
	 * This method returns the reference to the diagnostic data object generated
	 * during the validation process.
	 *
	 * @return {@code DiagnosticData}
	 */
	public DiagnosticData getDiagnosticData() {
		return diagnosticDataWrapper;
	}

	public DetailedReport getDetailedReport() {
		return detailedReport;
	}

	public SimpleReport getSimpleReport() {
		return simpleReportWrapper;
	}

	/**
	 * This method allows to set the sequence of the reports related to a
	 * document to validate. It's only used with ASiC-E container.
	 *
	 * @param nextReports
	 */
	public void setNextReport(final Reports nextReports) {

		this.nextReports = nextReports;
	}

	/**
	 * @return {@code Reports} which corresponds to the next signature found
	 *         with in the ASiC-E container. {@code null} if there is no more
	 *         signatures.
	 */
	public Reports getNextReports() {
		return nextReports;
	}

	/**
	 * For debug purpose.
	 */
	public void print() {

		String reportDeep = "#";
		Reports currentReports = this;
		do {

			System.out.println("[" + reportDeep + "] ----------------Diagnostic data-----------------");
			System.out.println(getXmlDiagnosticData());

			System.out.println("[" + reportDeep + "] ----------------Validation report---------------");
			System.out.println(currentReports.detailedReport);

			System.out.println("[" + reportDeep + "] ----------------Simple report-------------------");
			System.out.println(getXmlSimpleReport());

			System.out.println("[" + reportDeep + "] END ------------------------------------------------");
			reportDeep += "#";
			currentReports = currentReports.getNextReports();
		} while (currentReports != null);
	}

	public String getXmlDiagnosticData() {
		if (xmlDiagnosticData == null) {
			xmlDiagnosticData = getJAXBObjectAsString(diagnosticData, eu.europa.esig.dss.jaxb.diagnostic.DiagnosticData.class.getPackage().getName());
		}
		return xmlDiagnosticData;
	}

	public String getXmlDetailedReport() {
		throw new NotImplementedException();
	}

	public String getXmlSimpleReport() {
		if (xmlSimpleReport == null) {
			xmlSimpleReport = getJAXBObjectAsString(simpleReport, eu.europa.esig.dss.jaxb.simplereport.SimpleReport.class.getPackage().getName());
		}
		return xmlSimpleReport;
	}

	private String getJAXBObjectAsString(Object obj, String contextPath) {
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			logger.error("Unable to generate string value for context " + contextPath + " : " + e.getMessage(), e);
			return null;
		}
	}
}
