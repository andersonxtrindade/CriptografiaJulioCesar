<%-- 
    Document   : request
    Created on : 06/06/2020, 01:44:15
    Author     : axtri
--%>
<%@page import="java.io.IOException"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.HttpURLConnection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="use" class="beans.CodenationJulio"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="jquery.js"></script>
<script src="jquery.form.js"></script>
<script>
    $('#fileUploader').on('change', uploadFile);


function uploadFile(event)
	{
	    event.stopPropagation(); 
	    event.preventDefault(); 
	    var files = event.target.files; 
	    var data = new FormData();
	    $.each(files, function(key, value)
	    {
	        data.append(key, value);
	    });
	    postFilesData(data); 
	 }
	
function postFilesData(data)
	{
	 $.ajax({
        url: 'https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=133bbb58246deaa1d8eb7f2e789ea174db19917e',
        type: 'POST',
        data: data,
        cache: false,
        dataType: 'json',
        processData: false, 
        contentType: false, 
        success: function(data, textStatus, jqXHR)
        {
        	//success
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            console.log('ERRORS: ' + textStatus);
        }
	    });
	}
</script>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=133bbb58246deaa1d8eb7f2e789ea174db19917e" method="post" enctype="multipart/form-data">


<strong>Meu arquivo:</strong><br/>
<input type="file" name="answer">
</p>
<input type="submit" value = "Submit">
</form>
    </body>
</html>
