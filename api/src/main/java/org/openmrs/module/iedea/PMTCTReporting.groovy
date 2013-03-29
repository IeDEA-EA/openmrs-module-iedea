package org.openmrs.module.iedea
/*
import org.openmrs.module.reporting.report.definition.ReportDefinition
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition
import org.openmrs.module.reporting.dataset.definition.SimplePatientDataSetDefinition
import org.openmrs.module.reporting.dataset.definition.CohortCrossTabDataSetDefinition
import org.openmrs.module.reporting.report.ReportDesign
import org.openmrs.module.reporting.report.ReportDesignResource
import org.openmrs.module.reporting.report.renderer.TextTemplateRenderer
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService
import org.openmrs.module.reporting.evaluation.EvaluationContext
*/
import org.openmrs.util.OpenmrsClassLoader
import org.apache.commons.io.IOUtils
import org.openmrs.api.context.Context

/* 
public class PMTCTRenderer extends TextTemplateRenderer {
    def reportDesign

    public PMTCTRenderer() {
        
    }
        
    public PMTCTRenderer(r) {
        reportDesign = r
    }
    
    public ReportDesign getDesign(String argument) {
        return reportDesign
    }
}
*/

public class PMTCTReporting {

    /*  
    public PMTCTReporting() {
        // I don't understand why we need an empty constructor, something seems
        // to be trying to instantiate it during module startup
        println("PMTCTReporting default no-args constructor")
    }

    def runReport() {
        def reportDefinition = new ReportDefinition()
        reportDefinition.setName("IeDEA PMTCT")
        
        def allPatients = new SimplePatientDataSetDefinition("allPatients", "")
        allPatients.addPatientProperty("patientId")
        allPatients.addPatientProperty("gender")
        allPatients.addPatientProperty("birthdate")
        DataSetDefinition dsf = allPatients
        java.util.Map m = [:]
        println("SWG GAH! WHY WONT THESE CAST 66!!")
        // reportDefinition.addDataSetDefintion("allPatients", (DataSetDefinition) dsf, m)
        
        def males = new GenderCohortDefinition()
        males.setName("Males")
        males.setMaleIncluded(true)
        
        def females = new GenderCohortDefinition()
        females.setName("Females")
        females.setFemaleIncluded(true)
        
        def genderDsd = new CohortCrossTabDataSetDefinition()
        genderDsd.addColumn("males", males, null)
        genderDsd.addColumn("females", females, null)
        reportDefinition.addDataSetDefinition("genders", genderDsd, null)
        
        def reportDesign = new ReportDesign()
        reportDesign.setName("IeDEA Report Design")
        reportDesign.setReportDefinition(reportDefinition)
        reportDesign.setRendererType(TextTemplateRenderer.class)
     
        def context = new EvaluationContext()
        def reportDefService = Context.getService(ReportDefinitionService.class)
        def reportData = reportDefService.evaluate(reportDefinition, context)
        
        def resource = new ReportDesignResource()
        def templateName = "PmtctReportTemplate.groovy"
        resource.setName(templateName)
        def is = OpenmrsClassLoader.getInstance().getResourceAsStream(templateName)
        resource.setContents(IOUtils.toByteArray(is))
        IOUtils.closeQuietly(is)
        reportDesign.addResource(resource)
        
        def renderer = new PMTCTRenderer(reportDesign)
    
        def baos = new ByteArrayOutputStream()
        renderer.render(reportData, "ReportData", baos)
        println("SWGITHEN REPORT:")
        println(baos.toString())
    }
    */
}
