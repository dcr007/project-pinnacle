## Overview
This initiative is to build a Observability platform that will focus on error detection and identification for the currently identified areas of 
performance degradation . This will help us analyze the behavior pattern of various application components and services and how it scales over time for various production tenants and their configurations.

### Known Problem Areas

The process of Root-cause analysis  involves: 

- Analyzing and querying larger volumes of real-time data logs from Splunk,  correlating data to derive at conclusions. 

- This  consumes lots of splunk server resources which results longer query execution and turnaround time . 

- Identification  and reporting similar errors and exceptions across various modules and services are currently manual , time-consuming and repetitive process.

### Identified Use-Cases

These are some the use-cases identified as candidates to start with :
- **DWR Performance** - The direct web remoting  are API calls that provides faster interaction with web-servers via Ajax requests from a js file.
When the web page is AJAX enabled, it  results in large number of server hits as the client end up fetching largely dynamic documents, in order to provide a greater user experience.
Hence, monitoring DWR invocation performance is very critical to achieve pleasant user experience .
- **Count of active deadlocks** - Based on the results from this Hana service-layer splunk dashboard . We do see there are around 132 active row locks in just last 24 hours time period. This JIRA-LRN-132689 reported is to address one such dead-lock issue caused high volume of row locks.  Analyzing and reporting on all 132 active row locks manually may not be very effective approach.
- **Insights on errors on "update or delete by foreign key constraint violation"** - Multiple occurrences of the error "failed on update or delete by foreign key constraint violation:".This is observed various tables across different DC's. 

### Anomaly Detection Architecture

![](archDiagram/high-level-architecture.png)
