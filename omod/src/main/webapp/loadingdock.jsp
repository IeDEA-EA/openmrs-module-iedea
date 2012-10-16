<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h1>Loading Dock</h1>

<h3>Import Results:</h3>
<div>
${loadingDockImportResults}
</div>

<span>Current Loading Dock Directory:</span><br/>
<span>${loadingDockDirectory}</span>

<table>
<tr>
<th>File Name</th>
<th>Import Type</th>
</tr>
<c:forEach var="importtype" items="${importFiles}">
  <c:forEach var="importfile" items="${importtype.value}">
    <tr>
      <td>${importfile}</td>
      <td>${importtype.key}</td>
      <td>
        <form method="POST" action="checkPatientsOnImport.form">
          <input type="hidden" name="filename" value="${importfile}" />
          <input type="hidden" name="filetype" value="${importtype.key}" />
          <input type="submit" value="Check Patients" />
        </form>
      </td>
    </tr>  
  </c:forEach>
</c:forEach>
</table>

<h3>Debug and test utilities</h3>
<form method="POST" action="entireWorkflowTest.form">
  <input type="submit" value="Test Entire Input Workflow" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
