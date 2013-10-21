package com.nullin.jenkins.spbr;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.util.RunList;

import java.util.*;

/**
 * Action to display builds categorized by parameters used to run them.
 *
 * @author nullin
 */
public class MatrixBuildsReportAction implements Action {

    private MatrixProject project;
    public static final int MAX_BUILDS_PER_PARAM_DEF = 10;

    MatrixBuildsReportAction(MatrixProject project) {
        this.project = project;
    }

    public String getIconFileName() {
        return "notepad.png";
    }

    public String getDisplayName() {
        return "Simple Parameterized Builds Report";
    }

    public String getUrlName() {
        return "spbr";
    }

    /**
     * Gets at most {@code MAX_BUILDS_PER_PARAM_DEF} builds per set of parameter values as a map.
     *
     * @param builds all possible builds to look at to generate the map
     * @return
     */
    public Multimap<Map<String, String>, MatrixRun> getBuildsMap(RunList<MatrixBuild> builds) {
        Multimap<Map<String, String>, MatrixRun> buildsMap = ArrayListMultimap.create();

        if (builds.isEmpty()) {
            return buildsMap;
        }

        //List<MatrixRun> mr =  builds.getLastBuild().getExactRuns();
        //List<MatrixRun> mr =  builds.limit(1);
        //List<MatrixRun> mr = project.getLastBuild().getBuildVariables().keySet();
        MatrixBuild mb = builds.getLastBuild();
        List<MatrixRun> mr = mb.getExactRuns();

        if(mr.size() == 0) {
            //there is a build but it has no axes so there are no runs
            return buildsMap;
        }


        Set<String>  latestParamKeySet =  mr.get(mr.size()-1).getBuildVariables().keySet();

        MatrixBuild build = builds.getLastBuild();
            for (MatrixRun r: build.getRuns()){
                Map<String, String> vars = r.getBuildVariables();

                if (latestParamKeySet.equals(vars.keySet())) {
                    while(r != null){
                        if (buildsMap.get(vars).size() == MAX_BUILDS_PER_PARAM_DEF) {
                            continue;
                        }
                        buildsMap.put(vars, r);
                        r = r.getPreviousBuild();
                    }
                }else if(latestParamKeySet.containsAll(vars.keySet())){
                    //so the latest build has added to the parameters(but not removed any)
                    Set<String> diff = new HashSet<String>(latestParamKeySet);
                    diff.removeAll(vars.keySet());

                    for (String v: diff){
                        vars.put(v, "Unused");
                    }
                    while(r != null){
                        if (buildsMap.get(vars).size() == MAX_BUILDS_PER_PARAM_DEF) {
                            continue;
                        }
                        buildsMap.put(vars, r);
                        r = r.getPreviousBuild();
                    }
                } else {
                    /*
                    * Any build not matching the criteria gets added to the default null key.
                    */
                    buildsMap.put(null, r);
                }
            }

        //}
        return buildsMap;
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }
}
