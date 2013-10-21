package com.nullin.jenkins.spbr;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixRun;


import java.util.Collection;
import java.util.Collections;

/**
 * @author nullin
 */
@Extension
public class SimpleParameterizedBuildsReportFactory extends TransientProjectActionFactory {

    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        if (target.isParameterized()) {
            return Collections.singleton(new SimpleParameterizedBuildsReportAction(target));
        }else if (target instanceof MatrixProject ) {
            return Collections.singleton(new MatrixBuildsReportAction((MatrixProject)target));
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
