<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	   
	   <title>Pagina dei Risultati</title>
	 </head>
	 
	<body class="d-flex flex-column h-100">
	 
		<!-- Fixed navbar -->
		<jsp:include page="../navbar.jsp"></jsp:include>
	 
	
		<!-- Begin page content -->
		<main class="flex-shrink-0">
		  <div class="container">
		  
		  		<div class="alert alert-success alert-dismissible fade show  ${successMessage==null?'d-none':'' }" role="alert">
				  ${successMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-danger alert-dismissible fade show ${erroreMessage==null?'d-none':'' }" role="alert">
				  ${erroreMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-info alert-dismissible fade show d-none" role="alert">
				  Aggiungere d-none nelle class per non far apparire
				   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
		  
		  
		  
		  		<div class='card'>
				    <div class='card-header'>
				        <h5>SEI SUCURO DI VOLER ELIMINARE TUTTO ?????</h5> 
				    </div>
				    <div class='card-body'>
				    
				    <form:form modelAttribute="satellite_list_attribute" method="post" action="${pageContext.request.contextPath}/satellite/confermaTuttoDisabilita" class="row g-3" novalidate="novalidate">
				    
				    
				    <button type="submit" name="submit" value="submit" id="submit" class="btn btn-primary">Conferma</button>

				    
				    </form:form>
				    
				        <div class='table-responsive'>
				            <table class='table table-striped ' >
				                <thead>
				                    <tr>
			                         	<th>Denominazione</th>
				                        <th>Codice</th>
				                        <th>Data Lancio</th>
				                        <th>Data Rientro</th>
				                        <th>Stato</th>
				                    </tr>
				                </thead>
				                <tbody>
				                	<c:forEach items="${satellite_list_attribute }" var="satelliteItem">
										<tr>
											<td>${satelliteItem.denominazione }</td>
											<td>${satelliteItem.codice }</td>
											<td><fmt:formatDate type = "date" value = "${satelliteItem.dataDiLancio }" /></td>
											<td><fmt:formatDate type = "date" value = "${satelliteItem.dataDiRientro }" /></td>
											<td>${satelliteItem.stato }</td>
											
										</tr>
									</c:forEach>
				                </tbody>
				            </table>
				        </div>
				        
				        <div class="col-12">
							<a href="${pageContext.request.contextPath}/satellite/search" class='btn btn-outline-secondary' style='width:80px'>
					            <i class='fa fa-chevron-left'></i> Back 
					        </a>
						</div>
				   
					<!-- end card-body -->		
						   
			    </div>
			<!-- end card -->
			
			</div>	
			<p>  Numero Voci che verranno modificate in seguito alla procedura: ${satellite_list_attribute.size() }</p>
				   	<p> Numero Voci totali presenti nel sistema: ${satellite_listAll_attribute.size()} </p>
		 
		   
		 <!-- end container -->  
		  </div>
		  
		</main>
		
		<!-- Footer -->
		<jsp:include page="../footer.jsp" />
		
	</body>
</html>