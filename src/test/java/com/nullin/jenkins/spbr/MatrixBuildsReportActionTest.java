package com.nullin.jenkins.spbr;

import com.google.common.collect.Multimap;
import hudson.matrix.*;
import hudson.model.*;
import org.jvnet.hudson.test.HudsonTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nullin
 */
public class MatrixBuildsReportActionTest extends HudsonTestCase {

    public void testMatrixNoAxes() throws Exception {
        MatrixProject project = createMatrixProject();
        MatrixBuildsReportAction action = new MatrixBuildsReportAction(project);

        WebClient wc = new WebClient();
        wc.setThrowExceptionOnFailingStatusCode(false);
        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");

        Queue.Item q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        } else{
            Thread.sleep(1000);
        }

        Multimap<Map<String, String>, MatrixRun> buildsMap = action.getBuildsMap(project.getBuilds());
        assertEquals(buildsMap.keySet().size(), 0);
        assertEquals(buildsMap.values().size(), 0);
    }

    public void testMatrixOneAxis() throws Exception {
        MatrixProject project = createMatrixProject("test2");

        TextAxis axis = new TextAxis("TEST", "1", "2", "3");
        AxisList ax = new AxisList();
        ax.add(axis);
        project.setAxes(ax);

        MatrixBuildsReportAction action = new MatrixBuildsReportAction(project);

        WebClient wc = new WebClient();
        wc.setThrowExceptionOnFailingStatusCode(false);
        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");

        Queue.Item q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        } else{
            Thread.sleep(1000);
        }

        Multimap<Map<String, String>, MatrixRun> buildsMap = action.getBuildsMap(project.getBuilds());
        assertEquals(buildsMap.keySet().size(), 3);
        assertEquals(buildsMap.values().size(), 3);
    }

    public void testChangedParameterSet() throws Exception {
        MatrixProject project = createMatrixProject();
        MatrixBuildsReportAction action = new MatrixBuildsReportAction(project);

        TextAxis axis = new TextAxis("TEST", "1", "2", "3");
        TextAxis axis2 = new TextAxis("TEST2", "a", "b", "c");
        TextAxis axis3 = new TextAxis("TEST3", "x", "y", "Z");

        AxisList ax = new AxisList();
        ax.add(axis);
        ax.add(axis2);
        ax.add(axis3);
        project.setAxes(ax);

        WebClient wc = new WebClient();
        wc.setThrowExceptionOnFailingStatusCode(false);
        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");

        Queue.Item q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        }else{
            Thread.sleep(1000);
        }

        Multimap<Map<String, String>, MatrixRun> buildsMap = action.getBuildsMap(project.getBuilds());
        //3 x 3 x 3 = 27 keys
        assertEquals(27, buildsMap.keySet().size());
        assertEquals(27, buildsMap.values().size());

        ax.remove(axis3);
        project.setAxes(ax);

        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");
        q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        }else{
            Thread.sleep(1000);
        }

        buildsMap = action.getBuildsMap(project.getBuilds());
        //3 x 3 + 1 for null
        assertEquals(10, buildsMap.keySet().size());

        //27 for first test 9 for this one
        assertEquals(36, buildsMap.values().size());

        ax.remove(axis2);
        project.setAxes(ax);

        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");
        q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        }else{
            Thread.sleep(1000);
        }

        buildsMap = action.getBuildsMap(project.getBuilds());
        //3 + 1 for null
        assertEquals(4, buildsMap.keySet().size());

        //36 for previous + 3
        assertEquals(39, buildsMap.values().size());

        ax.add(axis2);
        project.setAxes(ax);

        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");
        q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        }else{
            Thread.sleep(1000);
        }

        buildsMap = action.getBuildsMap(project.getBuilds());
        //3 * 3 + 3 for one axis builds and 1 for null
        assertEquals(13, buildsMap.keySet().size());

        //39 + 9 here
        assertEquals(48, buildsMap.values().size());

        ax.add(axis3);
        project.setAxes(ax);

        wc.goTo("/job/" + project.getName() + "/build?delay=0sec");
        q = jenkins.getQueue().getItem(project);
        if (q != null){
            q.getFuture().get();
        }else{
            Thread.sleep(1000);
        }

        buildsMap = action.getBuildsMap(project.getBuilds());
        //(3 x 3 x 3) + (3 x 3) + 3 = 39 keys
        assertEquals(39, buildsMap.keySet().size());

        //48 previous + 27 here  (no null now)
        assertEquals(75, buildsMap.values().size());
    }

}
