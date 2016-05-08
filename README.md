Simple Parameterized Builds Report plugin
=========================================

This Jenkins plugin makes it easy to visualize the various builds for a parameterized project that were run using the same set of parameters.

Usage
-----

Once installed, a new link "Parameterized Builds Report" should show up in the sidebar for all parameterized jobs. Following that link will display a table where (at max, latest 10) builds for a given parameter set are displayed, grouped together based on the parameters used to invoke the builds.

Only the builds that match the parameter set used in the latest build are considered for inclusion in the table.

Also, users can configure the parameters that should appear in the reports by setting inclusion/exclusion lists while 
configuring the job.

Release Notes
-------------

### Upcoming
* Added MIT license

### 1.5 (5/8/2016)
* Fixed: [JENKINS-34525] Work around Groovy bug in Jenkins 2 (jglick)
* Added: Support for Matrix Jobs (JeremyMarshall)

### 1.4 (10/5/2014)
* Fixed: JENKINS-24928 - Spelling mistake
* Fixed: JENKINS-24927 - No table background colors

### 1.3 (7/20/2014)
* Fixed: Fix possible NPE reported in JENKINS-23841

### 1.2 (6/28/2014)

* Added: Configuration options for report
* Fixed: JENKINS-23604 - NPE when all builds are not parameterized

### 1.1 (08/17/2013)

* Fixed: JENKINS-19085 - Mask password properties
* Fixed: JENKINS-18565 - Only show build parameters and not all the user defined build variables
* Fixed: Only display completed builds in the table

### 1.0 (01/01/2013)

* Initial release