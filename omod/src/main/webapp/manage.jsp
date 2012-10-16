<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<p>Hello ${user.systemId}!</p>

<span>Current number of concepts: ${conceptCount}</span><br/>
<form method="POST" action="importConceptDictionaryCSV.form">
  <input type="submit" value="Import CSV Dictionary" />
</form>
<br/>

<a href="/openmrs/module/iedea/loadingdock.form">Loading Dock</a>

<form method="POST" action="dictionaryOperation.form">
  <input id="importCsvOp" type="radio" name="op" value="importCsvOp" />
  <label for="importCsvOp">Import CSV</label>
  <input type="text" name="importCsvPath" /><br/>
  <input id="purgeDictOp" type="radio" name="op" value="purgeDictOp" />
  <label for="purgeDictOp">Purge Dictionary</label><br/>
  <input type="submit" value="Submit operation" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
