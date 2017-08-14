/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mrerror.free.utils;


import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.mrerror.free.notification.NotificationReminderFirebaseJobService;

import java.util.concurrent.TimeUnit;


public class ReminderUtilities {
    public static final int REMINDER_INTERVAL_DAYS = 7;
    public static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.DAYS.toSeconds(REMINDER_INTERVAL_DAYS);
    public static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    public static final String REMINDER_JOB_TAG = "reminder-notification-job";
    public static boolean sInitialized;
    //  - REMINDER_INTERVAL_SECONDS should be an integer constant storing the number of seconds in 15 minutes
    //  - SYNC_FLEXTIME_SECONDS should also be an integer constant storing the number of seconds in 15 minutes
    //  - REMINDER_JOB_TAG should be a String constant, storing something like "hydration_reminder_tag"
    //  - sInitialized should be a private static boolean variable which will store whether the job
    //    has been activated or not
    public static synchronized void scheduleChargingReminder(Context context) {
        // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
        // every REMINDER_INTERVAL_SECONDS when the phone is charging. It will trigger NotificationReminderFirebaseJobService
        // Checkout https://github.com/firebase/firebase-jobdispatcher-android for an example
        if(sInitialized)return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(NotificationReminderFirebaseJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(REMINDER_JOB_TAG)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS
                        ,REMINDER_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true).build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }
}
