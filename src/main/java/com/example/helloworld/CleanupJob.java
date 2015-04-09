package com.example.helloworld;

import com.example.helloworld.db.ProfileViewDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CleanupJob implements Job
{
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        ProfileViewDAO profileViewDAO = (ProfileViewDAO) context.getMergedJobDataMap().get("dao");
        int deleteCount = profileViewDAO.removeOldProfileViews();
        System.out.println("Delete count: " + deleteCount);

    }

}
