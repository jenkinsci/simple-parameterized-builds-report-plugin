package com.nullin.jenkins.spbr.MatrixBuildsReportAction

import com.google.common.collect.Multimap
import hudson.model.AbstractBuild

l = namespace(lib.LayoutTagLib)
st = namespace("jelly:stapler")

l.layout(title: "Simple Parameterized Builds Report", css: "/plugin/simple-parameterized-builds-report/css/style.css") {
  st.include(page: "sidepanel.jelly", it: my.project)
  l.main_panel() {

    h1("Simple Parametized Builds Report")
    def builds = my.project.builds
    if (builds.empty) {
      text("No builds.")
    } else {
      showTable(builds)
    }
  }
}

private showTable(Collection<AbstractBuild> builds) {
    Multimap<Map<String, String>, AbstractBuild> buildsMap = my.getBuildsMap(builds);

    div() {
    table(border: 1, class: "report") {

        tr() {
            th() {
                text("Parameters")
            }
            th(colspan: "${my.MAX_BUILDS_PER_PARAM_DEF}") {
                text("Builds")
            }
        }

        for (Map<String, String> key : buildsMap.keySet()) {
            if (key == null) {
                // will handle separately later
                continue;
            }

            List<AbstractBuild> _builds = buildsMap.get(key)
            tr() {
                td() {
                    for (String _key: key.keySet()) {
                        text(_key + ": " + key.get(_key))
                        br()
                    }
                }

                for (build in _builds) {
                    td(bgcolor: "${build.getIconColor().getHtmlBaseColor()}", padding: 5) {
                        showEntry(build)
                    }
                  }
                }
            }
        }

        br()
        br()

        if (buildsMap.containsKey(null)) {
            p("Following builds were built using a parameter set other than that used for the latest build. (Hence, they were not categorized in the table above)")
            List<AbstractBuild> _builds = buildsMap.get(null)
            ul() {
                for (build in _builds) {
                    li() {
                        showBuildDetails(build)
                    }
                }
            }
        }

        }
    }

private def showBuildDetails(AbstractBuild build) {
      a(href: "${rootURL}/${build.url}") {
          text(build.displayName)
          br()
      }
}