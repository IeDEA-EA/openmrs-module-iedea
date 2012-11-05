<%@ include file="/WEB-INF/template/include.jsp"%>

<c:set var="DO_NOT_INCLUDE_JQUERY" value="true"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<link href="/openmrs/moduleResources/iedea/css/smoothness/jquery-ui-1.9.1.custom.css" type="text/css" rel="stylesheet" />
<link href="/openmrs/moduleResources/iedea/css/iedea.css" type="text/css" rel="stylesheet" />

<script src="/openmrs/moduleResources/iedea/js/jquery-1.8.2.min.js" type="text/javascript" ></script>
<script src="/openmrs/moduleResources/iedea/js/jquery-ui-1.9.1.custom.min.js" type="text/javascript" ></script>
<script src="/openmrs/moduleResources/iedea/js/iedea.js" type="text/javascript" ></script>

<div id="tabs">
  <ul>
    <li><a href="#tabs-0">Overview</a></li>
    <li><a href="#tabs-1">IeDEA Utilities</a></li>
    <li><a href="#tabs-2">CCSP Import</a></li>
    <li><a href="#tabs-3">CCSP Reporting</a></li>
    <li><a href="#tabs-4">PMTCT Reporting</a></li>
    <li><a href="#tabs-5">Debugging</a></li>
  </ul>

  <div id="tabs-0">
    <h2>IeDEA EA Module Overview</h2>
    <p>Descriptions of the module.</p>
  </div>

  <div id="tabs-1">
    <h2>Dictionary Utilities</h2>
    <div>
      <p>
        These utilities allow you to rebuild your concept dictionary to
        test against different environment. These are meant for DEVELOPMENT
        only, and will reset your concept dictionary.
      </p>
      <span>Current number of concepts: ${conceptCount}</span><br/>
      <form method="POST" action="dictionaryOperation.form">
        <input id="buildAmpathDictionary" type="radio" name="op" value="buildAmpathDictionary" />
        <label for="buildAmpathDictionary">Build AMPATH Dictionary from scratch</label><br/>
        <input id="buildFacesDictionary" type="radio" name="op" value="buildFacesDictionary" />
        <label for="buildFacesDictionary">Build FACES Dictionary from scratch</label><br/>
        <input id="importCsvOp" type="radio" name="op" value="importCsvOp" />
        <label for="importCsvOp">Import CSV</label>
        <input type="text" size="50" name="importCsvPath" /><br/>
        <input id="purgeDictOp" type="radio" name="op" value="purgeDictOp" />
        <label for="purgeDictOp">Purge Dictionary</label><br/>
        <input type="submit" value="Submit operation" />
      </form>
    </div>
  </div>
  <div id="tabs-2">
    <h2>CCSP Import</h2>
    <div>
      <form method="POST" action="ccspImportOperation.form">

        <h3>Import File Location (on local disk):</h3>
        <input type="text" name="ccspImportFilePath" size="50" /><br/>

        <h3>Import Profile</h3>
        <input id="facesInitial" type="radio" name="importProfile" value="facesInitial" />
        <label for="facesInitial">Faces Initial ODK</label>
        <!-- <input id="ampath" type="radio" name="importProfile" value="facesInitial" /> -->
        <!-- <label for="facesInitial">Faces Initial ODK</label> -->

        <h3>Encounter Type</h3>
        <input id="CCSPInitialForm" type="radio" name="importEncounterType" value="CCSPInitialForm" />
        <label for="CCSPInitialForm">CCSPInitialForm</label>        

        <br/><br/>
        <input type="submit" value="Submit operation" />
      </form>
    </div>
  </div>
  <div id="tabs-3">
    <h2>CCSP Reporting</h2>
    <div>CCSP Reporting stuff</div>
  </div>
  <div id="tabs-4">
    <h2>PMTCT Reporting</h2>
    <div>PMTCT Reporting Stuff</div>
  </div>
  <div id="tabs-5">
    <h2>Debugging In Progress</h2>
    <form method="POST" action="importConceptDictionaryCSV.form">
      <input type="submit" value="Import CSV Dictionary" />
    </form>
    <br/>

    <a href="/openmrs/module/iedea/loadingdock.form">Loading Dock</a>


    <h2>Odk Import Test Options</h2>
    <form method="POST" action="odkImportOperation.form">
      <span>Import File Location:</span><br/>
      <input type="text" name="odkCsvExportFilePath" size="50" /><br/>
      <input type="submit" value="Submit operation" />
    </form>

  </div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
