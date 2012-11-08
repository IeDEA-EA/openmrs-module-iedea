package org.openmrs.module.iedea

import au.com.bytecode.opencsv.CSVReader

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept
import org.openmrs.ConceptAnswer
import org.openmrs.ConceptName
import org.openmrs.ConceptDescription
import org.openmrs.api.context.Context
import java.util.Locale

/**
 * Utility to import dictionary concepts from the csv import. 
 * 
 * @author sgithens
 */
public class DictionaryCSVImport {
    protected final Log log = LogFactory.getLog(this.getClass());
    /**
     * The headers we get from the csv are:
     * 0 Concept Id
     * 1 Name
     * 2 Description
     * 3 Synonyms
     * 4 Answers
     * 5 Set Members
     * 6 Class
     * 7 Datatype
     * 8 Changed By
     * 9 Creator
     1,"ANEMIA, BLOOD LOSS","Anemia due to bleeding or a hemorrhagic process.","ANEMIA, BLOOD LOSS","","","Diagnosis","N/A","Super User","Super User"
     * @return
     */
    public void importFromCSV (datasource) {
        def iedeaUtil = new IeDEAUtil()
        def dataReader = iedeaUtil.getReader(datasource)
        
        def conService = Context.getConceptService()
        def reader = new CSVReader(dataReader)
        def String [] headerLine = reader.readNext()
        def String [] nextLine
        def conceptNameToId = [:]
        while ((nextLine = reader.readNext()) != null) {
            println nextLine
            def id = nextLine[0].toInteger()
            if (conService.getConcept(id) != null) {
                continue
            }
            if (conService.getConceptByName(nextLine[1])) {
                continue
            }
            def concept = new Concept()
            concept.conceptId = id
            concept.addName(new ConceptName(nextLine[1],Locale.US))
            concept.addDescription(new ConceptDescription(nextLine[2],Locale.US))
            def dataType = conService.getConceptDatatypeByName(nextLine[7])
            concept.setDatatype(dataType)
            def conClass = conService.getConceptClassByName(nextLine[6])
            concept.setConceptClass(conClass)
            conService.saveConcept(concept)
            conceptNameToId[nextLine[1]] = id
        }
        reader.close()
        dataReader.close()
        
        // Now that they are all imported, we need to go back and add the 
        // answers.
        dataReader = iedeaUtil.getReader(datasource)
        reader = new CSVReader(dataReader)
        headerLine = reader.readNext()
        while ((nextLine = reader.readNext()) != null) {
            def id = nextLine[0].toInteger()
            Concept theConcept = conService.getConcept(id) // What if we skipped this on initial import because
                                                           // the id already existed?
            if (theConcept == null) {
                theConcept = conService.getConceptByName(nextLine[1])
            }
            if (theConcept == null) {
                log.warn("Unable to find concept to check for answers ${id}, ${nextLine[1]}")
                continue
            }
            String csvFormattedAnswers = nextLine[4]
            String[] answerNames = csvFormattedAnswers.split("\n")
            if (answerNames.length == 0) {
                continue
            }
            println ("Going to add the following answers: ${answerNames}")
            for (String conceptName: answerNames) {
                if (conceptName.length() == 0) continue
                println("Adding answer: ##${conceptName}##")
                // We can't actually use getConceptByName here, because the indexing
                // of words is delayed, and it doesn't catch up from the loop
                // above to actually search things by their names.
                // Concept answerConcept = conService.getConceptByName(conceptName)
                Concept answerConcept = conService.getConcept(conceptNameToId[conceptName])
                if (answerConcept == null) {
                    log.warn("Unable to look up answerConcept ${conceptName}")
                    continue
                }
                ConceptAnswer answer = new ConceptAnswer(answerConcept)
                theConcept.addAnswer(answer)
            }
            conService.saveConcept(theConcept)
        }
        reader.close()
        dataReader.close()
    }

    // TODO Unit Tests
    public void purgeAllConcepts() {
        def conceptServ = Context.getConceptService()
        def concepts = conceptServ.getAllConcepts()
        concepts.each {
            conceptServ.purgeConcept(it)
        }
    }
}
