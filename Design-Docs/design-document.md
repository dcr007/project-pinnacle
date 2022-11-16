# Detailed Design Document


| ***Date*** | ***Version*** |  ***Authors***    | ***Comments***                                                 |
|:-----------|    :---      | :---  |:---------------------------------------------------------------|  
| 10/20/2022 | V0.1 | Dowlathram, Chanduram <chanduram.dowlathram@sap.com>| 1.Initial draft created. <br> 2.Includes Technical specs.      |
| 11/05/2022 | V0.12 | Dowlathram, Chanduram <chanduram.dowlathram@sap.com>| 1. Included Functional Requirements on  Data Ingestion Service |
## Table of Contents


  - [Functional Requirements](#business-requirements)
    - [Tenant usage based on Login data](#tenant-usage-based-on-login-data)
    - [Analytical Insights from Tenant usage based on Login data ](#analytical-insights-from-tenant-usage-based-on-login-data)
  - [Technical Specifications](#technical-specifications)
    - [Flow of Sequence](#flow-of-sequence)
    - [UseCase to post splunk report scheduled via Webhook](#usecase-to-post-splunk-report-scheduled-via-webhook)
    - [UseCase to Publish Power-BI Report](#usecase-to-publish-power-bi-report)
    - [Splunk Alert](#splunk-alert)
    - [Splunk query used to generate the report](#splunk-query-used-to-generate-the-report)
  - [Service Layer](#service-layer)
    - [Api to post splunk data](#api-to-post-splunk-data)
    - [Service Classes](#service-classes)
  - [Persistent Layer](#persistent-layer)
    - [Data Base Container specifications](#data-base-container-specifications)

## Overview
This initiative is to build a Observability platform that will focus on error detection and identification for the currently identified areas of 
performance degradation . This will help us analyze the behavior pattern of various application components and services and how it scales over time for various production tenants and their configurations.

### Known Problem Areas

The process of Root-cause analysis  involves: 

- Analyzing and querying larger volumes of real-time data logs from Splunk,  correlating data to derive at conclusions. 

- This  consumes lots of splunk server resources which results longer query execution and turnaround time . 

- Identification  and reporting similar errors and exceptions across various modules and services are currently manual , time-consuming and repetitive process.


## Functional Requirements

### Data Ingestion Service 

To analyze tenants usage based on login transactions in the sales data centers. Splunk will be main data source to be 
used for this requirement. The following data fields will extracted from Splunk :

> LoginDate - Date and time of login
>
> DataCenter - Sales Demo data center
>
> Company - Company ID
>
> LoginCount - total login transactions executed.

### Tenant usage based on Tennant Inventory list:

The Data for the Tenant Inventory List will be sourced from : 
    
 1. DRT Tenant Inventory
 2. Cloud Reporting 

### Analytical Insights from Tenant usage based on Login data and Tenant Inventory List.
 

The data collected from DRT Tenant Inventory table will be merged with Login data collected from splunk . 
The following usage analytics can the derived from the login data:
     
> Tenants that did not have logins over a period.
>
> Logins across each Data Center
>       
> Total Logins by CompanyID.
>
> Tenant distribution across data centers.

 
## Technical Specifications 

### Flow of Sequence
 
![Sequence diagram](https://github.wdf.sap.corp/pie/visionone-splunk-data-consumer-app/blob/master/png/sequence-diagram-service-layer.png)

### UseCase to post splunk report scheduled via Webhook
![SplunkReport](https://github.wdf.sap.corp/pie/visionone-splunk-data-consumer-app/blob/master/png/usecase_diagram_post_splunk_report_to_cosmodb.png)

 | ***UseCase***    | ***Description*** |  ***Pre-Conditions***    | ***Triggers*** | ***Dependency*** |
   | :---        |    :---      | :---  |:---  |:---  |  
   | Post_Splunk_Data | Service to post Splunk Data | Service to be  authenticated. |Triggered when a splunk alert is sent out |none |
   | Save_Splunk_Data | Service to post Splunk Data | Service to be  authenticated. |Triggered when a splunk alert is sent out |none |

### UseCase to Publish Power-BI Report
![BIReport](https://github.wdf.sap.corp/pie/visionone-splunk-data-consumer-app/blob/master/png/usecase_publish_report_powerbi.png)

 | ***UseCase***    | ***Description*** |  ***Pre-Conditions***    | ***Triggers*** | ***Dependency*** |
   | :---        |    :---      | :---  |:---  |:---  |  
   | Fetch_Data | Fetch data into BI Client from Cosmos DB | DB  authentication. |Triggered when new data is populated in the DB |none |
   | Publish_report | Publish report to BI service| Data available in BI tables |none |none |
 
### Splunk Alert

Splunk alert "Sales Demo Hourly Instance Logins" will be scheduled to send alerts on a daily basis.  
### Splunk query used to generate the report
    
    index=dc*_bizx_application host=ps* sourcetype="perf_log_bizx" URL="/login" | where UN != "null" | rename CMID as Company 
    | search Company="SFSALES*" 
    | eval DataCenter=upper(substr(index,0,4))
    | eval DateOnly=strftime(_time,"%F %H:%M:%S")
    | stats min(DateOnly) as LoginDate max(DateOnly) count(URL) as LoginCount  by DataCenter Company
    | sort LoginDate DataCenter Company
    | table LoginDate DataCenter Company LoginCount

The splunk query will be scheduled as an alert to send data via a WebHook.

## Service Layer

   The Service Layer will be hosted as a ***Azure App Service***. The following are some of basic specifications, required while developing the service. 
   
   | ***Service Name***    | ***Description*** |  ***Subscription***    | ***Resource group*** | ***Location*** | ***URL***|
   | :---        |    :---      | :---  |:---  |:---  |:---  |  
   | visionone-splunk-data-consumer-app | This Azure App Service is a Java Spring-Boot service that serves as a webhook for splunk. The data is persisted into Azure-Cosmos DB | sap-hcm-global-presales |rg-dpm-dev-eastus2|East US 2|https://visionone-splunk-data-consumer-app.azurewebsites.net 
   
### Api to post splunk data
   
 <table>
 <tr>
     <th><i>Endpoint</i></th>
     <td>POST https://visionone-splunk-data-consumer-app.azurewebsites.net/splunk/api/post/dcLogins</td>
 </tr>
 <tr>
     <th><i>Description</i></th>
     <td>Webhook to post data from splunk. The data will be validated and persisted to DB</td>
 </tr>

 <tr>
    <th><i>Request Payload</i></th>

 <td>
   
 ```json
 {
   "sid": "scheduler__admin__search__LoginTimelineByDC_at_1630474980_1832_897",
   "search_name": "LoginTimelineByDC",
   "app": "search",
   "owner": "admin",
   "results_link": "http://v1splunkhost:8000/app/search/@go?sid=scheduler__admin__search__LoginTimelineByDC_at_1630474980_1832",
   "result": {
     "LoginDate": "2021-08-18",
     "DataCenter": "DC401",
     "Company": "SFSALES009073",
     "LoginCount": "11"
   }
 }
 ``` 
 </td>
 </tr>
 <tr>
    <th>Response Code</th>
    <td>Created :201, ErrorCode(s) : Error: 500, InvalidData : 400 </td>
 </tr>
 </table>

### Service Classes  

 ![class1](https://github.wdf.sap.corp/pie/visionone-splunk-data-consumer-app/blob/master/png/class_diagram_service_layer.png)

## Persistent Layer

Serverless document data store will be hosted on **Azure Cosmos SQL Document Data Store**. The following are some basic specifications, 
required while developing the service. 

   | ***Cosmos DB Account***    | ***Description*** |  ***Subscription***    | ***Resource group*** | ***Location*** | ***URL***|
   | :---        |    :---      | :---  |:---  |:---  |:---  |  
   | visionone-splunk-data-consumer-app | Serverless document data store. | sap-hcm-global-presales |rg-dpm-dev-eastus2|East US 2|https://visionone-datapool.documents.azure.com:443/|

### Data Base Container specifications 

   | ***Cosmos DB Account***    | ***Database Id*** |  ***Container***    | ***Partition Key*** | ***Unique Key*** |
   | :---        |    :---      | :---  |:---  |:---  | 
   | visionone-datapool | SplunkDataSource | DCLogins |/result/company|id|
   | cosmos-visionone-qa | TablesDB | DrtTenantInventory |/dc|companyId|

