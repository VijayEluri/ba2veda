package org.gost19.ba2onto;

public class Predicate
{
//	public static final String HAS_PART = "";
//	public static final String MEMBER_OF = "";
//	public static final String LOGIN_NAME = "";
//	public static final String IS_ADMIN = "";

	////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final String xsd = "http://www.w3.org/2001/XMLSchema#";
	public static final String dc = "http://purl.org/dc/elements/1.1/";
	public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String owl = "http://www.w3.org/2002/07/owl#";
	public static final String swrc = "http://swrc.ontoware.org/ontology#";
	public static final String f_swrc = "http://swrc.ontoware.org/ontology-07";
	public static final String docs19 = "http://gost19.org/docs#";
	public static final String gost19 = "http://gost19.org/base#";
	public static final String zdb = "http://user-db#";
	public static final String f_zdb = "http://user-db";
	public static final String user_onto = "http://user-onto.org#";
	public static final String f_user_onto = "http://user-onto.org";
		
	////////////////////////////////////////////////////////////////////////////////////////////////////		
	public static final String owl__Ontology = owl + "Ontology";
	public static final String owl__imports = owl + "imports";
	public static final String owl__Restriction = owl + "Restriction"; 
	public static final String owl__onProperty = owl + "onProperty"; 
	public static final String owl__allValuesFrom = owl + "allValuesFrom";
	public static final String owl__someValuesFrom = owl + "someValuesFrom";	
	public static final String owl__maxCardinality = owl + "maxCardinality";
	public static final String owl__minCardinality = owl + "minCardinality";
	public static final String owl__hasValue = owl + "hasValue";
	
	public static final String xsd__string = xsd + "string";
	public static final String xsd__boolean = xsd + "boolean";
	public static final String xsd__date = xsd + "date";
	public static final String xsd__decimal = xsd + "decimal";
	public static final String xsd__integer = xsd + "integer";
	public static final String xsd__nonNegativeInteger = xsd + "nonNegativeInteger";

	public static final String rdf__type = rdf + "type";
	public static final String rdf__subject = rdf + "subject";
	public static final String rdf__predicate = rdf + "predicate";
	public static final String rdf__object = rdf + "object";
	public static final String rdf__Statement = rdf + "Statement";
	
	public static final String rdfs__subClassOf = rdfs + "subClassOf";
	public static final String rdfs__Class = rdfs + "Class";
	public static final String rdfs__label = rdfs + "label";

	public static final String dc__creator = dc + "creator";
	public static final String dc__identifier = dc + "identifier";
	public static final String dc__subject = dc + "subject";
	public static final String dc__title = dc + "title";
	public static final String dc__type = dc + "type";
	public static final String dc__description = dc + "description";
	public static final String dc__date = dc + "date";

	public static final String swrc__Department = swrc + "Department";
	public static final String swrc__Person = swrc + "Person";
	public static final String swrc__name = swrc + "name";
	public static final String swrc__lastName = swrc + "lastName";
	public static final String swrc__firstName = swrc + "firstName";
	public static final String swrc__organization = swrc + "organization";
	public static final String swrc__Organization = swrc + "Organization";
	public static final String swrc__email = swrc + "email";
	public static final String swrc__phone = swrc + "phone";
	public static final String swrc__creationDate = swrc + "creationDate";
	public static final String swrc__endDate = swrc + "endDate";
	public static final String swrc__startDate = swrc + "startDate";
	public static final String swrc__keywords = swrc + "keywords";
	public static final String swrc__number = swrc + "number";
	public static final String swrc__note = swrc + "note";

	public static final String gost19__middlename = gost19 + "middlename";
	public static final String gost19__externalIdentifer = gost19 + "externalIdentifer";	

	public static final String docs19__FileDescription = docs19 + "FileDescription";
	public static final String docs19__file = docs19 + "file";
	public static final String docs19__tumbnail = docs19 + "tumbnail";
	public static final String docs19__department = docs19 + "department";
	public static final String docs19__department_card = docs19 + "department_card";
	public static final String docs19__parentDepartment = docs19 + "parentDepartment";
	public static final String docs19__employee_card = docs19 + "employee_card";
	public static final String docs19__employee = docs19 + "employee";
	public static final String docs19__organization_card = docs19 + "organization_card";
	public static final String docs19__pager = docs19 + "pager";
	public static final String docs19__Document = docs19 + "Document";
	public static final String docs19__carbon_copy = docs19 + "carbon_copy";
	public static final String docs19__dateInterval = docs19 + "dateInterval"; 
	public static final String docs19__contractor = docs19 + "contractor"; 
	public static final String docs19__link = docs19 + "link"; 
	public static final String docs19__content = docs19 + "content"; 
	public static final String docs19__from = docs19 + "from";
	public static final String docs19__to = docs19 + "to";

}
