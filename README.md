# CodeGraph
A library that analyzes the graph of modules of the application. 
Based on the dependencies between modules, a Java application builds a graph in which the vertices are modules (modules of JPMS or Maven modules), 
and the relations between them are dependencies. 

Supports two types of modules: 
1. JPMS (Java Platform Module System) modules;
2. Maven modules. 

Analysis of the dependencies of the graph is performed using various graph algorithms, with the help of which 
various characteristics of the application are calculated: 
1. Obtaining transitive chains;
2. Determining the shortest path between modules and all available paths between modules;
3. Determining the bridge dependencies;
4. Detecting duplicates of classes / packages and conflicts of library versions
5. Detecting various architectural metrics (abstractness/concreteness of the module, stability of the module, etc). 
6. This allows you to evaluate the quality of the project architecture;
7. Detecting clusters of the modules.

The analysis can be performed both when assembling the project with Maven, and on an already assembled distribution.

When assembling the project, the analysis is carried out by connecting a specially developed plugin for Maven to your project and configuring it. 
The plugin can be configured to abort the build if any characteristics of the graph are outside specified limits.

Analysis of an already assembled distribution is carried out either through a console application, or by deploying a server application and accessing it using the REST API.
The console application, in addition to displaying information about characteristics, can visualize a graph and calculated characteristics using the GraphStream library.